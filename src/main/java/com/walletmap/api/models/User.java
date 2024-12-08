package com.walletmap.api.models;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Data;

@Entity
@Data
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String username;
    private String picture;
    private Number accessId;

    public User() {
        super();
    }

    public User(Long id, String email, String password, String username, String picture, Number accessId) {
        super();
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.picture = picture;
        this.accessId = accessId;
    }

}
