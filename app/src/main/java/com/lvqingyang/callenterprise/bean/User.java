package com.lvqingyang.callenterprise.bean;

/**
 * Author：LvQingYang
 * Date：2017/9/2
 * Email：biloba12345@gamil.com
 * Github：https://github.com/biloba123
 * Info：
 */
public class User {

    /**
     * EnterID : 1000009
     * EnterName : 文思海辉技术有限公司
     * CreditCode : null
     * CredentialsUrl : null
     * Balance : 0.0
     * Password : 123456
     * Email : admin@pactera.com
     * CorporateName : 何泽鹏
     * CorporateIdCard : 420922199610164632
     * CorporatePhone : 18696162662
     * RegisterTime : 2017-07-26T14:14:14
     */

    private int EnterID;
    private String EnterName;
    private Object CreditCode;
    private Object CredentialsUrl;
    private double Balance;
    private String Password;
    private String Email;
    private String CorporateName;
    private String CorporateIdCard;
    private String CorporatePhone;
    private String RegisterTime;

    public int getEnterID() {
        return EnterID;
    }

    public void setEnterID(int EnterID) {
        this.EnterID = EnterID;
    }

    public String getEnterName() {
        return EnterName;
    }

    public void setEnterName(String EnterName) {
        this.EnterName = EnterName;
    }

    public Object getCreditCode() {
        return CreditCode;
    }

    public void setCreditCode(Object CreditCode) {
        this.CreditCode = CreditCode;
    }

    public Object getCredentialsUrl() {
        return CredentialsUrl;
    }

    public void setCredentialsUrl(Object CredentialsUrl) {
        this.CredentialsUrl = CredentialsUrl;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double Balance) {
        this.Balance = Balance;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getCorporateName() {
        return CorporateName;
    }

    public void setCorporateName(String CorporateName) {
        this.CorporateName = CorporateName;
    }

    public String getCorporateIdCard() {
        return CorporateIdCard;
    }

    public void setCorporateIdCard(String CorporateIdCard) {
        this.CorporateIdCard = CorporateIdCard;
    }

    public String getCorporatePhone() {
        return CorporatePhone;
    }

    public void setCorporatePhone(String CorporatePhone) {
        this.CorporatePhone = CorporatePhone;
    }

    public String getRegisterTime() {
        return RegisterTime;
    }

    public void setRegisterTime(String RegisterTime) {
        this.RegisterTime = RegisterTime;
    }
}
