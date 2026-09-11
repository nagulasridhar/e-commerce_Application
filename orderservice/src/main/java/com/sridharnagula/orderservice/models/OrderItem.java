package com.sridharnagula.orderservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OrderItem extends BaseModel {

    @ManyToOne
    private Order order;

    private Long productId;
    private String productTitle;
    private int quantity;
    private double unitPrice;
}
