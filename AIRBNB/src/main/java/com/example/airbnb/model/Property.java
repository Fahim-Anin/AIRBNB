package com.example.airbnb.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int price;


//    FetchType.LAZY means: Don't go to the database to get the Owner's details
//    until I specifically ask for them in my code.
//    LAZY = Load on demand. EAGER = Load everything immediately.
//    Always use LAZY for @ManyToOne and @OneToMany relationships to keep your Airbnb clone scalable.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // --- CONSTRUCTORS ---

    /**
     * Required by Hibernate/JPA to create the object
     * when fetching data from the database.
     */
    public Property() {
    }

    /**
     * Used by you in the Service layer to create
     * a new property easily.
     */
    public Property(Long id, String name, int price, User owner) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.owner = owner;
    }

    // --- GETTERS AND SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}