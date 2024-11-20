package com.josefy.nnpda.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "sensors")
@NamedEntityGraph(
        name = "sensor-with-device",
        attributeNodes = @NamedAttributeNode("device")
)
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(length = 12, name = "serial_number", nullable = false, unique = true)
    private String serialNumber;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    public Sensor(String name, String serialNumber) {
        this.name = name;
        this.serialNumber = serialNumber;
    }
}
