package com.sridharnagula.orderservice.services;

import com.sridharnagula.orderservice.clients.CatalogServiceClient;
import com.sridharnagula.orderservice.clients.PaymentServiceClient;
import com.sridharnagula.orderservice.clients.UserServiceClient;
import com.sridharnagula.orderservice.dtos.CreateOrderRequestDTO;
import com.sridharnagula.orderservice.dtos.OrderItemRequestDTO;
import com.sridharnagula.orderservice.dtos.RemoteProductDTO;
import com.sridharnagula.orderservice.dtos.RemoteUserDTO;
import com.sridharnagula.orderservice.exceptions.OrderNotFoundException;
import com.sridharnagula.orderservice.exceptions.PaymentFailedException;
import com.sridharnagula.orderservice.exceptions.RemoteProductNotFoundException;
import com.sridharnagula.orderservice.exceptions.RemoteUserNotFoundException;
import com.sridharnagula.orderservice.models.Order;
import com.sridharnagula.orderservice.models.OrderItem;
import com.sridharnagula.orderservice.models.OrderStatus;
import com.sridharnagula.orderservice.repositories.OrderRepository;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Orchestrates the multi-service "place an order" transaction:
 *   1. verify the buyer with userservice
 *   2. price + validate each line item against the catalog (productservice)
 *   3. initiate payment via paymentService
 *   4. persist the order
 *
 * Each step is wrapped in its own child span (in addition to the client spans the
 * javaagent already creates for the outbound HTTP calls) so that in SigNoz's trace
 * waterfall you can see exactly which step of the distributed transaction a slow or
 * failed order spent its time in, rather than a single opaque "createOrder" span.
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final UserServiceClient userServiceClient;
    private final CatalogServiceClient catalogServiceClient;
    private final PaymentServiceClient paymentServiceClient;
    private final Tracer tracer;

    public OrderServiceImpl(OrderRepository orderRepository,
                             UserServiceClient userServiceClient,
                             CatalogServiceClient catalogServiceClient,
                             PaymentServiceClient paymentServiceClient,
                             Tracer tracer) {
        this.orderRepository = orderRepository;
        this.userServiceClient = userServiceClient;
        this.catalogServiceClient = catalogServiceClient;
        this.paymentServiceClient = paymentServiceClient;
        this.tracer = tracer;
    }

    @Override
    public Order createOrder(CreateOrderRequestDTO request)
            throws RemoteUserNotFoundException, RemoteProductNotFoundException, PaymentFailedException {

        Span transactionSpan = tracer.spanBuilder("order-service.place-order")
                .setAttribute("order.item_count", request.getItems().size())
                .startSpan();

        try (Scope scope = transactionSpan.makeCurrent()) {
            RemoteUserDTO user = verifyUser(request.getUserEmail());

            Order order = new Order();
            order.setUserId(user.getId());
            order.setStatus(OrderStatus.USER_VERIFIED);

            double total = priceItems(request.getItems(), order);
            order.setTotalAmount(total);
            order.setStatus(OrderStatus.ITEMS_PRICED);

            Order saved = orderRepository.save(order);

            String paymentReference = initiatePayment(user, saved);
            saved.setPaymentReference(paymentReference);
            saved.setStatus(OrderStatus.CONFIRMED);
            Order confirmed = orderRepository.save(saved);

            transactionSpan.setAttribute("order.id", confirmed.getId());
            transactionSpan.setAttribute("order.total_amount", total);
            transactionSpan.setStatus(StatusCode.OK);
            logger.info("Order {} confirmed for user {} - total {}", confirmed.getId(), user.getId(), total);
            return confirmed;

        } catch (RemoteUserNotFoundException | RemoteProductNotFoundException | PaymentFailedException e) {
            transactionSpan.recordException(e);
            transactionSpan.setStatus(StatusCode.ERROR, e.getMessage());
            throw e;
        } finally {
            transactionSpan.end();
        }
    }

    private RemoteUserDTO verifyUser(String email) throws RemoteUserNotFoundException {
        Span span = tracer.spanBuilder("order-service.verify-user").startSpan();
        try (Scope scope = span.makeCurrent()) {
            return userServiceClient.getUserByEmail(email);
        } catch (RemoteUserNotFoundException e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR, "user not found");
            throw e;
        } finally {
            span.end();
        }
    }

    private double priceItems(List<OrderItemRequestDTO> items, Order order) throws RemoteProductNotFoundException {
        Span span = tracer.spanBuilder("order-service.price-items").startSpan();
        try (Scope scope = span.makeCurrent()) {
            double total = 0;
            for (OrderItemRequestDTO itemRequest : items) {
                RemoteProductDTO product = catalogServiceClient.getProduct(itemRequest.getProductId());

                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProductId(product.getId());
                item.setProductTitle(product.getTitle());
                item.setQuantity(itemRequest.getQuantity());
                item.setUnitPrice(product.getPrice());
                order.getItems().add(item);

                total += product.getPrice() * itemRequest.getQuantity();
            }
            span.setAttribute("order.total_amount", total);
            return total;
        } catch (RemoteProductNotFoundException e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR, "product not found");
            throw e;
        } finally {
            span.end();
        }
    }

    private String initiatePayment(RemoteUserDTO user, Order order) throws PaymentFailedException {
        Span span = tracer.spanBuilder("order-service.initiate-payment")
                .setAttribute("order.id", order.getId())
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            long amountInSubunits = Math.round(order.getTotalAmount() * 100);
            return paymentServiceClient.initiatePayment(user, amountInSubunits, String.valueOf(order.getId()));
        } catch (PaymentFailedException e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR, "payment failed");
            throw e;
        } finally {
            span.end();
        }
    }

    @Override
    public Order getOrderById(Long orderId) throws OrderNotFoundException {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " was not found"));
    }

    @Override
    public List<Order> getOrdersForUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
