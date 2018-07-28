package com.hdn.cases;

import com.hdn.config.TestConfig;
import com.hdn.model.InterfaceName;
import com.hdn.model.LoginCase;
import com.hdn.utils.ConfigFile;
import com.hdn.utils.DataBaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginTest {

    @BeforeTest(groups = "loginSuccess", description = "测试准备，创建HttpClient")
    public void beforeTest() {
        TestConfig.httpClient = new DefaultHttpClient();
        TestConfig.loginUrl = ConfigFile.getUrl(InterfaceName.LOGIN);
        TestConfig.addUserUrl = ConfigFile.getUrl(InterfaceName.ADDUSER);
        TestConfig.getUserInfoUrl = ConfigFile.getUrl(InterfaceName.GETUSERINFO);
        TestConfig.getUserListUrl = ConfigFile.getUrl(InterfaceName.GETUSERLIST);
        TestConfig.updateUserInfoUrl = ConfigFile.getUrl(InterfaceName.UPDATEUSERINFO);
    }

    @Test(groups = "loginSuccess", description = "用户成功登录接口")
    public void loginSuccess() throws IOException, JSONException {
        int loginCaseIndex = 1;
        LoginCase loginCase = getLoginCaseFromDataBase(loginCaseIndex);

        //预期结果与实际结果对比
        String result = getResult(loginCase);
        Assert.assertEquals(loginCase.getExpected(), result);
    }

    @Test(groups = "loginFailed", description = "用户成功登录接口")
    public void loginFailed() throws IOException, JSONException {
        int loginCaseIndex = 2;
        LoginCase loginCase = getLoginCaseFromDataBase(loginCaseIndex);
        //预期结果与实际结果对比
        String result = getResult(loginCase);
        Assert.assertEquals(loginCase.getExpected(), result);
    }

    private LoginCase getLoginCaseFromDataBase(int loginCaseIndex) throws IOException {
        SqlSession session = DataBaseUtil.getSqlSession();
        LoginCase loginCase = session.selectOne("loginCase", loginCaseIndex);
        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginUrl);
        return loginCase;
    }

    private String getResult(LoginCase loginCase) throws IOException, JSONException {

        // 创建post请求
        HttpPost post = new HttpPost(TestConfig.loginUrl);

        //设置请求头信息 json格式
        post.setHeader("content-type", "application/json");
        //设置参数
        JSONObject params = new JSONObject();
        params.put("userName", loginCase.getUserName());
        params.put("password", loginCase.getPassword());
        StringEntity entity = new StringEntity(params.toString(), "utf-8");
        post.setEntity(entity);

        //执行post方法，获取结果
        HttpResponse response = TestConfig.httpClient.execute(post);
        String result = EntityUtils.toString(response.getEntity(), "utf-8");

        System.out.println(result);
        //添加cookie信息
        TestConfig.store = TestConfig.httpClient.getCookieStore();

        return result;


    }


}
