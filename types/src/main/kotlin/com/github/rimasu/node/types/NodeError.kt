/**
 * Copyright 2017 Richard Sunderland
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package com.github.rimasu.node.types

import com.github.rimasu.text.Region

/**
 * A possible error when accessing a node.
 */
sealed class NodeError
{
    abstract val path: Path
    abstract val anchor: Region?
}

/**
 * Error returned when a undefined label is accessed.
 */
data class UndefinedValue(override val path: Path, override val anchor: Region?) : NodeError()

/**
 * Error returned when a value could not be coerced into the requested type.
 */
data class IncompatibleValue(override val path: Path, override val anchor: Region?) : NodeError()

/**
 * Error returned when a value could be coerced into expected numerical type, but its value was too low.
 */
data class InvalidLowValue(override val path: Path, override val anchor: Region?) : NodeError()

/**
 * Error returned when a value could be coerced into expected numerical type, but its value was too high.
 */
data class InvalidHighValue(override val path: Path, override val anchor: Region?) : NodeError()