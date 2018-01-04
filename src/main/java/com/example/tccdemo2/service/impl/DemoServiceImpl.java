package com.example.tccdemo2.service.impl;

import com.example.tccdemo2.domain.Demo;
import com.example.tccdemo2.mapper.business.BusinessDemoMapper;
import com.example.tccdemo2.mapper.master.MasterDemoMapper;
import com.example.tccdemo2.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Transactional(rollbackFor = Exception.class)
@Service
public class DemoServiceImpl implements DemoService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private MasterDemoMapper masterDemoMapper;

    @Resource
    private BusinessDemoMapper businessDemoMapper;

    /**
     * 正常测试分布式事务
     *
     * @return
     * @throws Exception
     */
    @Override
    public int save() throws Exception {
        log.info("save");
        Demo dsDemo = new Demo();
        dsDemo.setName("xa事务测试");
        int row = masterDemoMapper.save(dsDemo);
        log.info("保存之后");
        Demo dsDemo1 = new Demo();
        dsDemo1.setName("xa事务测试2");
        int row2 = businessDemoMapper.save(dsDemo1);
        return row + row2;
    }

    /**
     * 测试分布式事务回滚
     * @return
     * @throws Exception
     */
    @Override
    public int save2() throws Exception {
        log.info("save2");
        Demo dsDemo = new Demo();
        dsDemo.setName("xa事务回滚测试");
        int row = masterDemoMapper.save(dsDemo);
        log.info("保存之后异常");
        int a = 1 / 0;

        Demo dsDemo1 = new Demo();
        dsDemo1.setName("xa事务回滚测试2");
        int row2 = businessDemoMapper.save(dsDemo1);
        return row + row2;
    }
}
