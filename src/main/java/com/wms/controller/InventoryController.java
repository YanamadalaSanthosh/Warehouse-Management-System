package com.wms.controller;
import com.wms.dto.*;
import com.wms.entity.Item;
import com.wms.service.InventoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/inventory") @Tag(name = "Inventory API") @SecurityRequirement(name = "bearerAuth")
public class InventoryController {
    @Autowired private InventoryService inventoryService;
    @GetMapping("/items") @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('WORKER')")
    public List<Item> getAllItems() { return inventoryService.getAllItems(); }
    @GetMapping("/items/{id}") @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('WORKER')")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return inventoryService.getItemById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/items") @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createItem(@Valid @RequestBody ItemDto itemDto) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Item newItem = inventoryService.createItem(itemDto, username);
            return new ResponseEntity<>(newItem, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }
    @PutMapping("/items/{id}") @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @Valid @RequestBody ItemDto itemDto) {
        try { return ResponseEntity.ok(inventoryService.updateItem(id, itemDto));
        } catch (Exception e) { return ResponseEntity.notFound().build(); }
    }
    @DeleteMapping("/items/{id}") @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        try { inventoryService.deleteItem(id); return ResponseEntity.noContent().build();
        } catch (Exception e) { return ResponseEntity.notFound().build(); }
    }
    @PostMapping("/receive") @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('WORKER')")
    public ResponseEntity<?> receiveStock(@Valid @RequestBody StockUpdateRequest request) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(inventoryService.receiveStock(request.getItemId(), request.getQuantity(), username));
        } catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }
    @PostMapping("/deduct") @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('WORKER')")
    public ResponseEntity<?> deductStock(@Valid @RequestBody StockUpdateRequest request) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(inventoryService.deductStock(request.getItemId(), request.getQuantity(), username));
        } catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }
}
