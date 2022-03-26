package com.yfh.springboot03yaml.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Pet {
    private String name;
    private Double weight;
}
