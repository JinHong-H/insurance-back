package cn.wghtstudio.insurance.service.entity;

import lombok.Data;

import java.util.List;

// 保单
@Data
public class InsuranceDocumentResponseBody {
	private String log_id;
	private int words_result_num;
	private Words_result words_result;
	
	public static class Words_result {
		private String InsBilTim;
		private String InsBthDa1;
		private String InsTolAmt;
		private String InsBilNo;
		private String InsIdcTy1;
		private String InsIdcNb1;
		private List<InsPerLst> InsPerLst;
		private String InsCltNa1;
		private List<InsPrdList> InsPrdList;
		private List<BenPerLst> BenPerLst;
		private String InsBilCom;
		
		public static class InsPerLst {
			private String InsIdcTy2;
			private String InsCltNa2;
			private String InsIdcNb2;
			private String InsBthDa2;
		}
		
		public static class BenPerLst {
			private String BenPerTyp;
			private String BenPerPro;
			private String BenCltNa;
			
		}
		
		public static class InsPrdList {
			private String InsPayFeq;
			private String InsPerAmt;
			private String InsPayDur;
			private String InsCovDur;
			private String InsPrdNam;
			private String InsIcvNum;
		}
	}
}
