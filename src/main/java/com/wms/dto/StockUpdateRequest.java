package com.wms.dto;
import jakarta.validation.constraints.*;
import lombok.*;
@Getter @Setter
public class StockUpdateRequest {
    @NotNull Long itemId;
    @Min(1) int quantity;
}
