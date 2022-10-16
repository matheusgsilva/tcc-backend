package br.senac.backend.handler;

import org.springframework.stereotype.Component;

import br.senac.backend.response.ResponseAPI;

@Component
public class HandlerRating {

	public void handleAddMessages(ResponseAPI response, int code, Object model) {
		if (code == 200) {
			response.setCode(200);
			response.setData(model);
			response.setMsg("SUCCESSFULLY_REGISTERED_RATING");
		} else if (code == 404) {
			response.setCode(404);
			response.setData(null);
			response.setMsg("RATING_ADDED_NOT_FOUND");
		} else if (code == 304) {
			response.setCode(304);
			response.setMsg("RATING_ALREADY_EXISTS");
			response.setData(null);
		} else if (code == 400) {
			response.setCode(400);
			response.setMsg("BAD_REQUEST");
			response.setData(null);
		}
	}

	public void handleUpdateMessages(ResponseAPI response, int code, Object model) {
		if (code == 200) {
			response.setCode(200);
			response.setData(model);
			response.setMsg("RATING_UPDATED_SUCCESSFULLY");
		} else if (code == 404) {
			response.setCode(404);
			response.setData(null);
			response.setMsg("RATING_UPDATED_NOT_FOUND");
		} else if (code == 304) {
			response.setCode(304);
			response.setMsg("RATING_ALREADY_EXISTS");
			response.setData(null);
		} else if (code == 400) {
			response.setCode(400);
			response.setMsg("BAD_REQUEST");
			response.setData(null);
		}
	}
	
	public void handleDetailMessages(ResponseAPI response, int code, Object model) {
		if (code == 200) {
			response.setCode(200);
			response.setData(model);
			response.setMsg("RATING_DETAIL_SUCCESSFULLY");
		} else if (code == 404) {
			response.setCode(404);
			response.setData(null);
			response.setMsg("RATING_NOT_FOUND");
		} else if (code == 400) {
			response.setCode(400);
			response.setMsg("BAD_REQUEST");
			response.setData(null);
		}
	}

	public void handleDeleteMessages(ResponseAPI response, int code) {
		if (code == 200) {
			response.setCode(200);
			response.setData(null);
			response.setMsg("RATING_DELETE_SUCCESSFULLY");
		} else if (code == 404) {
			response.setCode(404);
			response.setData(null);
			response.setMsg("RATING_NOT_FOUND");
		} else if (code == 400) {
			response.setCode(400);
			response.setMsg("BAD_REQUEST");
		}
	}

	public void handleListMessages(ResponseAPI response, int code, Object model) {
		if (code == 200) {
			response.setCode(200);
			response.setData(model);
			response.setMsg("RATING_LIST_SUCCESSFULLY");
		} else if (code == 404) {
			response.setCode(404);
			response.setData(null);
			response.setMsg("RATING_NOT_FOUND");
		} else if (code == 400) {
			response.setCode(400);
			response.setMsg("BAD_REQUEST");
			response.setData(null);
		}
	}
}
