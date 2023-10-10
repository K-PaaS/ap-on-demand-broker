package org.openpaas.ondemand.config;


import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.openpaas.ondemand.common.Common;
import org.openpaas.ondemand.common.ApConnectionContext;
import org.openpaas.ondemand.common.ApTokenContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class CloudFoundryConfigProvider {

    @Bean
    ApConnectionContext connectionContext(@Value("${cloudfoundry.cc.api.url}") String apiTarget, @Value("${cloudfoundry.cc.api.sslSkipValidation}") Boolean sslSkipValidation) {
        Common common = new Common();
        return new ApConnectionContext(common.defaultConnectionContextBuild(apiTarget, sslSkipValidation), new Date());
    }

    @Bean
    ApTokenContext tokenProvider(@Value("${cloudfoundry.user.admin.username}") String username, @Value("${cloudfoundry.user.admin.password}") String password) {
        return new ApTokenContext(PasswordGrantTokenProvider.builder().password(password).username(username).build(), new Date());
    }

    @Bean
    Common common(){
        return new Common();
    }
}
