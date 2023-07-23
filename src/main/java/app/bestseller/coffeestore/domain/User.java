package app.bestseller.coffeestore.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;


@Getter
@Setter
@ToString
@Table(name = "bs_users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Size(min = 5, max = 50)
    @Column(name = "username")
    private String username;
    @Column(name = "created")
    @CreationTimestamp
    private Instant created;
    @Column(name = "changed")
    @UpdateTimestamp
    private Instant changed;
}
