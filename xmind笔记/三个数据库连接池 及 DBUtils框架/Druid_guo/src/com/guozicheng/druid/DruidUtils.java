package com.guozicheng.druid;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * Ӳ����ķ�ʽ���� utils ������
 * ���� druid ��������
 * @author ���ӳɣ���׼��
 *
 */
public class DruidUtils {
	static DruidDataSource druidDataSource;
	static{
		//1.����  ���ӳ�
		druidDataSource=new DruidDataSource();
		//2.���ò���
		druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
		druidDataSource.setUrl("jdbc:mysql://localhost:3306/mydb1");
		druidDataSource.setUsername("root");
		druidDataSource.setPassword("123");
	}
	
	//1.�������ӳأ���ȡconnection
	/**
	 * ��ȡ���ӳ�
	 */
	public static Connection getConnection() throws SQLException{
		
		//3.��ȡconnection
		Connection connection = druidDataSource.getConnection();
		return connection;
	}
	
	/**
	 * �ͷ���Դ
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
