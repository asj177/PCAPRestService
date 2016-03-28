package demo;



import java.io.FileNotFoundException;

import demo.OutParams;
import demo.PCAPMiningStats;
import util.ERROR_PCAP_MAPPING;
import util.URIConstansts;

/**
 * This Class is contains all PCAP JNI Calls .
 * @author arpit
 *
 */
public class JNIWrapper {

	static {

		try {
			System.out.println("Loading Library "
					+ URIConstansts.PCAP_LIBRARY_NAME);
			System.load(URIConstansts.PCAP_SO_FILE_LOCATION);

		} catch (UnsatisfiedLinkError error) {
			System.err.println("Cannot load Library"
					+ URIConstansts.PCAP_SO_FILE_LOCATION
					+ ERROR_PCAP_MAPPING.LIBRARY_NOT_LOADED);
			error.printStackTrace();
			

		} catch (Exception error) {
			error.printStackTrace();
			
		}

		System.out.println("Library loaded successfully");

	}

	//private native static void pax_mining_lib_init();
		//Get Error String
		public native String pax_get_error_string(int error_code);
		
		//Get Path in output param and status
		//public native int  packet_mining_start(String packet_mining_fileName,OutParams filePath);
		public native int  pax_packet_mining_start(String packet_mining_fileName);
		
		
		//Get status
		public native int pax_packet_mining_cancel(String cookies);
		
		
		//Get the fileName and status
		//public native int packet_mining_get_pcap_file_name(String cookies,String fileName);
		
		
		//Get the Percentage status cookies=jstring cookies and perce_complete=jobject jobj
		public static native int pax_packet_mining_get_status(String cookies,OutParams percentage_complete);
		
		
		//Get Mining Stats
		public static native int pax_packet_mining_get_query_stats(String cookies,PCAPMiningStats miningstat);

}
