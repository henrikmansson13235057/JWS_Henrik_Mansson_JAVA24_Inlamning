package com.fulkoping.uthyrning.service;

import com.fulkoping.uthyrning.dto.RentalRequest;
import com.fulkoping.uthyrning.model.Bokning;
import com.fulkoping.uthyrning.model.Person;
import com.fulkoping.uthyrning.model.Sak;
import com.fulkoping.uthyrning.repository.BokningRepository;
import com.fulkoping.uthyrning.repository.SakRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SakService {

    private final SakRepository sakRepository;
    private final BokningRepository bokningRepository;
    private final PersonService personService;

    public SakService(SakRepository sakRepository,
                      BokningRepository bokningRepository,
                      PersonService personService) {
        this.sakRepository = sakRepository;
        this.bokningRepository = bokningRepository;
        this.personService = personService;
    }

    public List<Sak> getAll() {
        return sakRepository.findAll();
    }

    public Optional<Sak> findById(Long id) {
        return sakRepository.findById(id);
    }

    public Sak create(Sak sak) {
        return sakRepository.save(sak);
    }

    public Sak update(Long id, Sak updated) {
        return sakRepository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setTyp(updated.getTyp());
                    existing.setTotalQuantity(updated.getTotalQuantity());
                    existing.setAvailableQuantity(updated.getAvailableQuantity());
                    return sakRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Sak med ID " + id + " hittades inte"));
    }

    public void delete(Long id) {
        if (!sakRepository.existsById(id)) {
            throw new IllegalArgumentException("Sak med ID " + id + " hittades inte");
        }
        sakRepository.deleteById(id);
    }

    public List<String> getInventoryStatus() {
        return sakRepository.findAll().stream()
                .map(s -> s.getId() + ": " + s.getName() +
                        " (" + s.getAvailableQuantity() + "/" + s.getTotalQuantity() + " tillgängliga)")
                .toList();
    }

    @Transactional
    public Map<String, Object> rentItem(Long sakId, RentalRequest request) {
        Sak sak = sakRepository.findById(sakId)
                .orElseThrow(() -> new IllegalArgumentException("Sak med ID " + sakId + " hittades inte"));

        if (sak.getAvailableQuantity() <= 0) {
            LocalDate nextAvailableDate = sak.getBokningar().stream()
                    .map(Bokning::getSlutDatum)
                    .min(LocalDate::compareTo)
                    .orElse(null);

            throw new IllegalStateException("Allt är redan bokat. Nästa tillgängliga datum: " + nextAvailableDate);
        }

        Person person = personService.getOrCreateByNamn(request.getNamn());

        Bokning bokning = new Bokning();
        bokning.setPerson(person);
        bokning.setSak(sak);
        bokning.setStartDatum(request.getStartDatum());
        bokning.setSlutDatum(request.getSlutDatum());
        bokningRepository.save(bokning);

        sak.setAvailableQuantity(sak.getAvailableQuantity() - 1);
        sakRepository.save(sak);

        return Map.of(
                "namn", person.getNamn(),
                "sak", sak.getName(),
                "typ", sak.getTyp(),
                "startDatum", bokning.getStartDatum(),
                "slutDatum", bokning.getSlutDatum()
        );
    }
}
