package app.bestseller.coffeestore.domain;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Abe with ❤️.
 */


@Data
@Table(name = "bs_order")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String trackingNumber;
    @Column
    @Builder.Default
    private Double total = 0.0;
    @Column
    @Builder.Default
    private Double discount = 0.0;
    @Column
    @Builder.Default
    private Double finalPrice = 0.0;
    @ManyToMany
    @Builder.Default
    private List<Product> products = new ArrayList<>();
    @ManyToOne
    private User user;
    @Column(name = "created")
    @CreationTimestamp
    private Date created;
    @Column(name = "changed")
    @UpdateTimestamp
    private Date changed;
}
