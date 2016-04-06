import java.net.*;
import java.io.*;
import java.util.*;

import com.amazonaws.services.sqs.model.*;

import java.util.*;
class Server implements Runnable{
	
	Socket soc;	
	PrintWriter out;
	ObjectInputStream in;
	Thread t;
	static List<Message> messages=null;

	Server(Socket sc){
		soc = sc;
		System.out.println("\nClient connected : "+soc.getInetAddress());
		t = new Thread(this);	
		t.start();		
		//interact();
						
	}
	
	public void run(){
	//void interact(){
		System.out.println("Waiting for message from client "+soc.getInetAddress()+" ...");
		try{	

			out =  new PrintWriter(soc.getOutputStream(), true);
			in = new ObjectInputStream(soc.getInputStream());
			String msg = in.readObject().toString();
			
			////////////////////////////////------------------------ into the queue-----------------------////////////////////////
			//the message you get is stored in variable msg
			AWSSimpleQueueServiceUtil dem = AWSSimpleQueueServiceUtil.getInstance();
			String url = "https://sqs.ap-southeast-1.amazonaws.com/886148662449/PhotoQueue";
			dem.sendMessageToQueue(url, msg);
			messages=dem.getMessagesFromQueue(url);
			for (Message message : messages) {
	            System.out.println("  Message");
	            System.out.println("    MessageId:     " + message.getMessageId());
	          //  System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
	           // System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
	            System.out.println("    Body:          " + message.getBody());
	           
	            }
			System.out.println("Queue Name : "+dem.getQueueName());
			if(msg==null)
			{
				System.out.println("hieee");
				
			}
			//System.out.println(messages.size());
			for(int i=0;i<messages.size();i++)
			{
				System.out.println("msg="+messages.get(i));
				
				
				
			}
			
			
			//////////////////////////////---------------------------queue code ends--------------------////////////////////////////
			out.println("Message Stored");
			
			System.out.println("Response Sent to : "+soc.getInetAddress());
		}
		catch(Exception e){
			System.out.println("ERROR : \n"+e.getMessage());
		}
	}
	
	public static void main(String[] args){
		ServerSocket ssoc = null;
		Socket soc = null;
		Server sc;
		try{
			ssoc = new ServerSocket(8442);
		}
		catch(Exception e){
			System.out.println("ERROR : \n"+e.getMessage());	
		}
		while(true){

			try{
				System.out.println("Server Awaiting New Client...");
				soc= ssoc.accept();
			}
			catch(Exception e){
				System.out.println("ERROR : \n"+e.getMessage());
			}

			sc = new Server(soc); 
		}									
	}

}
