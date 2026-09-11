package com.sridharnagula.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class User extends BaseModel {

    private String name;
    private String email;
    private String phoneNumber;
    private String passwordHash;

    @PrePersist
    public void prePersist() {
        setCreatedAt(new Date());
        setLastUpdatedAt(new Date());
    }

    @PreUpdate
    public void preUpdate() {
        setLastUpdatedAt(new Date());
    }
}
