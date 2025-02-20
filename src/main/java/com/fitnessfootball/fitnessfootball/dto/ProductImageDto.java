package com.fitnessfootball.fitnessfootball.dto;

import lombok.Data;

@Data
public class ProductImageDto {

    private int id;
    private int product_id;
    private String location;
    private String original_filename;

}
