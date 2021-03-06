package day0413;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class Work0412Event extends WindowAdapter implements ActionListener, MouseListener {

	private RunCrudDAO rcDAO;
	private boolean selectFlag;
	
	public Work0412Event(RunCrudDAO rcDAO) {
		this.rcDAO=rcDAO;
		
	}//Work0412Event
	
	
	@Override
	public void windowClosing(WindowEvent we) {
		rcDAO.dispose();
	}//windowClosing

	@Override
	public void mouseClicked(MouseEvent me) {
		//마우스 버튼이 클릭 되었을 때 동작하는 method
		switch(me.getButton()) {//마우스 버튼은 3개. 이 중 누가 클릭 되었는지 알아야됨.  //button1 = 좌클릭.
			
		case MouseEvent.BUTTON1 :
			//선택된 아이템을 받아와서 JTextField 에 추가한다.
			String csvData = rcDAO.getJl().getSelectedValue(); //getJl --> view에서 가져온것
			String[] arrData = csvData.split(",");
			
			rcDAO.getJtfNum().setText(arrData[0]);
			rcDAO.getJtfName().setText(arrData[1]);
			rcDAO.getJtfAge().setText(arrData[2]);
			rcDAO.getJtfAddr().setText(arrData[3]);
			//JList의 item이 선택 되었는지 
			selectFlag = true;
		}//end catch
		
	}//mouseClicked

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	   @Override
	   public void actionPerformed(ActionEvent ae) {
	      //추가
	      if(ae.getSource()==rcDAO.getJbtnInsert()) {
	         addWork();
	      }//if
	      
	      //닫기
	      if(ae.getSource()==rcDAO.getJbtnClose()) {
	         rcDAO.dispose();
	      }//if
	      
	      //변경
	      if(ae.getSource()==rcDAO.getJbtnUpdate()) {
	         if(!selectFlag) {
	            JOptionPane.showMessageDialog(rcDAO,"변경하시려는 아이템을 먼저 선택해주세요");
	            return;
	         }//end if
	         modifyWork(); 
	      }//end if
	      
	      //삭제
	      if(ae.getSource()==rcDAO.getJbtnDelete()) {   
	    	  if(!selectFlag) {
	         JOptionPane.showMessageDialog(rcDAO,"삭제하시려는 아이템을 먼저 선택해주세요");
	         return;
	    	  }//end if
	    	  removeWork();
	      }//end if
	      
	      //버튼을 눌러서 작업을 수행하고 난 이후에는 JList의 item선택상태를 해제한다
	      selectFlag=false;
	      
	   }//actionPerformed

	private void removeWork() {
		//JTextField 의 값을 받아와서 DB에 삭제.
		String num = rcDAO.getJtfNum().getText().trim(); //사용자가 편집할 수 없음 (read Only)
		
		switch(JOptionPane.showConfirmDialog(rcDAO, num+"번 데이터를 삭제하시겠습니까?")) {
		case JOptionPane.OK_OPTION :
			Work0412DAO wDAO = Work0412DAO.getInstance();
			try {
				int cnt = wDAO.deleteWork(Integer.parseInt(num));
				String outputMsg = num +"번 데이터가 삭제 되지 않았습니다. 번호를 확인하세요";
				if (cnt ==1){//삭제 성공한 경우.
					outputMsg = num +"번 데이터가 삭제되었습니다.";
					setJListWork();//사용자에게 삭제 후 갱신한거 보여줘야함.
				}//end if
				JOptionPane.showMessageDialog(rcDAO, outputMsg);
			} catch (SQLException se) {
				se.printStackTrace();
				JOptionPane.showMessageDialog(rcDAO, "ㅈㅅ! DB에서 문제생겼음ㅋ");
			}//end catch --NumberFormatException 안떨어짐 -> readOnly라 사용자가 개입할 수 없음.
		}//end switch
	   }//removeWork
	   
	   private void modifyWork() {
			//JTextField 의 값을 받아와서 DB에 변경.
			String num = rcDAO.getJtfNum().getText().trim(); //사용자가 편집할 수 없음 (read Only)
			String name = rcDAO.getJtfName().getText().trim();
			String age = rcDAO.getJtfAge().getText().trim();
			String addr = rcDAO.getJtfAddr().getText().trim();
			
			if("".equals(name)) {
				JOptionPane.showMessageDialog(rcDAO, "이름은 필수 입력");
				rcDAO.getJtfName().requestFocus(); //커서 갖다놔주는게 좋을듯!
				return;//아래로 흘러내려가지 못하게 막음
			}//end if
			
			if("".equals(age)) {
				JOptionPane.showMessageDialog(rcDAO, "나이는 필수 입력");
				rcDAO.getJtfAge().requestFocus(); //커서 갖다놔주는게 좋을듯!
				return;//아래로 흘러내려가지 못하게 막음
			}//end if
			
			int intAge = 0;
			try {
				intAge = Integer.parseInt(age);
			}catch(NumberFormatException nfe) {
				JOptionPane.showMessageDialog(rcDAO, "나이는 숫자로 입력헤주세요");
				rcDAO.getJtfAge().setText("");
				rcDAO.getJtfAge().requestFocus(); //커서 갖다놔주는게 좋을듯!
				return;//아래로 흘러내려가지 못하게 막음
			}//end catch
			
			if("".equals(addr)) {
				JOptionPane.showMessageDialog(rcDAO, "주소는 필수 입력");
				rcDAO.getJtfAddr().requestFocus(); //커서 갖다놔주는게 좋을듯!
				return;//아래로 흘러내려가지 못하게 막음
			}//end if
			
			switch (JOptionPane.showConfirmDialog(rcDAO, name + "의 정보를 변경하시겠습니까?")) {
			case JOptionPane.OK_OPTION :
				//유효성 검증이 종료되었다면, 분할되어 있는 값을 VO에 넣는다. (쪼개져있는 값을 하나로 묶어서 쓰려고 VO에 넣는것)
				WorkAllVO waVO = new WorkAllVO(Integer.parseInt(num), name, intAge, addr);
				
				//DB작업을 위한 DAO 클래스 객체 얻기
				Work0412DAO wDAO = Work0412DAO.getInstance();
				
				try {
					int cnt = wDAO.updateWork(waVO);
					String outputMsg = waVO.getName()+"님의 정보가 변경되지 않았습니다.";
					
					if(cnt ==1) {
						outputMsg = waVO.getName()+"님의 정보가 변경되었습니다.";
						setJListWork();//변경 내용을 사용자에게 보여줍니다.
					}//end if
					
					JOptionPane.showMessageDialog(rcDAO, outputMsg);
				} catch (SQLException se) {
					se.printStackTrace();
					JOptionPane.showMessageDialog(rcDAO, "변경작업 중 문제가 발생하였습니다.");
				}//end catch
			
			}//end switch
			
	   }//modifyWork
	   
	   
	private void addWork() {
		//JTextField 의 값을 받아와서 DB에 추가.
		String name = rcDAO.getJtfName().getText().trim();
		String age = rcDAO.getJtfAge().getText().trim();
		String addr = rcDAO.getJtfAddr().getText().trim();
		
		if("".equals(name)) {
			JOptionPane.showMessageDialog(rcDAO, "이름은 필수 입력");
			rcDAO.getJtfName().requestFocus(); //커서 갖다놔주는게 좋을듯!
			return;//아래로 흘러내려가지 못하게 막음
		}//end if
		
		if("".equals(age)) {
			JOptionPane.showMessageDialog(rcDAO, "나이는 필수 입력");
			rcDAO.getJtfAge().requestFocus(); //커서 갖다놔주는게 좋을듯!
			return;//아래로 흘러내려가지 못하게 막음
		}//end if
		
		int intAge = 0;
		try {
			intAge = Integer.parseInt(age);
		}catch(NumberFormatException nfe) {
			JOptionPane.showMessageDialog(rcDAO, "나이는 숫자로 입력헤주세요");
			rcDAO.getJtfAge().setText("");
			rcDAO.getJtfAge().requestFocus(); //커서 갖다놔주는게 좋을듯!
			return;//아래로 흘러내려가지 못하게 막음
		}//end catch
		
		if("".equals(addr)) {
			JOptionPane.showMessageDialog(rcDAO, "주소는 필수 입력");
			rcDAO.getJtfAddr().requestFocus(); //커서 갖다놔주는게 좋을듯!
			return;//아래로 흘러내려가지 못하게 막음
		}//end if
		
		WorkAddVO waVO = new WorkAddVO(name, intAge, addr);
		Work0412DAO wDAO = Work0412DAO.getInstance();
		try {
			wDAO.insertWork(waVO);//입력한 데이터를 DBMS Table에 추가하고
			setJListWork();//입력한 상태의 데이터를 갱신하여 JList에 출력하고
			JOptionPane.showMessageDialog(rcDAO, "입력하신 정보가 추가되었습니다.");
		} catch (SQLException se) {
			se.printStackTrace();
			JOptionPane.showMessageDialog(rcDAO, "예외 발생 ㅈㅅ");
			
		}//end catch

	}//addWork
	
	/**
	 * WORK 테이블의 모든 레코드를 조회하여 JList에 설정하는 일
	 */
	public void setJListWork() {
		
		Work0412DAO wDAO = Work0412DAO.getInstance();
		try {
			List<WorkAllVO> list = wDAO.selectAllWork();
			
			//디자인 클래스에서 뷰(JList)의 데이터를 관리하는 객체(DefaultListModel)를 얻는다.
			DefaultListModel<String> dlm = rcDAO.getDflm();
			//기존의 데이터를 초기화
			dlm.clear();
			
			//조회된 결과를 Model 객체에 설정한다.
			StringBuilder viewData = new StringBuilder();
			for(WorkAllVO waVO : list) {
				viewData.append(waVO.getNum()).append(",")
				.append(waVO.getName()).append(",")
				.append(waVO.getAge()).append(",")
				.append(waVO.getAddr());
				
				dlm.addElement(viewData.toString());
				
				viewData.delete(0, viewData.length());
			}//end for
			
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(rcDAO, "서비스가 원활하지 못한점 ㅈㅅ!");
		}//end catch
		
	}//setJListWork
	
	

}//class
