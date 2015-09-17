package demo;

import javax.servlet.http.Cookie;

public class JNIWrapper {

	static{
		

		System.load("/home/arpit/Documents/JNI/latest/libpax_api.so");
		
		//System.loadLibrary("JNIDemo");
	}
	
	public native String pax_get_error_string(int error_code);
	public native int packet_mining_start(pax_store_mining_query_param_s packet_mining_params);
	public native int packet_mining_cancel(String cookies);
	public native int packet_mining_get_pcap_file_name(String cookies,String fileName);
	
	
//	public static void main(String args[]){
//		
//		try{
//	
//		JNIWrapper jni=new JNIWrapper();
//		int result=jni.multiply(7, 8);
//		System.out.println("Result is "+result);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//	}
}
