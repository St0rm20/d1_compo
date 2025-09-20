package com.d1sports.d1.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "line_options")
public class LineOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "line_id", nullable = false)
    private CompositionLine line;

    @ManyToOne
    @JoinColumn(name = "champion_id", nullable = false)
    private Champion champion;

    @Column(name = "option_order")
    private Integer optionOrder;

}