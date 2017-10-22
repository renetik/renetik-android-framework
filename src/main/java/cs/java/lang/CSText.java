package cs.java.lang;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Iterator;

import cs.java.collections.CSIterator;
import cs.java.collections.CSList;

import static cs.java.lang.CSLang.*;

public class CSText implements CSTextInterface {

    private final StringBuilder value = new StringBuilder();

    public CSText(CharSequence... strings) {
        add(strings);
    }

    public CSTextInterface add(CharSequence... strings) {
        for (CharSequence string : strings)
            value.append(string);
        return this;
    }

    public CSTextInterface add(CharSequence string) {
        value.append(string);
        return this;
    }

    public CSTextInterface add(Object... objects) {
        for (Object object : objects) add(object);
        return this;
    }

    public CSTextInterface add(Object object) {
        value.append(String.valueOf(object));
        return this;
    }

    public CSTextInterface addLine() {
        return add(NEWLINE);
    }

    public CSTextInterface addSpace() {
        return add(" ");
    }

    public CSTextInterface caseDown() {
        set(toString().toLowerCase());
        return this;
    }

    public CSTextInterface caseUp(int index) {
        set(value.substring(0, index) + value.substring(index, index + 1).toUpperCase()
                + value.substring(index + 1, length()));
        return this;
    }

    public CSTextInterface cut(int start, int end) {
        if (start >= length()) return this;
        if (end > length()) end = length();
        value.delete(start, end);
        return this;
    }

    public CSTextInterface cutEnd(int length) {
        return cut(length() - length, length());
    }

    public CSTextInterface leaveStart(int length) {
        return cut(length, length());
    }

    public CSTextInterface cutStart(int length) {
        return cut(0, length);
    }

    public boolean isEmpty() {
        return value.length() == 0;
    }

    public CSTextInterface remove(String... strings) {
        String text = toString();
        for (String string : strings)
            text = text.replaceAll(string, "");
        set(text);
        return this;
    }

    public CSTextInterface replace(String regex, String replace) {
        set(toString().replaceAll(regex, replace));
        return this;
    }

    public CSTextInterface replaceEnd(String string) {
        cutEnd(string.length());
        add(string);
        return this;
    }

    public CSList<CSTextInterface> split(String regex) {
        CSList<CSTextInterface> split = list();
        for (String string : toString().split(regex))
            split.add(new CSText(string));
        return split;
    }

    public CSTextInterface trim() {
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
