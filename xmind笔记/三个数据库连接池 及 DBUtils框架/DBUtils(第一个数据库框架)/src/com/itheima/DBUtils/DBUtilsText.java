package com.itheima.DBUtils;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

public class DBUtilsText {
	public static void main(String[] args) {
		//text1();
		//text2();
		//text3();
		//text4();
		//text5();
		text6();
	}
	
	/**
	 * �ģ���������  �� sql = "update student set chinese=100 where id=?";
	 */
	public static void text1(){
		//1.����QueryRunner����
		QueryRunner queryRunner = new QueryRunner(DruidUtils.getDataSource());
		//2.����SQL���
		String sql = "update student set chinese=100 where id=?";
		Object[] objects={2};
		int i = 0;
		try {
			i = queryRunner.update(sql,objects);
			
		} catch (SQLException e) {
			System.out.println("��������ʧ�ܣ�����");
		}
		System.out.println("�ɹ�����"+i+"������===");
	}
	
	
	/**
	 * ɾ��ɾ������  ��sql="delete from student where id=?";
	 * @throws SQLException 
	 */
	public static void text2(){
		//1.����QueryRunner ����
		QueryRunner queryRunner = new QueryRunner(DruidUtils.getDataSource());
		//2.����SQL���
		String sql="delete from student where id=?";
		Object[] objects={14};
		int i=0;
		try {
			 i = queryRunner.update(sql,objects);
		} catch (SQLException e) {
			System.out.println("ɾ������ʧ�ܣ�����");
		}
		System.out.println("�ɹ�����"+i+"������===");
	}
	
	/**
	 * �����������  ��sql="insert into student values(?,?,?,?,?,?)";
	 * @throws SQLException 
	 */
	public static void text3(){
		//1.����QueryRunner ����
		QueryRunner queryRunner = new QueryRunner(DruidUtils.getDataSource());
		//2.����SQL���
		String sql="insert into student values(?,?,?,?,?,?)";
		Object[] objects={null,"������",99,99,99,"342143"};
		int i=0;
		try {
			 i = queryRunner.update(sql,objects);
		} catch (SQLException e) {
			System.out.println("�������ʧ�ܣ�����");
			e.getSuppressed();
		}
		System.out.println("�ɹ�����"+i+"������===");
	}
	
	
	/**
	 * ��-1����ѯ����  ----  ��������  ===��new BeanListHandler<>(Student.class)
	 *    �� list = queryRunner.query(sql,new BeanListHandler<>(Student.class));
	 */
	public static void text4(){
		//���ڴ�����е�student
		List<Student> list = null;
		//1.����QueryRunner����
		QueryRunner queryRunner=new QueryRunner(DruidUtils.getDataSource());
		//2.����SQL���
		String sql="select * from student";
		try {
			//�õ� ���� ����
			 list = queryRunner.query(sql,new BeanListHandler<>(Student.class));
		} catch (SQLException e) {
			System.out.println("���Ҕ���ʧ��������");
			e.printStackTrace();
		}
		
		for (Student s : list) {
			System.out.println(s.getId()+"==��"+s.getName()+"==��"+s.getPassword()+"==��"+s.getChinese()+"==��"+s.getMath()+"==��"+s.getEnglish());
		}
	}
	
	/**
	 * ��-2����ѯ����  ----   һ������ ===��new BeanListHandler<>(Student.class)
	 *    ��s = queryRunner.query(sql,new BeanListHandler<>(Student.class));
	 * 
	 */
	public static void text5() {
		//1.����QueryRunner����  
		QueryRunner queryRunner = new QueryRunner(DruidUtils.getDataSource());
		//2.����sql���
		String sql="select * from student where id=?";
		Object[] objects={1};
		
		Student s=null;
		//�õ� һ�� ����
		try {
			 s = queryRunner.query(sql, new BeanHandler<>(Student.class), objects);
		} catch (SQLException e) {
			System.out.println("��ȡ����ʧ�ܣ�����");
			e.printStackTrace();
		}
		System.out.println(s.getId()+"==��"+s.getName()+"==��"+s.getPassword()+"==��"+s.getChinese()+"==��"+s.getMath()+"==��"+s.getEnglish());
	}
	
	
	/**
	 * ��-3����ѯ����  ----   һ������    ===��new ScalarHandler()
	 *    ��object = queryRunner.query(sql, new ScalarHandler(), objects);
	 * 
	 */
	public static void text6() {
		//1.����QueryRunner����  
		QueryRunner queryRunner = new QueryRunner(DruidUtils.getDataSource());
		//2.����sql���
		String sql="select name from student where id=?";
		Object[] objects={1};
		
		Object object=null;
		//�õ� һ�� ����
		try {
			object = queryRunner.query(sql, new ScalarHandler(), objects);
		} catch (SQLException e) {
			System.out.println("��ȡ����ʧ�ܣ�����");
			e.printStackTrace();
		}
		System.out.println(object);
	}
}
