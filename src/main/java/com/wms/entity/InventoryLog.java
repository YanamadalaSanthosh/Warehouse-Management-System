package com.wms.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="inventory_logs") @Getter @Setter @NoArgsConstructor
public class InventoryLog {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="item_id", nullable=false) private Item item;
    @Enumerated(EnumType.STRING) @Column(name="action_type", length=10, nullable=false) private ActionType actionType;
    @Column(nullable=false) private int quantity;
    @Column(nullable=false) private LocalDateTime timestamp;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id") private User user;
}
