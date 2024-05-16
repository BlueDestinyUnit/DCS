package com.scd.dcs.results;

public interface Result<T extends Result<T>> {
    String name();
}
