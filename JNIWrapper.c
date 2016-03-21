#include<stdio.h>
#include <jni.h>
#include "demo_JNIWrapper.h"
#include "pax_store_mining.h"


/*
 *This is the sample JNI file which is called from the REST service ,we create a .so file and then load the .SO file into the REST
 *Check out JNIWrapper.java for Loading
 */

/*
 * Class:     demo_JNIWrapper
 * Method:    pax_mining_lib_init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_demo_JNIWrapper_pax_1mining_1lib_1init
  (JNIEnv *env, jclass jcl){

}

JNIEXPORT jstring JNICALL Java_demo_JNIWrapper_pax_1get_1error_1string
  (JNIEnv *env, jobject jobj, jint error_code){



char *p = "";
jstring result;
if(error_code==1){

p="HTTP_ERROR";
}

if(error_code==2){


p="FILE_NOT_FOUND";

}

if(error_code>2 && error_code<10){

p="OPERATION_ERROR";

}

if(error_code>10){
p="FAILURE";

}

const char*out=p;
//result = (*env)->NewStringUTF(p);
return (*env)->NewStringUTF(env, out); ;

}

JNIEXPORT jint JNICALL Java_demo_JNIWrapper_packet_1mining_1start
  (JNIEnv *env, jobject obj1, jobject obj2, jobject obj3){

int status=0;

jstring path=(*env)->NewStringUTF(env,"/home/arpit/Documents/PCAP/dummyPath");
jclass dataClass=(*env)->GetObjectClass(env, obj3);
jmethodID meth=(*env)->GetMethodID(env,dataClass,"setPath","(Ljava/lang/String;)V");
(*env)->CallVoidMethod(env,obj3,meth,path);

return status;
}


JNIEXPORT jint JNICALL Java_demo_JNIWrapper_packet_1mining_1cancel
  (JNIEnv * env, jobject jobj, jstring cookies){

return 0;

}


JNIEXPORT jint JNICALL Java_demo_JNIWrapper_packet_1mining_1get_1pcap_1file_1name
  (JNIEnv * env, jobject jobj, jstring cookies, jstring fileName){

return 0;

}


// HACK

jint counter = 0;

JNIEXPORT jint JNICALL Java_demo_JNIWrapper_pax_1packet_1mining_1get_1status
  (JNIEnv *env, jclass class1, jstring cookies, jobject obj){

jclass dataClass=(*env)->GetObjectClass(env, obj);
jmethodID meth=(*env)->GetMethodID(env,dataClass,"setValue","(I)V");
(*env)->CallVoidMethod(env,obj,meth,counter);
counter += 10;
usleep(500000);
return 0;

}


JNIEXPORT jint JNICALL Java_demo_JNIWrapper_pax_1packet_1mining_1get_1query_1stats
  (JNIEnv *env, jclass class1, jstring cookies, jobject mining_stat){


jint pkts_matched=100;
jint pkts_searched=1990;
jint pages_searched=2000;
jint directory_entries_searched=200;

jclass dataClass=(*env)->GetObjectClass(env, mining_stat);
jmethodID meth=(*env)->GetMethodID(env,dataClass,"setPkts_matched","(I)V");
(*env)->CallVoidMethod(env,mining_stat,meth,pkts_matched);

meth=(*env)->GetMethodID(env,dataClass,"setPkts_searched","(I)V");
(*env)->CallVoidMethod(env,mining_stat,meth,pkts_searched);

meth=(*env)->GetMethodID(env,dataClass,"setPages_searched","(I)V");
(*env)->CallVoidMethod(env,mining_stat,meth,pages_searched);

meth=(*env)->GetMethodID(env,dataClass,"setDirectory_entries_searched","(I)V");
(*env)->CallVoidMethod(env,mining_stat,meth,directory_entries_searched);

return 0;

}
