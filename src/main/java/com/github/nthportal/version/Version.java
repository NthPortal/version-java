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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Comparator;

public class Version<T extends Enum<T>> implements Comparable<Version<T>> {
    public final int major;
    public final int minor;
    public final int patch;
    public final T type;
    private final boolean isDefaultType;
    private final Comparator<T> comparator;

    Version(int major, int minor, int patch, T type, boolean isDefaultType, Comparator<T> comparator) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.type = type;
        this.isDefaultType = isDefaultType;
        this.comparator = comparator;
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch +
                (isDefaultType ? "" : ("-" + type.toString()));
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(major)
                .append(minor)
                .append(patch)
                .append(type)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Version)) {
            return false;
        }
        Version rhs = (Version) obj;
        return new EqualsBuilder()
                .append(major, rhs.major)
                .append(minor, rhs.minor)
                .append(patch, rhs.patch)
                .append(type, rhs.type)
                .isEquals();
    }

    @Override
    public int compareTo(Version<T> o) {
        return  (major != o.major)    ? Integer.compare(major, o.major)
                : ((minor != o.minor) ? Integer.compare(minor, o.minor)
                : ((patch != o.patch) ? Integer.compare(patch, o.patch)
                : comparator.compare(type, o.type)                   ));
    }

    public Checker<T> checker() {
        return new Checker<>(this);
    }

    public static class Checker<T extends Enum<T>> {
        private final Version<T> version;
        private int major = -1;
        private int minor = -1;
        private int patch = -1;
        private T type = null;

        private Checker(Version<T> version) {
            this.version = version;
        }

        public Checker major(int value) throws IllegalArgumentException {
            Helper.valueCheck(value);
            major = value;
            return this;
        }

        public Checker minor(int value) throws IllegalArgumentException {
            Helper.valueCheck(value);
            minor = value;
            return this;
        }

        public Checker patch(int value) throws IllegalArgumentException {
            Helper.valueCheck(value);
            patch = value;
            return this;
        }

        public Checker type(T type) {
            Helper.typeCheck(type);
            this.type = type;
            return this;
        }

        public boolean isVersion() {
            return  (   (major < 0) || (major == version.major)     ) &&
                    (   (minor < 0) || (minor == version.minor)     ) &&
                    (   (patch < 0) || (patch == version.patch)     ) &&
                    (   (type == null) || (type == version.type)    );
        }
    }
}

