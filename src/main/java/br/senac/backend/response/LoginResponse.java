package br.senac.backend.response;

import br.senac.backend.util.ELOGIN_TYPE;

public class LoginResponse {

	private String email;
	private String token;
	private String guid;
	private String userName;
	private ELOGIN_TYPE type;
	private Integer permission;

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

	public Integer getPermission() {
		return permission;
	}

	public void setPermission(Integer permission) {
		this.permission = permission;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
