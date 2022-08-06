package br.senac.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		
		System.out.println("\r\n"
				+ "██████╗  █████╗  ██████╗██╗  ██╗███████╗███╗   ██╗██████╗ \r\n"
				+ "██╔══██╗██╔══██╗██╔════╝██║ ██╔╝██╔════╝████╗  ██║██╔══██╗\r\n"
				+ "██████╔╝███████║██║     █████╔╝ █████╗  ██╔██╗ ██║██║  ██║\r\n"
				+ "██╔══██╗██╔══██║██║     ██╔═██╗ ██╔══╝  ██║╚██╗██║██║  ██║\r\n"
				+ "██████╔╝██║  ██║╚██████╗██║  ██╗███████╗██║ ╚████║██████╔╝\r\n"
				+ "╚═════╝ ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═══╝╚═════╝ \r\n"
				+ "                                                          \r\n"
				+ "");
		System.out.println("Use Swagger @: http://localhost:9595/backend/swagger-ui.html");
		SpringApplication.run(BackendApplication.class, args);
	}

}
