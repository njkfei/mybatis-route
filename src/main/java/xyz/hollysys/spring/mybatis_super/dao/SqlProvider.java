package xyz.hollysys.spring.mybatis_super.dao;

import org.apache.ibatis.jdbc.SQL;
import org.apache.log4j.Logger;

import xyz.hollysys.spring.mybatis_super.model.User;

public class SqlProvider {
	static Logger logger = Logger.getLogger(SqlProvider.class);
	public String selectUser(int id){
		String sql = "select * from app_user where id=" + id;
		logger.info(sql);
		return sql;
	}
	
	public String updateUser(final User user){
		String sql =  new SQL() {
            {
                UPDATE("app_user");
                if (user.getEmail() != null) {
                    SET("email = #{email}");
                }
                if (user.getFirst_name() != null) {
                    SET("first_name = #{first_name}");
                }
                WHERE("id = #{id}");
            }
        }.toString();
		
        logger.info(sql);
        
		return sql;
	}

	public String insertUser(final User user){
		String sql =  new SQL() {
            {
                INSERT_INTO("app_user");
/*                if (user.getSso_id() != null) {
                	VALUES("sso_id",user.getSso_id());
                }
                if (user.getPassword() != null) {
                	VALUES("password",user.getPassword());
                }
                if (user.getEmail() != null) {
                	VALUES("email",user.getEmail());
                }
                if (user.getFirst_name() != null) {
                	VALUES("first_name",user.getFirst_name());
                }
                if (user.getLast_name() != null) {
                	VALUES("last_name",user.getLast_name());
                }
                if (user.getState() != null) {
                	VALUES("state",user.getState());
                }*/
                if (user.getSso_id() != null) {
                	VALUES("sso_id","#{sso_id}");
                }
                if (user.getPassword() != null) {
                	VALUES("password","#{password}");
                }
                if (user.getEmail() != null) {
                	VALUES("email","#{email}");
                }
                if (user.getFirst_name() != null) {
                	VALUES("first_name","#{first_name}");
                }
                if (user.getLast_name() != null) {
                	VALUES("last_name","#{last_name}");
                }
                if (user.getState() != null) {
                	VALUES("state","#{state}");
                }
            }
        }.toString();
		
        logger.info(sql);
        
		return sql;
	}
}
