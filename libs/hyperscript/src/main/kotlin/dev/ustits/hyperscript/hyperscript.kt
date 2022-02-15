package dev.ustits.hyperscript

import kotlinx.html.HTMLTag

var HTMLTag.hyperscript: String
    get() {
        return attributes.getOrDefault("_", "")
    }
    set(value) {
        attributes["_"] = value
    }
