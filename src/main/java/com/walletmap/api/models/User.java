package com.walletmap.api.models;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String username;
    private String picture;
    private Number accessId;

    @OneToMany(mappedBy = "sideA")
    @JsonBackReference
    // @JsonIgnore
    private List<Contract> contractsA;

    @OneToMany(mappedBy = "sideBShared")
    @JsonBackReference
    // @JsonIgnore
    private List<Contract> contractsB;
}
