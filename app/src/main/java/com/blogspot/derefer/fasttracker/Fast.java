package com.blogspot.derefer.fasttracker;

public class Fast {
    private long id;
    private long begin;
    private long end;

    public Fast(long id, long begin, long end) {
        this.id = id;
        this.begin = begin;
        this.end = end;
    }

    // TODO: Add string conversion for begin and end
    public String returnFastAsString() {
        return id + ": " + begin + " - " + end;
    }
}
