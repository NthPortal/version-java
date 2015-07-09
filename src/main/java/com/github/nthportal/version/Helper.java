package com.github.nthportal.version;

class Helper {
    static void valueCheck(int... values) throws IllegalArgumentException {
        for (int value : values) {
            if (value < 0) {
                throw new IllegalArgumentException("Versions cannot have negative values");
            }
        }
    }

    static <T extends Enum<T>> void typeCheck(T type) throws IllegalArgumentException {
        if (type == null) {
            throw new IllegalArgumentException("Version type cannot be null");
        }
    }
}
