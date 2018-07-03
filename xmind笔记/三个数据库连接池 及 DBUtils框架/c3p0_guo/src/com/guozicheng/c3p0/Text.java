package com.guozicheng.c3p0;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Text {
	public static void main(String[] args) throws SQLException {
		//1.注册驱动  获取连接
		Connection connection = C3p0Utils.getConnection();
		
		String sql = "delete from student where id=?";
		//2.获取statement 对象
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		//给？ 参数 设定值
		prepareStatement.setInt(1, 12);
		int i = prepareStatement.executeUpdate();
		System.out.println(i);
		
		C3p0Utils.close(null, prepareStatement, connection);
	}
}
