package com.github.nthportal.version;

public class VersionParseException extends IllegalArgumentException {
    VersionParseException(String versionString) {
        super("Invalid version string: " + versionString);
    }

    VersionParseException(String versionString, Throwable cause) {
        super(("Invalid version string: " + versionString), cause);
    }
}
