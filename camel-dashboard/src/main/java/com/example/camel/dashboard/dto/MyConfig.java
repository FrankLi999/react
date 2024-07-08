package com.example.camel.dashboard.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyConfig {
    private Map config = new HashMap<>();


    public MyConfig() {
    }

    public MyConfig(Map config) {
        this.config = config;
    }

    public Map getConfig() {
        return this.config;
    }

    public void setConfig(Map config) {
        this.config = config;
    }

    public MyConfig config(Map config) {
        setConfig(config);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MyConfig)) {
            return false;
        }
        MyConfig myConfig = (MyConfig) o;
        return Objects.equals(config, myConfig.config);
    }

    @Override
    public int hashCode() {
        return config.hashCode();
    }

    @Override
    public String toString() {
        return "{" +
            " config='" + getConfig() + "'" +
            "}";
    }
    
}
