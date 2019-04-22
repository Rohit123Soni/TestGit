import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Client
{
	public static void main(String arg[])throws Exception
	{
		ClientFrame win=new ClientFrame();
		RegistryConnection reg=new RegistryConnection(win);
		reg.start();
		win.show();
	}
}

class RegistryConnection extends Thread
{
	ClientFrame win;
	
	RegistryConnection(ClientFrame win)
	{
		this.win=win;
	}
	
	public void run()
	{
		try
		{
			Socket socket = new Socket("127.0.0.1",9090);
			DataInputStream in=new DataInputStream(socket.getInputStream());
		  do
		  {
			  
			String list=in.readLine();
			win.updateList(list);
		  
		  }while(true);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}

class ClientFrame extends JFrame implements ActionListener
{
	JList list=new JList();
	JTextArea area=new JTextArea();
	JTextField text=new JTextField();
	JLabel lab=new JLabel("Service Station");
	JLabel conversation=new JLabel("Conversation");
	JLabel onlinepeers=new JLabel("Online Peers");
	JButton exitb=new JButton("Exit");
	Font f1=new Font("arial",Font.BOLD,40);
	Font f2=new Font("arial",Font.BOLD,20);
	JButton send=new JButton("Send");
	
	ClientFrame()
	{
		Container c=getContentPane();
		c.setLayout(null);
		setBounds(0,0,700,700);
		conversation.setBounds(100,10,300,200);
		lab.setBounds(500,0,100,100);
		onlinepeers.setBounds(400,65,200,100);
		list.setBounds(400,160,230,400);
		exitb.setBounds(400,570,230,50);
		area.setBounds(100,160,280,250);
		text.setBounds(100,420,280,40);
		send.setBounds(100,500,80,40);
		c.add(conversation);
		c.add(onlinepeers);
		c.add(list);
		c.add(lab);
		c.add(text);
		c.add(area);
		c.add(exitb);
		c.add(send);
		setVisible(true);
		//text.addActionListener(this);
		send.addActionListener(this);
		ConnectSever con=new ConnectSever();
		con.start();
	}
	
	public void updateList(String clients)
	{
		String str[]=clients.split(":");
		Vector v=new Vector();
		for(int i=0;i<str.length;i++)
			
			v.addElement(str[i]);
			list.setListData(v);
	}
	
	//public void valueChanged(ListSelectionEvent e)
	//{
		//ip=(String)list.getSelectedValue();
	//}
	
	public void actionPerformed(ActionEvent e)
	{
		        String ip=(String)list.getSelectedValue();
				if(ip==null)return;
				
		   try
			{
				Socket socket=new Socket(ip,9080);
				DataInputStream in=new DataInputStream(socket.getInputStream());
				PrintStream out=new PrintStream(socket.getOutputStream());
				String k=text.getText();
				out.println(k);
				out.close();
				socket.close();
			}catch(Exception ee)
			{ee.printStackTrace();}

			if(exitb==e.getSource())
			{
				System.exit(0);
			}
		
	}
	

	class ConnectSever extends Thread{
   
		public void run(){
   
			try{
				ServerSocket server = new ServerSocket(9080);
				do{
					Socket socket=server.accept();
					DataInputStream in=new DataInputStream(socket.getInputStream());
					//PrintStream out=new PrintStream(socket.getOutputStream());
					String key=in.readLine();
					area.append("\n"+key);
					in.close();
					socket.close();
				}while(true);
			}catch(Exception e){e.printStackTrace();}
		}
	}

}