package com.hdn.cases;

import com.hdn.config.TestConfig;
import com.hdn.model.AddUserCase;
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

public class AddUserTest {

    @Test(dependsOnGroups = "loginSuccess", description = "添加用户")
    public void addUser() throws IOException, InterruptedException, JSONException {
        int addUserCaseIndex = 1;
        SqlSession session = DataBaseUtil.getSqlSession();
        AddUserCase addUserCase = session.selectOne("addUserCase", addUserCaseIndex);
        System.out.println(addUserCase.toString());
        System.out.println(TestConfig.addUserUrl);
        String result = getResult(addUserCase);

        TimeUnit.SECONDS.sleep(5);

        //查看数据库中是否添加用户成功
        User user = session.selectOne("addUser", addUserCase);
        System.out.println(user.toString());

        Assert.assertEquals(addUserCase.getExpected(), result);

    }

    private String getResult(AddUserCase addUserCase) throws IOException, JSONException {

        HttpPost post = new HttpPost(TestConfig.addUserUrl);
        post.setHeader("content-type", "application/json");

        JSONObject params = new JSONObject();
        params.put("userName", addUserCase.getUserName());
        params.put("password", addUserCase.getPassword());
        params.put("age", addUserCase.getAge());
        params.put("sex", addUserCase.getSex());
        params.put("permission", addUserCase.getPermission());
        params.put("isDelete", addUserCase.getIsDelete());
        StringEntity entity = new StringEntity(params.toString(), "utf-8");
        post.setEntity(entity);

        System.out.println("添加用户参数："+params.toString());

        //添加cookie
        TestConfig.httpClient.setCookieStore(TestConfig.store);

        HttpResponse response = TestConfig.httpClient.execute(post);

        String result = EntityUtils.toString(response.getEntity());

        System.out.println(result);
        return result;


    }


}
