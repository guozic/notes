package com.guozicheng.c3p0_2;


import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * ʹ�������ļ��ķ�ʽ  ���ò���
 * @author ���ӳɣ���׼��
 *
 */
public class C3p0Utils {
	static ComboPooledDataSource cpds;
	static{
		//1.�������ӳض���
		cpds = new ComboPooledDataSource();
		//2.���ò���
		
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
