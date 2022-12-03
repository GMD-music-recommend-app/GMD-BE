package com.sesac.gmd.utils;

import com.sesac.gmd.config.BaseResponseStatus;
import com.sesac.gmd.src.song.model.PostPinReq;
import com.sesac.gmd.src.user.model.PostUserReq;

import java.util.Objects;

import static com.sesac.gmd.config.BaseResponseStatus.*;

public class Validation {
    /* 회원 가입 시 비어있는 값이 있는지 확인 */
    public static BaseResponseStatus userValidation(PostUserReq postUserReq) {
        // 빈 값 확인
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

    /* PIN 생성 시 비어있는 값이 있는지 확인 */
    public static BaseResponseStatus pinValidation(PostPinReq postPinReq) {
        // 빈 값 확인
        if(postPinReq.getTitle().isBlank()) {
            return POST_PINS_EMPTY_TITLE;
        }
        if(postPinReq.getSinger().isBlank()) {
            return POST_PINS_EMPTY_SINGER;
        }
        if(postPinReq.getAlbum().isBlank()) {
            return POST_PINS_EMPTY_ALBUM;
        }
        if(postPinReq.getAlbumCover().isBlank()) {
            return POST_PINS_EMPTY_ALBUM_COVER;
        }
        if(postPinReq.getReason().isBlank()) {
            return POST_PINS_EMPTY_REASON;
        }
        if(postPinReq.getState().isBlank()) {
            return POST_PINS_EMPTY_STATE;
        }
        if(postPinReq.getCity().isBlank()) {
            return POST_PINS_EMPTY_CITY;
        }
        if(postPinReq.getStreet().isBlank()) {
            return POST_PINS_EMPTY_STREET;
        }

        // 위도, 경도 범위 검사
        if(!(postPinReq.getLatitude() < 180 && postPinReq.getLatitude() > -180)) {
            return POST_PINS_INVALID_LATITUDE;
        }
        if(!(postPinReq.getLongitude() < 90 && postPinReq.getLongitude() > -90)) {
            return POST_PINS_INVALID_LONGITUDE;
        }

        return SUCCESS;
    }
}
