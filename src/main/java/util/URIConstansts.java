package util;

public class URIConstansts {

	
	public static final String PCAP="/pcap";
	public static final String GET_STATUS=PCAP+"/status";
	public static final String GET_FILE=PCAP+"/file";
	public static final String GET_MINING_PCAP=PCAP+"/miningStat";
	public static final String GET_ES_DATA="/esdata/{from}/{to}";
	public static final String GET_ES_ENDPOINT="/esEndPoint";
	public static final String CHECK_STATUS="/checkFile/{flowId}";
	//public static final String PCAP_FILE_LOCATION="/opt/cn/store/";
	public static final String PCAP_FILE_LOCATION="/home/arpit/Documents/PCAP/dummyPath/";
	//public static final String PCAP_INI_FILE_LOCATION="/opt/cn/sdk/utils/REST/packetquey.ini";
	public static final String PCAP_INI_FILE_LOCATION="/home/arpit/Documents/JNI/packetquey.ini";
	public static final String PCAP_SO_FILE_LOCATION="/home/arpit/Documents/JNI/LatestJNI/lib_pcap.so";
	public static final String PCAP_LIBRARY_NAME="lib_pcap.so";
	//public static final String PCAP_SO_FILE_LOCATION="/opt/cn/sdk/utils/REST/lib_pcap.so";

	
}
