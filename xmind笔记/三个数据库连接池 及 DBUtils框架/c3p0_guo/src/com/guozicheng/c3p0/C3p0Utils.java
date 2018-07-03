package com.guozicheng.c3p0;


import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 硬编码 设置参数  ===》c3p0 
 * @author 郭子成（标准）
 *
 */
public class C3p0Utils {
	static ComboPooledDataSource cpds;
	static{
		//1.创建连接池对象
		cpds = new ComboPooledDataSource();
		//2.配置参数
		try {
			cpds.setDriverClass("com.mysql.jdbc.Driver");
			cpds.setJdbcUrl("jdbc:mysql://localhost:3306/mydb1");
			cpds.setUser("root");
			cpds.setPassword("123");
		} catch (PropertyVetoException e) {
			System.out.println("参数配置失败！！！");
		}
	}
	
	public static Connection getConnection() throws SQLException{
		return cpds.getConnection();
	}
	
	public static void close(ResultSet resultSet,Statement statement,Connection connection) throws SQLException{
		if(resultSet != null){
			resultSet.close();
		}
		if(statement != null){
			statement.close();
		}
		if(connection != null){
			connection.close();
		}
	}
}
