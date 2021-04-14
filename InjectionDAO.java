package day0414;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import day0412.ZipcodeVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.sist.dao.DbConnection;

public class InjectionDAO {

	public String statementLogin (String id, String pass) throws SQLException{
		String name="";

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		DbConnection dc= DbConnection.getInstance();
		
		//1.
		try {
		//2.
			con = dc.getConnection();
		//3.
			stmt = con.createStatement();
		//4.
			StringBuilder selectQuery = new StringBuilder();
			selectQuery
			.append("	select	name			")
			.append("	from	injection_test	")
			.append("	where	id='").append( blockSql(id) ).append("'and pass='").append( blockSql(pass) ).append("'");
								//'"id"'에는 공백이 있으면 안됨!!		
			
			System.out.println( selectQuery.toString()); // 확인으로 찍어보기
			
			rs=stmt.executeQuery(selectQuery.toString());
			
			//id는 pk -> 유일한 값
			if( rs.next()) {//입력한 id와 pass에 일치하는 레코드(컬럼)가 존재
				name = rs.getString("name");
			}//end if
			
		}finally {
		//5.
			if(rs != null) {rs.close();}
			if(stmt != null) {stmt.close();}
			if(con != null) {con.close();}
			
		}//end finally
		
		return name;
	}//login
	
	
	public String preparedstatementLogin (String id, String pass) throws SQLException{
		String name="";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		DbConnection dc= DbConnection.getInstance();
		
		//1.
		try {
		//2.
			con = dc.getConnection();
		//3.
			StringBuilder selectQuery = new StringBuilder();
			selectQuery
			.append("	select	name			")
			.append("	from	injection_test	")
			.append("	where	id=? and pass=?");
			//'"id"'에는 공백이 있으면 안됨!!		
			
			pstmt = con.prepareStatement( selectQuery.toString() );
		//4.
			pstmt.setString(1, id);
			pstmt.setString(2, pass);
			
			rs=pstmt.executeQuery();
			
			//id는 pk -> 유일한 값
			if( rs.next()) {//입력한 id와 pass에 일치하는 레코드(컬럼)가 존재
				name = rs.getString("name");
			}//end if
			
		}finally {
		//5.
			dc.close(rs, pstmt, con);
		}//end finally
		
		return name;
	}//preparedstatementLogin
	
	
	public List<ZipcodeVO> selectZipcode(String dong) throws SQLException{
		List<ZipcodeVO> list = new ArrayList<ZipcodeVO>();

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		DbConnection dc = DbConnection.getInstance();
		//1.
		try {
		//2.
			con=dc.getConnection();
		//3.
			stmt=con.createStatement();
		//4.
			StringBuilder selectQuery = new StringBuilder();
			selectQuery
			.append("	select	zipcode, sido, gugun, dong, bunji	")
			.append("	from	zipcode	")
			.append("	where	dong like '").append( blockSql(dong) ).append("%'");
			
			rs = stmt.executeQuery(selectQuery.toString());
			
			ZipcodeVO zVO = null;
			
			while(rs.next()) {
				zVO = new ZipcodeVO(rs.getString("zipcode"), rs.getString("sido"), 
						rs.getString("gugun"), rs.getString("dong"), rs.getString("bunji"));
				
				list.add(zVO);
			}//end while
			
		}finally {	
		//5.
			if(rs != null) {rs.close();}//end if
			if(stmt != null) {stmt.close();}//end if
			if(con != null) {con.close();}//end if
		}//end finally
		return list;
	}//selectZipcode
	
	/**
	 * 입력되는 쿼리문에 ', , --, 쿼리문에 대한 부분을 치환 (홀따옴표, 공백, 주석)
	 * @param originSql
	 * @return
	 */
	public String blockSql (String originSql) { //Injection 방어 코드!!!!!!!!!!!!!!!!!
		
		String resultSql = originSql;
		
		resultSql = resultSql.replaceAll("'","").replaceAll(" ", "").replaceAll("-", "")
				.replaceAll("select", "").replace("where", ""); //SQL Exception에 대한 방어 코드 
		
		return resultSql;
	}//blockSql
	
	
}//class
