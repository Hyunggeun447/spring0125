package com.store.chichi.domain.item;

import com.store.chichi.domain.Order;
import com.store.chichi.domain.OrderItem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

}
