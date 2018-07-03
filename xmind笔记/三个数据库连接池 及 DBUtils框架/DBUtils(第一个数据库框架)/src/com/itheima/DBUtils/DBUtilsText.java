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
	 * 改：更新数据  ： sql = "update student set chinese=100 where id=?";
	 */
	public static void text1(){
		//1.创建QueryRunner对象
		QueryRunner queryRunner = new QueryRunner(DruidUtils.getDataSource());
		//2.发送SQL语句
		String sql = "update student set chinese=100 where id=?";
		Object[] objects={2};
		int i = 0;
		try {
			i = queryRunner.update(sql,objects);
			
		} catch (SQLException e) {
			System.out.println("更新数据失败！！！");
		}
		System.out.println("成功操作"+i+"行数据===");
	}
	
	
	/**
	 * 删：删除数据  ：sql="delete from student where id=?";
	 * @throws SQLException 
	 */
	public static void text2(){
		//1.创建QueryRunner 对象
		QueryRunner queryRunner = new QueryRunner(DruidUtils.getDataSource());
		//2.发送SQL语句
		String sql="delete from student where id=?";
		Object[] objects={14};
		int i=0;
		try {
			 i = queryRunner.update(sql,objects);
		} catch (SQLException e) {
			System.out.println("删除数据失败！！！");
		}
		System.out.println("成功操作"+i+"行数据===");
	}
	
	/**
	 * 增：添加数据  ：sql="insert into student values(?,?,?,?,?,?)";
	 * @throws SQLException 
	 */
	public static void text3(){
		//1.创建QueryRunner 对象
		QueryRunner queryRunner = new QueryRunner(DruidUtils.getDataSource());
		//2.发送SQL语句
		String sql="insert into student values(?,?,?,?,?,?)";
		Object[] objects={null,"李清照",99,99,99,"342143"};
		int i=0;
		try {
			 i = queryRunner.update(sql,objects);
		} catch (SQLException e) {
			System.out.println("添加数据失败！！！");
			e.getSuppressed();
		}
		System.out.println("成功操作"+i+"行数据===");
	}
	
	
	/**
	 * 查-1：查询数据  ----  多条数据  ===》new BeanListHandler<>(Student.class)
	 *    ： list = queryRunner.query(sql,new BeanListHandler<>(Student.class));
	 */
	public static void text4(){
		//用于存放所有的student
		List<Student> list = null;
		//1.创建QueryRunner对象
		QueryRunner queryRunner=new QueryRunner(DruidUtils.getDataSource());
		//2.发送SQL语句
		String sql="select * from student";
		try {
			//得到 数条 数据
			 list = queryRunner.query(sql,new BeanListHandler<>(Student.class));
		} catch (SQLException e) {
			System.out.println("查找數據失敗！！！");
			e.printStackTrace();
		}
		
		for (Student s : list) {
			System.out.println(s.getId()+"==》"+s.getName()+"==》"+s.getPassword()+"==》"+s.getChinese()+"==》"+s.getMath()+"==》"+s.getEnglish());
		}
	}
	
	/**
	 * 查-2：查询数据  ----   一条数据 ===》new BeanListHandler<>(Student.class)
	 *    ：s = queryRunner.query(sql,new BeanListHandler<>(Student.class));
	 * 
	 */
	public static void text5() {
		//1.創建QueryRunner對象  
		QueryRunner queryRunner = new QueryRunner(DruidUtils.getDataSource());
		//2.发送sql语句
		String sql="select * from student where id=?";
		Object[] objects={1};
		
		Student s=null;
		//得到 一条 数据
		try {
			 s = queryRunner.query(sql, new BeanHandler<>(Student.class), objects);
		} catch (SQLException e) {
			System.out.println("获取数据失败！！！");
			e.printStackTrace();
		}
		System.out.println(s.getId()+"==》"+s.getName()+"==》"+s.getPassword()+"==》"+s.getChinese()+"==》"+s.getMath()+"==》"+s.getEnglish());
	}
	
	
	/**
	 * 查-3：查询数据  ----   一个数据    ===》new ScalarHandler()
	 *    ：object = queryRunner.query(sql, new ScalarHandler(), objects);
	 * 
	 */
	public static void text6() {
		//1.創建QueryRunner對象  
		QueryRunner queryRunner = new QueryRunner(DruidUtils.getDataSource());
		//2.发送sql语句
		String sql="select name from student where id=?";
		Object[] objects={1};
		
		Object object=null;
		//得到 一条 数据
		try {
			object = queryRunner.query(sql, new ScalarHandler(), objects);
		} catch (SQLException e) {
			System.out.println("获取数据失败！！！");
			e.printStackTrace();
		}
		System.out.println(object);
	}
}
