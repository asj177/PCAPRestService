package demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHit;
/*import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHit;*/
import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import util.ERROR_PCAP_MAPPING;
import util.URIConstansts;
import exceptions.PCAPException;

/**
 * This is rest controller with below API's
 * 1.Provide ES endpoints
 * 2.Start PCAP Mining
 * 3.Get PCAP Status
 * 4.Get PCAP file
 * 5.Get Mining Stats
 * 
 * @author arpit
 *
 */
@Controller
public class RESTController {

	/**
	 * Method to get the ES Status
	 * @param request
	 * @return Response Entity
	 */
	@RequestMapping(value = URIConstansts.GET_ES_ENDPOINT, produces = { "application/json" }, method = RequestMethod.GET)
	public @ResponseBody ResponseEntity getESData(HttpServletRequest request) {
		ResponseEntity respEntity = null;
		try{
		
		HashMap<String, String> es_data = getESData();
		respEntity = new ResponseEntity(es_data, HttpStatus.OK);
		
		}catch(Exception error){
			System.out.println("Error Occured at Method getESData(HttpServletRequest request)");
			error.printStackTrace();
			respEntity=new ResponseEntity(ERROR_PCAP_MAPPING.ERROR_IN_GETTING_ES_DATA,HttpStatus.INTERNAL_SERVER_ERROR);
			return respEntity;
		}
		return respEntity;
	}

	private HashMap<String, String> getESData() throws Exception {
		Properties properties = new Properties();
		HashMap<String, String> es = new HashMap<String, String>();
		String propertyFileName = "application.properties";
		try {
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(propertyFileName);
			if (inputStream != null) {

				properties.load(inputStream);
				es.put("es_ip", properties.getProperty("es_ip"));
				es.put("port", properties.getProperty("port"));

			}

		} catch (Exception error) {
			throw error;
		}
		return es;
	}

	/**
	 * Method to get the PCAP Status
	 * 
	 * @param request request from the client 
	 * @param pcap_params Input Parameters
	 * @return returns the result code 
	 */

