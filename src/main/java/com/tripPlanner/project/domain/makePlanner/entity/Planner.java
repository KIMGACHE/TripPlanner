package com.tripPlanner.project.domain.makePlanner.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="tbl_planner")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Planner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plannerid")
    private Long plannerid;

    @Column(name="userid")
//    @ManyToOne(fetch= FetchType.LAZY)
//    @JoinColumn(name ="userid", foreignKey = @ForeignKey(name="FK_PLANNER_USER",
//            foreignKeyDefinition = "FOREIGN KEY(userid) REFERENCES User(userid) ON DELETE CASCADE ON UPDATE CASCADE"))
    private String userid;

    @Column(name = "createAt")
    private LocalDate createAt;

    @Column(name = "updateAt")
    private LocalDate updateAt;

    @Column(name = "duration")
    private int duration;

    @Column(name = "people")
    private int people;
}
