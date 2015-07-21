package com.github.nthportal.version;

@FunctionalInterface
public interface VersionTypeParser<T extends Enum<T>> {
    T parse(String version) throws VersionParseException;
}
