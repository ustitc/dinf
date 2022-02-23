package io.ktor.server.html

import io.ktor.html.*

@Suppress("unused", "MemberVisibilityCanBePrivate")
class PlaceholderListExtended<TOuter, TInner> {
    private var items = ArrayList<PlaceholderItem<TInner>>()

    val size: Int
        get() {
            return items.size
        }

    operator fun invoke(meta: String = "", content: TInner.(Placeholder<TInner>) -> Unit = {}) {
        val placeholder = PlaceholderItem<TInner>(items.size, items)
        placeholder(meta, content)
        items.add(placeholder)
    }

    fun isEmpty(): Boolean = items.size == 0

    fun isNotEmpty(): Boolean = isEmpty().not()

    fun apply(destination: TOuter, render: TOuter.(PlaceholderItem<TInner>) -> Unit) {
        for (item in items) {
            destination.render(item)
        }
    }
}

fun <TOuter, TInner> TOuter.each(
    items: PlaceholderListExtended<TOuter, TInner>,
    itemTemplate: TOuter.(PlaceholderItem<TInner>) -> Unit
) {
    items.apply(this, itemTemplate)
}
