package org.openpaas.ondemand.common;

import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.openpaas.ondemand.config.TokenGrantTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Calendar;
import java.util.Date;

public class Common {

    @Value("${cloudfoundry.cc.api.url}")
    public String apiTarget;

    @Value("${cloudfoundry.cc.api.uaaUrl}")
    public String uaaTarget;

    @Value("${cloudfoundry.cc.api.sslSkipValidation}")
    public boolean cfskipSSLValidation;

    @Value("${cloudfoundry.user.admin.username}")
    public String adminUserName;

    @Value("${cloudfoundry.user.admin.password}")
    public String adminPassword;

    @Autowired
    ApConnectionContext apConnectionContext;

    @Autowired
    ApTokenContext apTokenContext;


    public ReactorCloudFoundryClient cloudFoundryClient() {
        return ReactorCloudFoundryClient.builder().connectionContext(connectionContext()).tokenProvider(tokenProvider()).build();
    }


    /**
     * DefaultConnectionContext 가져온다.
     *
     * @return DefaultConnectionContext
     */
    public DefaultConnectionContext connectionContext() {
        if (apConnectionContext == null) {
            apConnectionContext = new ApConnectionContext(defaultConnectionContextBuild(apiTarget, cfskipSSLValidation), new Date());
        } else {
            if (apConnectionContext.getCreate_time() != null) {
                if(ContextAndTokenTimeOut(apConnectionContext, 10)) {
                    apConnectionContext.getConnectionContext().dispose();
                    apConnectionContext = null;
                    apConnectionContext = new ApConnectionContext(defaultConnectionContextBuild(apiTarget, cfskipSSLValidation), new Date());
                }
            } else {
                apConnectionContext = null;
                apConnectionContext = new ApConnectionContext(defaultConnectionContextBuild(apiTarget, cfskipSSLValidation), new Date());
            }
        }
        return apConnectionContext.getConnectionContext();
    }

    public DefaultConnectionContext defaultConnectionContextBuild(String cfApiUrl, boolean cfskipSSLValidation) {
        return DefaultConnectionContext.builder().apiHost(cfApiUrl.replace("https://", "").replace("http://", "")).skipSslValidation(cfskipSSLValidation).keepAlive(true).build();
    }

    /**
     * TokenGrantTokenProvider 생성하여, 반환한다.
     *
     * @param token
     * @return DefaultConnectionContext
     * @throws Exception
     */
    public TokenProvider tokenProvider(String token) {

        if (token.indexOf("bearer") < 0) {
            token = "bearer " + token;
        }
        TokenGrantTokenProvider tokenProvider = new TokenGrantTokenProvider(token);
        return tokenProvider;

    }

    public PasswordGrantTokenProvider tokenProvider() {
        if (apTokenContext == null) {
            apTokenContext = new ApTokenContext(PasswordGrantTokenProvider.builder().password(adminPassword).username(adminUserName).build(), new Date());
        } else if(apTokenContext.getCreate_time() != null && ContextAndTokenTimeOut(apTokenContext, 5)){
            apTokenContext = new ApTokenContext(PasswordGrantTokenProvider.builder().password(adminPassword).username(adminUserName).build(), new Date());
        }
        return apTokenContext.tokenProvider();
    }

    public boolean ContextAndTokenTimeOut(ApContextInterface apContextInterface, int timelimit){
        Calendar now = Calendar.getInstance();
        Calendar create_time = Calendar.getInstance();
        create_time.setTime(apContextInterface.getCreate_time());
        create_time.add(Calendar.MINUTE, timelimit);
        return create_time.getTimeInMillis() < now.getTimeInMillis();
    }
}
