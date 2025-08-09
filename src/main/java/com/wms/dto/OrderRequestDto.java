package com.wms.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;
@Getter @Setter
public class OrderRequestDto {
    @NotBlank String customerName;
    @NotEmpty @Valid List<OrderItemRequestDto> items;
}
