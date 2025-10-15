package com.fulkoping.uthyrning;

import com.fulkoping.uthyrning.model.Sak;
import com.fulkoping.uthyrning.repository.SakRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InventoryPrinter implements CommandLineRunner {

    private final SakRepository sakRepository;

    public InventoryPrinter(SakRepository sakRepository) {
        this.sakRepository = sakRepository;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== Lagerinventering ===");

        sakRepository.findAll().forEach(sak -> {
            System.out.println(
                    sak.getId() + ": " + sak.getName() +
                            " (" + (sak.getBokningar() != null ? sak.getBokningar().size() : 0) + " bokade)"
            );
        });

        System.out.println("=======================");
    }
}
