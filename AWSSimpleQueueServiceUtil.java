

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.*;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

public class AWSSimpleQueueServiceUtil {
    private BasicAWSCredentials credentials;
    private AmazonSQS sqs;
    private String simpleQueue = "PhotoQueue";
    private static volatile  AWSSimpleQueueServiceUtil awssqsUtil = new AWSSimpleQueueServiceUtil();

    private   AWSSimpleQueueServiceUtil(){
        try{
            Properties properties = new Properties();
            properties.load(new FileInputStream("AwsCredentials.properties"));
            this.credentials = new   BasicAWSCredentials(properties.getProperty("accessKey"),
                                                         properties.getProperty("secretKey"));
            this.simpleQueue = "PhotoQueue";

            this.sqs = new AmazonSQSClient(this.credentials);
            
            this.sqs.setEndpoint("https://sqs.ap-southeast-1.amazonaws.com");
            
            //AmazonSQS sqs = new AmazonSQSClient(new ClasspathPropertiesFileCredentialsProvider());

        }catch(Exception e){
            System.out.println("exception while creating awss3client : " + e);
        }
    }

    public static AWSSimpleQueueServiceUtil getInstance(){
    	System.out.println("msgs  "+awssqsUtil);
        return awssqsUtil;
    }

    public AmazonSQS getAWSSQSClient(){
         return awssqsUtil.sqs;
    }

    public String getQueueName(){
         return awssqsUtil.simpleQueue;
    }

    
    public String createQueue(String queueName){
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
        String queueUrl = this.sqs.createQueue(createQueueRequest).getQueueUrl();
        return queueUrl;
    }

   
    public String getQueueUrl(String queueName){
        GetQueueUrlRequest getQueueUrlRequest = new GetQueueUrlRequest(queueName);
        return this.sqs.getQueueUrl(getQueueUrlRequest).getQueueUrl();
    }

    
    public ListQueuesResult listQueues(){
       return this.sqs.listQueues();
    }

    
    public void sendMessageToQueue(String queueUrl, String message){
        SendMessageResult messageResult =  this.sqs.sendMessage(new SendMessageRequest(queueUrl, message));
       // System.out.println(messageResult.toString());
        System.out.println("in f()");
    }

    public List<Message> getMessagesFromQueue(String queueUrl){
       ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
       List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
       System.out.println("here");
       return messages;
    }

   
    public void deleteMessageFromQueue(String queueUrl, Message message){
        String messageRecieptHandle = message.getReceiptHandle();
        System.out.println("message deleted : " + message.getBody() + "." + message.getReceiptHandle());
        sqs.deleteMessage(new DeleteMessageRequest(queueUrl, messageRecieptHandle));
    }

}
