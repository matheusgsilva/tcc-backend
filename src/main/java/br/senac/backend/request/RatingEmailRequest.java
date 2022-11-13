package br.senac.backend.request;

public class RatingEmailRequest {

	private String email;
	private String companyGuid;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompanyGuid() {
		return companyGuid;
	}

	public void setCompanyGuid(String companyGuid) {
		this.companyGuid = companyGuid;
	}

}
