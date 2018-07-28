package com.hdn.cases;

import com.hdn.config.TestConfig;
import com.hdn.model.UpdateUserInfoCase;
import com.hdn.model.User;
import com.hdn.utils.DataBaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class UpdateUserInfoTest {

    @Test(dependsOnGroups = "loginSuccess", description = "更新用户信息")
    public void updateUserInfo() throws IOException, InterruptedException, JSONException {
        SqlSession session = DataBaseUtil.getSqlSession();
        int updateUserInfoCaseIndex = 1;
        UpdateUserInfoCase updateUserInfoCase = session.selectOne("updateUserInfoCase", updateUserInfoCaseIndex);
        System.out.println(updateUserInfoCase.toString());
        System.out.println(TestConfig.updateUserInfoUrl);

        int result = getResult(updateUserInfoCase);

        TimeUnit.SECONDS.sleep(2);

        User user = session.selectOne(updateUserInfoCase.getExpected(), updateUserInfoCase);
        System.out.println(user.toString());
        //String actualResult = new JSONObject(user).toString();

        Assert.assertNotNull(result);
        Assert.assertNotNull(user);
    }

    private int getResult(UpdateUserInfoCase updateUserInfoCase) throws IOException, JSONException {
        HttpPost post = new HttpPost(TestConfig.updateUserInfoUrl);
        JSONObject param = new JSONObject();
        param.put("id", updateUserInfoCase.getUserId());
        param.put("userName", updateUserInfoCase.getUserName());
        param.put("password", updateUserInfoCase.getPassword());
        param.put("sex", updateUserInfoCase.getSex());
        param.put("age", updateUserInfoCase.getAge());
        param.put("permission", updateUserInfoCase.getPermission());
        param.put("isDelete", updateUserInfoCase.getIsDelete());
        //设置请求头信息 设置header
        post.setHeader("content-type", "application/json");
        //将参数信息添加到方法中
        StringEntity entity = new StringEntity(param.toString(), "utf-8");
        post.setEntity(entity);
        //设置cookies
        TestConfig.httpClient.setCookieStore(TestConfig.store);

        System.out.println("更新、删除用户参数记录：" + param.toString());
        //声明一个对象来进行响应结果的存储
        String result;
        //执行post方法
        HttpResponse response = TestConfig.httpClient.execute(post);
        //获取响应结果
        result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);
        return Integer.parseInt(result);

    }


    private int getResult1(UpdateUserInfoCase updateUserInfoCase) throws IOException, JSONException {
        HttpPost post = new HttpPost(TestConfig.updateUserInfoUrl);
        post.setHeader("content-type", "application/json");

        JSONObject params = new JSONObject();
        params.put("userId", updateUserInfoCase.getUserId());
        params.put("userName", updateUserInfoCase.getUserName());
        params.put("password", updateUserInfoCase.getPassword());
        params.put("sex", updateUserInfoCase.getSex());
        params.put("age", updateUserInfoCase.getAge());
        params.put("permission", updateUserInfoCase.getPermission());
        params.put("isDelete", updateUserInfoCase.getIsDelete());

        StringEntity entity = new StringEntity(params.toString(), "utf-8");
        post.setEntity(entity);

        //添加cookie
        TestConfig.httpClient.setCookieStore(TestConfig.store);

        HttpResponse response = TestConfig.httpClient.execute(post);

        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println("更新用户信息结果：" + result);
        return Integer.valueOf(result);
    }

    @Test(dependsOnGroups = "loginSuccess", description = "更新用户信息")
    public void deleteUser() throws IOException, JSONException {
        SqlSession session = DataBaseUtil.getSqlSession();
        int updateUserInfoCaseIndex = 2;
        UpdateUserInfoCase updateUserInfoCase = session.selectOne("updateUserInfoCase", updateUserInfoCaseIndex);

        int result = getResult(updateUserInfoCase);
        // 上下之间是有不同线程执行，因此需要间隔时间
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        User user = session.selectOne(updateUserInfoCase.getExpected(), updateUserInfoCase);
        System.out.println(user.toString());
        //String actualResult = new JSONObject(user).toString();

        Assert.assertNotNull(result);
        Assert.assertNotNull(user);
    }

}
