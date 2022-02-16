package com.store.chichi.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("pants")
@Getter
@Setter
public class Pants extends Item{
}
