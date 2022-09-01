package com.jun.jwt.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomBCryptPasswordENcoder extends BCryptPasswordEncoder {

}
