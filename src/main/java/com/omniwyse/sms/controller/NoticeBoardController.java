package com.omniwyse.sms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omniwyse.sms.models.NoticeBoard;
import com.omniwyse.sms.services.NoticeBoardService;
import com.omniwyse.sms.utils.NoticeBoardTransferObject;
import com.omniwyse.sms.utils.Response;

@RestController
public class NoticeBoardController {
	@Autowired
	NoticeBoardService service;
	@Autowired
	private Response response;

	@RequestMapping("/postnotice")
	public ResponseEntity<Response> postNotice(@RequestBody NoticeBoardTransferObject noticeboardTransferObject) {
		int rowEffected = service.postNotice(noticeboardTransferObject);
		if (rowEffected > 0) {
			response.setStatus(200);
			response.setMessage("Notice posted successfuly");
			response.setDescription("Notice posted successfuly");
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		} else {
			response.setStatus(400);
			response.setMessage("Not a valid Grade");
			response.setDescription("Not a valid Grade");
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);

		}

	}

	@RequestMapping("/editnotice")
	public ResponseEntity<Response> editNotice(@RequestBody NoticeBoard noticeboard) {
		service.editNotice(noticeboard);
		response.setStatus(200);
		response.setMessage("edited successfuly");
		response.setDescription("edited successfuly");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@RequestMapping("/listnotice")
	public List<NoticeBoardTransferObject> listNotice(@RequestBody NoticeBoard noticeboard) {
		return service.listNotice();

	}
}
