package com.walletmap.api.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contracts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contract implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference to User (side A)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "side_a", referencedColumnName = "id")
    @JsonManagedReference
    private User sideA;

    // Reference to Contact (side B Local)
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonManagedReference
    @JoinColumn(name = "side_b_local", referencedColumnName = "id")
    private Contact sideBLocal;

    // Reference to User (side B Shared)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "side_b_shared", referencedColumnName = "id")
    @JsonManagedReference
    // @JsonIgnore
    private User sideBShared;

    @JsonIgnore
    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    private String name;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String currency; // Currency for the ledger (ISO 4217 code)
    private Double netBalance = 0.0; // Net balance of the ledger
    private String status; // Status of the ledger (e.g., "Active", "Completed", "Cancelled")
    private boolean isShared; // Indicates if the ledger is shared or local

    public Contract(Contract contract) {
        this.id = contract.getId();
        this.sideA = contract.getSideA();
        this.sideBLocal = contract.getSideBLocal();
        this.sideBShared = contract.getSideBShared();
        this.name = contract.getName();
        this.createdAt = contract.getCreatedAt();
        this.currency = contract.getCurrency();
        this.netBalance = contract.getNetBalance();
        this.status = contract.getStatus();
        this.isShared = contract.isShared();
    }
}