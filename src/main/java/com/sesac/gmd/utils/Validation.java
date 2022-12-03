package com.sesac.gmd.utils;

import com.sesac.gmd.config.BaseResponseStatus;
import com.sesac.gmd.src.user.model.PostUserReq;

import java.util.Objects;

import static com.sesac.gmd.config.BaseResponseStatus.*;

public class Validation {
    /* 회원 가입 시 비어있는 값이 있는지 확인 */
    public static BaseResponseStatus userValidation(PostUserReq postUserReq) {
        if(postUserReq.getNickname().isBlank()) {
            return POST_USERS_EMPTY_NICKNAME;
        }
        if(postUserReq.getGender().isBlank()) {
            return POST_USERS_EMPTY_GENDER;
        }
        if(postUserReq.getAge().isBlank()) {
            return POST_USERS_EMPTY_GENDER;
        }
        if(postUserReq.getEmail().isBlank()) {
            return POST_USERS_EMPTY_EMAIL;
        }
        if(postUserReq.getPushId().isBlank()) {
            return POST_USERS_EMPTY_PUSH;
        }
        if(postUserReq.getOauthId().isBlank()) {
            return POST_USERS_EMPTY_OAUTH;
        }

        // 성별을 제대로 적었는지 확인
        if(!(Objects.equals(postUserReq.getGender(), "M") || Objects.equals(postUserReq.getGender(), "F"))) {
            return POST_USERS_INVAlID_GENDER;
        }

        // 나이 범위가 맞는지 확인... 이건 어케 하지?
        // 추후에 회의 후 진행

        return SUCCESS;
    }
}
