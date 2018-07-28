package com.hdn.controller;

import com.hdn.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Log4j
@Api(value = "v1", description = "这是我的第一个版本的demo")
@RestController
@RequestMapping("/test")
public class TestMybatis {

    @Autowired
    private SqlSessionTemplate session;

    @RequestMapping(value = "/getUserCount", method = RequestMethod.GET)
    @ApiOperation(value = "可以获取到用户数", httpMethod = "GET")
    public int getUserCount() {
        return session.selectOne("getUserCount");
    }

    @RequestMapping(value = "/insertUser", method = RequestMethod.POST)
    @ApiOperation(value = "添加新用户", httpMethod = "POST")
    public Integer insertUser(@RequestBody User user) {
        int res = session.insert("insertUser", user);
        return res;
    }

    @RequestMapping(value = "/updateUserById", method = RequestMethod.POST)
    @ApiOperation(value = "修改用户名与年龄", httpMethod = "POST")
    public Integer updateUserById(@RequestBody User user) {
        int res = session.update("updateUserById", user);
        return res;
    }

    @RequestMapping(value = "/deleteUserById", method = RequestMethod.GET)
    @ApiOperation(value = "修改用户名与年龄", httpMethod = "GET")
    public Integer deleteUserById(@RequestParam Integer id) {
        int res = session.delete("deleteUserById", id);
        return res;
    }


}
