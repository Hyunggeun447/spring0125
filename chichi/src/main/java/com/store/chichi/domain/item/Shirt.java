package com.store.chichi.domain.item;

import lombok.Getter;

import javax.persistence.*;

@Entity
@DiscriminatorValue("shirt")
@Getter
public class Shirt extends Item {

}
