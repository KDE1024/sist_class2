package day0412;

import java.sql.SQLException;

import javax.swing.JOptionPane;
import static java.lang.Integer.parseInt;

/**
 * 실행
 * @author user
 */
public class RunCallableStatement {

	public void addCpEmp() {
		String inputData = JOptionPane.showInputDialog("사원번호, 사원명, 부서번호, 직무를 입력하세요");
		String[] data = inputData.split(",");
		
		UseCallableStatementDAO ucsDAO = UseCallableStatementDAO.getInstance();
		try {
			String msg = ucsDAO.insertCpEmp4(new CpEmp4VO( parseInt(data[0]), data[1], parseInt(data[2]), data[3] ));
			JOptionPane.showMessageDialog(null, msg);
		} catch (SQLException e) {
			e.printStackTrace();
		}//end catch
		
	}//addCpEmp
	
	
	public static void main(String[] args) {
		RunCallableStatement rcs = new RunCallableStatement();
		rcs.addCpEmp();
		
	}//main

}//class
