package com.vision.Utility;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vision.entity.ServiceInfo;
import com.vision.entity.WebInfo;

@Service
public class UtilityService2 {
		
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface MethodOverloadingInfo {
	    String value() default ""; 
	}
	
	//using
	@MethodOverloadingInfo("This method will build signature for request pin and subscription")
	public String buildSignature(String apiKey, String apiSecret, String applicationId, String countryId,
	        String operatorId, String cpId, String timestamp,
	        String lang, String shortcode, String method)
	        throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		
	
	    StringBuilder decryptedSignature = new StringBuilder();

	    decryptedSignature.append("ApiKey=").append(customURLEncoder(apiKey)).append("&");
	    decryptedSignature.append("ApiSecret=").append(customURLEncoder(apiSecret)).append("&");
	    decryptedSignature.append("ApplicationId=").append(customURLEncoder(applicationId.toString())).append("&");
	    decryptedSignature.append("CountryId=").append(customURLEncoder(countryId.toString())).append("&");
	    decryptedSignature.append("OperatorId=").append(customURLEncoder(operatorId.toString())).append("&");
	    decryptedSignature.append("CpId=").append(customURLEncoder(cpId.toString())).append("&");
	    //decryptedSignature.append("MSISDN=").append(customURLEncoder(msisdn.toUpperCase())).append("&");
	    decryptedSignature.append("Timestamp=").append(customURLEncoder(timestamp.toUpperCase())).append("&");
	    decryptedSignature.append("Lang=").append(customURLEncoder(lang.toUpperCase())).append("&");
	    decryptedSignature.append("ShortCode=").append(customURLEncoder(shortcode.toUpperCase())).append("&");
	    decryptedSignature.append("Method=").append(customURLEncoder(method));
	    
	    System.out.println("Signature of request pin is:--- " + decryptedSignature);

	    String encryptedSignature = calculateHMACSHA256(apiSecret, decryptedSignature.toString());
	    System.out.println("Encrypted Signature----> " + encryptedSignature);
	    return encryptedSignature;
	}
	
	public String customURLEncoder(String value) throws UnsupportedEncodingException {
	    return URLEncoder.encode(value, "UTF-8").replaceAll("\\+", "%20").replaceAll("%2B", "%2b").replaceAll("%2F", "%2f");
	}
	
	public String customSmsURLEncoder(String value) throws UnsupportedEncodingException {
	    return URLEncoder.encode(value, "UTF-8");
	}

	public String calculateHMACSHA256(String key, String data)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Mac sha256HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256HMAC.init(secretKey);
        byte[] hashedBytes = sha256HMAC.doFinal(data.getBytes("UTF-8"));
        return bytesToHex(hashedBytes);
    }
	public String bytesToHex(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                hexStringBuilder.append('0');
            }
            hexStringBuilder.append(hex);
        }
        return hexStringBuilder.toString();
    }
	
	
	//using
	public String getCGUrlFromApi(ServiceInfo serviceInfo, WebInfo webInfo, String host, String signature, String dateTime)
	{
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://ksg.intech-mena.com/MSG/v1.1/API/RedirectToCG";
        
        
        try {
        	
        	HttpHeaders headers = new HttpHeaders();
        	headers.setContentType(MediaType.APPLICATION_JSON);
    		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    		
    		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
    				//.queryParam("msisdn", webInfo.getMobileNumber())
                    .queryParam("applicationId", serviceInfo.getApplicationId())
                    .queryParam("countryId", serviceInfo.getCountryId())
                    .queryParam("operatorId", serviceInfo.getOperatorId())
                    .queryParam("rurl", customURLEncoder("https://glamworld.h2ndigital.com/"))
                    .queryParam("cpId", serviceInfo.getCpId())
                    .queryParam("requestId", webInfo.getEvinaRequestId().toString())
                    .queryParam("apiKey", serviceInfo.getApiKey())
                    .queryParam("timestamp", dateTime)
                    .queryParam("lang", webInfo.getLanguage().toLowerCase())
                    .queryParam("shortcode", serviceInfo.getShortcode())
                    .queryParam("ipAddress", host)
                    .queryParam("lpUrl", customURLEncoder("https://glamworld.h2ndigital.com/"))
                    .queryParam("signature", signature);
    		
    		System.out.println("Sending GET request to: " + builder.toUriString());

 
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(builder.toUriString(), String.class);
	        
	        if (responseEntity.getStatusCode().is2xxSuccessful()) {
	            String responseBody = responseEntity.getBody();

	            try {
	                ObjectMapper objectMapper = new ObjectMapper();
	                JsonNode jsonNode = objectMapper.readTree(responseBody);
	                String cgwUrl = jsonNode.get("CGWUrl").asText();
	                System.out.println("CGWUrl: " + cgwUrl);
	                return cgwUrl;
	            } catch (Exception e) {
	                System.out.println("Error parsing JSON: " + e.getMessage());
	                return "Failed";
	            }
	        } else {
	            System.out.println("Error occurred: " + responseEntity.getStatusCode());
	        }
    		
		}catch (HttpServerErrorException.InternalServerError e) {
		    System.out.println("Response Body:---- " + e.getResponseBodyAsString());
		    e.printStackTrace();
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
        return "Failed";
	}
	
	public static String generateRequestId() 
	{
		 UUID uuid = UUID.randomUUID();
		 return uuid.toString();
	}
}
