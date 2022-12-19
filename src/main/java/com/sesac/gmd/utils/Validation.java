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
        if(postUserReq.getAccessToken().isBlank()) {
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
        if(postPinReq.getSongTitle().isBlank()) {
            return POST_PINS_EMPTY_TITLE;
        }
        if(postPinReq.getArtist().isBlank()) {
            return POST_PINS_EMPTY_SINGER;
        }
        if(postPinReq.getAlbumTitle().isBlank()) {
            return POST_PINS_EMPTY_ALBUM;
        }
        if(postPinReq.getAlbumImage().isBlank()) {
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

        // 위도 경도 범위 검사 후 리턴
        return locationValidation(postPinReq.getLatitude(), postPinReq.getLongitude());
    }

    /* 위도, 경도 범위 검사 */
    public static BaseResponseStatus locationValidation(double latitude, double longitude) {
        if(!(latitude < 90 && latitude > -90)) {  // 위도
            return POST_PINS_INVALID_LATITUDE;
        }
        if(!(longitude < 180 && longitude > -180)) {  // 경도
            return POST_PINS_INVALID_LONGITUDE;
        }

        return SUCCESS;
    }



}
