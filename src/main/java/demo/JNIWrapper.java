package demo;

import javax.servlet.http.Cookie;

public class JNIWrapper {

	static{
		

		System.load("/home/arpit/Documents/JNI/newJNI/libJNIDemo.so");
		
		//System.loadLibrary("JNIDemo");
	}
	
	//Get Error String
	public native String pax_get_error_string(int error_code);
	
	//Get Path in output param and status
	public native int  packet_mining_start(pax_store_mining_query_param_s packet_mining_params,OutParams filePath);
	
	
	//Get status
	public native int packet_mining_cancel(String cookies);
	
	
	//Get the fileName and status
	public native int packet_mining_get_pcap_file_name(String cookies,String fileName);
	
	
	//Get the Percentage status
	public static native int pax_packet_mining_get_status(String cookies,OutParams percentage_complete);
	
	
	//Get Mining Stats
	public static native int pax_packet_mining_get_query_stats(String cookies,PCAPMiningStats miningstat);
	
	/*public static void main(String args[]){
	
		JNIWrapper jni=new JNIWrapper();
		int per=70;
		OutParams out=new OutParams();
		int result=jni.packet_mining_start(new pax_store_mining_query_param_s(),out);
		System.out.println(out.getPath()+" reslt os "+result);
		
////		try{
////	
////		JNIWrapper jni=new JNIWrapper();
////		int result=jni.multiply(7, 8);
////		System.out.println("Result is "+result);
////		}catch(Exception e){
////			e.printStackTrace();
////		}
////		
	}*/
}
