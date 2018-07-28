package com.hdn.test.sever;

import com.hdn.test.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@Api(value = "/", description = "这是所有的post方法")
@RequestMapping("/v1")
public class MyPostMethod {

    private Cookie cookie;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "登录接口，成功后获取登录信息", httpMethod = "POST")
    public String login(HttpServletResponse response,
                        @RequestParam(value = "userName", required = true) String userName,
                        @RequestParam(value = "password", required = true) String password) {
        if (userName.equals("suda") && password.equals("123456")) {
            cookie = new Cookie("login", "true");
            response.addCookie(cookie);
            return "登陆成功";
        }
        return "用户名或者密码错误";
    }

    @RequestMapping(value = "/getUserList", method = RequestMethod.POST)
    @ApiOperation(value = "获取用户列表", httpMethod = "POST")
    public String getUserList(HttpServletRequest request,
                              @RequestBody User user) {
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("login")
                    && cookie.getValue().equals("true")
                    && user.getUserName().equals("suda")
                    && user.getPassword().equals("123456")
                    ) {
                user = new User();
                user.setName("lisi");
                user.setAge(18);
                user.setSex("man");
                return user.toString();
            }
        }
        return "用户名或者密码错误";
    }
}
