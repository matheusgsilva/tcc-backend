# Backend

Esta API é responsável por gerenciar informações dos usuários em um banco de dados, bem como realizar rotinas e operações para o bom funcionamento do Sistema 4PET.

## Pré-requisitos

- Java JDK 11
- Maven
- Spring Boot 2.4.5
- MySQL 8

## Instalação

####1. Clone o repositório
    git clone https://github.com/matheusgsilva/tcc-backend.git

####2. Crie o Banco de Dados
    Configure o arquivo application.properties com as configurações 
	do banco de dados criado.

####3. Compile a Aplicação
	mvn package

####4. Entre na pasta target/ e execute a aplicação
	java -jar backend-0.0.1-SNAPSHOT.jar