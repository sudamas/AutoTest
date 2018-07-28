package com.hdn.test.cookies;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MyCookiesForPost {

    private String url;
    private ResourceBundle bundle;
    private CookieStore store;

    @BeforeTest
    public void beforeTest() {
        //读取配置文件
        bundle = ResourceBundle.getBundle("application", Locale.CHINA);
        url = bundle.getString("test.url");
    }


    @Test
    public void testGetCookies() throws IOException {
        String result = null;
        String testUrl = url + bundle.getString("getCookies.uri");
        System.out.println(testUrl);

        HttpGet get = new HttpGet(testUrl);
        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(get);
        result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);
        this.store = client.getCookieStore();
        List<Cookie> cookies = store.getCookies();
        System.out.println(cookies.size());
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            String value = cookie.getValue();
            System.out.println(name + ":" + value);
        }

    }

    @Test(dependsOnMethods = "testGetCookies")
    public void testPostWithCookies() throws IOException {
        String result = null;
        String testUrl = url + bundle.getString("get.post.with.cookies");
        //声明client
        DefaultHttpClient client = new DefaultHttpClient();
        //声明方法
        HttpPost post = new HttpPost(testUrl);

        //设置请求头信息====可以将公共的头信息整合到一个方法中
        post.setHeader("content-type","application/json");

        //添加参数，并添加到方法中
        JSONObject params = new JSONObject();
        params.put("name","huhansan");
        params.put("age","18");
        StringEntity entity = new StringEntity(params.toString(), "utf-8");
        post.setEntity(entity);

        //设置cookie 信息
        client.setCookieStore(this.store);

        //执行post方法
        HttpResponse response = client.execute(post);
        //返回结果，处理结果，CAS
        result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);
        JSONObject resultJson = new JSONObject(result);
        String success = (String)resultJson.get("huhansan");
        String statusCode = (String)resultJson.get("status");
        Assert.assertEquals("success",success);
        Assert.assertEquals("1",statusCode);

    }

}
