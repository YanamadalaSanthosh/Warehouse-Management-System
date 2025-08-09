package com.wms.controller;
import com.wms.dto.*;
import com.wms.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
@RestController @RequestMapping("/api/orders") @Tag(name = "Orders API") @SecurityRequirement(name = "bearerAuth")
public class OrderController {
    @Autowired private OrderService orderService;
    @PostMapping @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('WORKER')")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequestDto orderRequest) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return new ResponseEntity<>(OrderResponseDto.fromEntity(orderService.createOrder(orderRequest, username)), HttpStatus.CREATED);
        } catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }
    @GetMapping @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('WORKER')")
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders().stream().map(OrderResponseDto::fromEntity).collect(Collectors.toList());
    }
    @GetMapping("/{id}") @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('WORKER')")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id).map(order -> ResponseEntity.ok(OrderResponseDto.fromEntity(order))).orElse(ResponseEntity.notFound().build());
    }
}
