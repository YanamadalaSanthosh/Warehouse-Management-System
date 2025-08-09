package com.wms.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;
@Entity @Table(name="orders") @Getter @Setter @NoArgsConstructor
public class Order {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="customer_name") private String customerName;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private OrderStatus status;
    @Column(nullable=false) private LocalDateTime date;
    @OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<OrderItem> orderItems = new ArrayList<>();
    public Order(String customerName) {
        this.customerName=customerName; this.status=OrderStatus.PENDING; this.date=LocalDateTime.now();
    }
}
