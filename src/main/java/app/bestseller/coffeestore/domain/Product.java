package app.bestseller.coffeestore.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

/**
 * Created by Abe with ❤️.
 */

@Data
@Table(name = "bs_product")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Product {

    public enum Type {
        DRINK,
        TOPPING
    }

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    @Builder.Default
    private Double price = 0.0;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;
    @Column(name = "created")
    @CreationTimestamp
    private Date created;
    @Column(name = "changed")
    @UpdateTimestamp
    private Date changed;
}
