package server;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.github.sarxos.webcam.Webcam;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

public class Server extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	
	private ServerSocket ss;
	private ServerSocket ss1;
	private Socket socket;
	private Socket socket1;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private DataInputStream dis;
	private DataOutputStream dos;
	private Webcam cam;
	BufferedImage bf;
	ImageIcon ic, ic1;
	public Thread tSend, tTake;
	public Thread tSendChat;
	public Thread tTakeChat;
	private JLabel lblYour;
	private JLabel lblCalled;
	private String s, s1;
	private JTextArea textArea;
	private JScrollPane js;

	public Server() {
		setBackground(Color.GREEN);
		setTitle("Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 777, 749);
		contentPane = new JPanel();
		contentPane.setBackground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblYour = new JLabel();
		lblYour.setBounds(10, 35, 367, 376);
		contentPane.add(lblYour);
		
		lblCalled = new JLabel();
		lblCalled.setBounds(387, 35, 367, 376);
		contentPane.add(lblCalled);
		
		JLabel lblNewLabel_2 = new JLabel("         Server");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_2.setBounds(128, 0, 127, 30);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("         Client");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_2_1.setBounds(500, 0, 127, 30);
		contentPane.add(lblNewLabel_2_1);
		
		textField = new JTextField();
		textField.setBounds(41, 506, 281, 56);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnSend = new JButton("Send mess");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SendChat();
			}
		});
		btnSend.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnSend.setBounds(87, 603, 152, 30);
		contentPane.add(btnSend);
		
		JLabel lblNewLabel_3 = new JLabel("          Chat");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_3.setBounds(525, 448, 134, 24);
		contentPane.add(lblNewLabel_3);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		js = new JScrollPane(textArea);
		js.setBounds(408, 506, 345, 196);
		js.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(js);
		
		cam = Webcam.getDefault();
			
		cam.open();
		
		try {
			ss = new ServerSocket(3000);
			ss1 = new ServerSocket(3002);
			socket = ss.accept();
			socket1 = ss1.accept();
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			
			dis = new DataInputStream(socket1.getInputStream());
			dos = new DataOutputStream(socket1.getOutputStream());
			
			Send();
			Take();
			SendChat();
			TakeChat();
			tTakeChat.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private ImageIcon getVideoServer() {
		bf = cam.getImage();
		ic = new ImageIcon(bf);
		return ic;
	}
	private void Send() {
		tSend = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					try {
					
					lblYour.setIcon(getVideoServer());
					oos.writeObject(getVideoServer());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	private void Take() {
		tTake = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					try {
						ic1 = (ImageIcon) ois.readObject();
						lblCalled.setIcon(ic1);

					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});	
	}
	private void SendChat() {
		try {
			s = textField.getText();
			textArea.append("Server: " + s + "\n");
			dos.writeUTF(s);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void TakeChat() {
		tTakeChat = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
					try {
						while(true) {
							s1 = dis.readUTF();
							textArea.append("Client: " + s1 + "\n");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		});
	}
}
