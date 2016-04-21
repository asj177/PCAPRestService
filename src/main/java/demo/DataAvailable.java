package demo;

public class DataAvailable {
String   timestamp;

long   bytesInSeconds;



/*
 	time_t time;		// Wall clock time... note only the secs part is relevant in this..
	uint64_t bytes_in_this_second;	
	/// fields below are for internal use ONLY
	uint32_t index_in_stat_array;
 * */



public String getTimestamp() {
	return timestamp;
}
public void setTimeStamp(String timestamp) {
	System.out.println("Timestamp is "+timestamp);
	this.timestamp = timestamp;
}
public long getBytesInSeconds() {
	return bytesInSeconds;
}
public void setBytesInSeconds(long bytesInSeconds) {
	this.bytesInSeconds = bytesInSeconds;
}

}



