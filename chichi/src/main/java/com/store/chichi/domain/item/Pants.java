package com.store.chichi.domain.item;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("pants")
@Getter
public class Pants extends Item{
}
