package cs.java.lang;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Iterator;

import cs.java.collections.CSIterator;
import cs.java.collections.CSList;

import static cs.java.lang.Lang.*;

public class TextImpl implements Text {

    private final StringBuilder value = new StringBuilder();

    public TextImpl(CharSequence... strings) {
        add(strings);
    }

    public Text add(CharSequence... strings) {
        for (CharSequence string : strings)
            value.append(string);
        return this;
    }

    public Text add(CharSequence string) {
        value.append(string);
        return this;
    }

    public Text add(Object... objects) {
        for (Object object : objects) add(object);
        return this;
    }

    public Text add(Object object) {
        value.append(String.valueOf(object));
        return this;
    }

    public Text addLine() {
        return add(NEWLINE);
    }

    public Text addSpace() {
        return add(" ");
    }

    public Text caseDown() {
        set(toString().toLowerCase());
        return this;
    }

    public Text caseUp(int index) {
        set(value.substring(0, index) + value.substring(index, index + 1).toUpperCase()
                + value.substring(index + 1, length()));
        return this;
    }

    public Text cut(int start, int end) {
        if (start >= length()) return this;
        if (end > length()) end = length();
        value.delete(start, end);
        return this;
    }

    public Text cutEnd(int length) {
        return cut(length() - length, length());
    }

    public Text leaveStart(int length) {
        return cut(length, length());
    }

    public Text cutStart(int length) {
        return cut(0, length);
    }

    public boolean isEmpty() {
        return value.length() == 0;
    }

    public Text remove(String... strings) {
        String text = toString();
        for (String string : strings)
            text = text.replaceAll(string, "");
        set(text);
        return this;
    }

    public Text replace(String regex, String replace) {
        set(toString().replaceAll(regex, replace));
        return this;
    }

    public Text replaceEnd(String string) {
        cutEnd(string.length());
        add(string);
        return this;
    }

    public CSList<Text> split(String regex) {
        CSList<Text> split = list();
        for (String string : toString().split(regex))
            split.add(new TextImpl(string));
        return split;
    }

    public Text trim() {
        set(toString().trim());
        return this;
    }

    public void set(CharSequence text) {
        clear();
        value.append(text);
    }

    @NonNull
    public String toString() {
        return string(value);
    }

    private void clear() {
        value.delete(0, length());
    }

    public int length() {
        return value.length();
    }

    public char charAt(int index) {
        return value.charAt(index);
    }

    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }

    public Appendable append(char c) throws IOException {
        return value.append(c);
    }

    public Appendable append(CharSequence csq) throws IOException {
        return value.append(csq);
    }

    public Appendable append(CharSequence csq, int start, int end) throws IOException {
        return value.append(csq, start, end);
    }

    public Iterator<Character> iterator() {
        return new CSIterator<Character>(length()) {
            public Character getValue() {
                return charAt(index());
            }
        };
    }
}
