package com.d1sports.d1.services;

import com.d1sports.d1.DTOs.ChampionOptionDTO;
import com.d1sports.d1.DTOs.CompositionDTO;
import com.d1sports.d1.DTOs.LineDTO;
import com.d1sports.d1.model.*;
import com.d1sports.d1.repositories.ChampionRepository;
import com.d1sports.d1.repositories.CompositionRepository;
import jakarta.transaction.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CompositionService {

    private final CompositionRepository compositionRepository;
    private final ChampionRepository championRepository;
    private final String adminPassword = "d1tupapa";

    public CompositionService(CompositionRepository compositionRepository,
                              ChampionRepository championRepository) {
        this.compositionRepository = compositionRepository;
        this.championRepository = championRepository;
    }

    public boolean validatePassword(String password) {
        return adminPassword.equals(password);
    }

    public Page<Composition> searchCompositions(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            return compositionRepository.findByTitleContainingIgnoreCase(searchTerm, pageable);
        }
        return compositionRepository.findAll(pageable);
    }

    public Composition createComposition(CompositionDTO compositionDTO) {
        if (compositionRepository.existsByTitle(compositionDTO.getTitle())) {
            throw new RuntimeException("Ya existe una composición con ese título");
        }

        Composition composition = new Composition();
        composition.setTitle(compositionDTO.getTitle());
        composition.setObservations(compositionDTO.getObservations());

        // Procesar líneas
        for (LineDTO lineDTO : compositionDTO.getLines()) {
            CompositionLine line = new CompositionLine();
            line.setComposition(composition);
            line.setLaneType(lineDTO.getLaneType());
            line.setPositionOrder(getOrderForLane(lineDTO.getLaneType()));

            // Procesar opciones
            for (ChampionOptionDTO optionDTO : lineDTO.getOptions()) {
                Champion champion = championRepository.findById(optionDTO.getChampionId())
                        .orElseThrow(() -> new RuntimeException("Campeón no encontrado"));

                LineOption option = new LineOption();
                option.setLine(line);
                option.setChampion(champion);
                option.setOptionOrder(optionDTO.getOrder());

                line.getOptions().add(option);
            }

            composition.getLines().add(line);
        }

        return compositionRepository.save(composition);
    }

    private int getOrderForLane(LaneType laneType) {
        return switch (laneType) {
            case TOP -> 1;
            case JG -> 2;
            case MID -> 3;
            case ADC -> 4;
            case SUPP -> 5;
        };
    }

    public void deleteComposition(Long id) {
        compositionRepository.deleteById(id);
    }

    public List<Champion> searchChampions(String query) {
        if (query == null || query.trim().isEmpty()) {
            return championRepository.findAllByOrderByName();
        }
        return championRepository.findByNameContainingIgnoreCase(query);
    }

    public Composition updateComposition(Long id, CompositionDTO compositionDTO) {
        Composition existingComposition = compositionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Composición no encontrada con ID: " + id));

        // Verificar si el nuevo título ya existe (excluyendo la composición actual)
        if (!existingComposition.getTitle().equals(compositionDTO.getTitle()) &&
                compositionRepository.existsByTitle(compositionDTO.getTitle())) {
            throw new RuntimeException("Ya existe una composición con ese título");
        }

        // Actualizar campos básicos
        existingComposition.setTitle(compositionDTO.getTitle());
        existingComposition.setObservations(compositionDTO.getObservations());

        // Limpiar las líneas existentes para reconstruirlas
        existingComposition.getLines().clear();

        // Procesar nuevas líneas
        for (LineDTO lineDTO : compositionDTO.getLines()) {
            CompositionLine line = new CompositionLine();
            line.setComposition(existingComposition);
            line.setLaneType(lineDTO.getLaneType());
            line.setPositionOrder(getOrderForLane(lineDTO.getLaneType()));

            // Procesar opciones de campeones
            for (ChampionOptionDTO optionDTO : lineDTO.getOptions()) {
                Champion champion = championRepository.findById(optionDTO.getChampionId())
                        .orElseThrow(() -> new RuntimeException("Campeón no encontrado con ID: " + optionDTO.getChampionId()));

                LineOption option = new LineOption();
                option.setLine(line);
                option.setChampion(champion);
                option.setOptionOrder(optionDTO.getOrder());

                line.getOptions().add(option);
            }

            existingComposition.getLines().add(line);
        }

        return compositionRepository.save(existingComposition);
    }
}