package demo;

import javax.servlet.http.Cookie;

public class PcapParameters {

	String time_range;
	String flow_id;
	String ip_a;
	String ip_b;
	String port_a;
	String port_b;
	Cookie[]cookies;
	public Cookie[] getCookies() {
		return cookies;
	}
	public void setCookies(Cookie[] cookies) {
		this.cookies = cookies;
	}
	public String getTime_range() {
		return time_range;
	}
	public void setTime_range(String time_range) {
		this.time_range = time_range;
	}
	public String getFlow_id() {
		return flow_id;
	}
	public void setFlow_id(String flow_id) {
		this.flow_id = flow_id;
	}
	public String getIp_a() {
		return ip_a;
	}
	public void setIp_a(String ip_a) {
		this.ip_a = ip_a;
	}
	public String getIp_b() {
		return ip_b;
	}
	public void setIp_b(String ip_b) {
		this.ip_b = ip_b;
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
	public String getRegex() {
		return regex;
	}
	public void setRegex(String regex) {
		this.regex = regex;
	}
	String regex;
}
