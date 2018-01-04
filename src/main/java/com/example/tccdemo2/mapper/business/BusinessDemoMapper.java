package com.example.tccdemo2.mapper.business;

import com.example.tccdemo2.domain.Demo;
import org.springframework.stereotype.Repository;


@Repository
public interface BusinessDemoMapper {
    int save(Demo dsDemo);
}
