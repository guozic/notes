package com.guozicheng.dbcp2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Text {
	public static void main(String[] args) throws SQLException {
		//1.ע������  ��ȡ����
		Connection connection = DbcpUtils.getConnection();
		
		String sql = "delete from student where id=?";
		//2.��ȡstatement ����
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		//���� ���� �趨ֵ
		prepareStatement.setInt(1, 9);
		int i = prepareStatement.executeUpdate();
		System.out.println(i);
		
		DbcpUtils.close(null, prepareStatement, connection);
	}
}
