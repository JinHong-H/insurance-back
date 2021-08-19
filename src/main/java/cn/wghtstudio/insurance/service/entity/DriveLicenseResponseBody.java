package cn.wghtstudio.insurance.service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

// 行驶证
@Data
public class DriveLicenseResponseBody {
	private Words_result words_result;
	private String log_id;
	private int words_result_num;
	
	public static class Words_result {
		@JsonProperty("车辆识别代号")
		private Info carVerfy;
		@JsonProperty("住址")
		private Info address;
		@JsonProperty("发证日期")
		private Info gottime;
		@JsonProperty("发证单位")
		private Info gotCompany;
		@JsonProperty("品牌型号")
		private Info brand;
		@JsonProperty("车辆类型")
		private Info model;
		@JsonProperty("所有人")
		private Info owner;
		@JsonProperty("使用性质")
		private Info natuere;
		@JsonProperty("发动机号码")
		private Info enginenumber;
		@JsonProperty("号牌号码")
		private Info plate;
		@JsonProperty("注册日期")
		private Info registration;
		
		
		public static class Info {
			private String words;
			
			
		}
	}
}
