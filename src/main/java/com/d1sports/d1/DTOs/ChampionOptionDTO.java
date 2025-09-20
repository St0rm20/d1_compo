package com.d1sports.d1.DTOs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChampionOptionDTO {
    private Long championId;
    private String championName;
    private String championImage;
    private Integer order;

    // Getters y setters
}
