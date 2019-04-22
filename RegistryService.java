import java.net.*;
import java.io.*;
import java.util.*;
class RegistryService{
	static Hashtable table=new Hashtable();
    static int counter=0;
	
	public static void main(String arg[])throws Exception{
		ServerSocket server=new ServerSocket(9090);
		System.out.println("SERVER STARTED....");
		do{

			counter++;
			Socket socket=server.accept();
			InetAddress addr=socket.getInetAddress();
			//String ip=""+counter;
			String ip=addr.getHostAddress();////""+counter;   
			System.out.println(addr.getHostAddress());
			table.put(ip,socket);
			System.out.println("Connected: "+ip);
			
			HandleClientConnection con=new HandleClientConnection(socket,ip);
			con.start();
		}while(true);
	}//mainmethod

static class HandleClientConnection extends Thread{
	Socket socket;
	String ip;
	
	HandleClientConnection(Socket socket,String ip){
		this.socket=socket;
		this.ip=ip;
	}//class HandleClientConnection

	public void run(){
		try{
			DataInputStream in=new DataInputStream(socket.getInputStream());
			sendClientList();
			String msg=in.readLine();	
		}catch(Exception e){
			table.remove(ip);
			System.out.println("Removed: "+ip);

			sendClientList();
		}		
	}//run

	private void sendClientList(){
	String clientList=getClientList();
	Enumeration e=table.keys();
		while(e.hasMoreElements()){
			String ipAddress=(String)e.nextElement();
			Socket socket=(Socket)table.get(ipAddress);	
			try{
				PrintStream out=new PrintStream(socket.getOutputStream());	
				out.println(clientList);	
			}catch(Exception ee){ee.printStackTrace();}	
		}
	}	

	public String getClientList(){
		Enumeration e=table.keys();
		String ips="";
		String symbol="";
		try{
			while(e.hasMoreElements()){
				String ipAddress=(String)e.nextElement();
				Socket socket=(Socket)table.get(ipAddress);
				ips+=symbol+ipAddress;
                                symbol=":"; 
			}//whileloop			
		}catch(Exception ee){ee.printStackTrace();}
		return ips;						
	}//getClientList
	
	}
}