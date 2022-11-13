package br.senac.backend.handler;

import org.springframework.stereotype.Component;

import br.senac.backend.response.ResponseAPI;

@Component
public class HandlerChangePassword {

	public void handleUpdateMessages(ResponseAPI response, int code, Object model) {
		if (code == 200) {
			response.setCode(200);
			response.setData(model);
			response.setMsg("PASSWORD_UPDATED_SUCCESSFULLY");
		} else if (code == 404) {
			response.setCode(404);
			response.setData(null);
			response.setMsg("PASSWORD_UPDATED_NOT_FOUND");
		} else if (code == 400) {
			response.setCode(400);
			response.setMsg("BAD_REQUEST");
			response.setData(null);
		} else if (code == 401) {
			response.setCode(401);
			response.setMsg("USER_NOT_AUTHORIZED_OR_PASS_INVALID");
			response.setData(null);
		}
	}
}