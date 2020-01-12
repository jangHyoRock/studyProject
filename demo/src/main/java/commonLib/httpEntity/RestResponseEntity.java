package commonLib.httpEntity;

/*
 * When Rest is called, the entity information to send success, failure, message, etc.
 */
public class RestResponseEntity<T> extends RestBaseResponse {

	private T result;
	
	public RestResponseEntity(Exception e) {
		super(e);
	}
	
	public RestResponseEntity(T result)
	{
		super(RestBaseResponse.SuccessCode);
		this.result = result;
	}
	
	public RestResponseEntity(T result, int code) {
		super(code);
		this.result = result;
	}
	
	public RestResponseEntity(int code, String message) {
		super(code, message);
	}
	
	public RestResponseEntity(T result, int code, String message ) {
		super(code, message);
		this.result = result;
	}
	
	public T getResult() {
		return this.result;
	}
}
