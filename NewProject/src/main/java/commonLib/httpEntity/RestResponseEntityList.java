package commonLib.httpEntity;

import java.util.List;

/*
 * When Rest is called, the entity information to send success, failure, message, etc.
 */
public class RestResponseEntityList<T> extends RestBaseResponse {

	private List<T> result;
	
	public RestResponseEntityList(Exception e) {
		super(e);
	}
	
	public RestResponseEntityList(List<T> result)
	{
		super(RestBaseResponse.SuccessCode);
		this.result = result;
	}
	
	public RestResponseEntityList(List<T> result, int code) {
		super(code);
		this.result = result;
	}
	
	public RestResponseEntityList(int code, String message) {
		super(code, message);
	}
	
	public RestResponseEntityList(List<T> result, int code, String message ) {
		super(code, message);
		this.result = result;
	}
	
	public List<T> getResult() {
		return this.result;
	}
}
