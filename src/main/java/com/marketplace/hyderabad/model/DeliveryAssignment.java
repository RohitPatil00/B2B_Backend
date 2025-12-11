package com.marketplace.hyderabad.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "delivery_assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long deliveryAgentId;
    private Long orderId;
    private String status; // ASSIGNED, PICKED_UP, IN_TRANSIT, DELIVERED
    private Instant assignedAt;
}
