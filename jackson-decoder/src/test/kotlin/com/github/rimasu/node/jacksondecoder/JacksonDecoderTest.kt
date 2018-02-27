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

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.getErrorOrElse
import com.github.michaelbull.result.getOrElse
import com.github.rimasu.node.types.*
import com.github.rimasu.text.Position
import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.fail

class JacksonDecoderTest {

    @Test
    fun isEmptyStruct() {
        givenJson("""{}""")
        whenDecoded()
        thenDecodeSucceedsWith(
                StructNode(emptyMap())
        )
    }

    @Test
    fun isEmptyArray() {
        givenJson("""[]""")
        whenDecoded()
        thenDecodeSucceedsWith(
                ListNode(emptyList())
        )
    }

    @Test
    fun isPopulatedArray() {
        givenJson( """["a",1,[],{}]""")
        whenDecoded()
        thenDecodeSucceedsWith(
                ListNode(
                        listOf(
                                "a".asNode(),
                                1.asNode(),
                                ListNode(emptyList()),
                                StructNode(emptyMap())
                        )
                )
        )
    }

    @Test
    fun isPopulatedStruct() {
        givenJson(  """{"a":"a","b":1,"c":[],"d":{}}""")
        whenDecoded()
        thenDecodeSucceedsWith(
                StructNode(
                        mapOf(
                                "a" to "a".asNode(),
                                "b" to 1.asNode(),
                                "c" to ListNode(emptyList()),
                                "d" to StructNode(emptyMap())
                        )
                )
        )
    }

    @Test
    fun isEmptyString() {
        givenJson(  "\"\"")
        whenDecoded()
        thenDecodeSucceedsWith("".asNode())
    }

    @Test
    fun isPopulatedString() {
        givenJson(  "\"a\"")
        whenDecoded()
        thenDecodeSucceedsWith("a".asNode())
    }

    @Test
    fun isInteger() {
        givenJson(  "456")
        whenDecoded()
        thenDecodeSucceedsWith(456.asNode())
    }

    @Test
    fun isDouble() {
        givenJson(  "456.890")
        whenDecoded()
        thenDecodeSucceedsWith(456.89.asNode())
    }

    @Test
    fun isTrue() {
        givenJson(  "true")
        whenDecoded()
        thenDecodeSucceedsWith(true.asNode())
    }

    @Test
    fun isFalse() {
        givenJson(  "false")
        whenDecoded()
        thenDecodeSucceedsWith(false.asNode())
    }

    @Test
    fun isNull() {
        givenJson(  "null")
        whenDecoded()
        thenDecodeSucceedsWith(NullNode())
    }

    @Test
    fun isEmpty() {
        givenJson(  "")
        whenDecoded()
        thenDecodeFailsWith(Position(1, 1    ))
    }

    @Test
    fun incompleteStruct() {
        givenJson(  "{")
        whenDecoded()
        thenDecodeFailsWith(Position(1, 2    ))
    }

    @Test
    fun incompleteArray() {
        givenJson(  "[")
        whenDecoded()
        thenDecodeFailsWith(Position(1, 2    ))
    }

    private lateinit var json: String
    private lateinit var result: Result<Node, ParseError>

    private fun givenJson(json: String) {this.json = json}

    private fun whenDecoded() {result = JacksonDecoder().decode(json)}

    private fun thenDecodeSucceedsWith(expected: Node) {
        val actual = result.getOrElse { fail(it.toString()) }
        assertEquals(expected, actual)
    }

    private fun thenDecodeFailsWith(expected: Position) {
        val actual = result.getErrorOrElse { fail(it.toString()) }
        assertEquals(expected, actual.where)
    }
}

