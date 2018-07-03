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
 * dbcp���ӳ� ====�� �����ļ�
 * @author ���ӳɣ���׼��
 *
 */
public class DbcpUtils {
	static DataSource ds;
	static{
		//����properties ����
		Properties properties = new Properties();
		//��ȡ�ļ��������ַ�ʽ  1.��ͨ�� 2.���������ȡ�ļ�����
		InputStream is = DbcpUtils.class.getClassLoader().getResourceAsStream("config.properties");
		try {
			properties.load(is);
		} catch (IOException e) {
			System.out.println("��ȡ��ʧ�ܣ�����");
		}
		//1.���� ���ӳ� ����
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
