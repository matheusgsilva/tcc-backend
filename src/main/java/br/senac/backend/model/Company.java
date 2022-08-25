package br.senac.backend.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.senac.backend.util.EACTIVE;
import br.senac.backend.util.ECOMPANY_PERMISSION;

@Entity
@Table(name = "COMPANY", indexes = { @Index(name = "GUID_INDEX", columnList = "guid", unique = true) })
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@Company")
public class Company implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "guid", columnDefinition = "VARCHAR(36)", nullable = false)
	private String guid;

	@Column(columnDefinition = "VARCHAR(100)", nullable = false)
	private String name;

	@Column(columnDefinition = "VARCHAR(14)", nullable = false)
	private String document;

	@Column(columnDefinition = "VARCHAR(200)", nullable = false)
	private String email;

	@Column(columnDefinition = "VARCHAR(255)", nullable = false)
	private String street;

	@Column(columnDefinition = "VARCHAR(10)", nullable = false)
	private String numberAddress;

	@Column(columnDefinition = "VARCHAR(8)", nullable = false)
	private String cep;

	@Column(columnDefinition = "VARCHAR(255)", nullable = false)
	private String district;

	@Column(columnDefinition = "VARCHAR(255)", nullable = false)
	private String city;

	@Column(columnDefinition = "VARCHAR(2)", nullable = false)
	private String uf;

	@Column(columnDefinition = "VARCHAR(20)", nullable = false)
	private String phone;

	@Column(columnDefinition = "VARCHAR(50)", nullable = false)
	private String country;

	@Column(columnDefinition = "VARCHAR(255)", nullable = true)
	private String description;

	private EACTIVE active;

	private ECOMPANY_PERMISSION permission;

	@Column(nullable = false, columnDefinition = "VARCHAR(100)")
	private String password;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumberAddress() {
		return numberAddress;
	}

	public void setNumberAddress(String numberAddress) {
		this.numberAddress = numberAddress;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public EACTIVE getActive() {
		return active;
	}

	public void setActive(EACTIVE active) {
		this.active = active;
	}

	public ECOMPANY_PERMISSION getPermission() {
		return permission;
	}

	public void setPermission(ECOMPANY_PERMISSION permission) {
		this.permission = permission;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}