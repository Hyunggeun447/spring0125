package jpabook.jpashop.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultApi<T> {

    private int count;
    private T data;
}
