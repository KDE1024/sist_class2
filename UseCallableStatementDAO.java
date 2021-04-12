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
		//1. 드라이버 로딩
		try {
		//2. 커넥션 얻기
			con = dc.getConnection();
		//3. 프로시저를 호출할수 있는 객체 얻기
			cstmt = con.prepareCall("{ call proc_insert(?, ?, ?, ?, ?, ?) }");
		//4. 바인드 변수에 값 설정
		//4-1. in parameter
			cstmt.setInt(1, ceVO.getEmpno());
			cstmt.setString(2, ceVO.getEname());
			cstmt.setInt(3, ceVO.getDeptno());
			cstmt.setString(4, ceVO.getJob());
		//4-2. out parameter
			cstmt.registerOutParameter(5, Types.NUMERIC);
			cstmt.registerOutParameter(6, Types.VARCHAR);
		
		//5. 프로시저 실행
			cstmt.execute();
		//6. out parameter에 설정된 값 받기
			int cnt = cstmt.getInt(5); //PL/SQL bind 변수에 저장된 값을 얻는 것.
			System.out.println("추가된 행 수 : "+cnt);
			str = cstmt.getString(6);
		}finally {
		//7. 연결 끊기
		}//end finally
			dc.close(null, cstmt, con);
			
		return str;
	}//insertCpEmp4
	
}//class
