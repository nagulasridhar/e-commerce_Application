package com.sridharnagula.orderservice.clients;

import com.sridharnagula.orderservice.configs.ServiceUrlProperties;
import com.sridharnagula.orderservice.dtos.RemoteUserDTO;
import com.sridharnagula.orderservice.exceptions.RemoteUserNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class UserServiceClient {

    private final RestTemplate restTemplate;
    private final ServiceUrlProperties serviceUrlProperties;

    public UserServiceClient(RestTemplate restTemplate, ServiceUrlProperties serviceUrlProperties) {
        this.restTemplate = restTemplate;
        this.serviceUrlProperties = serviceUrlProperties;
    }

    public RemoteUserDTO getUserByEmail(String email) throws RemoteUserNotFoundException {
        String url = serviceUrlProperties.getUserServiceUrl() + "/users/email/" + email;
        try {
            return restTemplate.getForObject(url, RemoteUserDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RemoteUserNotFoundException("No user found with email " + email);
        }
    }
}
