package com.bpwizard.configjdbc.core.audit;

import java.io.Serializable;

@FunctionalInterface
public interface IdConverter<ID extends Serializable> {

    ID toId(String id);
}
