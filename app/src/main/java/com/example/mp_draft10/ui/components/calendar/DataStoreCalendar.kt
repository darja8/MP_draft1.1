package com.example.mp_draft10.ui.components.calendar

class DataStoreCalendar<V>(
    private val store: MutableMap<Int, V> = HashMap(),
    private val create: (offset: Int) -> V,
) : MutableMap<Int, V> by store {
    override fun get(key: Int): V {
        val value = store[key]
        return if (value == null) {
            val data = create(key)
            put(key, data)
            data
        } else {
            value
        }
    }
}
