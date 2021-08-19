package cn.wghtstudio.insurance.service.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryUserResponseBody {
	private int id;
	private String username;
	private int roleID;
}
