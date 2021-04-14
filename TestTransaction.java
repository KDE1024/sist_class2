package day0414;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * test_trigger1 ���̺�� test_trigger2 ���̺� ��� insert�� �����ϸ� transaction�� �Ϸ� �Ǵ� �ڵ�
 * 
 * @author user
 */
public class TestTransaction {

	private Connection con;
	
	/**
	 * ���� �۾�
	 * 
	 * @param name
	 * @param age
	 * @return
	 * @throws SQLException
	 */
	public int insert(String name, int age) throws SQLException {
		int cnt = 0; // test_trigger1 ���̺��� ���� ���� ��
		int cnt2 = 0;// test_trigger2 ���̺��� ���� ���� ��
		
		//1. ����̹� �ε�
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}//end catch
		
		String url ="jdbc:oracle:thin:@localhost:1521:orcl";
		String id ="scott";
		String pass ="tiger";
		
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		
//		try {
		//2. Ŀ�ؼ� ���
			con = DriverManager.getConnection(url, id, pass);
		
		//3. autocommit
			con.setAutoCommit(false); //-> ���� commit�� �ؾ� ��. ���� ������ commit��.
		//4. ������ ���� ��ü ���
			String sql = "insert into test_trigger1(name, age) values (?, ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setInt(2, age);
		//5. ������ ���� �� ��� ���
			cnt = pstmt.executeUpdate();
			
		//4. ������ ���� ��ü ���
			String sql2 = "insert into test_trigger2(name, age) values (?, ?)";
			pstmt2 = con.prepareStatement(sql2);
			pstmt2.setString(1, name);
			pstmt2.setInt(2, age);
		//5. ������ ���� �� ��� ���
			cnt2 = pstmt2.executeUpdate();
			
//			if(cnt == 1 && cnt2 == 1) { 
//				con.commit();
//			}else {
//				con.rollback();
//			}//end else
//			
//		}finally {
//			if(con!=null) {con.close();}//end if //rollback ���� �ʴ´�.
//			
//			
//		}//end finally
//			
		return cnt + cnt2;
	}// insert
	
	/**
	 * ������ ����ϴ� ���� ó��
	 * Ŀ�ؼ� ����, commit, rollback�� �����Ѵ�.
	 */
	public void add() {
		//DB ���� method ȣ���ϰ� ��� �ޱ�
		//�����ϴ� ���� ���� Ƚ���� ���Դٸ� commit �׷��� �ʴٸ� rollback ����. (if else)
		//���� ����
		try {
			int totalCnt = insert("����ƾ", 14);
			if(totalCnt==2) {//��ǥ�� ���� ���� ��ȯ�Ǹ� commit�� ����.
				con.commit();
				System.out.println("�������� Ʈ������ ó�� : ���� ���̺� ��� ���ڵ尡 �߰���");
			}//end if
			//update�� delete�� ��� else�� rollback�� �����ϰ�
		} catch (SQLException e) {
			try {
				con.rollback();//insert�� ��� catch���� rollback ����
				System.out.println("���������� Ʈ������ ó�� : �� �� ���̺� ������ �����ߴ��� ��� �߰� �۾��� ��ҵ�");
			} catch (SQLException e1) {
			e1.printStackTrace();
		}//end catch
			e.printStackTrace();
		}finally {
			try {
				if(con!=null) {con.close();}
			} catch (SQLException e) {
				e.printStackTrace();
			}//end catch
		}//end catch
		
	}//add
	
	public static void main(String[] args) {
		TestTransaction tt = new TestTransaction();
			tt.add();
	}// main
	
}// class

