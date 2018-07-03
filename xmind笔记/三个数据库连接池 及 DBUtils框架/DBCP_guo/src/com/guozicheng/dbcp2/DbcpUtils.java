package com.guozicheng.dbcp2;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;



/**
 * dbcp连接池 ====》 配置文件
 * @author 郭子成（标准）
 *
 */
public class DbcpUtils {
	static DataSource ds;
	static{
		//创建properties 对象
		Properties properties = new Properties();
		//获取文件流（两种方式  1.普通流 2.类加载器获取文件流）
		InputStream is = DbcpUtils.class.getClassLoader().getResourceAsStream("config.properties");
		try {
			properties.load(is);
		} catch (IOException e) {
			System.out.println("获取流失败！！！");
		}
		//1.创建 连接池 对象
		 try {
			ds=BasicDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static Connection getConnection() throws SQLException{
		return ds.getConnection();
	}
	
	public static void close(ResultSet resultSet,Statement statement,Connection connection) throws SQLException{
		if (resultSet != null) {
			resultSet.close();
		}
		if (statement != null) {
			statement.close();
		}
		if (connection != null) {
			connection.close();
		}
	}
}
