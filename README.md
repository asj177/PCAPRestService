# PCAPRestService
REST service to provide users information regarding the packet capture process .It talks with a C library via JNI ,and provides a JSON response to the client .
Its also use to get packet data from the elasticsearch .Please see the section on Functions provided 

#Build process,requirements
1)Below are the gradle specifications (Please check build.gradle file )
-Gradle version:2.3
-Java Source Compatibility :1.7
-Elasticsearch support:2.2 

2)git clone https://github.com/asj177/PCAPRestService.git
3)Go to PCAPRestService 
4)gradle clean build 
5)After build is successful ,got to build/libs 
6)run the command java -jar demo-0.0.1-SNAPSHOT.jar

Runs at 8080

#Configurations 
1)Do not forget to add your .SO file path at URIConstansts.java (Name of the Variable :PCAP_SO_FILE_LOCATION)
2)Do not forget to add your INI file path at URIConstants.java (Name of the Variable:PCAP_INI_FILE_LOCATION)
3)Do not forget to add your  file storage  path at URIConstants.java (Name of the Variable:PCAP_FILE_LOCATION)

#Functions 
  1)Start the PCAP Mining Process API 
   EndPoint:/pcap
   Method:POST
   Accept:application/json
   Content-type:application/json 
   Body:PCAPParams in JSON format eg:{"ip_a":"10.X.X.X"}
   Headers Should also contain the Key 
   
  2)Get PCAP Status API 
   EndPoint:/pcap/status
   Metod:GET
   Accept:Applications/JSON
   Content-Type:Application/JSON
   Headers should also contain the Key 
  
  3)Get PCAP Mining Stats 
   EndPoint:/pcap/miningStat
   Method:POST
   Accept:Applications/JSON
   Content-Type:Application/JSON
   Body:PCAP Params as in Start PCAP Mining 
   Headers should also contain the Key 
   
   4)Cancel PCAP Operations:Work in Progress
   
   5)Get PCAP File 
   EndPoint:/pcap/file
   Method:GET
   Accept:APPLICATION_OCTET_STREAM_VALUE
   Request Params:Should contain the file name which is the key 
   
   
   6)Get ES Data
   EndPoint:/esdata/{to}/{from}
   Method:GET
   Accept:Application/Json
   Content-Type:applications/json
   
   
-
