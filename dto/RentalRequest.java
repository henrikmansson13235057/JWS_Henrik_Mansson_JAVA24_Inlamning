package com.fulkoping.uthyrning.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class RentalRequest {

    @NotBlank(message = "Namnet måste anges")
    private String namn;

    private LocalDate startDatum;
    private LocalDate slutDatum;

    public String getNamn() { return namn; }
    public void setNamn(String namn) { this.namn = namn; }
    public LocalDate getStartDatum() { return startDatum; }
    public void setStartDatum(LocalDate startDatum) { this.startDatum = startDatum; }
    public LocalDate getSlutDatum() { return slutDatum; }
    public void setSlutDatum(LocalDate slutDatum) { this.slutDatum = slutDatum; }
}
