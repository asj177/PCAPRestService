package demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	public @ResponseBody ResponseEntity getPcap(HttpServletRequest request,@RequestBody PcapParameters pcap_params ){
		
		ResponseEntity respEntity = null;
		
		
		Cookie cookies[]=request.getCookies();
	  
		if(cookies==null || cookies.length==0){
			
			respEntity = new ResponseEntity( HttpStatus.BAD_REQUEST);
			return respEntity;
			
		}
		pcap_params.setCookies(cookies);
		pax_store_mining_query_param_s jniDTO=new pax_store_mining_query_param_s();
		
		JNIWrapper pax_output=new JNIWrapper();
		HashMap result_output=new HashMap();
		OutParams output_parm=new OutParams();
		int result_code=pax_output.packet_mining_start(jniDTO,output_parm);
		
		if(result_code>0){
			String result_status=pax_output.pax_get_error_string(result_code);
			result_output.put("error_string", result_status);
			result_output.put("cookie", cookies);
			respEntity = new ResponseEntity (result_output, HttpStatus.OK);
			return respEntity;
		}
		result_output.put("result_code", result_code);
		result_output.put("path", output_parm.getPath());
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
	public @ResponseBody ResponseEntity getStatus(HttpServletRequest request){
		
		ResponseEntity respEntity = null;
		
	    Cookie[]cookies=request.getCookies();
          
	    if(cookies==null || cookies.length==0){
			
			respEntity = new ResponseEntity( HttpStatus.BAD_REQUEST);
			return respEntity;
			
		}		
	    JNIWrapper pax_output=new JNIWrapper();
	    OutParams percentage_complete=new OutParams();
	    int status=pax_output.pax_packet_mining_get_status(cookies[0].toString(), percentage_complete);
	    HashMap result_out=new HashMap();
	    result_out.put("status", status);
	    result_out.put("percentage_complete", percentage_complete.getValue());
	    result_out.put("cookie", cookies);
	    respEntity=new ResponseEntity(result_out,HttpStatus.OK);
	    
		
		
		
		return respEntity;

	}
	
	
	/**
	 * Method to get the file
	 * @param fileName
	 * @param request
	 * @return
	 * @throws IOException 
	 *//*
	@RequestMapping(value = URIConstansts.GET_FILE, produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE}, method = RequestMethod.GET)
	public @ResponseBody ResponseEntity getFile(@RequestParam(value="fileName", required=false) String fileName,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		ResponseEntity respEntity = null;
		
		byte[] reportBytes = null;
		File result=new File("/home/arpit/Documents/PCAP/dummyPath/"+fileName);
		
		if(result.exists()){
			InputStream inputStream = new FileInputStream("/home/arpit/Documents/PCAP/dummyPath/"+fileName); 
			String type=result.toURL().openConnection().guessContentTypeFromName(fileName);
			
			byte[]out=org.apache.commons.io.IOUtils.toByteArray(inputStream);
			
			reportBytes=new byte[(int)result.length()];//New change
			OutputStream os=response.getOutputStream();//New change
			int read=0;
			while((read=inputStream.read(reportBytes))!=-1){
				os.write(reportBytes,0,read);
			}
			os.flush();
			os.close();
			
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("content-disposition", "attachment; filename=" + fileName);
			responseHeaders.add("Content-Type",type);
			
			respEntity = new ResponseEntity(out, responseHeaders,HttpStatus.OK);
			
			
		}else{
			
			respEntity = new ResponseEntity ("File Not Found", HttpStatus.OK);
		}
		
		
		return respEntity;

	}*/
	
	/**
	 * Method to get the file
	 * @param fileName
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = URIConstansts.GET_FILE, produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE}, method = RequestMethod.GET)
	public void getFile(@RequestParam(value="fileName", required=false) String fileName,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		
		
		byte[] reportBytes = null;
		File result=new File("/home/arpit/Documents/PCAP/dummyPath/"+fileName);
		System.out.println("File received");
		if(result.exists()){
			InputStream inputStream = new FileInputStream("/home/arpit/Documents/PCAP/dummyPath/"+fileName); 
			String type=result.toURL().openConnection().guessContentTypeFromName(fileName);
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			response.setHeader("Content-Type",type);
			
			reportBytes=new byte[100];//New change
			OutputStream os=response.getOutputStream();//New change
			int read=0;
			while((read=inputStream.read(reportBytes))!=-1){
				os.write(reportBytes,0,read);
			}
			System.out.println("Bytes sent"+reportBytes);
			os.flush();
			os.close();
			
			System.out.println("FIle sent ");
			
			
			
			
		}
		
		

	}
	
	
	/**
	 * Method to delete the operation
	 * @param request
	 * @return
	 */
	
	
	@RequestMapping(value = URIConstansts.PCAP, produces = { "application/json" }, method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity cancelOperation(HttpServletRequest request){
		
		ResponseEntity respEntity = null;
		
	    Cookie[]cookies=request.getCookies();
	    
	    
      if(cookies==null || cookies.length==0){
			
			respEntity = new ResponseEntity( HttpStatus.BAD_REQUEST);
			return respEntity;
			
		}		
	    
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
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Access-Control-Allow-Origin", "*");
		result_output.put("result_code", result_code);
		result_output.put("cookie", cookies);
		
		respEntity = new ResponseEntity (result_output,responseHeaders, HttpStatus.OK);
		return respEntity;

	}
	
	/*private void mapRequestToJniClass(PcapParameters input,pax_store_mining_query_param_s output){
		
		String times[]=input.getTime_range().split("-");
		
		output.setTime_start(times[0]);
		output.setTime_end(times[1]);
		output.setFlow_id(input.getFlow_id());
		output.setIp_a(input.getIp_a());
		output.setIp_b(input.getIp_b());
		output.setPort_a(input.getPort_a());
		output.setPort_b(input.getPort_b());
		output.setPayload_contains_expression(input.getRegex());
	//	output.setQuery_cookie(input.getCookies()[0].getValue());
		
	}*/
	
	@RequestMapping(value = URIConstansts.GET_MINING_PCAP, produces = { "application/json" }, method = RequestMethod.POST)
	public @ResponseBody ResponseEntity getMiningStat(HttpServletRequest request,@RequestBody PcapParameters pcap_params ){
		ResponseEntity respEntity = null;
		Cookie cookies[]=request.getCookies();
		  
		if(cookies==null || cookies.length==0){
			
			respEntity = new ResponseEntity( HttpStatus.BAD_REQUEST);
			return respEntity;
			
		}
		JNIWrapper pax_output=new JNIWrapper();
		HashMap result_output=new HashMap();
		PCAPMiningStats pcapMining=new PCAPMiningStats();
		int resultCode=pax_output.pax_packet_mining_get_query_stats(cookies[0].toString(),pcapMining);
		result_output.put("result_code", resultCode);
		result_output.put("cookies", cookies);
		result_output.put("mining_stats", pcapMining);
        respEntity = new ResponseEntity (result_output, HttpStatus.OK);
		
		
		
		
		return respEntity;
		
	}

	
	
}