	@CrossOrigin(origins = "*")
	@RequestMapping(value = URIConstansts.PCAP, produces = { "application/json" }, method = RequestMethod.POST)
	public @ResponseBody ResponseEntity getPcap(HttpServletRequest request,
			@RequestBody PcapParameters pcap_params) {
		System.out.println("PCAP Start called");
		ResponseEntity respEntity = null;
		
		try{
		JNIWrapper pax_pkt_mining = new JNIWrapper();// JNI Wrapper Class
														// Initialization
		HashMap result_output = new HashMap();// to store the output as Response
												// Entity can accept only one
												// output
		OutParams output_parm = new OutParams();// Out put params to get the
												// path of the file

		String key = request.getHeader("Key");
		
		if(!checkString(key)){
			throw new PCAPException(ERROR_PCAP_MAPPING.KEY_NOT_PRESENT);
		}
		
	    pcap_params.setKey(key);
	    
		String fileName = createPCAPMiningQueryINI(pcap_params);

		int result_code = pax_pkt_mining.pax_packet_mining_start(fileName);

		System.out.println("result code returned is " + result_code);
		if (result_code > 0) {
			String result_status = pax_pkt_mining
					.pax_get_error_string(result_code);

			result_output.put("error_string", result_status);
			result_output.put("cookie", key);
			respEntity = new ResponseEntity(result_output, HttpStatus.OK);
			return respEntity;
		}
		result_output.put("result_code", result_code);
		result_output.put("path", output_parm.getPath());
		result_output.put("cookie", key);

		respEntity = new ResponseEntity(result_output, HttpStatus.OK);
		
		}catch(UnsatisfiedLinkError error){
			System.err.println("Error in Method getPcap "
					+ URIConstansts.PCAP_SO_FILE_LOCATION
					+ ERROR_PCAP_MAPPING.LIBRARY_NOT_LOADED);
			error.printStackTrace();
			respEntity=new ResponseEntity(ERROR_PCAP_MAPPING.ERROR_IN_PCAP_MINING_STATUS, HttpStatus.INTERNAL_SERVER_ERROR);
			return respEntity;
			
		}catch(InvalidFileFormatException fileError){
			System.out.println("Error Occured in getPcap(HttpServletRequest request,PcapParameters pcap_params) due to Invalid File Format in Method createPCAPMiningQueryINI");
			fileError.printStackTrace();
			respEntity=new ResponseEntity(ERROR_PCAP_MAPPING.ERROR_IN_START_MINING, HttpStatus.INTERNAL_SERVER_ERROR);
			return respEntity;
		}catch(PCAPException pcap){
			System.out.println("Error Occured in getStatus(HttpServletRequest request)");
			pcap.printStackTrace();
			respEntity = new ResponseEntity(ERROR_PCAP_MAPPING.KEY_NOT_PRESENT, HttpStatus.UNAUTHORIZED);
			return respEntity;
		}catch(IOException IOerror){
			System.out.println("Error Occured in getPcap(HttpServletRequest request,PcapParameters pcap_params) due to IOException in Method createPCAPMiningQueryINI");
			IOerror.printStackTrace();
			respEntity=new ResponseEntity(ERROR_PCAP_MAPPING.ERROR_IN_START_MINING, HttpStatus.INTERNAL_SERVER_ERROR);
			return respEntity;
		} catch(Exception error){
			error.printStackTrace();
			respEntity=new ResponseEntity(ERROR_PCAP_MAPPING.ERROR_IN_START_MINING, HttpStatus.INTERNAL_SERVER_ERROR);
			return respEntity;
		 }
		return respEntity;

	}

	
	private String createPCAPMiningQueryINI(PcapParameters output) throws InvalidFileFormatException,IOException {
		String formattedOutput = "";
		File file = new File(URIConstansts.PCAP_INI_FILE_LOCATION);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			Ini ini = new Ini(file);

			Config config = ini.getConfig();
			config.setStrictOperator(true);
			ini.setConfig(config);
			ini.put("PACKET_MINING", "mining_output_location", "/opt/cn/store");
			ini.put("PACKET_QUERY", "query_cookie", output.getKey());

			if (checkString(output.getTime_start())) {
				ini.put("PACKET_QUERY", "time_start", output.getTime_start()
						.trim());
			}

			if (checkString(output.getTime_end())) {
				ini.put("PACKET_QUERY", "time_end", output.getTime_end().trim());
			}

			if (checkString(output.getFlow_id())) {
				ini.put("PACKET_QUERY", "flow_id", output.getFlow_id().trim());

			}

			if (checkString(output.getService_id())) {
				ini.put("PACKET_QUERY", "service_id", output.getService_id()
						.trim());
			}

			if (checkString(output.getIp_a())) {
				ini.put("PACKET_QUERY", "ip_a", output.getIp_a().trim());
			}

			if (checkString(output.getIp_b())) {
				ini.put("PACKET_QUERY", "ip_b", output.getIp_b().trim());
			}

			if (checkString(output.getPort_a())) {
				ini.put("PACKET_QUERY", "port_a", output.getPort_a().trim());
			}
			if (checkString(output.getPort_b())) {
				ini.put("PACKET_QUERY", "port_b", output.getPort_b().trim());
			}

			if (checkString(output.getRegex())) {
				ini.put("PACKET_QUERY", "port_b", output.getRegex().trim());
			}

			ini.store();

		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw e;
		}catch(Exception e){
			throw e;
		}

