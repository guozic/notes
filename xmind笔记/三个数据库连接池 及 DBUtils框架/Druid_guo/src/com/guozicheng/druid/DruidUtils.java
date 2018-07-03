package com.guozicheng.druid;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 硬编码的方式创建 utils 工具类
 * 根据 druid 创建工具
 * @author 郭子成（标准）
 *
 */
public class DruidUtils {
	static DruidDataSource druidDataSource;
	static{
		//1.创建  连接池
		druidDataSource=new DruidDataSource();
		//2.设置参数
		druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
		druidDataSource.setUrl("jdbc:mysql://localhost:3306/mydb1");
		druidDataSource.setUsername("root");
		druidDataSource.setPassword("123");
	}
	
	//1.创建连接池，获取connection
	/**
	 * 获取连接池
	 */
	public static Connection getConnection() throws SQLException{
		
		//3.获取connection
		Connection connection = druidDataSource.getConnection();
		return connection;
	}
	
	/**
	 * 释放资源
	 */
	public static void close(ResultSet resultSet,Statement statement,Connection connection) throws SQLException{
		if (resultSet != null) {
			resultSet.close();
		}if (statement != null) {
			statement.close();
		}
		if (connection != null) {
			connection.close();
		}
	}
	
}
