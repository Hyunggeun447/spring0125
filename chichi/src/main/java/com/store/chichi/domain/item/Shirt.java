package com.store.chichi.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@DiscriminatorValue("shirt")
@Getter
@Setter
public class Shirt extends Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String itemName;

    private int itemPrice;

    private int stockQuantity;
}
