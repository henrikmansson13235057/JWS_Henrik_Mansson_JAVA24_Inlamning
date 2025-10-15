package com.fulkoping.uthyrning.service;

import com.fulkoping.uthyrning.model.Bokning;
import com.fulkoping.uthyrning.repository.BokningRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BokningService {

    private final BokningRepository bokningRepository;

    public BokningService(BokningRepository bokningRepository) {
        this.bokningRepository = bokningRepository;
    }


    public boolean isAvailable(Bokning bokning, Long excludeId) {
        if (bokning.getStartDatum() == null || bokning.getSlutDatum() == null) {
            throw new IllegalArgumentException("Start- och slutdatum krävs");
        }
        if (bokning.getSlutDatum().isBefore(bokning.getStartDatum())) {
            throw new IllegalArgumentException("Slutdatum måste vara efter startdatum");
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



    public Bokning boka(Bokning bokning) {
        if (!isAvailable(bokning, null)) {
            throw new IllegalArgumentException("Saken är redan uthyrd under denna period");
        }
        return bokningRepository.save(bokning);
    }

    public Optional<Bokning> findById(Long id) {
        return bokningRepository.findById(id);
    }

    public Bokning update(Long id, Bokning updated) {
        return bokningRepository.findById(id).map(existing -> {
            // validate and check availability while excluding current booking id
            existing.setPerson(updated.getPerson());
            existing.setSak(updated.getSak());
            existing.setStartDatum(updated.getStartDatum());
            existing.setSlutDatum(updated.getSlutDatum());
            if (!isAvailable(existing, id)) {
                throw new IllegalArgumentException("Saken är redan uthyrd under denna period");
            }
            return bokningRepository.save(existing);
        }).orElseThrow(() -> new IllegalArgumentException("Bokning hittades inte"));
    }

    public void delete(Long id) {
        bokningRepository.deleteById(id);
    }
}
