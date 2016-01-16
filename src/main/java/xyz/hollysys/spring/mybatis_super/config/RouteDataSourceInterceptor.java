package xyz.hollysys.spring.mybatis_super.config;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import xyz.hollysys.spring.mybatis_super.model.User;

@EnableAspectJAutoProxy
@Aspect
public class RouteDataSourceInterceptor {
	static Logger logger = Logger.getLogger(RouteDataSourceInterceptor.class);

	
	@Pointcut("execution(* xyz.hollysys.spring.mybatis_super.dao.UserDao.insertUser(xyz.hollysys.spring.mybatis_super.model.User))")
	private void insertMethod(){}
	
	@Autowired
	@Qualifier("routeDao")
	private MybatisRouteConfiguration route;
	
	@Before("insertMethod() && args(user)")
	public void doRoute(User user){
		logger.info("前置通知");
		logger.info(user.toString());
		logger.info("切换数据源到slave");
		
		route.setDataSourceKey("slave");
	}
	
	@AfterReturning("insertMethod()")
	public void doAfter(){
		logger.info("后置通知");
	}
	
	@After("insertMethod()")
	public void after(){
		logger.info("最终通知");
	}
	
	@AfterThrowing("insertMethod()")
	public void doAfterThrow(){
		logger.info("例外通知");
	}
	
	@Around("insertMethod()")
	public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable{
		logger.info("进入环绕通知");
		Object object = pjp.proceed();//执行该方法
		logger.info("退出方法");
		return object;
	}
}
