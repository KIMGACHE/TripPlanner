package com.tripPlanner.project.domain.makePlanner.dto;

import com.tripPlanner.project.domain.makePlanner.entity.Accom;
import com.tripPlanner.project.domain.makePlanner.entity.Food;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodDto {
    public String id;
    public String businessName;
    public double xCoordinate;
    public double yCoordinate;
    public String locationPhoneNumber;
    public String locationPostalCode;
    public String locationFullAddress;
    public String streetPostalCode;
    public String streetFullAddress;
    public String businessCategory;

    // Dto -> Entity
    public static Food dtoToEntity(FoodDto foodDto) {
        return Food.builder()
                .id(foodDto.getId())
                .businessName(foodDto.getBusinessName())
                .xCoordinate(foodDto.getXCoordinate())
                .yCoordinate(foodDto.getYCoordinate())
                .locationPhoneNumber(foodDto.getLocationPhoneNumber())
                .locationPostalCode(foodDto.getLocationPostalCode())
                .locationFullAddress(foodDto.getLocationFullAddress())
                .streetPostalCode(foodDto.getStreetPostalCode())
                .streetFullAddress(foodDto.getStreetFullAddress())
                .businessCategory(foodDto.getBusinessCategory())
                .build();
    }

    // Entity -> DTO
    public static FoodDto entityToDto(Food food) {
        return FoodDto.builder()
                .id(food.getId())
                .businessName(food.getBusinessName())
                .xCoordinate(food.getXCoordinate())
                .yCoordinate(food.getYCoordinate())
                .locationPhoneNumber(food.getLocationPhoneNumber())
                .locationPostalCode(food.getLocationPostalCode())
                .locationFullAddress(food.getLocationFullAddress())
                .streetPostalCode(food.getStreetPostalCode())
                .streetFullAddress(food.getStreetFullAddress())
                .businessCategory(food.getBusinessCategory())
                .build();
    }
}
