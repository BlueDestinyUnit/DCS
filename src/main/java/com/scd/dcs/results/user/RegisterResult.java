package com.scd.dcs.results.user;

import com.scd.dcs.results.Result;

public enum RegisterResult implements Result<RegisterResult> {
    FAILURE_DUPLICATE_EMAIL,
    FAILURE_DUPLICATE_NICKNAME
}
