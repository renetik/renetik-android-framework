package renetik.java.lang;

import java.io.IOException;
import java.util.Iterator;

import renetik.java.collections.CSIterator;
import renetik.java.collections.CSList;

import static renetik.android.lang.CSLang.NEWLINE;
import static renetik.android.lang.CSLang.list;
import static renetik.android.lang.CSLang.stringify;

public class CSText implements CSTextInterface {

    private final StringBuilder value = new StringBuilder();

    public CSText(CharSequence... strings) {
        add(strings);
    }

    public static String remove(String string, String toRemove) {
        return string.replace(toRemove, "");
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

    public CSTextInterface delete(int start, int length) {
        return cut(start, start + length);
    }

    public CSTextInterface deleteLast(int length) {
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
        deleteLast(string.length());
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

    public String toString() {
        return stringify(value);
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
            public Character getCurrent() {
                return charAt(index());
            }
        };
    }

    public CSText removeEnd(String end) {
        int start = value.lastIndexOf(end);
        if (start > -1) delete(start, end.length());
        return this;
    }
}
