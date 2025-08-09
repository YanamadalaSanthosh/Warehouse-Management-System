package com.wms.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
@Entity @Table(name="items") @Getter @Setter @NoArgsConstructor
public class Item {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotBlank @Column(nullable=false) private String name;
    @NotBlank @Column(unique=true, nullable=false) private String sku;
    @NotNull @Min(0) private int quantity;
    private String location;
    private String category;
    @NotNull @Min(0) private int threshold;
}
