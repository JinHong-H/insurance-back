package cn.wghtstudio.insurance.util;

import org.springframework.stereotype.Component;

import java.net.URLEncoder;

@Component
public class OcrInfoGet {
	public String insuranceDocuments(String imgUrl, String token) {
		// 请求url
		String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/insurance_documents";
		try {
			String param = "url=" + imgUrl;
			return HttpUtil.post(url, token, param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String idcard(String imgUrl, String accessToken) {
		// 请求url
		String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard";
		try {
			String param = "id_card_side=" + "front" + "&url=" + imgUrl;
			return HttpUtil.post(url, accessToken, param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String businessLicense(String imgUrl,String token) {
		// 请求url
		String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/business_license";
		try {
			String param = "url=" + imgUrl;
			
			return HttpUtil.post(url, token, param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String vehicleLicense(String imgUrl,String token) {
		// 请求url
		String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/vehicle_license";
		try {
			String param = "url=" + imgUrl;
			return HttpUtil.post(url, token, param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public String vehicleCertificate(String imgUrl,String token) {
		// 请求url
		String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/vehicle_certificate";
		try {
			String param = "url=" + imgUrl;
			return HttpUtil.post(url, token, param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
