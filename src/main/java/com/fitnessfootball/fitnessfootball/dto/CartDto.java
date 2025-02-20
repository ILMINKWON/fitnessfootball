package com.fitnessfootball.fitnessfootball.dto;

import lombok.Data;

@Data
public class CartDto {
    private int id;       // 장바구니 고유 ID
    private int user_id;       // 사용자 ID
    private int product_id;    // 상품 ID
    private int quantity;     // 수량
}
