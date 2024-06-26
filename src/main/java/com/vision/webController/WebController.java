package com.vision.webController;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.vision.entity.WebInfo;
import com.vision.service.RequestPinService;

//48694878641 testing number

@RestController
public class WebController {
	
	@Autowired
	private RequestPinService validationService;
	
	
	@PostMapping("/ani")
	@CrossOrigin
	public ResponseEntity<?> getMsisdn(@RequestBody WebInfo webInfo, @RequestHeader Map<String,String> header)
	{
		Map<String,String> response = new HashMap<>();
		String flag="Error";
		try {
			if(webInfo != null && webInfo.getEvinaRequestId()!=null)
			{
				JSONObject jsonObject = new JSONObject(header);
				String[] parts = jsonObject.get("host").toString().split(":");
		        String host = parts[0];
		        
		        System.out.println("ani " + webInfo.getMobileNumber() + "lang" + webInfo.getLanguage());
		        
				flag = validationService.requestRedirectUrl(webInfo, host,"0");

				response.put("response",flag); 	
				return ResponseEntity.ok(response);
				
			}else {
				response.put("response",flag); 
				return ResponseEntity.ok(response);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Error");
		}
	}
}
