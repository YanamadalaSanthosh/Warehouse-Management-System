package com.wms.dto;
import com.wms.entity.Item;
import lombok.*;
@Getter @Setter
public class LowStockReportDto {
    private Long itemId;
    private String name, sku, location;
    private int quantity, threshold;
    public static LowStockReportDto fromEntity(Item item) {
        LowStockReportDto dto = new LowStockReportDto();
        dto.setItemId(item.getId()); dto.setName(item.getName()); dto.setSku(item.getSku());
        dto.setQuantity(item.getQuantity()); dto.setThreshold(item.getThreshold()); dto.setLocation(item.getLocation());
        return dto;
    }
}
