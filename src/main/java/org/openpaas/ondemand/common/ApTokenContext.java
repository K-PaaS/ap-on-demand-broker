package org.openpaas.ondemand.common;

import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;

import java.util.Date;

public class ApTokenContext extends ApContextInterface {

    PasswordGrantTokenProvider tokenProvider;
    Date create_time;

    public ApTokenContext(PasswordGrantTokenProvider tokenProvider, Date create_time){
        this.tokenProvider = tokenProvider;
        this.create_time = create_time;
    }

    public PasswordGrantTokenProvider tokenProvider() {
        return tokenProvider;
    }

    @Override
    public Date getCreate_time() {
        return create_time;
    }
}
