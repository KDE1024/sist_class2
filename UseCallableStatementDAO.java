package day0412;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import kr.co.sist.dao.DbConnection;

public class UseCallableStatementDAO {

	private static UseCallableStatementDAO ucsDAO;
	
	private UseCallableStatementDAO() {
	}
	
	public static UseCallableStatementDAO getInstance() {
		if(ucsDAO == null) {
			ucsDAO = new UseCallableStatementDAO();
		}//end if
		
		return ucsDAO;
	}//getInstance
	
	public String insertCpEmp4(CpEmp4VO ceVO) throws SQLException {
		
		String str = "";
		
		Connection con = null;
		CallableStatement cstmt = null;
		
		DbConnection dc = DbConnection.getInstance();
		//1. ����̹� �ε�
		try {
		//2. Ŀ�ؼ� ���
			con = dc.getConnection();
		//3. ���ν����� ȣ���Ҽ� �ִ� ��ü ���
			cstmt = con.prepareCall("{ call proc_insert(?, ?, ?, ?, ?, ?) }");
		//4. ���ε� ������ �� ����
		//4-1. in parameter
			cstmt.setInt(1, ceVO.getEmpno());
			cstmt.setString(2, ceVO.getEname());
			cstmt.setInt(3, ceVO.getDeptno());
			cstmt.setString(4, ceVO.getJob());
		//4-2. out parameter
			cstmt.registerOutParameter(5, Types.NUMERIC);
			cstmt.registerOutParameter(6, Types.VARCHAR);
		
		//5. ���ν��� ����
			cstmt.execute();
		//6. out parameter�� ������ �� �ޱ�
			int cnt = cstmt.getInt(5); //PL/SQL bind ������ ����� ���� ��� ��.
			System.out.println("�߰��� �� �� : "+cnt);
			str = cstmt.getString(6);
		}finally {
		//7. ���� ����
		}//end finally
			dc.close(null, cstmt, con);
			
		return str;
	}//insertCpEmp4
	
}//class
