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
public class CompositionDTO {
    private Long id;
    private String title;
    private String observations;
    private List<LineDTO> lines;

}

