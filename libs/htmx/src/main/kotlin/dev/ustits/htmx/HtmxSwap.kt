package dev.ustits.htmx

enum class HtmxSwap(val htmxName: String) {
    INNER_HTML("innerHtml"),
    OUTER_HTML("outerHTML"),
    AFTER_BEGIN("afterbegin"),
    BEFORE_BEGIN("beforebegin"),
    BEFORE_END("beforeend"),
    AFTER_END("afterend"),
    NONE("none")
}