		return file.getAbsolutePath();
	}

	private boolean checkString(String value) {
		if (value == null) {
			return false;
		}
		if (value.isEmpty()) {
			return false;
		}
		return true;
	}

	

	/**
	 * Method to get the status
	 * 
	 * @param request:Http request from the client 
	 * @return Response Entity which contains percentage completed 
	 */

	@CrossOrigin(origins = "*")
	@RequestMapping(value = URIConstansts.GET_STATUS, produces = { "application/json" }, method = RequestMethod.GET)
	public @ResponseBody ResponseEntity getStatus(HttpServletRequest request) {

		ResponseEntity respEntity = null;

		try{
		
		String key = request.getHeader("Key");
		if(!checkString(key)){
			throw new PCAPException(ERROR_PCAP_MAPPING.KEY_NOT_PRESENT);
			
		}
		System.out.println("Key is " + key);

		JNIWrapper pax_output = new JNIWrapper();
		OutParams percentage_complete = new OutParams();
		int status = pax_output.pax_packet_mining_get_status(key,
				percentage_complete);
		HashMap result_out = new HashMap();
		result_out.put("status", status);
		result_out.put("percentage_complete", percentage_complete.getValue());
		result_out.put("cookie", key);
		respEntity = new ResponseEntity(result_out, HttpStatus.OK);

		System.out.println("Percentage is " + percentage_complete.getValue());
		System.out.println("Path is " + percentage_complete.getPath());
		}catch(UnsatisfiedLinkError error){
			System.err.println("Error in GET PCAP Status "
					+ URIConstansts.PCAP_SO_FILE_LOCATION
					+ ERROR_PCAP_MAPPING.LIBRARY_NOT_LOADED);
			error.printStackTrace();
			respEntity=new ResponseEntity(ERROR_PCAP_MAPPING.ERROR_IN_PCAP_MINING_STATUS, HttpStatus.INTERNAL_SERVER_ERROR);
			return respEntity;
			
		}catch(PCAPException pcap){
			System.out.println("Error Occured in getStatus(HttpServletRequest request)");
			pcap.printStackTrace();
			respEntity = new ResponseEntity(ERROR_PCAP_MAPPING.KEY_NOT_PRESENT, HttpStatus.UNAUTHORIZED);
			return respEntity;
		}catch(Exception error){
			System.out.println("Error Occured in getStatus(HttpServletRequest request)");
			error.printStackTrace();
			respEntity=new ResponseEntity(ERROR_PCAP_MAPPING.ERROR_IN_PCAP_MINING_STATUS, HttpStatus.INTERNAL_SERVER_ERROR);
			return respEntity;
		}
		return respEntity;

	}

	/*@CrossOrigin(origins = "*")
	@RequestMapping(value = URIConstansts.CHECK_STATUS, produces = { "application/json" }, method = RequestMethod.GET)
	public @ResponseBody ResponseEntity checkFile(HttpServletRequest request,
			@PathVariable("flowId") String flowId) {

		ResponseEntity respEntity = null;
		boolean check = false;
		respEntity = new ResponseEntity(check, HttpStatus.OK);

		return respEntity;

	}*/

	/**
	 * Method to get the file
	 * 
	 * @param fileName Name of the File which is actually the Flow Id 
	 * @param request Request from the client 
	 * @return returns the file 
	 * @throws IOException
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = URIConstansts.GET_FILE, produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE }, method = RequestMethod.GET)
	public void getFile(
			@RequestParam(value = "fileName", required = false) String fileName,
			HttpServletRequest request, HttpServletResponse response)
			{
        try{
		fileName = "File.pcap";
		byte[] reportBytes = null;
		File result = new File(URIConstansts.PCAP_FILE_LOCATION + fileName);
		System.out.println("File received");
		if (result.exists()) {
			InputStream inputStream = new FileInputStream(
					URIConstansts.PCAP_FILE_LOCATION + fileName);
			String type = result.toURL().openConnection()
					.guessContentTypeFromName(fileName);
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
			response.setHeader("Content-Type", type);

			reportBytes = new byte[131072];// New change
			OutputStream os = response.getOutputStream();// New change
			int read = 0;
			while ((read = inputStream.read(reportBytes)) != -1) {
				os.write(reportBytes, 0, read);
			}
			System.out.println("Bytes sent" + reportBytes);
			os.flush();
			os.close();

			System.out.println("FIle sent ");

		}
        }catch (IOException ioError){
        	System.out.println("IO Exception Error Occured in getFile method ");
        	ioError.printStackTrace();
			
        }catch(Exception error){
        	error.printStackTrace();
        }

	}

	/**
	 * Method to delete the operation
	 * 
	 * @param request Request from the Client
	 * @return Result code of the cancel operation
	 */

	@RequestMapping(value = URIConstansts.PCAP, produces = { "application/json" }, method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity cancelOperation(
			HttpServletRequest request) {

		ResponseEntity respEntity = null;
		try{
		String key = request.getHeader("Key");
	

		if(!checkString(key)){
			throw new PCAPException(ERROR_PCAP_MAPPING.KEY_NOT_PRESENT);
		}

		JSONObject result = null;

		JNIWrapper pax_output = new JNIWrapper();
		HashMap result_output = new HashMap();

		int result_code = pax_output.pax_packet_mining_cancel(key);

		if (result_code > 0) {
			String result_status = pax_output.pax_get_error_string(result_code);
			result_output.put("error_string", result_status);
			result_output.put("cookie", key);
			respEntity = new ResponseEntity(result_output, HttpStatus.OK);
			return respEntity;
		}
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Access-Control-Allow-Origin", "*");
		result_output.put("result_code", result_code);
		result_output.put("cookie", key);

		respEntity = new ResponseEntity(result_output, responseHeaders,
				HttpStatus.OK);
		
		}catch(UnsatisfiedLinkError error){
			System.err.println("Error Occured in Method  cancelOperation"
					+ URIConstansts.PCAP_SO_FILE_LOCATION
					+ ERROR_PCAP_MAPPING.LIBRARY_NOT_LOADED);
			error.printStackTrace();
			respEntity=new ResponseEntity(ERROR_PCAP_MAPPING.ERROR_IN_PCAP_MINING_STATUS, HttpStatus.INTERNAL_SERVER_ERROR);
			return respEntity;
			
		}catch(PCAPException pcap){
			System.out.println("Error Occured in Method  cancelOperation");
			pcap.printStackTrace();
			respEntity = new ResponseEntity(ERROR_PCAP_MAPPING.KEY_NOT_PRESENT, HttpStatus.UNAUTHORIZED);
			return respEntity;
		}catch(Exception error){
			System.out.println("Error Occured in Method  cancelOperation");
			error.printStackTrace();
			respEntity=new ResponseEntity(ERROR_PCAP_MAPPING.ERROR_IN_PCAP_MINING_STATUS, HttpStatus.INTERNAL_SERVER_ERROR);
			return respEntity;
		}
		return respEntity;

	}
	
	
	/**
	 * Method to get the stats
	 * 
	 * @param request:Http request from the client 
	 * @param pcap_params:PCAP Parameters passed by the client 
	 * @return Response Entity which contains mining statistics 
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = URIConstansts.GET_MINING_PCAP, produces = { "application/json" }, method = RequestMethod.POST)
	public @ResponseBody ResponseEntity getMiningStat(
			HttpServletRequest request, @RequestBody PcapParameters pcap_params) {
		ResponseEntity respEntity = null;
        try{
		String key = request.getHeader("Key");
		if(!checkString(key)){
			throw new PCAPException(ERROR_PCAP_MAPPING.KEY_NOT_PRESENT);
		}
		JNIWrapper pax_output = new JNIWrapper();
		HashMap result_output = new HashMap();
		PCAPMiningStats pcapMining = new PCAPMiningStats();
		int resultCode = pax_output.pax_packet_mining_get_query_stats(key,
				pcapMining);
		result_output.put("result_code", resultCode);
		result_output.put("cookies", key);
		result_output.put("mining_stats", pcapMining);
		respEntity = new ResponseEntity(result_output, HttpStatus.OK);
        }catch(UnsatisfiedLinkError error){
			System.err.println("Error Occured in Method  getMiningStat "
					+ URIConstansts.PCAP_SO_FILE_LOCATION
					+ ERROR_PCAP_MAPPING.LIBRARY_NOT_LOADED);
			error.printStackTrace();
			respEntity=new ResponseEntity(ERROR_PCAP_MAPPING.ERROR_IN_PCAP_MINING_STATUS, HttpStatus.INTERNAL_SERVER_ERROR);
			return respEntity;
			
		}catch(PCAPException pcap){
			System.out.println("Error Occured in Method getMiningStat");
			pcap.printStackTrace();
			respEntity = new ResponseEntity(ERROR_PCAP_MAPPING.KEY_NOT_PRESENT, HttpStatus.UNAUTHORIZED);
			return respEntity;
		}catch(Exception error){
			System.out.println("Error Occured in Method getMiningStat");
			error.printStackTrace();
			respEntity=new ResponseEntity(ERROR_PCAP_MAPPING.ERROR_IN_PCAP_MINING_STATUS, HttpStatus.INTERNAL_SERVER_ERROR);
			return respEntity;
		}
		return respEntity;

	}

	/**
	 * Method to get the ES Data 
	 * 
	 * @param request:Http request from the client 
	 * @param to:Record from where ES data is requested
	 * @param from:Record till where ES data is requested
	 * @return Response Entity which contains ES data 
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = URIConstansts.GET_ES_DATA, produces = { "application/json" }, method = RequestMethod.GET, headers = "Accept=*/*")
	public @ResponseBody ResponseEntity getESData(HttpServletRequest request,
			@PathVariable("to") int to, @PathVariable("from") int from) {
		ResponseEntity respEntity = null;
		JSONArray es_data = new JSONArray();
		Client client;
		try {
			client = TransportClient.builder().build()
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(URIConstansts.ES_END_POINT), 9300));
		
		SearchResponse response = client
				.prepareSearch(URIConstansts.ES_INDEX)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setFrom(from)
				.setSize(to).setExplain(true).execute().actionGet();
		if (response.getHits().getHits().length > 0) {
			for (SearchHit searchData : response.getHits().getHits()) {
				JSONObject value = new JSONObject(searchData.getSource());
				es_data.put(value);
			}

		}
		
		respEntity = new ResponseEntity(es_data.toString(), HttpStatus.OK);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("Error Occured in Method getESData");
			e.printStackTrace();
			respEntity = new ResponseEntity(ERROR_PCAP_MAPPING.ERROR_WHILE_CONNECTING_ES, HttpStatus.INTERNAL_SERVER_ERROR);
			return respEntity;
		}catch(Exception error){
			System.out.println("Error Occured in Method getESData");
			error.printStackTrace();
			respEntity = new ResponseEntity(ERROR_PCAP_MAPPING.ERROR_WHILE_CONNECTING_ES, HttpStatus.INTERNAL_SERVER_ERROR);
			return respEntity;
		}
		return respEntity;
	}

}
