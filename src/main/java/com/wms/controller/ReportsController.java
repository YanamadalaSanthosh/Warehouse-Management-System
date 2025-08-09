package com.wms.controller;
import com.wms.dto.*;
import com.wms.service.ReportsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/reports") @Tag(name = "Reports API") @SecurityRequirement(name = "bearerAuth")
public class ReportsController {
    @Autowired private ReportsService reportsService;
    @GetMapping("/low-stock") @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('WORKER')")
    public ResponseEntity<List<LowStockReportDto>> getLowStockReport() {
        return ResponseEntity.ok(reportsService.getLowStockItemsReport());
    }
    @GetMapping("/inventory-movement/{itemId}") @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('WORKER')")
    public ResponseEntity<List<InventoryMovementReportDto>> getInventoryMovementReport(@PathVariable Long itemId) {
        try { return ResponseEntity.ok(reportsService.getInventoryMovementReport(itemId));
        } catch (Exception e) { return ResponseEntity.notFound().build(); }
    }
}
