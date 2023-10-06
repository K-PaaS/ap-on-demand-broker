package org.openpaas.ondemand.exception;


import org.openpaas.servicebroker.exception.ServiceBrokerException;

public class OndemandServiceException extends ServiceBrokerException {
    public OndemandServiceException(String message) {
        super(message);
    }
}
