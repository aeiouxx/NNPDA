package com.josefy.nnpda.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.metamodel.mapping.ValueMapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

/*
 * it might make more sense to have a separate table per data type and retention period
 * but for the sake of simplicity, we'll just have one table for all data types and retention periods
 */
@Entity
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "measurements",
        indexes = {
        @Index(name = "idx_sensor_id", columnList = "sensor_id"),
        @Index(name = "idx_timestamp", columnList = "timestamp")} ,
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_sensor_timestamp", columnNames = {"sensor_id", "timestamp"})
        }
)
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime timestamp;

    @Column(nullable = false)
    private Double value;
}
