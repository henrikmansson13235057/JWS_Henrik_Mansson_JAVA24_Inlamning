package com.fulkoping.uthyrning.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
public class Sak {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message= " Namnet får inte vara tomt")
    private String name;

    private String typ;


    @OneToMany(mappedBy = "sak", cascade = CascadeType.ALL)
    private List<Bokning> bokningar;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public List<Bokning> getBokningar() {
        return bokningar;
    }

    public void setBokningar(List<Bokning> bokningar) {
        this.bokningar = bokningar;
    }
}
