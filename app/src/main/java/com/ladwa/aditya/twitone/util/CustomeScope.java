package com.ladwa.aditya.twitone.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Aditya on 25-Jun-16.
 */
@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomeScope {
}
