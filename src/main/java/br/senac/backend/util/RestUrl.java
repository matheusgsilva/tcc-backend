package br.senac.backend.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestUrl {

	public String getData(String url, String token) {
		StringBuilder resultJson = new StringBuilder();
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json");
			if (!token.equals(""))
				headers.add("Token", token);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			if (!result.getStatusCode().isError()) {
				if (result.hasBody())
					resultJson.append(result.getBody());
			} else
				resultJson.append(result.getStatusCodeValue() + "::");
		} catch (HttpStatusCodeException e) {
			resultJson.append(e.getRawStatusCode() + "::");
		}
		return resultJson.toString();
	}

	public String postData(String url, String json, String token) {
		StringBuilder resultJson = new StringBuilder();
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json");
			if (!token.equals(""))
				headers.add("Token", token);
			HttpEntity<String> entity = new HttpEntity<String>(json, headers);
			ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			if (!result.getStatusCode().isError()) {

				if (result.hasBody())
					resultJson.append(result.getBody());
			} else
				resultJson.append(result.getStatusCodeValue() + "::");
		} catch (HttpStatusCodeException e) {
			resultJson.append(e.getRawStatusCode() + "::");
		}
		return resultJson.toString();
	}
}