package com.josefy.nnpda.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyToOne;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "devices")
@NamedEntityGraph(
        name = "device-with-sensors",
        attributeNodes = @NamedAttributeNode("sensors")
)
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(length = 12, name = "serial_number", nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String serialNumber;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @OneToOne(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DeviceCredential deviceCredential;

    @OneToMany(mappedBy = "device", fetch = FetchType.LAZY)
    private List<Sensor> sensors = new ArrayList<>();

    public Device(String serialNumber, String modelName) {
        this.serialNumber = serialNumber;
        this.modelName = modelName;
    }
}
