package cn.wghtstudio.insurance.service;

import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.service.entity.QueryUserResponseBody;

public interface UserDealService {
	void addUserService(String username,String password,int roleId);
	void deleteUserService(int id);
	QueryUserResponseBody[] queryUserService(String question,int pageSize,int offset);
	void updateUserService(int id,String username,String password,int roleId);
	void updateOwnPasswordService(int id, String newpassword);
}
