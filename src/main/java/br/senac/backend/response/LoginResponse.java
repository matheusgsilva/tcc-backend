package br.senac.backend.response;

import br.senac.backend.util.ELOGIN_TYPE;

public class LoginResponse {

	private String email;
	private String token;
	private String guid;
	private ELOGIN_TYPE type;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public ELOGIN_TYPE getType() {
		return type;
	}

	public void setType(ELOGIN_TYPE type) {
		this.type = type;
	}

}
