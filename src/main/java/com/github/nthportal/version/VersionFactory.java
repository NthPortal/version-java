/*

Significant portions of this file were adapted from OTB Project,
which is licensed under the following license:



The MIT License (MIT)

Copyright (c) 2015 OTBProject
<http://otbproject.github.io>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.


 */

package com.github.nthportal.version;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class VersionFactory<T extends Enum<T>> {
    private final Class<T> tClass;
    private final T defaultType;

    private VersionFactory(Class<T> tClass) {
        this(tClass, Collections.max(Arrays.asList(tClass.getEnumConstants())));
    }

    private VersionFactory(Class<T> tClass, T defaultType) {
        this.tClass = tClass;
        this.defaultType = defaultType;
    }

    public static <T extends Enum<T>> VersionFactory<T> ofEnum(Class<T> tClass) {
        return new VersionFactory<>(tClass);
    }

    public static <T extends Enum<T>> VersionFactory<T> withDefault(Class<T> tClass, T defaultType) {
        return new VersionFactory<>(tClass, defaultType);
    }

    public static VersionFactory<MavenVersion> mavenVersionFactory() {
        return new VersionFactory<>(MavenVersion.class);
    }

    public Version<T> create(int major, int minor, int patch, T type) throws IllegalArgumentException {
        Helper.valueCheck(major, minor, patch);
        Helper.typeCheck(type);
        return new Version<>(major, minor, patch, type, (type == defaultType));
    }

    public Version<T> parseVersion(String versionString) throws VersionParseException {
        if (versionString == null) {
            throw new VersionParseException(null);
        }

        String[] split = versionString.split("\\.", 3);
        if (split.length != 3) {
            throw new VersionParseException(versionString);
        }

        try {
            int major = Integer.parseInt(split[0]);
            T type;

            int minor = Integer.parseInt(split[1]);
            split = split[2].split("-", 2);
            int patch = Integer.parseInt(split[0]);

            if (split.length == 1) {
                type = defaultType;
            } else {
                type = Enum.valueOf(tClass, split[1].toUpperCase());
            }

            if ((major < 0) || (minor < 0) || (patch < 0)) {
                throw new VersionParseException(versionString);
            }

            return new Version<>(major, minor, patch, type, (type == defaultType));
        } catch (IllegalArgumentException e) {
            throw new VersionParseException(versionString, e);
        }
    }

    public Optional<Version<T>> parseAsOptional(String versionString) {
        try {
            return Optional.of(parseVersion(versionString));
        } catch (VersionParseException ignored) {
            return Optional.empty();
        }
    }
}
