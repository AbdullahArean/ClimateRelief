package com.arean.ClimateRelief.ui.activity;

public class ClaimerInfo {
    String userName, userDistrict, userFemaleCount, userSeniorCitizenCount, userNIDNo, userBkashContactNo;

    public ClaimerInfo()
    {

    }
    public ClaimerInfo(String userName, String userDistrict, String userFemaleCount, String userSeniorCitizenCount, String userNIDNo, String userBkashContactNo) {
        this.userName = userName;
        this.userDistrict = userDistrict;
        this.userFemaleCount = userFemaleCount;
        this.userSeniorCitizenCount = userSeniorCitizenCount;
        this.userNIDNo = userNIDNo;
        this.userBkashContactNo = userBkashContactNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDistrict() {
        return userDistrict;
    }

    public void setUserDistrict(String userDistrict) {
        this.userDistrict = userDistrict;
    }

    public String getUserFemaleCount() {
        return userFemaleCount;
    }

    public void setUserFemaleCount(String userFemaleCount) {
        this.userFemaleCount = userFemaleCount;
    }

    public String getUserSeniorCitizenCount() {
        return userSeniorCitizenCount;
    }

    public void setUserSeniorCitizenCount(String userSeniorCitizenCount) {
        this.userSeniorCitizenCount = userSeniorCitizenCount;
    }

    public String getUserNIDNo() {
        return userNIDNo;
    }

    public void setUserNIDNo(String userNIDNo) {
        this.userNIDNo = userNIDNo;
    }

    public String getUserBkashContactNo() {
        return userBkashContactNo;
    }

    public void setUserBkashContactNo(String userBkashContactNo) {
        this.userBkashContactNo = userBkashContactNo;
    }
}
