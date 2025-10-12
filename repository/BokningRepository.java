package com.fulkoping.uthyrning.repository;

import com.fulkoping.uthyrning.model.Bokning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BokningRepository extends JpaRepository<Bokning, Long> {
    @Query("""
SELECT COUNT(b) > 0  
FROM Bokning b
WHERE b.sak.id = :sakId
AND (b.startDatum <= :slutDatum AND b.slutDatum >= :startDatum)



""")
    boolean ärUthyrd(Long sakId, LocalDate startDatum,LocalDate slutDatum);


}
