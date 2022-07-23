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

    private LocalDateTime generateTime = LocalDateTime.now();

    private Integer price;

    private Integer stockQuantity;

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        if (stockQuantity < quantity) {
            throw new RuntimeException("재고부족");
        }
        this.stockQuantity -= quantity;
    }

    public void changeItemName(String newItemName) {
        this.itemName = newItemName;
    }

    public void changeItemSize(Size newItemSize) {
        this.itemSize = newItemSize;
    }

    public void changePrice(Integer newPrice) {
        this.price = newPrice;
    }

    public void changeStockQuantity(Integer newStockQuantity) {
        this.stockQuantity = newStockQuantity;
    }

    public void changeColor(Color newColor) {
        this.itemColor = newColor;
    }
}
