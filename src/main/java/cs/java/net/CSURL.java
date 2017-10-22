package cs.java.net;

import java.util.Map;

import cs.java.collections.CSIteration;
import cs.java.collections.CSList;
import cs.java.collections.CSMapped;
import cs.java.lang.CSKeyValue;
import cs.java.lang.CSTextInterface;
import cs.java.lang.CSLang;

import static cs.java.lang.CSLang.iterate;
import static cs.java.lang.CSLang.list;
import static cs.java.lang.CSLang.map;
import static cs.java.lang.CSLang.text;
import static cs.java.lang.CSLang.urlEncode;

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
        _arguments.add(new CSKeyValue<>(argument, urlEncode(CSLang.string(value))));
        return this;
    }

    public CSURL remove(String key) {
        CSIteration<CSKeyValue<String, String>> iteration = iterate(_arguments);
        for (CSKeyValue<String, String> argument : iteration)
            if (argument.key.equals(key)) iteration.remove();
        return this;
    }

    public String toString() {
        CSTextInterface url = text(_baseUrl);
        url.add("?");
        for (CSKeyValue<String, String> argument : _arguments)
            url.add(argument.key, "=", argument.value, "&");
        url.cutEnd(1);
        return url.toString();
    }

    public void setURL(String url) {
        if (url.startsWith("http")) _baseUrl = url;
        else _baseUrl = _baseUrl + url;
    }
}
