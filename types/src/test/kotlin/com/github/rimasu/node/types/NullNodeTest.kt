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

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class NullNodeTest : NodeTest() {

    override val node = NullNode()

    @Nested
    inner class `when getting null node value as string`  : WhenGettingNodeAsString(node) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()

    }

    @Nested
    inner class `when getting null node value as int`  : WhenGettingNodeAsInt(node) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting null node value as struct`  : WhenGettingNodeAsStruct(node) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

    @Nested
    inner class `when getting null node value as list`  : WhenGettingNodeAsList(node) {
        @Test
        fun `then result is incompatible`() = assertIncompatibleValue()
    }

}

