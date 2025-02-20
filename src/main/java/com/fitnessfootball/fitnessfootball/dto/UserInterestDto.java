package com.fitnessfootball.fitnessfootball.dto;

import java.util.Date;

import lombok.Data;

@Data
public class UserInterestDto {
    private int id;
    private int user_id;
    private int tag_id;
    private Date created_at;
}
