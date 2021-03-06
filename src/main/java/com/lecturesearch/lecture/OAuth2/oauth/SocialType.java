package com.lecturesearch.lecture.OAuth2.oauth;

public enum SocialType {
    FACEBOOK("facebook"),
    GOOGLE("google"),
    GITHUB("github");

    private final String ROLE_PREFIX = "ROLE_";
    private String name;

    SocialType(String name) {
        this.name = name;
    }

    public String getRoleType() {
        return ROLE_PREFIX + name.toUpperCase();
    }

    public String getValue() {
        return name;
    }

    public boolean isEquals(String authority) {
//        return this.getRoleType().equals(authority);
        return this.name.equals(authority);
    }
}
