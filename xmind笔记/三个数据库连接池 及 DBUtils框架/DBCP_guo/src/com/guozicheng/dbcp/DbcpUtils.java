package com.guozicheng.dbcp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp.BasicDataSource;


/**
 * dbcp连接池 ====》 硬编码
 * @author 郭子成（标准）
 *
 */
public class DbcpUtils {
	static BasicDataSource ds;
	static{
		//1.创建 连接池 对象
		 ds=new BasicDataSource();
		//2.设置参数
		ds.setUsername("root");
		ds.setUrl("jdbc:mysql://localhost:3306/mydb1");
		ds.setPassword("123");
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setInitialSize(10);//连接池的初始化大小
	}
	
	public static Connection getConnection() throws SQLException{
		return ds.getConnection();
	}
	
	public static void close(ResultSet resultSet,Statement statement,Connection connection) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			System.out.println("释放资源失败！！");
		}
	}
}
