package com.fulkoping.uthyrning.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class Sak {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Namnet får inte vara tomt")
    private String name;

    private String typ;
    private int totalQuantity;
    private int availableQuantity;

    @OneToMany(mappedBy = "sak", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Bokning> bokningar;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTyp() { return typ; }
    public void setTyp(String typ) { this.typ = typ; }
    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
        this.availableQuantity = totalQuantity;
    }
    public int getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(int availableQuantity) { this.availableQuantity = availableQuantity; }
    public List<Bokning> getBokningar() { return bokningar; }
    public void setBokningar(List<Bokning> bokningar) { this.bokningar = bokningar; }
}
