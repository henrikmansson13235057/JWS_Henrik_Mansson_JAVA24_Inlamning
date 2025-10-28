package com.fulkoping.uthyrning.service;

import com.fulkoping.uthyrning.model.Person;
import com.fulkoping.uthyrning.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findById(Long id) {
        return personRepository.findById(id);
    }

    public Optional<Person> findByNamn(String namn) {
        return personRepository.findByNamn(namn);
    }

    public Person create(Person person) {
        return personRepository.save(person);
    }

    public Person update(Long id, Person updated) {
        return personRepository.findById(id)
                .map(existing -> {
                    existing.setNamn(updated.getNamn());
                    return personRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Person med ID " + id + " hittades inte"));
    }

    public void delete(Long id) {
        if (!personRepository.existsById(id)) {
            throw new IllegalArgumentException("Person med ID " + id + " hittades inte");
        }
        personRepository.deleteById(id);
    }

    public Person getOrCreateByNamn(String namn) {
        return personRepository.findByNamn(namn)
                .orElseGet(() -> {
                    Person nyPerson = new Person();
                    nyPerson.setNamn(namn);
                    return personRepository.save(nyPerson);
                });
    }
}
