package com.example.tccdemo2.web;

import com.example.tccdemo2.service.DemoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class DemoController {

    @Resource
    private DemoService dsService;

    @RequestMapping("/testXaDatasource")
    public String testXaDatasource() {
        int result = 0;
        try {
            result = dsService.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(result);
    }

    @RequestMapping("/testXaDatasource2")
    public String testXaDatasource2() {
        int result = 0;
        try {
            result = dsService.save2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(result);
    }
}
