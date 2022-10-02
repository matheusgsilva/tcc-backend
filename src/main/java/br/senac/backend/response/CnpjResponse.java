package br.senac.backend.response;

import java.util.Date;

public class CnpjResponse {

	public String cnpj_raiz;
	public String razao_social;
	public Date atualizado_em;
	public Estabelecimento estabelecimento;

	public String getCnpj_raiz() {
		return cnpj_raiz;
	}

	public void setCnpj_raiz(String cnpj_raiz) {
		this.cnpj_raiz = cnpj_raiz;
	}

	public String getRazao_social() {
		return razao_social;
	}

	public void setRazao_social(String razao_social) {
		this.razao_social = razao_social;
	}

	public Date getAtualizado_em() {
		return atualizado_em;
	}

	public void setAtualizado_em(Date atualizado_em) {
		this.atualizado_em = atualizado_em;
	}

	public Estabelecimento getEstabelecimento() {
		return estabelecimento;
	}

	public void setEstabelecimento(Estabelecimento estabelecimento) {
		this.estabelecimento = estabelecimento;
	}

}

class Estabelecimento {

	public String cnpj;
	public String cnpj_raiz;
	public String cnpj_ordem;
	public String cnpj_digito_verificador;
	public String tipo;
	public String nome_fantasia;
	public String situacao_cadastral;
	public String data_situacao_cadastral;
	public String data_inicio_atividade;
	public String logradouro;
	public String bairro;
	public String email;

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getCnpj_raiz() {
		return cnpj_raiz;
	}

	public void setCnpj_raiz(String cnpj_raiz) {
		this.cnpj_raiz = cnpj_raiz;
	}

	public String getCnpj_ordem() {
		return cnpj_ordem;
	}

	public void setCnpj_ordem(String cnpj_ordem) {
		this.cnpj_ordem = cnpj_ordem;
	}

	public String getCnpj_digito_verificador() {
		return cnpj_digito_verificador;
	}

	public void setCnpj_digito_verificador(String cnpj_digito_verificador) {
		this.cnpj_digito_verificador = cnpj_digito_verificador;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNome_fantasia() {
		return nome_fantasia;
	}

	public void setNome_fantasia(String nome_fantasia) {
		this.nome_fantasia = nome_fantasia;
	}

	public String getSituacao_cadastral() {
		return situacao_cadastral;
	}

	public void setSituacao_cadastral(String situacao_cadastral) {
		this.situacao_cadastral = situacao_cadastral;
	}

	public String getData_situacao_cadastral() {
		return data_situacao_cadastral;
	}

	public void setData_situacao_cadastral(String data_situacao_cadastral) {
		this.data_situacao_cadastral = data_situacao_cadastral;
	}

	public String getData_inicio_atividade() {
		return data_inicio_atividade;
	}

	public void setData_inicio_atividade(String data_inicio_atividade) {
		this.data_inicio_atividade = data_inicio_atividade;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
