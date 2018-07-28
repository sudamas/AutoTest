package com.hdn.model;

import lombok.Data;

@Data
public class GetUserListCase {
    private String userName;
    private String age;
    private String sex;
    private String permission;
    private String isDelete;
    private String expected;
}
