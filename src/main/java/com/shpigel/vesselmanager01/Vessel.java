package com.shpigel.vesselmanager01;

import jakarta.persistence.*;

import java.util.UUID;


@Entity
public class Vessel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String color;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    //TODO: add a validation that the type is correct
    public void setType(String name) {
        this.type = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
