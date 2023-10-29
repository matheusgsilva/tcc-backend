package br.senac.backend.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ImageVerificationServiceBean {

	@Value("${url.image.server}")
	private String urlServer;

	public String getImageVerification(String base64Image) {

		HttpClient httpClient = HttpClients.createDefault();
		HttpPost request = new HttpPost(urlServer);

		try {
			if (base64Image.startsWith("data:image/")) {
				base64Image = base64Image.split(",")[1].trim();
			}
			byte[] imageBytes = Base64.getDecoder().decode(base64Image);

			InputStream imageStream = new ByteArrayInputStream(imageBytes);

			HttpEntity multipartEntity = MultipartEntityBuilder.create()
					.addBinaryBody("image", imageStream, ContentType.MULTIPART_FORM_DATA, "image.jpg").build();

			request.setEntity(multipartEntity);

			HttpResponse response = httpClient.execute(request);

			return EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
