package dev.ustits.htmx

import kotlinx.html.HTMLTag

const val HTMX_INDICATOR: String = "htmx-indicator"

var HTMLTag.hxGet: String
    get() {
        return attributes.getOrDefault("hx-get", "")
    }
    set(value) {
        attributes["hx-get"] = value
    }

var HTMLTag.hxTarget: String
    get() {
        return attributes.getOrDefault("hx-target", "")
    }
    set(value) {
        attributes["hx-target"] = value
    }

var HTMLTag.hxTrigger: String
    get() {
        return attributes.getOrDefault("hx-trigger", "")
    }
    set(value) {
        attributes["hx-trigger"] = value
    }

var HTMLTag.hxIndicator: String
    get() {
        return attributes.getOrDefault("hx-indicator", "")
    }
    set(value) {
        attributes["hx-indicator"] = value
    }
