package cs.java.net;

import cs.java.collections.CSIteration;
import cs.java.collections.CSList;
import cs.java.collections.CSMap;
import cs.java.collections.CSMapped;
import cs.java.lang.KeyValue;
import cs.java.lang.Lang;
import cs.java.lang.Text;

import static cs.java.lang.Lang.*;

public class Url {

    private final CSList<KeyValue<String, String>> _arguments = list();
    private String _baseUrl;

    public Url(String url, String... arguments) {
        this(url);
        add(map(arguments));
    }

    public Url(String baseUrl) {
        _baseUrl = baseUrl;
    }

    public Url(String url, CSMap<String, String> arguments) {
        this(url);
        add(arguments);
    }

    public Url add(CSMap<String, String> arguments) {
        for (CSMapped<String, String> mapped : iterate(arguments))
            add(mapped.key(), mapped.value());
        return this;
    }

    public Url add(String argument, Object value) {
        _arguments.add(new KeyValue<>(argument, urlEncode(Lang.string(value))));
        return this;
    }

    public Url remove(String key) {
        CSIteration<KeyValue<String, String>> iteration = iterate(_arguments);
        for (KeyValue<String, String> argument : iteration)
            if (argument.key.equals(key)) iteration.remove();
        return this;
    }

    public String toString() {
        Text url = text(_baseUrl);
        url.add("?");
        for (KeyValue<String, String> argument : _arguments)
            url.add(argument.key, "=", argument.value, "&");
        url.cutEnd(1);
        return url.toString();
    }

    public void setURL(String url) {
        if (url.startsWith("http")) _baseUrl = url;
        else _baseUrl = _baseUrl + url;
    }
}
