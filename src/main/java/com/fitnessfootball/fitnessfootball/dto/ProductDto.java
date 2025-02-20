package com.fitnessfootball.fitnessfootball.dto;

import java.text.DecimalFormat;

import lombok.Data;

@Data
public class ProductDto {

    private int id;
    private int manager_id;
    private String product_name;
    private String productdescription;
    private String image_link;
    private int price;
    private int inventory;

    public String getFormattedPrice() {
    DecimalFormat formatter = new DecimalFormat("#,###");
    return formatter.format(price);
    }

}
