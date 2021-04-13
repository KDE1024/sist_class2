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
	
	public Work0412Event(RunCrudDAO rcDAO) {
		this.rcDAO=rcDAO;
		
	}//Work0412Event
	
	
	@Override
	public void windowClosing(WindowEvent we) {
		rcDAO.dispose();
	}//windowClosing

	@Override
	public void mouseClicked(MouseEvent me) {
		//���콺 ��ư�� Ŭ�� �Ǿ��� �� �����ϴ� method
		switch(me.getButton()) {//���콺 ��ư�� 3��. �� �� ���� Ŭ�� �Ǿ����� �˾ƾߵ�.  //button1 = ��Ŭ��.
			
		case MouseEvent.BUTTON1 :
			//���õ� �������� �޾ƿͼ� JTextField �� �߰��Ѵ�.
			String csvData = rcDAO.getJl().getSelectedValue(); //getJl --> view���� �����°�
			String[] arrData = csvData.split(",");
			
			rcDAO.getJtfNum().setText(arrData[0]);
			rcDAO.getJtfName().setText(arrData[1]);
			rcDAO.getJtfAge().setText(arrData[2]);
			rcDAO.getJtfAddr().setText(arrData[3]);
			
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
		if(ae.getSource()==rcDAO.getJbtnInsert()) {
			addWork();
		}//end if
		
		if(ae.getSource()==rcDAO.getJbtnClose()) {
			rcDAO.dispose();
		}//end if
		
		
	}//actionPerformed
	
	private void addWork() {
		//JTextField �� ���� �޾ƿͼ� DB�� �߰�.
		String name = rcDAO.getJtfName().getText().trim();
		String age = rcDAO.getJtfAge().getText().trim();
		String addr = rcDAO.getJtfAddr().getText().trim();
		
		if("".equals(name)) {
			JOptionPane.showMessageDialog(rcDAO, "�̸��� �ʼ� �Է�");
			rcDAO.getJtfName().requestFocus(); //Ŀ�� ���ٳ��ִ°� ������!
			return;//�Ʒ��� �귯�������� ���ϰ� ����
		}//end if
		
		if("".equals(age)) {
			JOptionPane.showMessageDialog(rcDAO, "���̴� �ʼ� �Է�");
			rcDAO.getJtfAge().requestFocus(); //Ŀ�� ���ٳ��ִ°� ������!
			return;//�Ʒ��� �귯�������� ���ϰ� ����
		}//end if
		
		int intAge = 0;
		try {
			intAge = Integer.parseInt(age);
		}catch(NumberFormatException nfe) {
			JOptionPane.showMessageDialog(rcDAO, "���̴� ���ڷ� �Է����ּ���");
			rcDAO.getJtfAge().setText("");
			rcDAO.getJtfAge().requestFocus(); //Ŀ�� ���ٳ��ִ°� ������!
			return;//�Ʒ��� �귯�������� ���ϰ� ����
		}//end catch
		
		if("".equals(addr)) {
			JOptionPane.showMessageDialog(rcDAO, "�ּҴ� �ʼ� �Է�");
			rcDAO.getJtfAddr().requestFocus(); //Ŀ�� ���ٳ��ִ°� ������!
			return;//�Ʒ��� �귯�������� ���ϰ� ����
		}//end if
		
		WorkAddVO waVO = new WorkAddVO(name, intAge, addr);
		Work0412DAO wDAO = Work0412DAO.getInstance();
		try {
			wDAO.insertWork(waVO);//�Է��� �����͸� DBMS Table�� �߰��ϰ�
			setJListWork();//�Է��� ������ �����͸� �����Ͽ� JList�� ����ϰ�
			JOptionPane.showMessageDialog(rcDAO, "�Է��Ͻ� ������ �߰��Ǿ����ϴ�.");
		} catch (SQLException se) {
			se.printStackTrace();
			JOptionPane.showMessageDialog(rcDAO, "���� �߻� ����");
			
		}//end catch

		
		
	}//addWork
	
	/**
	 * WORK ���̺��� ��� ���ڵ带 ��ȸ�Ͽ� JList�� �����ϴ� ��
	 */
	public void setJListWork() {
		
		Work0412DAO wDAO = Work0412DAO.getInstance();
		try {
			List<WorkAllVO> list = wDAO.selectAllWork();
			
			//������ Ŭ�������� ��(JList)�� �����͸� �����ϴ� ��ü(DefaultListModel)�� ��´�.
			DefaultListModel<String> dlm = rcDAO.getDflm();
			//������ �����͸� �ʱ�ȭ
			dlm.clear();
			
			//��ȸ�� ����� Model ��ü�� �����Ѵ�.
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
			JOptionPane.showMessageDialog(rcDAO, "���񽺰� ��Ȱ���� ������ ����!");
		}//end catch
		
	}//setJListWork

}//class
