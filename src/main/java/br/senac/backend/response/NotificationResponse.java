package br.senac.backend.response;

import java.util.Date;

public class NotificationResponse {

	private Date date;
	private String companyName;
	private String companyGuid;
	private String petGuid;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyGuid() {
		return companyGuid;
	}

	public void setCompanyGuid(String companyGuid) {
		this.companyGuid = companyGuid;
	}

	public String getPetGuid() {
		return petGuid;
	}

	public void setPetGuid(String petGuid) {
		this.petGuid = petGuid;
	}

}
