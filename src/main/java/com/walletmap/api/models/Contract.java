package com.walletmap.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "side_a", referencedColumnName = "id")
    private User sideA;

    // Reference to Contact (side B Local)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "side_b_local", referencedColumnName = "id")
    private Contact sideBLocal;

    // Reference to User (side B Shared)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "side_b_shared", referencedColumnName = "id")
    private User sideBShared;

    @Column(name = "creation_date")
    private LocalDate creationDate; // Ledger creation date

    @Column
    private String currency; // Currency for the ledger (ISO 4217 code)

    @Column
    private Double netBalance; // Net balance of the ledger

    @Column
    private String status; // Status of the ledger (e.g., "Active", "Completed", "Cancelled")

    @Column
    private boolean isShared; // Indicates if the ledger is shared or local
}