package br.senac.backend.request;

public class PetRequest {

	private String description;
	private String size;
	private String age;
	private String breed;
	private String vaccines;
	private String medication;
	private String photo1;
	private String typePet;
	private String gender;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getBreed() {
		return breed;
	}

	public void setBreed(String breed) {
		this.breed = breed;
	}

	public String getVaccines() {
		return vaccines;
	}

	public void setVaccines(String vaccines) {
		this.vaccines = vaccines;
	}

	public String getMedication() {
		return medication;
	}

	public void setMedication(String medication) {
		this.medication = medication;
	}

	public String getPhoto1() {
		return photo1;
	}

	public void setPhoto1(String photo1) {
		this.photo1 = photo1;
	}

	public String getTypePet() {
		return typePet;
	}

	public void setTypePet(String typePet) {
		this.typePet = typePet;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
