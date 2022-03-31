package com.mars.java.classload;

public class HotSwapClassLoader extends ClassLoader {

    public HotSwapClassLoader() {
    }

    public Class<?> loadClass(byte[] classByte) {
        return defineClass(null, classByte, 0, classByte.length);
    }
}
