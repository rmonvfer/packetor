package es.uniovi.eii.paquetor.entities.locations;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * Define una Ubicación en el mapa
 */
@Entity
@Data
@Accessors(chain = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "location_type")
public class Location {

    public Location() {
        setCity(new City());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "CALLE", nullable = false)
    private String calle;

    @Column(name = "NUMERO", nullable = false)
    private int numero;

    @Column(name = "PISO")
    private String piso;

    @Column(name = "PUERTA")
    private String puerta;

    @Column(name = "CODIGO_POSTAL", nullable = false)
    private int codigoPostal;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
}
