package com.metropolitan.techsale.auth;

import com.metropolitan.techsale.utils.AbstractRetrofitService;

public class AuthServiceImpl extends AbstractRetrofitService {

    private AuthService authService;

    protected AuthServiceImpl() {
        super("http://10.0.2.2:8090/");
        this.authService = retrofit.create(AuthService.class);
    }

    public AuthService getAuthService() {
        return authService;
    }

}
