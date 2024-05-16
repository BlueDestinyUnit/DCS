package com.scd.dcs.regexes;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserRegex {
    public static final Regex email = new Regex("^(?=.{5,50}$)([\\da-zA-Z_.]{4,20})@([\\da-z\\-]{1,20}\\.)?([\\da-z\\-]{1,20})\\.([a-z]{2,10})(\\.[a-z]{2})?$");
    public static final Regex password = new Regex("^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:\",<.>/?]{5,50})$");
}
