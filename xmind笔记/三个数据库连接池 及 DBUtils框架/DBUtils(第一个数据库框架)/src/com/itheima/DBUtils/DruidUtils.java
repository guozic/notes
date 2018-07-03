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
 * �����ļ��ķ�ʽ���� utils ������
 * ���� druid ��������
 * @author ���ӳɣ���׼��
 *
 */
public class DruidUtils {
	static DataSource dataSource;
	static{
		try {
			//����properties����
			Properties properties=new Properties();
			//������� ��ȡ�ļ���
			InputStream is = DruidUtils.class.getClassLoader().getResourceAsStream("config.properties");
			//properties�������ļ�֮��  ����
			properties.load(is);
			//1.ͨ�� ����  ����  ���ӳ�
			dataSource=DruidDataSourceFactory.createDataSource(properties);
			//2.���ò���
		} catch (Exception e) {
			System.out.println("�������ӳ�ʧ�ܣ�����");
		}
	}
	
	//1.�������ӳأ���ȡconnection
	/**
	 * ��ȡ���ӳ�
	 */
	public static Connection getConnection() throws SQLException{
		
		//3.��ȡconnection
		Connection connection = dataSource.getConnection();
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
	public static DataSource getDataSource(){
		return dataSource;
	}
}
