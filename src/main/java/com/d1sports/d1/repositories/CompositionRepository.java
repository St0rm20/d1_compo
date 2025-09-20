package com.d1sports.d1.repositories;

import com.d1sports.d1.model.Champion;
import com.d1sports.d1.model.Composition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompositionRepository extends JpaRepository<Composition, Long> {
    Page<Composition> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    boolean existsByTitle(String title);
}

