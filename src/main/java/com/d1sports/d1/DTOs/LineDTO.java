package com.d1sports.d1.DTOs;

import com.d1sports.d1.model.LaneType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LineDTO {
    private LaneType laneType;
    private List<ChampionOptionDTO> options;

    // Getters y setters
}
