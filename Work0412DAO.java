package day0413;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.sist.dao.DbConnection;

public class Work0412DAO {

	private static Work0412DAO wDAO;
	
	public Work0412DAO() {
	}//Work0412DAO
	
	public static Work0412DAO getInstance() {
		if(wDAO == null) {
			wDAO = new Work0412DAO();
		}//end if
		return wDAO;
	}//getInstance
	
	/**
	 * 모든 레코드 조회
	 * @return
	 * @throws SQLException
	 */
	public List<WorkAllVO> selectAllWork() throws SQLException{
		List<WorkAllVO> list = new ArrayList<WorkAllVO>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		DbConnection dc = DbConnection.getInstance();
		
		try {
			con = dc.getConnection();
			String selectWork = "select num, name, age, addr from work order by num"; //insert 하면 가장 마지막께 위로 올라가는거 개발자만 이해함..그래서order by num
			pstmt = con.prepareStatement(selectWork);
			
			
			rs = pstmt.executeQuery();//ResultSet 은 cursor의 제어권을 갖는다.
			
			WorkAllVO waVO = null; 
			while(rs.next()) {
				// 커서가 존재하는 레코드 위치에 컬럼값을 받아와서 VO에 설정
				waVO = new WorkAllVO(rs.getInt("num"),rs.getString("name"),rs.getInt("age"), rs.getString("addr"));
				// 레코드의 정보를 가진 VO를 list에 추가
				list.add(waVO);
				
			}//end while
			
		}finally {
			dc.close(rs, pstmt, con);
		}//end finally
		
		
		return list;
	}//selectAllWork

	/**
	 * 레코드 추가
	 * @param waVO
	 * @throws SQLException
	 */
	public void insertWork(WorkAddVO waVO) throws SQLException {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		DbConnection dc = DbConnection.getInstance();
		//1.
		try {
		//2.
			con = dc.getConnection();
		//3.
			String insertWork = "insert into work(num, name, age, addr) values(seq_work.nextval,?,?,?)";
			pstmt = con.prepareStatement(insertWork);
		//4.
			pstmt.setString(1, waVO.getName());
			pstmt.setInt(2, waVO.getAge());
			pstmt.setString(3, waVO.getAddr());
		//5.
			pstmt.executeUpdate();
		}finally {
		//6.
			dc.close(null, pstmt, con);
			
		}//end finally
	}//insertWork
	
	/**
	 * 번호를 가지고 이름, 나이, 주소를 변경하는 일
	 * @param waVO
	 * @return
	 * @throws SQLException
	 */
	public int updateWork(WorkAllVO waVO) throws SQLException{
		int cnt = 0;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		DbConnection dc = DbConnection.getInstance();
		//1.
		try {
		//2.
			con = dc.getConnection();
		//3.
			String updateWork = "update work set name=?, age=?, addr=? where num=?";
			pstmt = con.prepareStatement(updateWork);
		//4. 바인드 변수 총 4개
			pstmt.setString(1, waVO.getName());
			pstmt.setInt(2, waVO.getAge());
			pstmt.setString(3, waVO.getAddr());
			pstmt.setInt(4, waVO.getNum());
		//5.
			cnt = pstmt.executeUpdate();
		}finally {	
		//6.
			dc.close(null, pstmt, con);
		}//finally
		
		return cnt;
	}//updateWork
	
	public int deleteWork(int num) throws SQLException{
		int cnt = 0;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		DbConnection dc = DbConnection.getInstance();
		
		//1.
		try {
		//2.
			con = dc.getConnection();
		//3.
			String deleteWork = "delete from work where num=?";
			pstmt = con.prepareStatement(deleteWork);
		//4.
			pstmt.setInt(1,num);
		//5.
			cnt = pstmt.executeUpdate();
		}finally {
		//6.
			dc.close(null, pstmt, con);
		}//end finally
		return cnt;
	}//deleteWork
	
	
}//class
