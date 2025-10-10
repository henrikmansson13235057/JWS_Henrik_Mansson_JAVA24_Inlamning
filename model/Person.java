package com.fulkoping.uthyrning.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Namn Krävs")
    private String namn;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Bokning> bokningar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamn() {
        return namn;
    }

    public void setNamn(String namn) {
        this.namn = namn;
    }

    public List<Bokning> getBokningar() {
        return bokningar;
    }

    public void setBokningar(List<Bokning> bokningar) {
        this.bokningar = bokningar;
    }
}
