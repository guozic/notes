package com.guozicheng.c3p0;


import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Ӳ���� ���ò���  ===��c3p0 
 * @author ���ӳɣ���׼��
 *
 */
public class C3p0Utils {
	static ComboPooledDataSource cpds;
	static{
		//1.�������ӳض���
		cpds = new ComboPooledDataSource();
		//2.���ò���
		try {
			cpds.setDriverClass("com.mysql.jdbc.Driver");
			cpds.setJdbcUrl("jdbc:mysql://localhost:3306/mydb1");
			cpds.setUser("root");
			cpds.setPassword("123");
		} catch (PropertyVetoException e) {
			System.out.println("��������ʧ�ܣ�����");
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
