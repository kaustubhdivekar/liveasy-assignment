package com.liveasy.assignment.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator; // For UUID generation pre-JPA 2.2
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "loads")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Load {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Or GenerationType.UUID depending on JPA/Hibernate version
    private UUID id;

    @Column(nullable = false)
    private String shipperId;

    @Embedded // Embed the Facility class
    private Facility facility;

    private String productType;
    private String truckType;
    private int noOfTrucks;
    private double weight;
    private String comment;

    @CreationTimestamp // Automatically set on creation
    @Column(updatable = false)
    private Timestamp datePosted;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoadStatus status = LoadStatus.POSTED; // Default status
}