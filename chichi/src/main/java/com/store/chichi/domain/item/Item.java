package com.store.chichi.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String itemName;

    @Enumerated
    private Size itemSize; //XXS,XS,S,M,L,XL

    @Enumerated
    private Color itemColor; //RED,BLUE,BLACK,WHITE,YELLOW

    private LocalDateTime generateTime;
    private int price;
    private int stockQuantity;


    /**
     * 재고 관련
     */

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
