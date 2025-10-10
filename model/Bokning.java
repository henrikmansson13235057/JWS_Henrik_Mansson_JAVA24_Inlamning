package com.fulkoping.uthyrning.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class Bokning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "sak_id")
    private Sak sak;


    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;


    @NotNull
    private LocalDate startDatum;

    @NotNull
    private LocalDate slutDatum;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sak getSak() {
        return sak;
    }

    public void setSak(Sak sak) {
        this.sak = sak;
    }

    public LocalDate getStartDatum() {
        return startDatum;
    }

    public void setStartDatum(LocalDate startDatum) {
        this.startDatum = startDatum;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public LocalDate getSlutDatum() {
        return slutDatum;
    }

    public void setSlutDatum(LocalDate slutDatum) {
        this.slutDatum = slutDatum;
    }
}
