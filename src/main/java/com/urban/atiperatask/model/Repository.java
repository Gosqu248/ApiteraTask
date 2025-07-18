package com.urban.atiperatask.model;


import lombok.Getter;

@Getter
public class Repository {
    private String name;
    private Owner owner;
    private boolean fork;
}
