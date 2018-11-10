package renetik.android.java.net;

import java.util.Map;

import renetik.android.java.collections.CSIteration;
import renetik.android.java.collections.CSList;
import renetik.android.java.collections.CSMapped;
import renetik.android.java.lang.CSKeyValue;
import renetik.android.java.lang.CSTextInterface;
import renetik.android.lang.CSLang;

import static renetik.android.lang.CSLang.iterate;
import static renetik.android.lang.CSLang.list;
import static renetik.android.lang.CSLang.map;
import static renetik.android.lang.CSLang.textBuilder;
import static renetik.android.lang.CSLang.urlEncode;

public class CSURL {

    private final CSList<CSKeyValue<String, String>> _arguments = list();
    private String _baseUrl;

    public CSURL(String url, String... arguments) {
        this(url);
        add(map(arguments));
    }

    public CSURL(String baseUrl) {
        _baseUrl = baseUrl;
    }

    public CSURL(String url, Map<String, String> arguments) {
        this(url);
        add(arguments);
    }

    public CSURL add(Map<String, String> arguments) {
        for (CSMapped<String, String> mapped : iterate(arguments))
            add(mapped.key(), mapped.value());
        return this;
    }

    public CSURL add(String argument, Object value) {
        _arguments.add(new CSKeyValue<>(argument, urlEncode(CSLang.stringify(value))));
        return this;
    }

    public CSURL remove(String key) {
        CSIteration<CSKeyValue<String, String>> iteration = iterate(_arguments);
        for (CSKeyValue<String, String> argument : iteration)
            if (argument.key.equals(key)) iteration.remove();
        return this;
    }

    public String toString() {
        CSTextInterface url = textBuilder(_baseUrl);
        url.add("?");
        for (CSKeyValue<String, String> argument : _arguments)
            url.add(argument.key, "=", argument.value, "&");
        url.deleteLast(1);
        return url.toString();
    }

    public void setURL(String url) {
        if (url.startsWith("http")) _baseUrl = url;
        else _baseUrl = _baseUrl + url;
    }
}
