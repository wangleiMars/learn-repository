package com.mars.multi.thread.learn.test.concurrent;

public class StringOver {
    private String name;
    private int hash;

    public StringOver() {
    }

    public StringOver(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        int result = 12122;
        return result;
    }
}
