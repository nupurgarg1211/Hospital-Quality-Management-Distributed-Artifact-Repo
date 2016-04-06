
import java.io.FileInputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.bson.Document;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriter;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.hoh.util.repackage.Base64.Charsets;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.v22.datatype.PN;
import ca.uhn.hl7v2.model.v22.message.ADT_A01;
import ca.uhn.hl7v2.model.v22.message.ADT_A04;
import ca.uhn.hl7v2.model.v22.segment.MSH;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.Parser; 
  

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.*;

  public class ParseHL7Message_old
  {
  
        private static BasicAWSCredentials credentials;
	    private static AmazonSQS sqs;
	    private static String simpleQueue = "PhotoQueue";
	  
	  
	  
      public static void main(String[] args) throws HL7Exception {
     /*    String msg = "MSH|^~\\&|HIS|RIH|EKG|EKG|199904140038||ADT^A01|MSGADT003|P|2.2\r"
                  + "PID|0001|00009874|00001122|A00977|SMITH^JOHN^M|MOM|19581119|F|NOTREAL^LINDA^M|C|564 SPRING ST^^NEEDHAM^MA^02494^US|0002|(818)565-1551|(425)828-3344|E|S|C|0000444444|252-00-4414||||SA|||SA||||NONE|V1|0001|I|D.ER^50A^M110^01|ER|P00055|11B^M011^02|070615^BATMAN^GEORGE^L|555888^NOTREAL^BOB^K^DR^MD|777889^NOTREAL^SAM^T^DR^MD^PHD|ER|D.WT^1A^M010^01|||ER|AMB|02|070615^NOTREAL^BILL^L|ER|000001916994|D||||||||||||||||GDD|WA|NORM|02|O|02|E.IN^02D^M090^01|E.IN^01D^M080^01|199904072124|199904101200|199904101200||||5555112333|||666097^NOTREAL^MANNY^P\r"
                  + "NK1|0222555|NOTREAL^JAMES^R|FA|STREET^OTHER STREET^CITY^ST^55566|(222)111-3333~(222)222-4444|(888)999-0000|||||||ORGANIZATION\r"
                  + "PV1|0001|I|D.ER^1F^M950^01|ER|P000998|11B^M011^02|070615^BATMAN^GEORGE^L|555888^OKNEL^BOB^K^DR^MD|777889^NOTREAL^SAM^T^DR^MD^PHD|ER|D.WT^1A^M010^01|||ER|AMB|02|070615^VOICE^BILL^L|ER|000001916994|D||||||||||||||||GDD|WA|NORM|02|O|02|E.IN^02D^M090^01|E.IN^01D^M080^01|199904072124|199904101200|||||5555112333|||666097^DNOTREAL^MANNY^P\r"
                  + "PV2|||0112^TESTING|55555^PATIENT IS NORMAL|NONE|||19990225|19990226|1|1|TESTING|555888^NOTREAL^BOB^K^DR^MD||||||||||PROD^003^099|02|ER||NONE|19990225|19990223|19990316|NONE\r"
                  + "AL1||SEV|001^POLLEN\r"
                  + "GT1||0222PL|NOTREAL^BOB^B||STREET^OTHER STREET^CITY^ST^77787|(444)999-3333|(222)777-5555||||MO|111-33-5555||||NOTREAL GILL N|STREET^OTHER STREET^CITY^ST^99999|(111)222-3333\r"
                  + "IN1||022254P|4558PD|BLUE CROSS|STREET^OTHER STREET^CITY^ST^00990||(333)333-6666||221K|LENIX|||19980515|19990515|||PATIENT01 TEST D||||||||||||||||||02LL|022LP554";
  */
          /*
62           * The HapiContext holds all configuration and provides factory methods for obtaining
63           * all sorts of HAPI objects, e.g. parsers. 
64           */
    	/*  String msg = "MSH|^~\\&|HL7Soup|Instance1|HL7Soup|Instance2|200808181126|SECURITY|ADT^A04|MSG00001|P|2.5.1\r"
    			  +"EVN|A01-|200808181123|\r"
    			  +"PID||77291|PATID1234^9^55A||Smith^Willian^A^III||19720415|M-||2106-3|555 Upper Harbour Rd^^Springfield^NC^3401-540|NN|(919)479-4354|(919)371-654~(919)377-3455||S|AGN|PATID12345001^2^R10|1234567890|A-84334^NC\r"
    			  +"NK1|1|Smith^Barbara^K|SPO|||||20011105\r"
    			  +"NK1|1|Smith^Bill^A|FTH\r"
    			  +"PV1|1|I|2000^2012^01||||004777^LEBAUER^SIDNEY^J.|||SUR||-||1|A0-\r"
    			  +"AL1|1||^PENICILLIN||PRODUCES HIVES~RASH\r"
    			  +"AL1|2||^CAT DANDER\r"
    			  +"DG1|001|I9|1550|MAL NEO LIVER, PRIMARY|20080501103005|F||\r"
    			  +"PR1|2234|M11|111^CODE151|COMMON PROCEDURES|200809081123\r"
    			  +"ROL|45^RECORDER^ROLE MASTER LIST|AD|CP|34622^SMITH^JILL|200805011201\r"
    			  +"GT1|1122|1519|BILL^GATES^A\r"
    			  +"IN1|001|A357|1234|BCMD|||||132987\r"
    			  +"IN2|ID1551001|SSN12345678";*/
  
    	  String url = "https://sqs.ap-southeast-1.amazonaws.com/886148662449/PhotoQueue";
    	  
    	  
    	  try{
	            Properties properties = new Properties();
	            properties.load(new FileInputStream("AwsCredentials.properties"));
	            credentials = new   BasicAWSCredentials(properties.getProperty("accessKey"),
	                                                         properties.getProperty("secretKey"));
	            simpleQueue = "PhotoQueue";

	           sqs = new AmazonSQSClient(credentials);
	            
	            sqs.setEndpoint("https://sqs.ap-southeast-1.amazonaws.com");
	       

	        }catch(Exception e){
	            System.out.println("exception while creating awss3client : " + e);
	        }
		  
		  //msg=dem.getMessagesFromQueue(url);
		  ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(url);
	       List<com.amazonaws.services.sqs.model.Message> msglist = sqs.receiveMessage(receiveMessageRequest).getMessages();
			for (com.amazonaws.services.sqs.model.Message message : msglist) {
//	            System.out.println("  Message");
//	            System.out.println("    MessageId:     " + message.getMessageId());
//	            System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
//	            System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
//	            System.out.println("    Body:          " + message.getBody());
	           
	            }
			
			if(msglist==null)
			{
				System.out.println("NO Message in queue!!");
				
			}
			System.out.println(msglist.size());
    	  
			ParseHL7Message_old obj = new ParseHL7Message_old();
			
			for(com.amazonaws.services.sqs.model.Message message : msglist){
				obj.storeinDB(message.getBody());
				
				String messageRecieptHandle = message.getReceiptHandle();
		        System.out.println("message deleted : " + message.getBody() + "." + message.getReceiptHandle());
		        sqs.deleteMessage(new DeleteMessageRequest(url, messageRecieptHandle));
			}
			
			
			
			
      }
      
      void storeinDB(String msg) throws HL7Exception{
    	  
    	  HapiContext context = new DefaultHapiContext();
    	  MongoClient client = new MongoClient("localhost",27017);
    	  MongoDatabase db = client.getDatabase("HL7");
    	  MongoCollection<Document> coll = db.getCollection("ParsedHL7");
    	  Document document = new Document();
          
          Parser p = context.getGenericParser();
          
          Message hapiMsg;

              // The parse method performs the actual parsing
              hapiMsg = p.parse(msg);
 //             System.out.println("====>"+hapiMsg.encode()+"<=======");
  
          /*Class<?> adtMsg = null;
          try{
        	  adtMsg = f.getClass().getMethod("getMSH()", new Class<?>[0]);
	  		}catch(Exception e){
	  			e.printStackTrace();
	  		}*/
          
              
              Structure[] list2 = hapiMsg.getAll("PID");
              
              for (Structure structure : list2) {
//				System.out.println("====hi====>"+structure.getName()+"<====hi=====");
			}
          
  //        System.out.println("======>"+hapiMsg.getClass().getName());	
          
          
         
          
      
          ADT_A01 adtMsg = (ADT_A01)hapiMsg;
           MSH msh = adtMsg.getMSH();
         /* Method mshobj;
          MSH msh = null;
		try {
			mshobj = hapiMsg.getClass().getMethod("getMSH", new Class<?>[0]);
			msh = (MSH) mshobj.invoke(hapiMsg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		
	//	System.out.println(msh.getMessageControlID()+"ID");
		
		//document.append("_id", msh.getMessageControlID().toString());
		 document.append("Event", msh.getMessageType().getTriggerEvent().getValue().toString() );
		 document.append("MsgDtTm", msh.getDateTimeOfMessage().toString() );
         
		
		
	
		Method[] methods =hapiMsg.getClass().getMethods();
		
		for(Method method : methods){
		   // System.out.println("method = " + method.getName());
		}
        
		
          // Retrieve some data from the MSH segment
       
          String msgType = msh.getMessageType().getMessageType().getValue();
         String msgTrigger = msh.getMessageType().getTriggerEvent().getValue();
 
         // Prints "ADT A01"
    //     System.out.println(msgType + "--" + msgTrigger);
       
         
         PN patientName = null;
         
         if(hapiMsg.getClass().getName().toString().substring(hapiMsg.getClass().getName().toString().lastIndexOf(".") +1).equals("ADT_A01")){
        	 patientName = adtMsg.getPID().getPatientName();
         }
         // Prints "SMITH"
         String familyName = patientName.getFamilyName().getValue();
         //patientName.getComponents();
         /*for(int i=0;i<patientName.getComponents().length;i++){
        	 try {
				System.out.println(patientName.getComponent(i).getName());
			} catch (DataTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}*/
         
         //1. Component Separator ^
         //2. Repeat Field character ~
         //3. Escape character \
         //4. Subcomponent separator &
         String charac =  msh.getEncodingCharacters().toString();
      //  System.out.println(msh.getMessageType());
      //   System.out.println(familyName+" "+patientName.getGivenName().getValue());
      //   System.out.println("----------------------------------------------------------------------------------");
         /*
         StringTokenizer segments = new StringTokenizer(msg,"\r");
        while(segments.hasMoreTokens()){
        	 String str = segments.nextToken();
        	 System.out.println(str);
        	 //System.out.println("======");
        	 
        	 StringTokenizer fields = new StringTokenizer(str,msh.getFieldSeparator().toString());
        	 String segmentName = fields.nextToken();
        	 //System.out.print(segmentName+"  :  ");
        	 int i=0;
        	 while(fields.hasMoreTokens()){
        		
        		 String str2 = fields.nextToken();
        		 StringTokenizer components = new StringTokenizer(str2,""+charac.charAt(0));			//^
        		 //System.out.println("^^^^^^^^^^");
        		 int j=1;
        		 i++;
        		 boolean flag = false;
        		 if(components.countTokens() > 1){
    				 flag = true; 
    			 }
        		 while(components.hasMoreElements()){
        			 System.out.print(segmentName+"_"+i);
        			 if(flag){
        				 System.out.print("."+j++); 
        			 }
        			 System.out.println(" "+components.nextToken());
        		 }
        		 //System.out.println();
        		 //System.out.println("^^^^^^^^^^");
        	 }
         }
         Component Separator ^
         //2. Repeat Field character ~
         //3. Escape character \
         //4. Subcomponent separator &
        */
         
         
         String backup = msg;
         
         //converting seperators from string to their hex values for split functions
         
         String temp = String.format("%040x", new BigInteger(1, msh.getFieldSeparator().toString().getBytes(Charsets.UTF_8)));
  	 	 int x = temp.length();
         String Field_Separator = temp.substring(x-2);
         String char_to_string = ""+ charac.charAt(0) ;
         temp = String.format("%040x", new BigInteger(1, char_to_string.getBytes(Charsets.UTF_8)));
  	 	 x = temp.length();
         String Component_Separator = temp.substring(x-2);
         
         char_to_string = ""+ charac.charAt(1) ;
         temp = String.format("%040x", new BigInteger(1, char_to_string.getBytes(Charsets.UTF_8)));
  	 	 x = temp.length();
         String Repeat_field = temp.substring(x-2);
         
         char_to_string = ""+ charac.charAt(2) ;
         temp = String.format("%040x", new BigInteger(1, char_to_string.getBytes(Charsets.UTF_8)));
  	 	 x = temp.length();
         String Escape_character = temp.substring(x-2);
         
         char_to_string = ""+ charac.charAt(3) ;
         temp = String.format("%040x", new BigInteger(1, char_to_string.getBytes(Charsets.UTF_8)));
  	 	 x = temp.length();
         String Sub_Component_Separator = temp.substring(x-2);
 	 	 
         
         
         StringTokenizer segments = new StringTokenizer(msg,"\r"); // segments
         Document allSegments = new Document();
         int globalCountSegment= 0;
        while(segments.hasMoreTokens()){
        	Document eachSegment = new Document();
 
         	//for(String str : msg.split("\r")){
        	 String str = segments.nextToken();
        	 System.out.println(str);
        	 eachSegment.append("Val",str);
        	 eachSegment.append("Seq", globalCountSegment);
        	 globalCountSegment++;
        	 //System.out.println("======");
        	 //StringTokenizer fields = new StringTokenizer(str,msh.getFieldSeparator().toString());		//fields.....seperator: |
        	 //String segmentName = fields.nextToken(); //segment type like MSH,....
        	 //System.out.print(segmentName+"  :  ");
        	 //int i=0;
        	 //while(fields.hasMoreTokens()){
        	 	int i=0;
        	 	String segmentName="";
        	 	/*System.out.println("loll");
        	 	for(String fields1 : str.split(msh.getFieldSeparator().toString())){
        	 		System.out.println(fields1);
        	 	}
        	 	
        	 	System.out.println("===========>"+msh.getFieldSeparator().toString()+"<============");*/
        	 	
        	 	
    /*    	 	String temp = String.format("%040x", new BigInteger(1, msh.getFieldSeparator().toString().getBytes(Charsets.UTF_8)));
        	 	int x = temp.length();
        	 	System.out.println(temp.substring(x-2));*/
        	 	boolean inheader = true;
        	 	ArrayList<Document> fieldsList = new ArrayList<Document>();
        		for(String fields : str.split("[\\x"+Field_Separator+"]")){
        			
        		
        			Document eachfield = new Document();
        			if(i==0 & inheader){
        				segmentName = fields;
        				//System.out.println("---"+fields+"----");
        				inheader = false;
        				continue;
        			}
        			
        	 
        		// String str2 = fields.nextToken(); // fields that are in particular segment type
        			String str2 = fields;
        		 i++;
        		 if(!fields.equals("")){
        			 System.out.println(segmentName+"_"+i+": "+str2);
        			 eachfield.append("_id",segmentName+"_"+i);
        			 eachfield.append("Val", str2);
        			 //fieldsList.add(eachfield);
        			 if(fields.equals(charac)){
        				fieldsList.add(eachfield);
        				continue;
        			 }
        		 }
        		 else{
        			 continue;
        		 }
        		 
        		 if(str2.contains(""+charac.charAt(1))){ // repeater seperator : like ~
        			
        			 int rep = 0;
        			 ArrayList<Document> repeaterList = new ArrayList<Document>();
        			 for(String repcomponents : str2.split("[\\x"+Repeat_field+"]")){ // split the field based on the repeater
        				 
        				 Document eachrepeated = new Document();
        				 
        				 rep++;
        				 System.out.println("Rep : "+ rep +" : "+repcomponents);
        				 
            			 eachrepeated.append("_id",segmentName+"_"+i);
            			 eachrepeated.append("Val", repcomponents);
            			 eachrepeated.append("Rep", rep);
            			 repeaterList.add(eachrepeated);
        				 
        				 if(repcomponents.contains(""+charac.charAt(0))){ // checking wheather there is a ^ in repeated components after spliting
        					 
        					 //StringTokenizer components = new StringTokenizer(repcomponents,""+charac.charAt(0));			//^
        	        		 //System.out.println("^^^^^^^^^^");
        					 int j=0;
        	        		 //while(components.hasMoreElements()){
        					 for(String components : repcomponents.split("[\\x"+Component_Separator+"]")){ // component seperator ^
        	        			 //String str3 = components.nextToken();
            					 Document eachrepeated_component = new Document();
        						 j++;
        						 String str3 = components;
        						 if(!components.equals("")){
	        	        			 System.out.print(segmentName+"_"+i);
	        	        			 System.out.print("_"+j); 
	        	        			 System.out.println(" "+str3);
	        	        			 
	        	        			 eachrepeated_component.append("_id",segmentName+"_"+i+"_"+j);
	        	        			 eachrepeated_component.append("Val",str3);
	        	        			 eachrepeated_component.append("Rep",rep);
	        	        			 repeaterList.add(eachrepeated_component);
	        	        			 
        						 }
        	        			 if(str3.contains(""+charac.charAt(3))){
        	  //      				 StringTokenizer subcomponents = new StringTokenizer(str3,""+charac.charAt(3));			//^
                	        		 //System.out.println("^^^^^^^^^^");
                					 int k=0;
                					 for(String subcomponents : repcomponents.split("[\\x"+Sub_Component_Separator+"]")){
                	        		 //while(subcomponents.hasMoreElements()){
                						 Document eachrepeated_subcomponent = new Document();
                						 k++;
                						 String str4 = subcomponents;
                						 if(!subcomponents.equals("")){
	                	        			 System.out.print(segmentName+"_"+i);
	                	        			 System.out.print("_"+j); 
	                	        			 System.out.print("_"+k);
	                	        			 System.out.println(" "+str4);
	                	        			 
	                	        			 eachrepeated_subcomponent.append("_id",segmentName+"_"+i+"_"+j+"_"+k);
	                	        			 eachrepeated_subcomponent.append("Val",str4);
	                	        			 eachrepeated_subcomponent.append("Rep",rep);
	                	        			 repeaterList.add(eachrepeated_subcomponent);
                						 }
                	        		 }
        	        			 }
        	        		 }
        	        		 //System.out.println();
        	        		 //System.out.println("^^^^^^^^^^");

        				 }
        				 
        				 
        			 }
        			 
        			 int size = repeaterList.size();
        			 Document [] repeateds = new Document[size];
        			 for(int s=0;s<size;s++){
        				 repeateds[s] = repeaterList.get(s);
        			 }
        			 
        			 eachfield.append("Repetitions",Arrays.asList(repeateds));
        			 fieldsList.add(eachfield);
        		 }
        		 else{
        			 fieldsList.add(eachfield);
        			 if(str2.contains(""+charac.charAt(0))){ // checking wheather there is a ^ in repeated components after spliting
    					 
    		//			 StringTokenizer components = new StringTokenizer(str2,""+charac.charAt(0));			//^
    	        		 //System.out.println("^^^^^^^^^^");
    					 int j=0;
    					 for(String components : str2.split("[\\x"+Component_Separator+"]")){
    	        		 //while(components.hasMoreElements()){
    	        			 //String tokens = components.nextToken();
    						 Document eachcomponent = new Document();
    						 j++;
    						 String tokens = components;
    						 if(!components.equals("")){
    							 
	    	        			 System.out.print(segmentName+"_"+i);
	    	        			 System.out.print("_"+j); 
	    	        			 System.out.println(" "+tokens);
	    	        			 
	    	        			 eachcomponent.append("_id",segmentName+"_"+i+"_"+j);
        	        			 eachcomponent.append("Val",components);
        	        			 
        	        			 fieldsList.add(eachcomponent);
	    	        			 
    						 }
    	        			 if(tokens.contains(""+charac.charAt(3))){
    	        				 //StringTokenizer subcomponents = new StringTokenizer(tokens,""+charac.charAt(3));			//^
            	        		 //System.out.println("^^^^^^^^^^");
            					 int k=0;
            					 for(String subcomponents : tokens.split("[\\x"+Sub_Component_Separator+"]")){
            	        		// while(subcomponents.hasMoreElements()){
            						 k++;
            						 String subtokens = subcomponents;
            						 Document eachsubcomponent = new Document();
            						 if(!subcomponents.equals("")){
            							 
	            	        			 System.out.print(segmentName+"_"+i);
	            	        			 System.out.print("_"+j); 
	            	        			 System.out.print("_"+k);
	            	        			 System.out.println(" "+subtokens);
	            	        			 
	            	        			 
	            	        			 eachsubcomponent.append("_id",segmentName+"_"+i+"_"+j+"_"+k);
                	        			 eachsubcomponent.append("Val",subtokens);
                	        			 
                	        			 fieldsList.add(eachsubcomponent);
            						 }
            	        		 }
    	        			 }
    	        		 }
    	        		 //System.out.println();
    	        		 //System.out.println("^^^^^^^^^^"); 
    				 }
        		 }
        		 
        	 }
        		
        		int size = fieldsList.size();
        		Document [] arr = new Document[size];
        		for(int s=0;s<size;s++){
        			arr[s] = fieldsList.get(s);
        		}
        		
        		eachSegment.append("Fields", Arrays.asList(arr));
        		allSegments.append(segmentName, eachSegment);
         }
        document.append("Segments",allSegments);
        coll.insertOne(document);
        
        JsonWriter jw1 = new JsonWriter(new StringWriter(), new JsonWriterSettings(JsonMode.SHELL,true));	// true : for indentation... false : w/o indentation
        new DocumentCodec().encode(jw1, document, EncoderContext.builder().isEncodingCollectibleDocument(true).build());
        System.out.println(jw1.getWriter());
        System.out.flush();
       
     }
 
 }
