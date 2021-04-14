package day0414;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class SQLInjection extends JFrame implements ActionListener {

	private JTextField jtfId;
	private JPasswordField jpfPass;
	private JLabel jlOutput;
	
	public SQLInjection() {
		super("SQLInjection ����");
		jtfId=new JTextField();
		jpfPass=new JPasswordField();
		jlOutput=new JLabel("��� ���");
		
		jtfId.setBorder(new TitledBorder("���̵�"));
		jpfPass.setBorder(new TitledBorder("��й�ȣ"));
		jlOutput.setBorder(new TitledBorder("���"));
		
		setLayout(new GridLayout(3,1));
		
		add( jtfId );
		add( jpfPass );
		add( jlOutput );
		
		jtfId.addActionListener(this);
		jpfPass.addActionListener(this);
		
		setBounds(100, 100, 300, 250);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}//SQLInjection
	
	@Override
	public void actionPerformed(ActionEvent ae) {

		if(ae.getSource()==jtfId) {			
			chkNull();
		}//end if
		
		if(ae.getSource()==jpfPass) {			
			chkNull();
			
		}//end if
		
	}//actionPerformed 
	
	private void chkNull() {
//		String id = jtfId.getText().replaceAll(" ",""); //���� ��� X
//		String pass = new String( jpfPass.getPassword()).replaceAll(" ", ""); //���� ��� X
		String id = jtfId.getText(); //' or 1=1 -- //������ ������ �����
		String pass = new String(jpfPass.getPassword());
	
		if("".endsWith(id)) {
			JOptionPane.showMessageDialog(this, "���̵�� �ʼ� �Է� ����!!");
			jtfId.requestFocus();//���̵� Ŀ�� ���ٳ���
			return;
		}//end if
	
		if("".endsWith(pass)) {
			JOptionPane.showMessageDialog(this, "��й�ȣ�� �ʼ� �Է� ����!!");
			jpfPass.requestFocus();//���̵� Ŀ�� ���ٳ���
			return;
		}//end if
		
		login(id, pass);
		
	}//chkNull
	
	private void login (String id, String pass){
		InjectionDAO iDAO = new InjectionDAO();
		
		try {
//			String name = iDAO.statementLogin(id, pass);
			String name = iDAO.preparedstatementLogin(id, pass);
			
			if("".equals(name)) {
				jlOutput.setText("���̵� ��й�ȣ�� Ȯ���ϼ���.");
			}else {
				jlOutput.setText(name + "�� �α��� �ϼ̽��ϴ�.");
			}//end else
			
		}catch (SQLException e){
			e.printStackTrace();
		}//end catch

	}//login

	public static void main(String[] args) {
		new  SQLInjection();
	}//main

}//class