package com.sesac.gmd.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRes {
    private int userIdx;
    private String nickname;
    private char gender;
    private char age;
    private String email;
    private String location;
    private String pushId;
    private String oAuthId;
}
