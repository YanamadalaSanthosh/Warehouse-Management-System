package com.wms.controller;
import com.wms.dto.OrderResponseDto;
import com.wms.entity.*;
import com.wms.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/shipping") @Tag(name = "Shipping API") @SecurityRequirement(name = "bearerAuth")
public class ShippingController {
    @Autowired private OrderService orderService;
    @PostMapping("/pick/{orderId}") @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('WORKER')")
    public ResponseEntity<?> pickOrder(@PathVariable Long orderId) {
        try { return ResponseEntity.ok(OrderResponseDto.fromEntity(orderService.updateOrderStatus(orderId, OrderStatus.PICKED)));
        } catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }
    @PostMapping("/ship/{orderId}") @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('WORKER')")
    public ResponseEntity<?> shipOrder(@PathVariable Long orderId) {
        try { return ResponseEntity.ok(OrderResponseDto.fromEntity(orderService.updateOrderStatus(orderId, OrderStatus.SHIPPED)));
        } catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }
    @PostMapping("/cancel/{orderId}") @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        try { return ResponseEntity.ok(OrderResponseDto.fromEntity(orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED)));
        } catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }
}
