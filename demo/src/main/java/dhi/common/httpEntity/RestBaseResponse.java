package dhi.common.httpEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dhi.common.exception.InvalidParameterException;
import dhi.common.util.Utilities;

/*
 * Parent entity information to be transmitted when Rest is called.
 */
public class RestBaseResponse {

	static final Logger logger = LoggerFactory.getLogger(RestBaseResponse.class);
	
	public final static int SuccessCode = 200;
	public final static int FailDefaultCode = 999;
	public final static int FailInvalidParameter = 900;
	public final static int FailLoginErrorCode = 800;

	public enum ResponseStatus {
		success, fail
	}

	private ResponseStatus status;
	private int code;
	private String message;

	public RestBaseResponse(Exception e) {
		logger.error(Utilities.getStackTrace(e));
		
		if (e instanceof InvalidParameterException) {
			this.code = FailInvalidParameter;
		} else {
			this.code = FailDefaultCode;
		}
		
		this.SetStatus();
	}

	public RestBaseResponse(int code) {
		this.code = code;
		this.SetStatus();
	}

	public RestBaseResponse(int code, String message) {
		this.code = code;
		this.message = message;
		this.SetStatus();
	}

	private void SetStatus() {
		switch (code) {
		case SuccessCode:
			this.status = ResponseStatus.success;
			break;

		default:
			this.status = ResponseStatus.fail;
		}
	}

	public ResponseStatus getStatus() {
		return this.status;
	}

	public int getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}

	public void SetMessage(String message) {
		this.message = message;
	}
}
