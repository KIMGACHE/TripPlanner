package com.tripPlanner.project.domain.makePlanner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_accom")
@Builder
public class Accom {
    @Id
    @Column(name="id")
    private int id;

    @Column(name="business_name")
    private String businessName;

    @Column(name="x_coordinate")
    private double xCoordinate;

    @Column(name="y_coordinate")
    private double yCoordinate;

    @Column(name="location_phone_number")
    private String locationPhoneNumber;

    @Column(name="location_postal_code")
    private String locationPostalCode;

    @Column(name="location_full_address")
    private String locationFullAddress;

    @Column(name="street_postal_code")
    private String streetPostalCode;

    @Column(name="street_full_address")
    private String streetFullAddress;

    @Column(name="business_category")
    private String businessCategory;

    @Column(name="hygiene_category")
    private String hygieneCategory;
}
