package com.marty.beeconnect;

public
class UserReg {
    private String uid;
    private String name;
    private String email;
    private String userPass;

    public
    String getUid () {
        return uid;
    }

    public
    void setUid ( String uid ) {
        this.uid = uid;
    }

    public
    String getName () {
        return name;
    }

    public
    void setName ( String name ) {
        this.name = name;
    }

    public
    String getEmail () {
        return email;
    }

    public
    void setEmail ( String email ) {
        this.email = email;
    }

    public
    String getUserPass () {
        return userPass;
    }

    public
    void setUserPass ( String userPass ) {
        this.userPass = userPass;
    }

    public
    String getProfileUrl () {
        return profileUrl;
    }

    public
    void setProfileUrl ( String profileUrl ) {
        this.profileUrl = profileUrl;
    }

    public
    String getCoverUrl () {
        return coverUrl;
    }

    public
    void setCoverUrl ( String coverUrl ) {
        this.coverUrl = coverUrl;
    }

    public
    String getUserToken () {
        return userToken;
    }

    public
    void setUserToken ( String userToken ) {
        this.userToken = userToken;
    }

    private String profileUrl;
    private String coverUrl;
    private String userToken;

}
