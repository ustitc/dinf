package dev.ustits.htmx

import kotlinx.html.FlowContent

const val HTMX_INDICATOR: String = "htmx-indicator"

class HTMX(private val content: FlowContent) {

    fun hxGet(value: String) {
        content.attributes["hx-get"] = value
    }

    fun hxTarget(value: String) {
        content.attributes["hx-target"] = value
    }

    fun hxTrigger(value: String) {
        content.attributes["hx-trigger"] = value
    }

    fun hxSwap(value: HtmxSwap) {
        content.attributes["hx-swap"] = value.htmxName
    }

    fun hxIndicator(value: String) {
        content.attributes["hx-indicator"] = value
    }

}

fun FlowContent.htmx(block: HTMX.() -> Unit) {
    block(HTMX(this))
}
