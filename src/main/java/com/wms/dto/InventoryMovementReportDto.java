package com.wms.dto;
import com.wms.entity.InventoryLog;
import lombok.*;
import java.time.LocalDateTime;
@Getter @Setter
public class InventoryMovementReportDto {
    private Long logId;
    private String actionType, username;
    private int quantity;
    private LocalDateTime timestamp;
    public static InventoryMovementReportDto fromEntity(InventoryLog log) {
        InventoryMovementReportDto dto = new InventoryMovementReportDto();
        dto.setLogId(log.getId()); dto.setActionType(log.getActionType().name());
        dto.setQuantity(log.getQuantity()); dto.setTimestamp(log.getTimestamp());
        dto.setUsername(log.getUser() != null ? log.getUser().getUsername() : "N/A");
        return dto;
    }
}
