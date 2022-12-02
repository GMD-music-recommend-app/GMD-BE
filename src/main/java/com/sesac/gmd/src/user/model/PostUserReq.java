package com.sesac.gmd.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUserReq {
    private String nickname;
    private String gender;
    private String age;
    private String email;
    private String location;
    private String pushId;
    private String oauthId;
}