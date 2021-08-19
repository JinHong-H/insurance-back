package cn.wghtstudio.insurance.service.entity;

import lombok.Data;

// 车辆合格证
@Data
public class CertificateResponseBody {
	private String log_id;
	private int words_result_num;
	private int direction;
	private Words_result words_result;
	
	public static class Words_result {
		private String ManufactureDate;
		private String CarColor;
		private String LimitPassenger;
		private String EngineType;
		private String TotalWeight;
		private String Power;
		private String CertificationNo;
		private String FuelType;
		private String Manufacturer;
		private String SteeringType;
		private String Wheelbase;
		private String SpeedLimit;
		private String EngineNo;
		private String SaddleMass;
		private String AxleNum;
		private String CarModel;
		private String VinNo;
		private String CarBrand;
		private String EmissionStandard;
		private String Displacement;
		private String CertificateDate;
		private String CarName;
		private String TyreNum;
		private String ChassisID;
		private String ChassisModel;
		private String SeatingCapacity;
		private String QualifySeal;
		private String CGSSeal;
	}
}
