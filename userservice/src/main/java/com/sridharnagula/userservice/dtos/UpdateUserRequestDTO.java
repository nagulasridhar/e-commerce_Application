package com.sridharnagula.userservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequestDTO {
    private String name;
    private String phoneNumber;
}
