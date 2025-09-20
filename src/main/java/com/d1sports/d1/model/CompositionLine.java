package com.d1sports.d1.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "composition_lines")
public class CompositionLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "composition_id", nullable = false)
    private Composition composition;

    @Enumerated(EnumType.STRING)
    @Column(name = "lane_type", nullable = false)
    private LaneType laneType;

    @Column(name = "position_order")
    private Integer positionOrder;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineOption> options = new ArrayList<>();

}

