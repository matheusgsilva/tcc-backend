package br.senac.backend.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "PREFERENCES", indexes = { @Index(name = "GUID_INDEX", columnList = "guid", unique = true),
		@Index(name = "TYPE_PET_INDEX", columnList = "typePet", unique = false),
		@Index(name = "SIZE_INDEX", columnList = "size", unique = false),
		@Index(name = "AGE_INDEX", columnList = "age", unique = false),
		@Index(name = "BREED_INDEX", columnList = "breed", unique = false),
		@Index(name = "GENDER_INDEX", columnList = "gender", unique = false) })
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@Preferences")
public class Preferences implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "guid", columnDefinition = "VARCHAR(36)", nullable = false)
	private String guid;

	@Column(columnDefinition = "VARCHAR(30)", nullable = true)
	private String size;

	@Column(columnDefinition = "VARCHAR(30)", nullable = true)
	private String age;

	@Column(columnDefinition = "VARCHAR(50)", nullable = true)
	private String breed;

	@Column(columnDefinition = "VARCHAR(50)", nullable = true)
	private String gender;

	@Column(columnDefinition = "VARCHAR(50)", nullable = true)
	private String typePet;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user", nullable = true)
	private User user;

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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getTypePet() {
		return typePet;
	}

	public void setTypePet(String typePet) {
		this.typePet = typePet;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}