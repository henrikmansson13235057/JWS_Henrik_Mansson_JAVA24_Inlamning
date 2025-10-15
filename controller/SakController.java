package com.fulkoping.uthyrning.controller;

import com.fulkoping.uthyrning.dto.RentalRequest;
import com.fulkoping.uthyrning.model.Bokning;
import com.fulkoping.uthyrning.model.Person;
import com.fulkoping.uthyrning.repository.BokningRepository;
import com.fulkoping.uthyrning.repository.PersonRepository;
import com.fulkoping.uthyrning.repository.SakRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/saker")
public class SakController {

    private final SakRepository sakRepository;
    private final PersonRepository personRepository;
    private final BokningRepository bokningRepository;

    public SakController(SakRepository sakRepository,
                         PersonRepository personRepository,
                         BokningRepository bokningRepository) {
        this.sakRepository = sakRepository;
        this.personRepository = personRepository;
        this.bokningRepository = bokningRepository;
    }

    @Operation(summary = "Visa lagerstatus")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista med saker och tillgänglig kvantitet returneras")
    })
    @GetMapping("/lager")
    public List<String> showInventory() {
        return sakRepository.findAll().stream()
                .map(s -> s.getId() + ": " + s.getName() +
                        " (" + s.getAvailableQuantity() + "/" + s.getTotalQuantity() + " tillgängliga)")
                .toList();
    }


    @Operation(summary = "Boka en sak")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Saken bokades framgångsrikt"),
            @ApiResponse(responseCode = "409", description = "Saken är redan bokad"),
            @ApiResponse(responseCode = "404", description = "Saken hittades inte")
    })
    @PostMapping("/valj/{id}")
    public ResponseEntity<?> rentItem(@PathVariable Long id,
                                      @Valid @RequestBody RentalRequest request) {
        return sakRepository.findById(id)
                .map(sak -> {
                    if (sak.getAvailableQuantity() > 0) {
                        Person person = personRepository.findByNamn(request.getNamn())
                                .orElseGet(() -> {
                                    Person nyPerson = new Person();
                                    nyPerson.setNamn(request.getNamn());
                                    return personRepository.save(nyPerson);
                                });

                        Bokning bokning = new Bokning();
                        bokning.setPerson(person);
                        bokning.setSak(sak);
                        bokning.setStartDatum(request.getStartDatum());
                        bokning.setSlutDatum(request.getSlutDatum());
                        bokningRepository.save(bokning);

                        sak.setAvailableQuantity(sak.getAvailableQuantity() - 1);
                        sakRepository.save(sak);

                        return ResponseEntity.ok(Map.of(
                                "namn", person.getNamn(),
                                "sak", sak.getName(),
                                "typ", sak.getTyp(),
                                "startDatum", bokning.getStartDatum(),
                                "slutDatum", bokning.getSlutDatum()
                        ));
                    } else {
                        LocalDate nextAvailableDate = sak.getBokningar().stream()
                                .map(Bokning::getSlutDatum)
                                .min(LocalDate::compareTo)
                                .orElse(null);

                        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                                "message", "Allt är redan bokat",
                                "nextAvailableDate", nextAvailableDate
                        ));
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
