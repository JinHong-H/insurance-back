package cn.wghtstudio.insurance.controller;

import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.service.UserDealService;
import cn.wghtstudio.insurance.service.entity.GetUserListResponseBody;
import cn.wghtstudio.insurance.util.CurrentUser;
import cn.wghtstudio.insurance.util.Result;
import cn.wghtstudio.insurance.util.ResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserDealController {
    private final Logger logger = LoggerFactory.getLogger(UserDealController.class);

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

    @GetMapping
    public Result<GetUserListResponseBody> QueryUser(
            @CurrentUser User user,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(defaultValue = "10", value = "pageSize") int pageSize,
            @RequestParam(defaultValue = "1", value = "current") int current
    ) {
        Map<String, Object> params = new HashMap<>() {
            {
                put("currentUserId", user.getId());
                put("limit", pageSize);
                put("offset", (current - 1) * pageSize);
                put("username", username);
            }
        };

        try {
            GetUserListResponseBody res = userDealService.getUserListService(params);
            return Result.success(res);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }

    @PostMapping
    public Result<?> AddUser(@Valid @RequestBody UserAddRequestBody req) {
        userDealService.addUserService(req.username, req.password, req.roleId);
        return Result.success(null);
    }

    @PutMapping
    public Result<?> UpdateUser(@Valid @RequestBody UpdateRequestBody req) {
        userDealService.updateUserService(req.id, req.username, req.password, req.roleId);
        return Result.success(null);
    }

    @DeleteMapping
    public Result<?> deleteUser(@Valid @RequestBody UserdeleteRequestBody req) {
        userDealService.deleteUserService(req.id);
        return Result.success(null);
    }

    @PostMapping("/changePasswd")
    public Result<?> changePassword(@CurrentUser User user, @Valid @RequestBody ChangePasswdRequestBody req) {
        userDealService.updateOwnPasswordService(user.getId(), req.newpassword);
        return Result.success(null);
    }
}
