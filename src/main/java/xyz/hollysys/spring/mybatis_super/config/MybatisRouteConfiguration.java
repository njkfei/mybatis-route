package xyz.hollysys.spring.mybatis_super.config;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MybatisRouteConfiguration extends AbstractRoutingDataSource{
	static Logger logger = Logger.getLogger(MybatisRouteConfiguration.class);

	private final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<String>();
    
    public void setDataSourceKey(String dataSource) {
        dataSourceKey.set(dataSource);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceKey.get();
    }
}
