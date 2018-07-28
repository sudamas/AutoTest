package com.hdn.config;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;

public class TestConfig {

    //登录接口
    public static String loginUrl;
    //添加用户接口uri
    public static String addUserUrl;
    //获取用户信息接口uri
    public static String getUserInfoUrl;
    //获取用户列表接口uri
    public static String getUserListUrl;
    //更新用户信息接口uri
    public static String updateUserInfoUrl;

    //存储cookies信息
    public static DefaultHttpClient httpClient;
    //http客户端
    public static CookieStore store;
}
