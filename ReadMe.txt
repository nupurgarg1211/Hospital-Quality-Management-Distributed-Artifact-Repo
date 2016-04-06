/*************************************************************
*
* Hospital Quality Management Distributed Artifact Repository
*
*@author : NUPUR GARG(MT2014081)
	   
*
*
**************************************************************/


> The folder contains the programs for storing HL7 message from Client to SQS Queue ans from SQS Queue to MongoDB.

> Requirements To run the program : 
 AWS SQS account.
 MongoDB running.
 JAVA.
 
> For Running Script to receive message from client and store in SQS queue:

	> Go to folder ClientToQueue.
	> It contains all the API and dependencies for queue.
	> Store accessKey and secretKey in AwsCredentials.properties file.
	  This will be your AWS account that would be used for SQS.
	> Also set your Queue name and other credentials in file AWSSimpleQueueServiceUtil.java
	> For running the script for first time, You would need to create class file from source file.
	> Enter commmand :
	
	  javac -classpath .:aws-java-sdk-1.9.29.jar:aws-java-sdk-1.9.29-javadoc.jar:aws-java-sdk-1.9.29-sources.jar:aws-java-sdk-flow-build-			tools-1.9.29.jar:aspectjrt.jar:aspectjweaver.jar:commons-codec-1.6.jar:commons-logging-1.1.3.jar:freemarker-2.3.18.jar:httpclient-4.3.jar:httpcore-4.3.jar:jackson-annotations-2.3.0.jar:jackson-core-2.3.2.jar:jackson-databind-2.3.2.jar:javax.mail-api-1.4.6.jar:joda-time-2.2.jar:spring-beans-3.0.7.jar:spring-context-3.0.7.jar:spring-core-3.0.7.jar *.java
	
	> Server runs on port 8442.
	> To run the Program :
	
	   java -classpath .:aws-java-sdk-1.9.29.jar:aws-java-sdk-1.9.29-javadoc.jar:aws-java-sdk-1.9.29-sources.jar:aws-java-sdk-flow-build-tools-1.9.29.jar:aspectjrt.jar:aspectjweaver.jar:commons-codec-1.6.jar:commons-logging-1.1.3.jar:freemarker-2.3.18.jar:httpclient-4.3.jar:httpcore-4.3.jar:jackson-annotations-2.3.0.jar:jackson-core-2.3.2.jar:jackson-databind-2.3.2.jar:javax.mail-api-1.4.6.jar:joda-time-2.2.jar:spring-beans-3.0.7.jar:spring-context-3.0.7.jar:spring-core-3.0.7.jar Server
	   
	> Now your Server is waiting for client to send message.
	> Once it will receive the message, message will be stored in SQS.
	
	
> For Running Script to fetch message from SQS and store in MongoDB:

	> Go to folder QueueToDB.
	> It contains all the API and dependencies for queue, HAPI and MongoDB.
	> Store accessKey and secretKey in AwsCredentials.properties file.
	  This will be your AWS account that would be used for SQS.
	> Set Document and database name of MongoDB in ParseHL7Message_old.java
	  Default is HL7 database and ParsedHL7 collection
	> For running the script for first time, You would need to create class file from source file.
	> Enter commmand :
	
	  javac -classpath .:aws-java-sdk-1.9.29.jar:aws-java-sdk-1.9.29-javadoc.jar:aws-java-sdk-1.9.29-sources.jar:aws-java-sdk-flow-build-tools-1.9.29.jar:aspectjrt.jar:aspectjweaver.jar:commons-codec-1.6.jar:commons-logging-1.1.3.jar:freemarker-2.3.18.jar:httpclient-4.3.jar:httpcore-4.3.jar:jackson-annotations-2.3.0.jar:jackson-core-2.3.2.jar:jackson-databind-2.3.2.jar:javax.mail-api-1.4.6.jar:joda-time-2.2.jar:spring-beans-3.0.7.jar:spring-context-3.0.7.jar:spring-core-3.0.7.jar:hapi-base-2.2.jar:hapi-examples-2.2.jar:hapi-hl7overhttp-2.2.jar:hapi-structures-v21-2.2.jar:hapi-structures-v22-2.2.jar:hapi-structures-v23-2.2.jar:hapi-structures-v24-2.2.jar:hapi-structures-v25-2.2.jar:hapi-structures-v26-2.2.jar:hapi-structures-v231-2.2.jar:hapi-structures-v251-2.2.jar:log4j-1.2.17.jar:slf4j-api-1.6.6.jar:slf4j-log4j12-1.6.6.jar:mongo-java-driver-3.0.0-rc1.jar ParseHL7Message_old.java
	
	> To run the Program :
	
	   java -classpath .:aws-java-sdk-1.9.29.jar:aws-java-sdk-1.9.29-javadoc.jar:aws-java-sdk-1.9.29-sources.jar:aws-java-sdk-flow-build-tools-1.9.29.jar:aspectjrt.jar:aspectjweaver.jar:commons-codec-1.6.jar:commons-logging-1.1.3.jar:freemarker-2.3.18.jar:httpclient-4.3.jar:httpcore-4.3.jar:jackson-annotations-2.3.0.jar:jackson-core-2.3.2.jar:jackson-databind-2.3.2.jar:javax.mail-api-1.4.6.jar:joda-time-2.2.jar:spring-beans-3.0.7.jar:spring-context-3.0.7.jar:spring-core-3.0.7.jar:hapi-base-2.2.jar:hapi-examples-2.2.jar:hapi-hl7overhttp-2.2.jar:hapi-structures-v21-2.2.jar:hapi-structures-v22-2.2.jar:hapi-structures-v23-2.2.jar:hapi-structures-v24-2.2.jar:hapi-structures-v25-2.2.jar:hapi-structures-v26-2.2.jar:hapi-structures-v231-2.2.jar:hapi-structures-v251-2.2.jar:log4j-1.2.17.jar:slf4j-api-1.6.6.jar:slf4j-log4j12-1.6.6.jar:mongo-java-driver-3.0.0-rc1.jar ParseHL7Message_old
	   
	> HL7 message is stored in MongoDB.
