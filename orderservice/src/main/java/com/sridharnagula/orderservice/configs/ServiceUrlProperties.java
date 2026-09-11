package com.sridharnagula.orderservice.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "services")
@Getter
@Setter
public class ServiceUrlProperties {
    private String userServiceUrl;
    private String catalogServiceUrl;
    private String paymentServiceUrl;
}
