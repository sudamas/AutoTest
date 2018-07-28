package com.hdn.utils;

import com.hdn.model.InterfaceName;

import java.util.Locale;
import java.util.ResourceBundle;

public class ConfigFile {

    private static ResourceBundle bundle =
            ResourceBundle.getBundle("application", Locale.CHINA);

    public static String getUrl(InterfaceName name) {
        String address = bundle.getString("test.url");
        String uri = "";
        String testUrl;

        switch (name) {
            case LOGIN:
                uri = bundle.getString("login.uri");
                break;
            case ADDUSER:
                uri = bundle.getString("addUser.uri");
                break;
            case GETUSERINFO:
                uri = bundle.getString("getUserInfo.uri");
                break;
            case GETUSERLIST:
                uri = bundle.getString("getUserList.uri");
                break;
            case UPDATEUSERINFO:
                uri = bundle.getString("updateUserInfo.uri");
                break;
            default:
                System.out.println("此接口测试不存在");
                break;
        }

        testUrl = address + uri;
        return testUrl;
    }
}
