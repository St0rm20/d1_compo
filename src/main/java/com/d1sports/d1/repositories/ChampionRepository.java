package com.d1sports.d1.repositories;

import com.d1sports.d1.model.Champion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChampionRepository extends JpaRepository<Champion, Long> {
    List<Champion> findByNameContainingIgnoreCase(String name);
    List<Champion> findAllByOrderByName();
}
