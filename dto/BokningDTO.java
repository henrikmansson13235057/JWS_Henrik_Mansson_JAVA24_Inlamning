package com.fulkoping.uthyrning.dto;

import java.time.LocalDate;

public class BokningDTO {
    private Long id;
    private Long personId;
    private Long sakId;
    private LocalDate startDatum;
    private LocalDate slutDatum;
    public BokningDTO() {

    }

    public BokningDTO(Long id, Long personId, Long sakId, LocalDate startDatum, LocalDate slutDatum) {
        this.id = id;
        this.personId = personId;
        this.sakId = sakId;
        this.startDatum = startDatum;
        this.slutDatum = slutDatum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getSakId() {
        return sakId;
    }

    public void setSakId(Long sakId) {
        this.sakId = sakId;
    }

    public LocalDate getStartDatum() {
        return startDatum;
    }

    public void setStartDatum(LocalDate startDatum) {
        this.startDatum = startDatum;
    }

    public LocalDate getSlutDatum() {
        return slutDatum;
    }

    public void setSlutDatum(LocalDate slutDatum) {
        this.slutDatum = slutDatum;
    }
}
