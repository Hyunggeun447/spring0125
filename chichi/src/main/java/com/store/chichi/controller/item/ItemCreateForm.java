package com.store.chichi.controller.item;

import com.store.chichi.domain.item.Color;
import com.store.chichi.domain.item.Size;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ItemCreateForm {

    /**
     * NotEmpty ===> String
     * etc... ====> NotNull
     *
     *  변수는 final
     */

    @NotEmpty(message = "상품명은 필수입니다.") // String 타입에만 사용 가능/
    private String name;

    @NotNull(message = "사이즈 필수")
    private Size size;

    @NotNull(message = "색상 필수")
    private Color color;

    final int minPrice = 1000;
    @NotNull(message = "가격은 필수입니다.")
    @Min(message = "최소값을 만족하지 못합니다. 최소값 : " + minPrice  , value = minPrice) //value는 final
    private Integer price;

    @NotNull(message = "수량은 필수입니다.")
    private Integer stockQuantity;
}
