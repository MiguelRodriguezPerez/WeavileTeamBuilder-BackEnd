package com.example.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NatureDto {
    
    private Long id;
    private String name;
    private String increased_stat;
    private String decreased_stat;
    
}
