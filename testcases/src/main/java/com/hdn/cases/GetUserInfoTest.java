package com.hdn.cases;

import com.hdn.config.TestConfig;
import com.hdn.model.GetUserInfoCase;
import com.hdn.model.User;
import com.hdn.utils.DataBaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GetUserInfoTest {

    @Test(dependsOnGroups = "loginSuccess", description = "获取用户信息接口")
    public void getUserInfo() throws IOException, InterruptedException, JSONException {
        SqlSession session = DataBaseUtil.getSqlSession();
        int getUserInfoCaseIndex = 1;
        GetUserInfoCase getUserInfoCase = session.selectOne("getUserInfoCase", getUserInfoCaseIndex);

        JSONArray resultJson = getJsonResult(getUserInfoCase);
        JSONObject actual = (JSONObject) resultJson.get(0);

        TimeUnit.SECONDS.sleep(2);
        //数据库中查询结果
        User user = session.selectOne(getUserInfoCase.getExpected(), getUserInfoCase);

        JSONObject expected = new JSONObject();
        expected.put("id",user.getId());
        expected.put("userName",user.getUserName());
        expected.put("password",user.getPassword());
        expected.put("age",user.getAge());
        expected.put("sex",user.getSex());
        expected.put("permission",user.getPermission());
        expected.put("isDelete",user.getIsDelete());
        System.out.println(actual.toString());
        System.out.println(expected.toString());

        Assert.assertEquals(actual.toString(),expected.toString());


    }


    private JSONArray getJsonResult(GetUserInfoCase getUserInfoCase) throws IOException, JSONException {
        HttpPost post = new HttpPost(TestConfig.getUserInfoUrl);
        JSONObject param = new JSONObject();
        param.put("id", getUserInfoCase.getUserId());

        //设置请求头信息 设置header
        post.setHeader("content-type", "application/json");
        //将参数信息添加到方法中
        StringEntity entity = new StringEntity(param.toString(), "utf-8");
        post.setEntity(entity);

        System.out.println("添加用户参数：" + param.toString());

        //设置cookies
        TestConfig.httpClient.setCookieStore(TestConfig.store);

        //执行post方法
        HttpResponse response = TestConfig.httpClient.execute(post);

        //获取响应结果
        String result = EntityUtils.toString(response.getEntity(), "utf-8");

        System.out.println("获取用户信息 result:" + result);

        JSONArray jsonArray = new JSONArray(result);

        return jsonArray;

    }


}

