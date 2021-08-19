package cn.wghtstudio.insurance.controller;

import cn.wghtstudio.insurance.service.UserDealService;
import cn.wghtstudio.insurance.service.entity.QueryUserResponseBody;
import cn.wghtstudio.insurance.util.Result;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Controller
public class UserDealController {
	@Resource
	UserDealService userDealService;
	
	@Getter
	@Setter
	static class UserAddRequestBody {
		@NotEmpty
		private String username;
		@NotEmpty
		private String password;
		@NotEmpty
		private int roleId;
	}
	
	@Getter
	@Setter
	static class UserdeleteRequestBody {
		@NotEmpty
		private int id;
	}
	
	@Getter
	@Setter
	static class QueryRequestBody {
		private String question;
	}
	
	@Getter
	@Setter
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
	
	@PostMapping("/userdeal")
	public Result<Object> AddUser(@Valid @RequestBody UserAddRequestBody req) {
		userDealService.addUserService(req.username, req.password, req.getRoleId());
		return Result.success(null);
	}
	
	@DeleteMapping("/userdeal")
	public Result<Object> deleteUserUser(@Valid @RequestBody UserdeleteRequestBody req) {
		userDealService.deleteUserService(req.getId());
		return Result.success(null);
	}
	
	
	@GetMapping("/userdeal")
	public Result<QueryUserResponseBody[]> QueryUser(@Valid @RequestBody QueryRequestBody req, @RequestParam(defaultValue = "10", value = "pageSize") int pageSize,
													 @RequestParam(defaultValue = "0", value = "offset") int offset) {
		QueryUserResponseBody[] res = userDealService.queryUserService(req.question,pageSize,offset);
		return Result.success(res);
	}
	
	@PutMapping("/userdeal")
	public Result<Object> UpdateUser(@Valid @RequestBody UpdateRequestBody req) {
		userDealService.updateUserService(req.getId(), req.username, req.password, req.getRoleId());
		return Result.success(null);
	}
}
