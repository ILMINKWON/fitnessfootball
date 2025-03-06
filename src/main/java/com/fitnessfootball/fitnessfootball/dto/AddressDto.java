package com.fitnessfootball.fitnessfootball.dto;

import lombok.Data;

@Data
public class AddressDto {

    private int id;
    private int order_id;
    private String user_name;
    private String address;
    private String phone_number;
    private String memo;

}
