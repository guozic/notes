package com.guozicheng.druid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class Text {
	public static void main(String[] args) throws SQLException {
		//1.注册驱动  获取连接
		Connection connection = DruidUtils.getConnection();
		
		String sql = "insert into student values(?,?,?,?,?,?)";
		//2.获取statement 对象
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		//给？ 参数 设定值
		prepareStatement.setNull(1, Types.INTEGER);
		prepareStatement.setString(2, "段誉");
		prepareStatement.setDouble(3, 11);
		prepareStatement.setDouble(4, 11);
		prepareStatement.setDouble(5, 11);
		prepareStatement.setString(6, "");
		
		System.out.println("=====");
		int i = prepareStatement.executeUpdate();
		System.out.println(">>>>>>>>>");
		System.out.println(i);
		
		DruidUtils.close(null, prepareStatement, connection);
	}
}
