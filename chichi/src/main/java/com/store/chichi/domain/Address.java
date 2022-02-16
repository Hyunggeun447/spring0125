package com.store.chichi.domain;

import lombok.Getter;
import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String address1;
    private String address2; //detail address

    protected Address() {
    }

    public Address(String address1, String address2) {
        this.address1 = address1;
        this.address2 = address2;
    }
}
