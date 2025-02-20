package com.fitnessfootball.fitnessfootball.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class UserDto {
    private int id;
    private String user_id;
    private String password;
    private String name;
    private String nickname;
    private String gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;
    private String email;
    private String athletic_experience;
    private String phone_number;
}
