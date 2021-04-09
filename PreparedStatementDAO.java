package day0409;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton Pattern 도입한 클래스
 * @author user
 */
public class PreparedStatementDAO {
	
	private static PreparedStatementDAO psDAO;
	
	private PreparedStatementDAO() {
	}//PreparedStatementDAO

	/**
	 * singleton으로 생성된 instance를 반환하는 일 
	 * @return 하나의 객체
	 */
	public static PreparedStatementDAO getInstance() {
		if(psDAO == null ) {//객체가 만들어져있지 않다면
			psDAO = new PreparedStatementDAO(); //객체화
		}//end if
		
		return psDAO;
	}//getInstance
	
	/**
	 * 커넥션 반환하는 일
	 * @return DB와 연결된 Connection
	 */
	private Connection getConnection() throws SQLException {
		//1. 드라이버 로딩
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}//end catch
		
		//2. Connection 얻기
		String url = "jdbc:oracle:thin:@localhost:1521:orcl";
		String id = "scott";
		String pass ="tiger";
		
		Connection con = DriverManager.getConnection(url,id,pass);
		
		return con;
		
	}//getConnection
	
	/**
	 * 부서 정보를 추가하는 일
	 * @param dVO 부서번호, 부서명, 위치를 가진 VO
	 * @throws SQLException
	 */
	public void insertCpDept(DeptVO dVO) throws SQLException { //insert는 실행 하니면 예외.
		
		Connection con= null;
		PreparedStatement pstmt = null;
		
		//1. DB Vendor사에서 배포된 드라이버 로딩
		
		try {
		//2. 로딩된 드라이버를 사용하여 커넥션 얻기
			con = getConnection();
		//3. 쿼리문을 할당하여 쿼리문 실행객체 얻기
			String insertCpDept = "insert into cp_dept(deptno, dname, loc)values(?,?,?)";
			pstmt = con.prepareStatement(insertCpDept);
		//4. 바인드 변수에 값 할당
			pstmt.setInt(1, dVO.getDeptno());
			pstmt.setString(2, dVO.getDname());
			pstmt.setString(3, dVO.getLoc());
		//5. 쿼리문을 실행 후 결과 얻기
			
			pstmt.executeUpdate();
		}finally {
		//6. 연결 끊기
			close(null,pstmt,con);
		}//end finally
		
		
	}//insertCpDept
	
	
	/**
	 * 부서 정보를 변경하는 일
	 * 부서 번호와 일치하는 부서명과 위치를 변경한다. 
	 * @param dVO
	 * @return
	 * @throws SQLException
	 */
	public int updateCpDept(DeptVO dVO) throws SQLException {
		int cnt = 0;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		//1. 드라이버 로딩
		try {
		//2. 커넥션 얻기
			con = getConnection();
		//3. 쿼리문 생성 객체 얻기
			StringBuilder updateCpDept = new StringBuilder();
			updateCpDept
			.append("	update	cp_dept			")
			.append("	set		dname=?,loc=?	")
			.append("	where	deptno=?		");
			
			pstmt=con.prepareStatement( updateCpDept.toString() );
		
		//4. 바인드 변수에 값 설정
			pstmt.setString(1, dVO.getDname());
			pstmt.setString(2, dVO.getLoc());
			pstmt.setInt(3, dVO.getDeptno());
			
		//5. 쿼리문 수행 후 결과 얻기
			cnt=pstmt.executeUpdate();
			
			
		}finally{
		//6. 연결 끊기
			close(null, pstmt, con);
		}//end finally
		
		
		return cnt;
	}//updateCpDept
	
	/**
	 * 부서 번호를 입력받아 부서 정보를 삭제하는 일
	 * @param deptno 삭제할 부서 번호
	 * @return
	 * @throws SQLException
	 */
	public int deleteCpDept(int deptno) throws SQLException {
		int cnt = 0;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		//1. 드라이버 로딩
		
		try {
		//2. 커넥션 얻기
			con = getConnection();
		//3. 쿼리문 생성 객체 얻기
			String deleteCpDept = "delete from cp_dept where deptno=?";
			pstmt = con.prepareStatement(deleteCpDept);
		//4. 바인드 변수에 값 설정
			pstmt.setInt(1,deptno);
		//5. 쿼리문 수행 후 결과 얻기
			cnt=pstmt.executeUpdate();
		}finally {
		//6. 연결 끊기
			close(null, pstmt, con);
		}//end finally
		
		return cnt;
	}//deleteCpDept
	
	/**
	 * 모든 부서 정보 조회
	 * @return
	 */
	public List<DeptVO> selectAllCpDept() throws SQLException{
		List<DeptVO> list = new ArrayList<DeptVO>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		//1. 드라이버 로딩
		try {
		
		//2. 커넥션 얻기
			con = getConnection();
		//3. 쿼리문 생성 객체 얻기
			String selectCpDept = "select deptno, dname, loc from cp_dept";
			pstmt = con.prepareStatement(selectCpDept);
			
		//4. 바인드 변수에 값 넣기 --skip
		//5. 쿼리문 수행 후 결과 얻기
			rs = pstmt.executeQuery();
			
			DeptVO dVO = null;
			
			while(rs.next()) {
				//조회된 컬럼 값을 VO에 할당
				dVO=new DeptVO(rs.getInt("deptno"),rs.getString("dname"), rs.getString("loc"));
				// 생성된 VO (레코드 값을 가지고 있음)를 List에 추가
				list.add(dVO);
			}//end while
			
		}finally {
		//6. 연결 끊기
			close(rs, pstmt, con);
		}//end finally
		
		return list;		
	}//selectAllCpDept
	
	/**
	 * 부서 번호를 입력 받아 하나의 부서 정보를 조회하는 일
	 * @param deptno 조회할 부서 번호
	 * @return
	 */
	public DeptOneVO selectOneCpDept(int deptno)throws SQLException {
		DeptOneVO doVO= null;
		
		//1. 드라이버 로딩
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
		//2. 커넥션 얻기
			con=getConnection();
		//3. 쿼리문 생성 객체 얻기
			StringBuilder selectCpDept = new StringBuilder();
			selectCpDept
			.append("	select	dname,loc	")
			.append("	from	cp_dept		")
			.append("	where	deptno=?	");
			
			pstmt = con.prepareStatement(selectCpDept.toString());
		//4. 바인드 변수에 값 설정
			pstmt.setInt(1,deptno);
		//5. 쿼리문 수행 후 결과 얻기
			rs=pstmt.executeQuery();
			
			if (rs.next()) {// 조회 결과가 있다면
				doVO = new DeptOneVO(rs.getString("dname"), rs.getString("loc"));
			}//end if
			
			
		}finally {
		//6. 연결 끊기
			close(rs, pstmt, con);
		}//end finally
		
		return doVO;
	}//selectOneCpDept
	
	public void close(ResultSet rs, PreparedStatement pstmt,Connection con)throws SQLException {
		if( rs != null) { rs.close();	}//end if
		if( pstmt != null) { pstmt.close();	}//end if
		if( con != null) { con.close();	}//end if
		
	}//close
	
}//class
