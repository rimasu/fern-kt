package com.github.rimasu.text

/**
 * A position in a document.
 */
data class Position(
        /** line containing the position, must be greater than zero. */
        val line: Int,
        /** location of the position within thi line, must be greater than zero. */
        var column: Int
) {
    init {
        require(line > 0) { "A text position's line must be greater than zero"}
        require(column > 0) { "A text position's column must be greater than zero"}
    }

    override fun toString() = "$line,$column"
}