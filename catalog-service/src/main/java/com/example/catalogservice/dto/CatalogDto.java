package com.example.catalogservice.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CatalogDto implements Serializable { // 직렬화: 객체를 다른 상태로 변환하거나 전송할 때 바이트 배열의 형태로 변경해주는 과정과 같다.
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;

    private String orderId;
    private String userId;
}
