package br.senac.backend.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.senac.backend.util.ESTATUS_PET;

@Entity
@Table(name = "PETS", indexes = { @Index(name = "GUID_INDEX", columnList = "guid", unique = true),
		@Index(name = "TYPE_PET_INDEX", columnList = "typePet", unique = false),
		@Index(name = "SIZE_INDEX", columnList = "size", unique = false),
		@Index(name = "BREED_INDEX", columnList = "breed", unique = false),
		@Index(name = "GENDER_INDEX", columnList = "gender", unique = false) })
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@Pet")
public class Pet implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "guid", columnDefinition = "VARCHAR(36)", nullable = false)
	private String guid;

	@Lob
	private String description;

	@Column(columnDefinition = "VARCHAR(30)", nullable = true)
	private String size;

	@Temporal(TemporalType.TIMESTAMP)
	private Date birthDate;

	@Column(columnDefinition = "VARCHAR(50)", nullable = true)
	private String breed;

	@Column(columnDefinition = "VARCHAR(50)", nullable = true)
	private String gender;

	@ElementCollection
	@CollectionTable(name = "pet_vaccines", joinColumns = @JoinColumn(name = "pet_id"))
	@Column(name = "vaccine")
	private List<String> vaccines = new ArrayList<String>();

	@Column(columnDefinition = "VARCHAR(200)", nullable = true)
	private String color;

	@Column(columnDefinition = "VARCHAR(10)")
	private String identification;

	@Lob
	private String photo1;

	@Lob
	private String photo2;

	@Lob
	private String photo3;

	@Lob
	private String photo4;

	@Column(columnDefinition = "INT(1) default 0")
	private ESTATUS_PET status;

	@Column(columnDefinition = "VARCHAR(50)", nullable = true)
	private String typePet;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company")
	private Company company;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user", nullable = true)
	private User adopterUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true)
	private Date reservationDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

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

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getBreed() {
		return breed;
	}

	public void setBreed(String breed) {
		this.breed = breed;
	}

	public List<String> getVaccines() {
	    return vaccines;
	}

	public void setVaccines(List<String> vaccines) {
	    this.vaccines = vaccines;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getPhoto1() {
		return photo1;
	}

	public void setPhoto1(String photo1) {
		this.photo1 = photo1;
	}

	public String getPhoto2() {
		return photo2;
	}

	public void setPhoto2(String photo2) {
		this.photo2 = photo2;
	}

	public String getPhoto3() {
		return photo3;
	}

	public void setPhoto3(String photo3) {
		this.photo3 = photo3;
	}

	public String getPhoto4() {
		return photo4;
	}

	public void setPhoto4(String photo4) {
		this.photo4 = photo4;
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

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public User getAdopterUser() {
		return adopterUser;
	}

	public void setAdopterUser(User adopterUser) {
		this.adopterUser = adopterUser;
	}

	public ESTATUS_PET getStatus() {
		return status;
	}

	public void setStatus(ESTATUS_PET status) {
		this.status = status;
	}

	public Date getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(Date reservationDate) {
		this.reservationDate = reservationDate;
	}

}