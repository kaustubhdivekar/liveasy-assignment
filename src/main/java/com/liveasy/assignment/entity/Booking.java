package com.liveasy.assignment.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Or GenerationType.UUID
    private UUID id;

    // Many Bookings can be associated with one Load
    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetch is often better for performance
    @JoinColumn(name = "load_id", nullable = false)
    private Load load; // Link to the Load entity

    @Column(nullable = false)
    private String transporterId;

    private double proposedRate;
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING; // Default status

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp requestedAt;
}