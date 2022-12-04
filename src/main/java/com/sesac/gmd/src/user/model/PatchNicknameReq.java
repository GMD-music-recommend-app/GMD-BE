package com.sesac.gmd.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatchNicknameReq {
    private int userIdx;
    private String nickname;
}
