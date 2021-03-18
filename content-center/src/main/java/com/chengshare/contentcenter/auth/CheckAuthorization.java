package com.chengshare.contentcenter.auth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//运行时期能使用反射找到
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAuthorization {

    String value();
}
