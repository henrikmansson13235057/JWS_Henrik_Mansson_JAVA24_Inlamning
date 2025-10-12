package com.fulkoping.uthyrning.service;

import com.fulkoping.uthyrning.model.Bokning;
import com.fulkoping.uthyrning.repository.BokningRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BokningService {
    private final BokningRepository bokningRepository;

    public BokningService(BokningRepository bokningRepository) {
        this.bokningRepository = bokningRepository;
    }

    public boolean isAvailble(Bokning bokning) {
        List<Bokning> existing = bokningRepository.findAll();
        for (Bokning b : existing) {

        }
    }
}
