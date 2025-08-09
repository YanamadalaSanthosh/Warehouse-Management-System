package com.wms.dto;
import jakarta.validation.constraints.*;
import lombok.*;
@Getter @Setter
public class ItemDto {
    @NotBlank String name;
    @NotBlank String sku;
    @Min(0) int quantity;
    String location;
    String category;
    @Min(0) int threshold;
}
