package demo;

public class JNIWrapper {

	static{
		

		//System.load("/home/arpit/Documents/JNI/libJNIDemo.so");
		
		System.loadLibrary("JNIDemo");
	}
	
	
	public native int multiply(int a,int b);
	
	
	public static void main(String args[]){
		
		try{
	
		JNIWrapper jni=new JNIWrapper();
		int result=jni.multiply(7, 8);
		System.out.println("Result is "+result);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
