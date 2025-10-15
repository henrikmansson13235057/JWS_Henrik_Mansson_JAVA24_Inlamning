package com.fulkoping.uthyrning;

import com.fulkoping.uthyrning.model.Sak;
import com.fulkoping.uthyrning.repository.SakRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final SakRepository sakRepository;

    public DataLoader(SakRepository sakRepository) {
        this.sakRepository = sakRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== Lagerinventering ===");

        // Skapa saker
        Sak sak1 = new Sak();
        sak1.setName("Cykel");
        sak1.setTyp("Fordon");
        sak1.setTotalQuantity(5);

        Sak sak2 = new Sak();
        sak2.setName("Stol");
        sak2.setTyp("Möbel");
        sak2.setTotalQuantity(10);

        Sak sak3 = new Sak();
        sak3.setName("Projektor");
        sak3.setTyp("Elektronik");
        sak3.setTotalQuantity(2);

        // Spara i repository
        sakRepository.save(sak1);
        sakRepository.save(sak2);
        sakRepository.save(sak3);

        // Hämta alla saker och skriv ut
        sakRepository.findAll().forEach(sak ->
                System.out.println("Sak: " + sak.getName() +
                        ", Typ: " + sak.getTyp() +
                        ", Totalt: " + sak.getTotalQuantity() +
                        ", Tillgängligt: " + sak.getAvailableQuantity())
        );

        System.out.println("=======================");
    }
}
