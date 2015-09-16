package demo;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	@RequestMapping(value = URIConstansts.PCAP, produces = { "application/json" }, method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<JSONObject> getPcap(HttpServletRequest request,@RequestBody PcapParameters pcap_params ){
		
		ResponseEntity<JSONObject> respEntity = null;
		
	    
		JSONObject result=new JSONObject();
		
		respEntity = new ResponseEntity<JSONObject>(result, HttpStatus.OK);
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
		
	    
		JSONObject result=new JSONObject();
		
		respEntity = new ResponseEntity<JSONObject>(result, HttpStatus.OK);
		return respEntity;

	}
	
	
	/**
	 * Method to get the file
	 * @param fileName
	 * @param request
	 * @return
	 */
	@RequestMapping(value = URIConstansts.GET_FILE, produces = { "application/json" }, method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<File> getFile(@PathVariable("fileName") String fileName,HttpServletRequest request){
		
		ResponseEntity<File> respEntity = null;
		
	    
		File result=null;
		
		respEntity = new ResponseEntity<File>(result, HttpStatus.OK);
		return respEntity;

	}
	
	
	/**
	 * Method to delete the operation
	 * @param request
	 * @return
	 */
	
	@RequestMapping(value = URIConstansts.PCAP, produces = { "application/json" }, method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<JSONObject> deleteOperation(HttpServletRequest request){
		
		ResponseEntity<JSONObject> respEntity = null;
		
	    
		JSONObject result=null;
		
		respEntity = new ResponseEntity<JSONObject>(result, HttpStatus.OK);
		return respEntity;

	}
	
	
	

	
	
}
