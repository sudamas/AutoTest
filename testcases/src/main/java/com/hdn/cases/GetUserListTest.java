package com.hdn.cases;

import com.hdn.config.TestConfig;
import com.hdn.model.GetUserInfoCase;
import com.hdn.model.GetUserListCase;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GetUserListTest {

    @Test(dependsOnGroups = "loginSuccess", description = "获取用户列表接口")
    public void getUserList() throws IOException, JSONException {
        SqlSession session = DataBaseUtil.getSqlSession();
        int getUserListCaseIndex = 1;

        GetUserListCase getUserListCase = session.selectOne("getUserListCase", getUserListCaseIndex);

        JSONArray resultJson = getJsonResult(getUserListCase);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //数据库中查询结果
        List<User> userList = session.selectList(getUserListCase.getExpected(), getUserListCase);

        Assert.assertEquals(userList.size(), resultJson.length());
        for (int i = 0; i < resultJson.length(); ++i) {
            User user = userList.get(i);
            JSONObject expected = new JSONObject();
            expected.put("id",user.getId());
            expected.put("userName",user.getUserName());
            expected.put("password",user.getPassword());
            expected.put("age",user.getAge());
            expected.put("sex",user.getSex());
            expected.put("permission",user.getPermission());
            expected.put("isDelete",user.getIsDelete());

            JSONObject expect = (JSONObject) resultJson.get(i);
            JSONObject actual = expected;
            Assert.assertEquals(expect.toString(), actual.toString());
        }

    }

    private JSONArray getJsonResult(GetUserListCase getUserListCase) throws IOException, JSONException {
        HttpPost post = new HttpPost(TestConfig.getUserListUrl);
        JSONObject param = new JSONObject();
        param.put("userName", getUserListCase.getUserName());
        param.put("sex", getUserListCase.getSex());
        param.put("age", getUserListCase.getAge());
        param.put("permission", getUserListCase.getPermission());
        param.put("isDelete", getUserListCase.getIsDelete());
        //设置请求头信息 设置header
        post.setHeader("content-type", "application/json");
        //将参数信息添加到方法中
        StringEntity entity = new StringEntity(param.toString(), "utf-8");
        post.setEntity(entity);

        System.out.println("添加用户参数："+param.toString());

        //设置cookies
        TestConfig.httpClient.setCookieStore(TestConfig.store);
        //声明一个对象来进行响应结果的存储
        String result;
        //执行post方法
        HttpResponse response = TestConfig.httpClient.execute(post);
        //获取响应结果
        result = EntityUtils.toString(response.getEntity(), "utf-8");

        System.out.println("调用接口list result:" + result);

        JSONArray jsonArray = new JSONArray(result);

        return jsonArray;

    }

}
