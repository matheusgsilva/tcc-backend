package br.senac.backend.request;

public class RatingEmailRequest {

	private String petGuid;
	private String companyGuid;

	public String getPetGuid() {
		return petGuid;
	}

	public void setPetGuid(String petGuid) {
		this.petGuid = petGuid;
	}

	public String getCompanyGuid() {
		return companyGuid;
	}

	public void setCompanyGuid(String companyGuid) {
		this.companyGuid = companyGuid;
	}

}
