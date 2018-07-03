package com.guozicheng.c3p0_2;


import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 使用配置文件的方式  设置参数
 * @author 郭子成（标准）
 *
 */
public class C3p0Utils {
	static ComboPooledDataSource cpds;
	static{
		//1.创建连接池对象
		cpds = new ComboPooledDataSource();
		//2.配置参数
		
	}
	
	public static Connection getConnection() throws SQLException{
		return ds.getConnection();
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
