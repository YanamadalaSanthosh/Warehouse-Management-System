package com.wms.dto;
import jakarta.validation.constraints.*;
import lombok.*;
@Getter @Setter
public class OrderItemRequestDto { @NotNull Long itemId; @Min(1) int quantity; }
