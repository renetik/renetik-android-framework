package renetik.android.java.lang;

import java.io.Serializable;
import java.io.Writer;

/**
 * Created by Rene Dohan on 07/05/15.
 */
public class CSStringBuilderWriter extends Writer implements Serializable {

    private final StringBuilder builder;

    public CSStringBuilderWriter() {
        this.builder = new StringBuilder();
    }

    public CSStringBuilderWriter(int capacity) {
        this.builder = new StringBuilder(capacity);
    }

    public CSStringBuilderWriter(StringBuilder builder) {
        this.builder = (builder != null ? builder : new StringBuilder());
    }

    public Writer append(char value) {
        builder.append(value);
        return this;
    }

    public Writer append(CharSequence value) {
        builder.append(value);
        return this;
    }

    public Writer append(CharSequence value, int start, int end) {
        builder.append(value, start, end);
        return this;
    }

    public void close() {
    }

    public void flush() {
    }

    public void write(String value) {
        if (value != null) builder.append(value);
    }

    public void write(char[] value, int offset, int length) {
        if (value != null) builder.append(value, offset, length);
    }

    public StringBuilder getBuilder() {
        return builder;
    }

    public String toString() {
        return builder.toString();
    }
}

