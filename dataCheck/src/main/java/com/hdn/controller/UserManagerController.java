package com.hdn.controller;

import com.hdn.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@Log4j
@RequestMapping("/v1")
@Api(value = "v1", description = "用户管理系统")
public class UserManagerController {

    @Autowired
    private SqlSessionTemplate session;

    @ApiOperation(value = "登录接口", httpMethod = "POST")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public boolean login(HttpServletResponse response,
                         @RequestBody User user) {
        if (Objects.isNull(user)) {
            return false;
        }

        int i = session.selectOne("login", user);

        if (i == 1) {
            Cookie cookie = new Cookie("login", "true");
            response.addCookie(cookie);
            log.info("登陆成功，用户：" + user.toString());
            return true;
        }
        return false;
    }

    @ApiOperation(value = "添加用户接口", httpMethod = "POST")
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public boolean addUser(@RequestBody User user,
                           HttpServletRequest request) {

        boolean flag = verifyCookie(request);
        int result = 0;
        if (flag) {
            result = session.insert("addUser", user);
        }
        if (result > 0) {
            log.info("添加用户成功：" + user.toString());
            log.info("添加用户数量：" + result);
            return true;
        }
        log.info("添加用户失败");
        return false;
    }


    @ApiOperation(value = "获取用户列表（信息）接口", httpMethod = "POST")
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    public List<User> getUserInfo(@RequestBody User user,
                                  HttpServletRequest request) {

        boolean flag = true;//verifyCookie(request);
        List<User> userList = new ArrayList<>();
        if (flag) {
            userList = session.selectList("getUserInfo", user);
            log.info("获取到的用户户数量是：" + userList.size());
        }
        return userList;

    }

    @ApiOperation(value = "更新（删除）用户信息接口", httpMethod = "POST")
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    public int updateUserInfo(@RequestBody User user,
                              HttpServletRequest request) {
        boolean flag = verifyCookie(request);
        int res = 0;
        if (flag) {
            res = session.update("updateUserInfo", user);
            log.info("更新（删除）用户数量是：" + res);
        }
        return res;
    }

    private boolean verifyCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            log.info("cookie 为空");
            return false;
        }
        for (Cookie cookie : cookies) {
            if ("login".equals(cookie.getName())
                    && "true".equals(cookie.getValue())) {
                log.info("cookie验证成功");
                return true;
            }
        }
        return false;
    }
}
