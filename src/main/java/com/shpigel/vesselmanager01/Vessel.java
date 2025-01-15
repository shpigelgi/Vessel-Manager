package com.shpigel.vesselmanager01;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;


@Entity
public class Vessel {
    @Id
    private UUID id = UUID.randomUUID(); // id of vessel - automatically generated

    @Column(nullable = false)
    private String type; // type of vessel, limited to a set of values

    @Column(nullable = false)
    private String color; // color of vessel

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

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
