package examples;
import java.io.*;  
import java.net.*; 

public class MyServer {
	public static void main(String args[])throws Exception{  
		Socket s=new Socket("192.168.0.229",65111);  
		DataInputStream din=new DataInputStream(s.getInputStream());  
		DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  
		  
		String str="",str2="";  
		while(!str.equals("stop")){  
		str=br.readLine();  
		dout.writeUTF(str);  
		dout.flush();  
		str2=din.readUTF();  
		System.out.println("Server says: "+str2);  
		}  
		  
		dout.close();  
		s.close();  
		}   
}
