package com.fulkoping.uthyrning.service;

import com.fulkoping.uthyrning.model.Bokning;
import com.fulkoping.uthyrning.model.Person;
import com.fulkoping.uthyrning.model.Sak;
import com.fulkoping.uthyrning.repository.BokningRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class BokningService {

    private final BokningRepository bokningRepository;
    private final PersonService personService;
    private final SakService sakService;

    public BokningService(
        BokningRepository bokningRepository,
        PersonService personService,
        SakService sakService
    ) {
        this.bokningRepository = bokningRepository;
        this.personService = personService;
        this.sakService = sakService;
    }

    public boolean isAvailable(Bokning bokning, Long excludeId) {
        if (bokning.getStartDatum() == null || bokning.getSlutDatum() == null) {
            throw new IllegalArgumentException("Start- och slutdatum krävs");
        }
        if (bokning.getSlutDatum().isBefore(bokning.getStartDatum())) {
            throw new IllegalArgumentException(
                "Slutdatum måste vara efter startdatum"
            );
        }
        if (bokning.getSak() == null || bokning.getSak().getId() == null) {
            throw new IllegalArgumentException("Sak-id krävs");
        }
        return !bokningRepository.overlapExists(
            bokning.getSak().getId(),
            bokning.getStartDatum(),
            bokning.getSlutDatum(),
            excludeId
        );
    }

    public List<Bokning> getAll() {
        return bokningRepository.findAll();
    }

    @Transactional
    public Bokning boka(Bokning bokning) {
        // Validera och hämta person
        if (
            bokning.getPerson() == null || bokning.getPerson().getId() == null
        ) {
            throw new IllegalArgumentException("Person-id krävs");
        }
        Person person = personService
            .findById(bokning.getPerson().getId())
            .orElseThrow(() ->
                new IllegalArgumentException("Person hittades inte")
            );

        // Validera och hämta sak
        if (bokning.getSak() == null || bokning.getSak().getId() == null) {
            throw new IllegalArgumentException("Sak-id krävs");
        }
        Sak sak = sakService
            .findById(bokning.getSak().getId())
            .orElseThrow(() ->
                new IllegalArgumentException("Sak hittades inte")
            );

        // Kontrollera tillgänglighet
        if (!isAvailable(bokning, null)) {
            throw new IllegalArgumentException(
                "Saken är redan uthyrd under denna period"
            );
        }

        // Sätt relationerna
        bokning.setPerson(person);
        bokning.setSak(sak);

        return bokningRepository.save(bokning);
    }

    public Optional<Bokning> findById(Long id) {
        return bokningRepository.findById(id);
    }

    @Transactional
    public Bokning update(Long id, Bokning updated) {
        return bokningRepository
            .findById(id)
            .map(existing -> {
                // Validera och uppdatera person
                if (
                    updated.getPerson() != null &&
                    updated.getPerson().getId() != null
                ) {
                    Person person = personService
                        .findById(updated.getPerson().getId())
                        .orElseThrow(() ->
                            new IllegalArgumentException("Person hittades inte")
                        );
                    existing.setPerson(person);
                }

                // Validera och uppdatera sak
                if (
                    updated.getSak() != null && updated.getSak().getId() != null
                ) {
                    Sak sak = sakService
                        .findById(updated.getSak().getId())
                        .orElseThrow(() ->
                            new IllegalArgumentException("Sak hittades inte")
                        );
                    existing.setSak(sak);
                }

                existing.setStartDatum(updated.getStartDatum());
                existing.setSlutDatum(updated.getSlutDatum());

                if (!isAvailable(existing, id)) {
                    throw new IllegalArgumentException(
                        "Saken är redan uthyrd under denna period"
                    );
                }

                return bokningRepository.save(existing);
            })
            .orElseThrow(() ->
                new IllegalArgumentException("Bokning hittades inte")
            );
    }

    @Transactional
    public void delete(Long id) {
        bokningRepository
            .findById(id)
            .ifPresent(bokning -> {
                if (bokning.getSak() != null) {
                    var sak = bokning.getSak();
                    sak.setAvailableQuantity(sak.getAvailableQuantity() + 1);
                }
                bokningRepository.delete(bokning);
            });
    }
}
