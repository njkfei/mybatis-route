## 项目简介
 基于spring mytabis 构建本项目,指在学习mybatis的高级功能－多个数据源的配置。下列均有所涉及：
 * spring 提供框架管理功能。
 * mybatis 提供数据访问框架
 * mybatis采用注解方式使用
 * mybatis支持动态sql，例如更新对象数据时，只有有效的数据，才会进行更新，且通过注解的方式完成
 * mybatis支持对象与表的映射，且表的字段名字与对象的属性不相同
 * mytatis支持表间一对一，一对多，多对多查询。
 * __增加读写分离__(其实与分库分表的思路是一样的)

## 基本思路
* 对于多个数据源的配置，新增一个数据源路由层
* 通过spring aop,动态拦截数据库dao接口，将设置路由

 ## 相关注解
 * @EnableAspectJAutoProxy: 允许通过java config配置生效。我忘记了这个项目，导致无法进行拦截
 * @SqlProvider : 动态自定义sql

 ## 关键代码
 ### 数据源配置
 ``` bash
 	@Bean(name="master")
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);

		return dataSource;
	}
	
	@Bean(name="slave")
	public DataSource dataSource2() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(driverClassName2);
		dataSource.setUrl(url2);
		dataSource.setUsername(username2);
		dataSource.setPassword(password2);

		return dataSource;
	}
 ```

 ### 路由配置
 ``` bash
	@Bean(name="routeDao")
	public MybatisRouteConfiguration MultipleDataSource(){
		MybatisRouteConfiguration route = new MybatisRouteConfiguration();
		
		route.setDefaultTargetDataSource(dataSource());
		route.setTargetDataSources(targetDataSources());
		route.setDataSourceKey("dataSource");

		return route;
	}

	// 配置路由表
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
		sessionFactory.setDataSource(MultipleDataSource());                            // 配置路由
		sessionFactory.setTypeAliasesPackage(MAPPERS_PACKAGE_NAME_1);
		return sessionFactory.getObject();
	}
 ```
上面有一个路由类，这个是关键。这个类的实现如下：
``` bash
public class MybatisRouteConfiguration extends AbstractRoutingDataSource{
	private final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<String>();
    
    public void setDataSourceKey(String dataSource) {
        dataSourceKey.set(dataSource);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceKey.get();
    }
}
```

　其中见代码。


本项目没有xml配置文件，全部以注解的方式，进行配置注入。

#### 项目参考：[www.websystique.com](http://www.websystique.com)
#### 个人blog: [wiki.niejinkun.com](http://wiki.niejinkun.com)
