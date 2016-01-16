package xyz.hollysys.spring.mybatis_super.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan({ "xyz.hollysys.spring.mybatis_super.dao" })
//@ComponentScan(basePackages = "xyz.hollysys.spring.mybatis_super")
@PropertySource(value = { "classpath:jdbc.properties", "classpath:log4j.properties" })
public class MybatisConfiguration {
	static Logger logger = Logger.getLogger(MybatisConfiguration.class);

	// master database
	@Value("${jdbc.driverClassName:com.mysql.jdbc.Driver}")
	private String driverClassName;

	@Value("${jdbc.url:jdbc:mysql://localhost:3306/sanhao_test}")
	private String url;

	@Value("${jdbc.username:root}")
	private String username;

	@Value("${jdbc.password:root}")
	private String password;
	
	// slave database
	@Value("${jdbc2.driverClassName:com.mysql.jdbc.Driver}")
	private String driverClassName2;

	@Value("${jdbc2.url:jdbc:mysql://localhost:3306/test}")
	private String url2;

	@Value("${jdbc2.username:root}")
	private String username2;

	@Value("${jdbc2.password:root}")
	private String password2;

	
	public static final String MAPPERS_PACKAGE_NAME_1 = "xyz.hollysys.spring.mybatis_super.model";


    //You need this
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
       return new PropertySourcesPlaceholderConfigurer();
    }
	
	@Bean(name="master")
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		logger.info(driverClassName);
		logger.info(url);
		logger.info(username);
		logger.info(password);

		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);

		return dataSource;
	}
	
	@Bean(name="slave")
	public DataSource dataSource2() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		logger.info(driverClassName2);
		logger.info(url2);
		logger.info(username2);
		logger.info(password2);

		dataSource.setDriverClassName(driverClassName2);
		dataSource.setUrl(url2);
		dataSource.setUsername(username2);
		dataSource.setPassword(password2);

		return dataSource;
	}
	
	@Bean(name="routeDao")
	public MybatisRouteConfiguration MultipleDataSource(){
		MybatisRouteConfiguration route = new MybatisRouteConfiguration();
		
		route.setDefaultTargetDataSource(dataSource());
		route.setTargetDataSources(targetDataSources());
		route.setDataSourceKey("dataSource");

		return route;
	}
	
	@Bean
	public Map<Object, Object> targetDataSources() {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("master", dataSource());
		map.put("slave", dataSource2());
		
		return map;
	}

	@Bean
	public DataSourceTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(MultipleDataSource());
		sessionFactory.setTypeAliasesPackage(MAPPERS_PACKAGE_NAME_1);
		return sessionFactory.getObject();
	}
}
