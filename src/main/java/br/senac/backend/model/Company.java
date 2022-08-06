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
	private String nome;

	@Column(columnDefinition = "VARCHAR(14)", nullable = false)
	private String documento;

	@Column(columnDefinition = "VARCHAR(200)", nullable = false)
	private String email;

	@Column(columnDefinition = "VARCHAR(255)", nullable = false)
	private String rua;

	@Column(columnDefinition = "VARCHAR(10)", nullable = false)
	private String numero;

	@Column(columnDefinition = "VARCHAR(8)", nullable = false)
	private String cep;

	@Column(columnDefinition = "VARCHAR(255)", nullable = false)
	private String bairro;

	@Column(columnDefinition = "VARCHAR(255)", nullable = false)
	private String cidade;

	@Column(columnDefinition = "VARCHAR(2)", nullable = false)
	private String uf;

	@Column(columnDefinition = "VARCHAR(20)", nullable = false)
	private String telefone;

	@Column(columnDefinition = "VARCHAR(255)", nullable = true)
	private String descricao;

	private EACTIVE ativo;

	private ECOMPANY_PERMISSION permissao;

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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public EACTIVE getAtivo() {
		return ativo;
	}

	public void setAtivo(EACTIVE ativo) {
		this.ativo = ativo;
	}

	public ECOMPANY_PERMISSION getPermissao() {
		return permissao;
	}

	public void setPermissao(ECOMPANY_PERMISSION permissao) {
		this.permissao = permissao;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}