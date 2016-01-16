package xyz.hollysys.spring.mybatis_super;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import xyz.hollysys.spring.mybatis_super.config.MybatisConfiguration;
import xyz.hollysys.spring.mybatis_super.config.MybatisRouteConfiguration;
import xyz.hollysys.spring.mybatis_super.config.RouteDataSourceInterceptor;
import xyz.hollysys.spring.mybatis_super.dao.UserDao;
import xyz.hollysys.spring.mybatis_super.model.User;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String args[]){
        AbstractApplicationContext  context = new AnnotationConfigApplicationContext(MybatisConfiguration.class,RouteDataSourceInterceptor.class);
         
        UserDao dao = (UserDao)context.getBean("userDao");
        
        MybatisRouteConfiguration route = (MybatisRouteConfiguration) context.getBean("routeDao");
        
        route.setDataSourceKey("slave");
        
        User user = dao.getUser(19);
        
        System.out.println(user.toString());
        
        route.setDataSourceKey("master");
        user = dao.findById(7);
        
        System.out.println(user.toString());
        
        user.setFirst_name("njp");
        
        dao.update(user);
        
        user = dao.findById(7);
        
        user.setEmail("njp@sanhao.com");
        
        dao.update(user);
        
        user = dao.findById(7);
        
        System.out.println(user.toString());
        
        dao.insertUser(user);
        
        /*        user = dao.findById(3);
        
        System.out.println(user.toString());
        
        user = dao.findById(4);
        
        System.out.println(user.toString());
        
        
        user = dao.findById(5);
        
        System.out.println(user.toString());
        
        user = dao.findById(6);
        
        System.out.println(user.toString());
        
        user = dao.findById(7);
        
        System.out.println(user.toString());*/
    }
}
