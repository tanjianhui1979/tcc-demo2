package com.example.tccdemo2.mapper.master;

import com.example.tccdemo2.domain.Demo;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterDemoMapper {
    int save(Demo dsDemo);
}
