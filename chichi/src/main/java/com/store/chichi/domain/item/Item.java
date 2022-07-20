package com.store.chichi.domain.item;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String itemName;

    @Enumerated
    private Size itemSize;

    @Enumerated
    private Color itemColor;

    private LocalDateTime generateTime;
    private int price;
    private int stockQuantity;

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        if (stockQuantity < quantity) {
            throw new RuntimeException("재고부족");
        }
        this.stockQuantity -= quantity;
    }

}
