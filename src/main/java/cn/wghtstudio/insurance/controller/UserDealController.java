package cn.wghtstudio.insurance.controller;

import cn.wghtstudio.insurance.controller.entity.UserRequestBody;
import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.exception.UserExistedException;
import cn.wghtstudio.insurance.service.UserDealService;
import cn.wghtstudio.insurance.service.entity.GetUserListResponseBody;
import cn.wghtstudio.insurance.util.CurrentUser;
import cn.wghtstudio.insurance.util.Result;
import cn.wghtstudio.insurance.util.ResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserDealController {
    private final Logger logger = LoggerFactory.getLogger(UserDealController.class);

    @Resource
    UserDealService userDealService;

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
                put("username", username != null ? "%" + username + "%" : username);
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
    public Result<?> AddUser(@Valid @RequestBody UserRequestBody.AddUserRequestBody req) {
        try {
            userDealService.addUserService(req.getUsername(), req.getPassword(), req.getRoleId());
            return Result.success(null);
        } catch (UserExistedException e) {
            logger.warn("UserExistedException", e);
            return Result.error(ResultEnum.USER_EXISTED_ERROR);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }

    @PutMapping
    public Result<?> UpdateUser(@Valid @RequestBody UserRequestBody.UpdateUserRequestBody req) {
        try {
            userDealService.updateUserService(req.getId(), req.getPassword(), req.getRoleId());
            return Result.success(null);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }

    @DeleteMapping
    public Result<?> deleteUser(@Valid @RequestBody UserRequestBody.DeleteUserRequestBody req) {
        try {
            userDealService.deleteUserService(req.getId());
            return Result.success(null);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }

    @PostMapping("/changePasswd")
    public Result<?> changePassword(@CurrentUser User user, @Valid @RequestBody UserRequestBody.ChangePasswdRequestBody req) {
        try {
            userDealService.updateOwnPasswordService(user.getId(), req.getNewPassword());
            return Result.success(null);
        } catch (Exception e) {
            logger.warn("Exception", e);
            return Result.error(ResultEnum.DEFAULT_ERROR);
        }
    }
}
