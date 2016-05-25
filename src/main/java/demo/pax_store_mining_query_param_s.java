package demo;

public class pax_store_mining_query_param_s {

	String query_id;						// A unique number, if applicable.
							// A unique number, if applicable.
	String flow_id;						
	String time_start;
	String time_end;
	String ip_a;
	String mask_a;
	String ip_b;
	String mask_b;
	String port_a;
	String port_b;
	String service_id;						// TBD FUTURE... multiple service names
	String payload_contains_expression; 		//;PCRE or exact match 	----> FUTURE multiple service names
	String expression_id;	//;Unique 2 byte ID for above expression. Ideally to be kept same across queries. Can be written back to packet header, but could possibly affect WRITE performance
	String mining_output_location;
	String query_cookie;			// TBD:  ---> How big will the cookie string really be from REST etc??
	
	public String getQuery_id() {
		return query_id;
	}

	public void setQuery_id(String query_id) {
		this.query_id = query_id;
	}

	public String getFlow_id() {
		return flow_id;
	}

	public void setFlow_id(String flow_id) {
		this.flow_id = flow_id;
	}

	public String getTime_start() {
		return time_start;
	}

	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getTime_end() {
		return time_end;
	}

	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}

	public String getIp_a() {
		return ip_a;
	}

	public void setIp_a(String ip_a) {
		this.ip_a = ip_a;
	}

	public String getMask_a() {
		return mask_a;
	}

	public void setMask_a(String mask_a) {
		this.mask_a = mask_a;
	}

	public String getIp_b() {
		return ip_b;
	}

	public void setIp_b(String ip_b) {
		this.ip_b = ip_b;
	}

	public String getMask_b() {
		return mask_b;
	}

	public void setMask_b(String mask_b) {
		this.mask_b = mask_b;
	}

	public String getPort_a() {
		return port_a;
	}

	public void setPort_a(String port_a) {
		this.port_a = port_a;
	}

	public String getPort_b() {
		return port_b;
	}

	public void setPort_b(String port_b) {
		this.port_b = port_b;
	}

	public String getService_id() {
		return service_id;
	}

	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	public String getPayload_contains_expression() {
		return payload_contains_expression;
	}

	public void setPayload_contains_expression(String payload_contains_expression) {
		this.payload_contains_expression = payload_contains_expression;
	}

	public String getExpression_id() {
		return expression_id;
	}

	public void setExpression_id(String expression_id) {
		this.expression_id = expression_id;
	}

	public String getMining_output_location() {
		return mining_output_location;
	}

	public void setMining_output_location(String mining_output_location) {
		this.mining_output_location = mining_output_location;
	}

	public String getQuery_cookie() {
		return query_cookie;
	}

	public void setQuery_cookie(String query_cookie) {
		this.query_cookie = query_cookie;
	}

	public int getPcap_out_file_fd() {
		return pcap_out_file_fd;
	}

	public void setPcap_out_file_fd(int pcap_out_file_fd) {
		this.pcap_out_file_fd = pcap_out_file_fd;
	}

	//- - - - NOTE: The variables below this line are for INTERNAL use only - - - - - - - - //
	int pcap_out_file_fd;	 						// A unique number, if applicable.
		
	
}
