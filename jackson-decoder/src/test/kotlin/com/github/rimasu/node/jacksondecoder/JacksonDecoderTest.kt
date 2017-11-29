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
package com.github.rimasu.node.jacksondecoder

import com.github.michaelbull.result.Err
import com.github.rimasu.node.types.*
import com.github.rimasu.text.Position
import com.winterbe.expekt.should
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class JacksonDecoderTest {

    @Nested
    inner class `when json text is empty structure` : SuccessfulParse(
            jsonText = """{}""",
            expectedNode = StructNode(emptyMap())
    )

    @Nested
    inner class `when json text is empty array` : SuccessfulParse(
            jsonText = """[]""",
            expectedNode = ListNode(emptyList())
    )

    @Nested
    inner class `when json text is populated array` : SuccessfulParse(
            jsonText = """["a",1,[],{}]""",
            expectedNode = ListNode(
                    listOf(
                        "a".asNode(),
                        1.asNode(),
                        ListNode(emptyList()),
                        StructNode(emptyMap())
                    )
            )
    )

    @Nested
    inner class `when json text is populated struct` : SuccessfulParse(
            jsonText = """{"a":"a","b":1,"c":[],"d":{}}""",
            expectedNode = StructNode(
                    mapOf(
                            "a" to "a".asNode(),
                            "b" to 1.asNode(),
                            "c" to ListNode(emptyList()),
                            "d" to StructNode(emptyMap())
                    )
            )
    )

    @Nested
    inner class `when json text is empty string` : SuccessfulParse(
            jsonText = "\"\"",
            expectedNode = "".asNode()
    )

    @Nested
    inner class `when json text is populated string` : SuccessfulParse(
            jsonText = "\"a\"",
            expectedNode = "a".asNode()
    )

    @Nested
    inner class `when json text is integer` : SuccessfulParse(
            jsonText = "456",
            expectedNode = 456.asNode()
    )

    @Nested
    inner class `when json text is double` : SuccessfulParse(
            jsonText = "456.890",
            expectedNode = 456.89.asNode()
    )

    @Nested
    inner class `when json text is true` : SuccessfulParse(
            jsonText = "true",
            expectedNode = true.asNode()
    )

    @Nested
    inner class `when json text is false` : SuccessfulParse(
            jsonText = "false",
            expectedNode = false.asNode()
    )


    @Nested
    inner class `when json text is null` : SuccessfulParse(
            jsonText = "null",
            expectedNode = NullNode()
    )

    @Nested
    inner class `when json text is empty` : UnsuccessfulParse(
            jsonText = "",
            expectedPosition = Position(1, 1)
    )

    @Nested
    inner class `when json text is incomplete object` : UnsuccessfulParse(
            jsonText = "{",
            expectedPosition = Position(1, 1)
    )

    @Nested
    inner class `when json text is incomplete array` : UnsuccessfulParse(
            jsonText = "[",
            expectedPosition = Position(1, 1)
    )

    open inner class SuccessfulParse(
            jsonText: String,
            private val expectedNode: Node
    )
    {
        private val decoder = JacksonDecoder()
        private val result = decoder.decode(jsonText)

        @Test
        fun `then parse was successful`() {
            assertOk(result) {
                it.should.equal(expectedNode)
            }
        }
    }


    open inner class UnsuccessfulParse(
            jsonText: String,
            private val expectedPosition: Position
    )
    {
        private val decoder = JacksonDecoder()
        private val result = decoder.decode(jsonText)

        @Test
        fun `then parse error captured expected position`() {
            assertErr(result) {
                it.where.should.equal(expectedPosition)
            }
        }
    }
}

