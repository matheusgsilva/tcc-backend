package br.senac.backend.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.senac.backend.util.EACTIVE;
import br.senac.backend.util.ETYPE_USER;

@Entity
@Table(name = "USERS", indexes = { @Index(name = "GUID_INDEX", columnList = "guid", unique = true) })
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@User")
public class User implements Serializable {

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

	@Column(columnDefinition = "VARCHAR(20)", nullable = false)
	private String phone;

	@Column(nullable = false, columnDefinition = "VARCHAR(100)")
	private String password;

	private EACTIVE active;

	@Column(columnDefinition = "INT(1) default 0")
	private ETYPE_USER type;

	@JsonIgnore
	@OneToMany(mappedBy = "user", targetEntity = Token.class, fetch = FetchType.LAZY)
	private List<Token> tokens = new ArrayList<Token>();

	@OneToMany(mappedBy = "user", targetEntity = Rating.class, fetch = FetchType.LAZY)
	private List<Rating> ratings = new ArrayList<Rating>();

	@ManyToMany(targetEntity = Pet.class, fetch = FetchType.LAZY)
	private List<Pet> favoritePets = new ArrayList<Pet>();

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public EACTIVE getActive() {
		return active;
	}

	public void setActive(EACTIVE active) {
		this.active = active;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}

	public List<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}

	public ETYPE_USER getType() {
		return type;
	}

	public void setType(ETYPE_USER type) {
		this.type = type;
	}

	public List<Pet> getFavoritePets() {
		return favoritePets;
	}

	public void setFavoritePets(List<Pet> favoritePets) {
		this.favoritePets = favoritePets;
	}

}