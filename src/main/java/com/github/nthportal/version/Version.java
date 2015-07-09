package com.github.nthportal.version;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Version<T extends Enum<T>> implements Comparable<Version<T>> {
    public final int major;
    public final int minor;
    public final int patch;
    public final T type;
    private final boolean defaultType;

    Version(int major, int minor, int patch, T type, boolean defaultType) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.type = type;
        this.defaultType = defaultType;
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch +
                (defaultType ? "" : ("-" + type.name()) );
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
    public int compareTo(Version o) {
        return (major != o.major) ? Integer.compare(major, o.major)
                : ((minor != o.minor) ? Integer.compare(minor, o.minor)
                : Integer.compare(patch, o.patch)   );
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

