package com.github.rimasu.text

/**
 * A region of a document.
 */
data class Region(
        val start: Position,
        val end: Position
) {

    override fun toString() = when {
            start == end -> start.toString()
            start.line == end.line -> "$start->${end.column}"
            else -> "$start->$end"
    }
}