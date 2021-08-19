package cn.wghtstudio.insurance.service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

// 营业执照
@Data
public class BussyLicenseResponseBdoy {
	
	private String log_id;
	private Words_result words_result;
	private int words_result_num;
	
	public static class Words_result {
		@JsonProperty("社会信用代码")
		private Info creditcode;
		@JsonProperty("组成形式")
		private Info formation;
		@JsonProperty("经营范围")
		private Info scope;
		@JsonProperty("成立日期")
		private Info establish;
		@JsonProperty("法人")
		private Info legalperson;
		@JsonProperty("注册资本")
		private Info captial;
		@JsonProperty("证件编号")
		private Info idnumber;
		@JsonProperty("地址")
		private Info address;
		@JsonProperty("单位名称")
		private Info companyname;
		@JsonProperty("有效期")
		private Info validperiod;
		@JsonProperty("类型")
		private Info type;
		public static class Info {
			private String words;
			private Location location;
		}
	}
	
	
}
