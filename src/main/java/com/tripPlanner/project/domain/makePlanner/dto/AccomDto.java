package com.tripPlanner.project.domain.makePlanner.dto;

import com.tripPlanner.project.domain.makePlanner.entity.Accom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccomDto {
    public int id;
    public String businessName;
    public double xCoordinate;
    public double yCoordinate;
    public String locationPhoneNumber;
    public String locationPostalCode;
    public String locationFullAddress;
    public String streetPostalCode;
    public String streetFullAddress;
    public String businessCategory;
    public String hygieneCategory;

    // Dto -> Entity
    public static Accom dtoToEntity(AccomDto accomDto) {
        return Accom.builder()
                .id(accomDto.getId())
                .businessName(accomDto.getBusinessName())
                .xCoordinate(accomDto.getXCoordinate())
                .yCoordinate(accomDto.getYCoordinate())
                .locationPhoneNumber(accomDto.getLocationPhoneNumber())
                .locationPostalCode(accomDto.getLocationPostalCode())
                .locationFullAddress(accomDto.getLocationFullAddress())
                .streetPostalCode(accomDto.getStreetPostalCode())
                .streetFullAddress(accomDto.getStreetFullAddress())
                .businessCategory(accomDto.getBusinessCategory())
                .hygieneCategory(accomDto.getHygieneCategory())
                .build();
    }

    // Entity -> DTO
    public static AccomDto entityToDto(Accom accom) {
        return AccomDto.builder()
                .id(accom.getId())
                .businessName(accom.getBusinessName())
                .xCoordinate(accom.getXCoordinate())
                .yCoordinate(accom.getYCoordinate())
                .locationPhoneNumber(accom.getLocationPhoneNumber())
                .locationPostalCode(accom.getLocationPostalCode())
                .locationFullAddress(accom.getLocationFullAddress())
                .streetPostalCode(accom.getStreetPostalCode())
                .streetFullAddress(accom.getStreetFullAddress())
                .businessCategory(accom.getBusinessCategory())
                .hygieneCategory(accom.getHygieneCategory())
                .build();
    }
}
