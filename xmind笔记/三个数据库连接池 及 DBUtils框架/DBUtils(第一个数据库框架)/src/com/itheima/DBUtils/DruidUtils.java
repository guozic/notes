package com.itheima.DBUtils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
 * 配置文件的方式创建 utils 工具类
 * 根据 druid 创建工具
 * @author 郭子成（标准）
 *
 */
public class DruidUtils {
	static DataSource dataSource;
	static{
		try {
			//创建properties对象
			Properties properties=new Properties();
			//类加载器 获取文件流
			InputStream is = DruidUtils.class.getClassLoader().getResourceAsStream("config.properties");
			//properties对象与文件之间  关联
			properties.load(is);
			//1.通过 工厂  创建  连接池
			dataSource=DruidDataSourceFactory.createDataSource(properties);
			//2.设置参数
		} catch (Exception e) {
			System.out.println("创建连接池失败！！！");
		}
	}
	
	//1.创建连接池，获取connection
	/**
	 * 获取连接池
	 */
	public static Connection getConnection() throws SQLException{
		
		//3.获取connection
		Connection connection = dataSource.getConnection();
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
	public static DataSource getDataSource(){
		return dataSource;
	}
}
