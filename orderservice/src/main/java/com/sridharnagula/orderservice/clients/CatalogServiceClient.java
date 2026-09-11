package com.sridharnagula.orderservice.clients;

import com.sridharnagula.orderservice.configs.ServiceUrlProperties;
import com.sridharnagula.orderservice.dtos.RemoteProductDTO;
import com.sridharnagula.orderservice.exceptions.RemoteProductNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class CatalogServiceClient {

    private final RestTemplate restTemplate;
    private final ServiceUrlProperties serviceUrlProperties;

    public CatalogServiceClient(RestTemplate restTemplate, ServiceUrlProperties serviceUrlProperties) {
        this.restTemplate = restTemplate;
        this.serviceUrlProperties = serviceUrlProperties;
    }

    public RemoteProductDTO getProduct(Long productId) throws RemoteProductNotFoundException {
        String url = serviceUrlProperties.getCatalogServiceUrl() + "/products/" + productId;
        try {
            return restTemplate.getForObject(url, RemoteProductDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RemoteProductNotFoundException("No product found with id " + productId);
        }
    }
}
