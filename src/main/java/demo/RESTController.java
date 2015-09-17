package demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import util.URIConstansts;

@Controller
public class RESTController {

	/**
	 * Method to get the PCAP Status
	 * @param request
	 * @param pcap_params
	 * @return
	 */
	@RequestMapping(value = URIConstansts.PCAP, produces = { "application/json" }, method = RequestMethod.POST)
	public  ResponseEntity getPcap(HttpServletRequest request,@RequestBody PcapParameters pcap_params ){
		
		ResponseEntity respEntity = null;
		System.out.println(request.getHeaderNames());
		Enumeration<String> out=request.getHeaderNames();
		Cookie cookies[]=request.getCookies();
	    
		if(cookies==null || cookies.length==0){
			
			respEntity = new ResponseEntity( HttpStatus.BAD_REQUEST);
			return respEntity;
			
		}
		pcap_params.setCookies(cookies);
		pax_store_mining_query_param_s jniDTO=new pax_store_mining_query_param_s();
		mapRequestToJniClass(pcap_params,jniDTO);
		
		JNIWrapper pax_output=new JNIWrapper();
		HashMap result_output=new HashMap();
		
		int result_code=pax_output.packet_mining_start(jniDTO);
		
		if(result_code>0){
			String result_status=pax_output.pax_get_error_string(result_code);
			result_output.put("error_string", result_status);
			result_output.put("cookie", cookies);
			respEntity = new ResponseEntity (result_output, HttpStatus.OK);
			return respEntity;
		}
		result_output.put("result_code", result_code);
		result_output.put("cookie", cookies);
		
		respEntity = new ResponseEntity (result_output, HttpStatus.OK);
		
		
		
		
		return respEntity;

	}
	
	/**
	 * Method to get the status
	 * @param request
	 * @return
	 */
	
	@RequestMapping(value = URIConstansts.GET_STATUS, produces = { "application/json" }, method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<JSONObject> getStatus(HttpServletRequest request){
		
		ResponseEntity<JSONObject> respEntity = null;
		
	    Cookie[]cookies=request.getCookies();
          
	    if(cookies==null || cookies.length==0){
			
			respEntity = new ResponseEntity( HttpStatus.BAD_REQUEST);
			return respEntity;
			
		}		
	    JNIWrapper pax_output=new JNIWrapper();
		HashMap result_output=new HashMap(); 	
		
		
		//respEntity = new ResponseEntity<JSONObject>(null, HttpStatus.OK);
		return null;

	}
	
	
	/**
	 * Method to get the file
	 * @param fileName
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = URIConstansts.GET_FILE, produces = { "application/json" }, method = RequestMethod.GET)
	public @ResponseBody ResponseEntity getFile(@RequestParam(value="fileName", required=false) String fileName,HttpServletRequest request) throws IOException{
		
		ResponseEntity respEntity = null;
		
		byte[] reportBytes = null;
		File result=new File("/home/arpit/Documents/PCAP/dummyPath/"+fileName);
		
		if(result.exists()){
			InputStream inputStream = new FileInputStream("/home/arpit/Documents/PCAP/dummyPath/"+fileName); 
			String type=result.toURL().openConnection().guessContentTypeFromName(fileName);
			//InputStream targetStream =ServletContext.class.getResourceAsStream("/home/arpit/Documents/PCAP/dummyPath/"+fileName);
			byte[]out=org.apache.commons.io.IOUtils.toByteArray(inputStream);
			//InputStreamResource inputStreamResource = new InputStreamResource(targetStream);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("content-disposition", "attachment; filename=" + fileName);
			responseHeaders.add("Content-Type",type);
			//responseHeaders.set
			//responseHeaders.setContentLength(inputStream.);
			respEntity = new ResponseEntity(out, responseHeaders,HttpStatus.OK);
			
			
		}else{
			
			respEntity = new ResponseEntity ("File Not Found", HttpStatus.OK);
		}
		
		
		return respEntity;

	}
	
	
	/**
	 * Method to delete the operation
	 * @param request
	 * @return
	 */
	
	@RequestMapping(value = URIConstansts.PCAP, produces = { "application/json" }, method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity deleteOperation(HttpServletRequest request){
		
		ResponseEntity respEntity = null;
		
	    Cookie[]cookies=request.getCookies();
	    
	    
//      if(cookies==null || cookies.length==0){
//			
//			respEntity = new ResponseEntity( HttpStatus.BAD_REQUEST);
//			return respEntity;
//			
//		}		
	    
		JSONObject result=null;
		
		JNIWrapper pax_output=new JNIWrapper();
		HashMap result_output=new HashMap();
		
		int result_code=pax_output.packet_mining_cancel("abc");
		
		if(result_code>0){
			String result_status=pax_output.pax_get_error_string(result_code);
			result_output.put("error_string", result_status);
			result_output.put("cookie", cookies);
			respEntity = new ResponseEntity (result_output, HttpStatus.OK);
			return respEntity;
		}
		result_output.put("result_code", result_code);
		result_output.put("cookie", cookies);
		
		respEntity = new ResponseEntity (result_output, HttpStatus.OK);
		return respEntity;

	}
	
	private void mapRequestToJniClass(PcapParameters input,pax_store_mining_query_param_s output){
		
		String times[]=input.getTime_range().split("-");
		
		output.setTime_start(times[0]);
		output.setTime_end(times[1]);
		output.setFlow_id(input.getFlow_id());
		output.setIp_a(input.getIp_a());
		output.setIp_b(input.getIp_b());
		output.setPort_a(input.getPort_a());
		output.setPort_b(input.getPort_b());
		output.setPayload_contains_expression(input.getRegex());
		output.setQuery_cookie(input.getCookies()[0].getValue());
		
	}
	
	

	
	
}
