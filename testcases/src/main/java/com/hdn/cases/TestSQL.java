package com.hdn.cases;

import com.hdn.model.AddUserCase;
import com.hdn.model.UpdateUserInfoCase;
import com.hdn.model.User;
import com.hdn.utils.DataBaseUtil;
import org.apache.ibatis.session.SqlSession;
import org.testng.Assert;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TestSQL {

    public static void main(String[] args) throws IOException {
//        addUser();
        deleteUser();
    }

    public static void addUser()throws IOException{
        SqlSession session = DataBaseUtil.getSqlSession();
        AddUserCase addUserCase = session.selectOne("addUserCase", 1);
        User user = session.selectOne("addUser", addUserCase);
        System.out.println(user);
    }

    public static void deleteUser()throws IOException{

        SqlSession session = DataBaseUtil.getSqlSession();
        int updateUserInfoCaseIndex = 2;

        UpdateUserInfoCase updateUserInfoCase = session.selectOne("updateUserInfoCase", updateUserInfoCaseIndex);
        System.out.println(updateUserInfoCase.toString());
        User user = session.selectOne(updateUserInfoCase.getExpected(), updateUserInfoCase);
        System.out.println(user.toString());


    }
}
