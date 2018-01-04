package com.example.tccdemo2.domain;

import java.io.Serializable;

/**
 * @author Linyu Chen
 */
public class Demo implements Serializable {
    private static final long serialVersionUID = 8097597276750444748L;
    private Long id;

    private String name;

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
