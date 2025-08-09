package com.wms.dto;
import com.wms.entity.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Getter @Setter
public class OrderResponseDto {
    private Long id;
    private String customerName;
    private OrderStatus status;
    private LocalDateTime date;
    private List<OrderItemDetailDto> items;
    @Getter @Setter public static class OrderItemDetailDto {
        private Long itemId; String itemName, sku; int quantity;
    }
    public static OrderResponseDto fromEntity(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId()); dto.setCustomerName(order.getCustomerName());
        dto.setStatus(order.getStatus()); dto.setDate(order.getDate());
        dto.setItems(order.getOrderItems().stream().map(oi -> {
            OrderItemDetailDto itemDto = new OrderItemDetailDto();
            itemDto.setItemId(oi.getItem().getId()); itemDto.setItemName(oi.getItem().getName());
            itemDto.setSku(oi.getItem().getSku()); itemDto.setQuantity(oi.getQuantity());
            return itemDto;
        }).collect(Collectors.toList()));
        return dto;
    }
}
