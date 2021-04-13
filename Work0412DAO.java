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
	 * ��� ���ڵ� ��ȸ
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
			String selectWork = "select num, name, age, addr from work order by num"; //insert �ϸ� ���� �������� ���� �ö󰡴°� �����ڸ� ������..�׷���order by num
			pstmt = con.prepareStatement(selectWork);
			
			
			rs = pstmt.executeQuery();//ResultSet �� cursor�� ������� ���´�.
			
			WorkAllVO waVO = null; 
			while(rs.next()) {
				// Ŀ���� �����ϴ� ���ڵ� ��ġ�� �÷����� �޾ƿͼ� VO�� ����
				waVO = new WorkAllVO(rs.getInt("num"),rs.getString("name"),rs.getInt("age"), rs.getString("addr"));
				// ���ڵ��� ������ ���� VO�� list�� �߰�
				list.add(waVO);
				
			}//end while
			
		}finally {
			dc.close(rs, pstmt, con);
		}//end finally
		
		
		return list;
	}//selectAllWork

	/**
	 * ���ڵ� �߰�
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
	
	
	
}//class
