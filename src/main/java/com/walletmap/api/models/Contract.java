package com.walletmap.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    private String name;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String currency; // Currency for the ledger (ISO 4217 code)
    private Double netBalance; // Net balance of the ledger
    private String status; // Status of the ledger (e.g., "Active", "Completed", "Cancelled")
    private boolean isShared; // Indicates if the ledger is shared or local
}