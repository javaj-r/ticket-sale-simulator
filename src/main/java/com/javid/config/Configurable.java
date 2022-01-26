package com.javid.config;

/**
 * @author javid
 * Created on 1/4/2022
 */
public interface Configurable {
    Configuration CONFIG = ConfigurationImpl.getInstance();
}
