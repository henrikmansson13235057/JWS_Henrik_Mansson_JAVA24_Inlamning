package com.fulkoping.uthyrning.repository;

import com.fulkoping.uthyrning.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByNamn(String namn);
}


