package dhi.common.httpEntity;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * When Rest is called, the entity information to send success, failure, message, etc.
 */
public class RestResponse extends RestBaseResponse {

	private String result;

	public RestResponse() {
		super(RestBaseResponse.SuccessCode);
	}
	
	public RestResponse(String result)
	{
		this();		
		this.result = result;
	}
	
	public RestResponse(Exception e) {
		super(e);
	}
	
	public RestResponse(int code) {
		super(code);
	}
	
	public RestResponse(String result, int code) {
		super(code);
		this.result = result;
	}
	
	public RestResponse(String result, int code, String message ) {
		super(code, message);
		this.result = result;
	}
	
	public String getResult() {
		return this.result;
	}	
}
