package br.com.jfestrela.endpoint.response;

/**
 * 
 * @author Fernando Estrela
 * @since 4 Mac 2020
 */
public class ApiResponse {
	
	private String message;
	private int code;

	public static ApiResponse of(int code,String message) {
		return new ApiResponse(code, message);
	}
	
	private ApiResponse(int code,String message) {
		super();
		this.message = message;
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
}
