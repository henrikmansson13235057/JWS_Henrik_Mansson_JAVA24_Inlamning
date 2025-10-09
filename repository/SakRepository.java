package com.fulkoping.uthyrning.repository;

import com.fulkoping.uthyrning.model.Sak;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SakRepository extends JpaRepository<Sak, Long> {
}
