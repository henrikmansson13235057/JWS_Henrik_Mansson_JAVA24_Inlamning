package com.fulkoping.uthyrning.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Bokning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Person person;

    @ManyToOne
    private Sak sak;

    private LocalDate startDatum;
    private LocalDate slutDatum;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Person getPerson() { return person; }
    public void setPerson(Person person) { this.person = person; }

    public Sak getSak() { return sak; }
    public void setSak(Sak sak) { this.sak = sak; }

    public LocalDate getStartDatum() { return startDatum; }
    public void setStartDatum(LocalDate startDatum) { this.startDatum = startDatum; }

    public LocalDate getSlutDatum() { return slutDatum; }
    public void setSlutDatum(LocalDate slutDatum) { this.slutDatum = slutDatum; }
}
