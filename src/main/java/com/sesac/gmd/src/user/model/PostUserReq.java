package com.sesac.gmd.src.user.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUserReq {
    private String nickname;
    private String gender;
    private String age;
    private String email;
    private String state;
    private String city;
    private String pushId;
    private String oauthId;
}
