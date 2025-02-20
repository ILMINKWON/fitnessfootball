package com.fitnessfootball.fitnessfootball.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class RestResponseDto {

    private String result; // 항상 값은 "success" or "fail"
    private String reason; // ex) 실패이유 : 데이터가 부족합니다.
    private Map<String,Object> data = new HashMap<>(); // ex) 실패이유 : 데이터가 부족합니다.

    public void add(String name,  Object value){
        data.put(name, value);
    }
}
