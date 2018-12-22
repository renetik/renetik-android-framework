package renetik.android.java.common

import java.io.Serializable
import java.io.Writer

class CSStringBuilderWriter : Writer, Serializable {

    private val builder: StringBuilder

    constructor() {
        this.builder = StringBuilder()
    }

    constructor(capacity: Int) {
        this.builder = StringBuilder(capacity)
    }

    constructor(builder: StringBuilder?) {
        this.builder = builder ?: StringBuilder()
    }

    override fun append(value: Char): Writer {
        builder.append(value)
        return this
    }

    override fun append(value: CharSequence?): Writer {
        builder.append(value)
        return this
    }

    override fun append(value: CharSequence?, start: Int, end: Int): Writer {
        builder.append(value, start, end)
        return this
    }

    override fun close() {}

    override fun flush() {}

    override fun write(value: String?) {
        if (value != null) builder.append(value)
    }

    override fun write(value: CharArray?, offset: Int, length: Int) {
        if (value != null) builder.append(value, offset, length)
    }

    override fun toString(): String {
        return builder.toString()
    }
}

