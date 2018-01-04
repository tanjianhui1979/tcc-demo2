package com.example.tccdemo2.config;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * @author Linyu Chen
 */
@Configuration
public class DataSourceConfig {

    private static final String PRIMARY_MAPPER_BASE_PACKAGE = "com.example.tccdemo2.mapper.master";
    private static final String BUSINESS_MAPPER_BASE_PACKAGE = "com.example.tccdemo2.mapper.business";

    private static final String DATASOURCE_DRUID_PROPERTIES = "datasource/druid.properties";
    private static final String DATASOURCE_DRUID_PRIMARY_PROPERTIES = "datasource/druid-primary.properties";
    private static final String DATASOURCE_DRUID_BUSINESS_PROPERTIES = "datasource/druid-business.properties";

    private static final String CLASSPATH_MAPPER_XML = "classpath:mapper/*/*.xml";

    /**
     * druid 公共配置
     */
    private static Properties commonProperties;

    static {
        commonProperties = new Properties();
        InputStream in = DataSourceConfig.class.getClassLoader().getResourceAsStream(DATASOURCE_DRUID_PROPERTIES);
        try {
            commonProperties.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Properties loadDruidProperties(String path) throws IOException {
        Properties properties = new Properties();
        InputStream in = getClass().getClassLoader().getResourceAsStream(path);
        properties.load(in);
        for (Map.Entry<Object, Object> entry : commonProperties.entrySet()) {
            properties.setProperty(entry.getKey().toString(), entry.getValue().toString());
        }
        return properties;
    }

    /**
     * 设置数据源
     *
     * @return
     * @throws IOException
     */
    @Primary
    @Bean
    public AtomikosDataSourceBean primaryDataSource() throws IOException {
        return getAtomikosDataSourceBean(DATASOURCE_DRUID_PRIMARY_PROPERTIES);
    }


    @Bean
    public AtomikosDataSourceBean businessDataSource() throws IOException {
        return getAtomikosDataSourceBean(DATASOURCE_DRUID_BUSINESS_PROPERTIES);
    }

    private AtomikosDataSourceBean getAtomikosDataSourceBean(String dataSourceProperties) throws IOException {
        Properties properties = loadDruidProperties(dataSourceProperties);
        AtomikosDataSourceBean dataSourceBean = new AtomikosDataSourceBean();
        // 配置DruidXADataSource
        DruidXADataSource xaDataSource = new DruidXADataSource();
        xaDataSource.configFromPropety(properties);
        // 设置置AtomikosDataSourceBean XADataSource
        dataSourceBean.setXaDataSource(xaDataSource);
        return dataSourceBean;
    }

    /**
     * 设置{@link SqlSessionFactoryBean}的数据源
     * @param primaryDataSource 主数据源
     * @return
     */
    @Primary
    @Bean
    public SqlSessionFactoryBean primarySqlSessionFactoryBean(@Qualifier("primaryDataSource") AtomikosDataSourceBean primaryDataSource) {
        return getSqlSessionFactoryBean(primaryDataSource);
    }

    @Bean
    public SqlSessionFactoryBean businessSqlSessionFactoryBean(@Qualifier("businessDataSource") AtomikosDataSourceBean businessDataSource) {
        return getSqlSessionFactoryBean(businessDataSource);
    }

    private SqlSessionFactoryBean getSqlSessionFactoryBean(AtomikosDataSourceBean dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            sqlSessionFactoryBean.setMapperLocations(resolver.getResources(CLASSPATH_MAPPER_XML));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sqlSessionFactoryBean;
    }

    /**
     * 搜索{@link DataSourceConfig#PRIMARY_MAPPER_BASE_PACKAGE} 包下的Mapper接口，并且将这些接口
     * 交由{@link MapperScannerConfigurer#sqlSessionFactoryBeanName} 属性设置的SqlSessionFactoryBean管理
     * @return
     */
    @Bean
    public MapperScannerConfigurer primaryMapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage(PRIMARY_MAPPER_BASE_PACKAGE);
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("primarySqlSessionFactoryBean");
        return mapperScannerConfigurer;
    }

    /**
     * 搜索{@link DataSourceConfig#BUSINESS_MAPPER_BASE_PACKAGE} 包下的Mapper接口，并且将这些接口
     * 交由{@link MapperScannerConfigurer#sqlSessionFactoryBeanName} 属性设置的SqlSessionFactoryBean管理
     * @return
     */
    @Bean
    public MapperScannerConfigurer businessMapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage(BUSINESS_MAPPER_BASE_PACKAGE);
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("businessSqlSessionFactoryBean");
        return mapperScannerConfigurer;
    }
}
