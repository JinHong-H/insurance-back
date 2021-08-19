package cn.wghtstudio.insurance.controller;

import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.service.UserDealService;
import cn.wghtstudio.insurance.service.entity.QueryUserResponseBody;
import cn.wghtstudio.insurance.util.CurrentUser;
import cn.wghtstudio.insurance.util.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Controller
public class UserDealController {
	@Resource
	UserDealService userDealService;
	
	
	static class UserAddRequestBody {
		@NotEmpty
		private String username;
		@NotEmpty
		private String password;
		@NotEmpty
		private int roleId;
	}
	
	
	static class UserdeleteRequestBody {
		@NotEmpty
		private int id;
	}
	
	static class UpdateRequestBody {
		@NotEmpty
		private int id;
		@NotEmpty
		private String username;
		@NotEmpty
		private String password;
		@NotEmpty
		private int roleId;
	}
	
	static class ChangePasswdRequestBody {
		@NotEmpty
		private String newpassword;
	}
	
	@PostMapping("/userdeal")
	public Result<Object> AddUser(@Valid @RequestBody UserAddRequestBody req) {
		userDealService.addUserService(req.username, req.password, req.roleId);
		return Result.success(null);
	}
	
	@DeleteMapping("/userdeal")
	public Result<Object> deleteUserUser(@Valid @RequestBody UserdeleteRequestBody req) {
		userDealService.deleteUserService(req.id);
		return Result.success(null);
	}
	
	
	@GetMapping("/userdeal")
	public Result<QueryUserResponseBody[]> QueryUser(@RequestParam(defaultValue = "", value = "condition") String condition, @RequestParam(defaultValue = "10", value = "pageSize") int pageSize,
													 @RequestParam(defaultValue = "0", value = "offset") int offset) {
		QueryUserResponseBody[] res = userDealService.queryUserService(condition, pageSize, offset);
		return Result.success(res);
	}
	
	@PutMapping("/userdeal")
	public Result<Object> UpdateUser(@Valid @RequestBody UpdateRequestBody req) {
		userDealService.updateUserService(req.id, req.username, req.password, req.roleId);
		return Result.success(null);
	}
	
	@PostMapping("/changepasswd")
	public Result<Object> changePassword(@CurrentUser User user, @Valid @RequestBody ChangePasswdRequestBody req) {
		userDealService.updateOwnPasswordService(user.getId(), req.newpassword);
		return Result.success(null);
	}
}
