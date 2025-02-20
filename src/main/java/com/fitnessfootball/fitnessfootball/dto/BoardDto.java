package com.fitnessfootball.fitnessfootball.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class BoardDto {

    private int id;
    private int user_id;
    private String title;
    private String content;
    private int read_count;
    private LocalDateTime created_at;
    private boolean isNew; // 최근 30분 내 작성 여부

      public boolean getIsNew() {
        return created_at.isAfter(LocalDateTime.now().minusMinutes(30));
    }

     public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return created_at.format(formatter);
    }

}
