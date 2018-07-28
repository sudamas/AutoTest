package com.hdn.test.cookies;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MyCookiesForGet {

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
    public void testGetCookies() {
        String result = null;
        String testUrl = url + bundle.getString("getCookies.uri");
        System.out.println(testUrl);
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(dependsOnMethods = "testGetCookies")
    public void testGetWithCookies() throws IOException {
        String result = null;
        String testUrl = url + bundle.getString("get.with.cookies");
        HttpGet get = new HttpGet(testUrl);

        DefaultHttpClient client = new DefaultHttpClient();
        client.setCookieStore(this.store);

        HttpResponse response = client.execute(get);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("statusCode = " + statusCode);
        if(statusCode==200){
            result = EntityUtils.toString(response.getEntity(), "utf-8");
            System.out.println(result);
        }





    }

}
