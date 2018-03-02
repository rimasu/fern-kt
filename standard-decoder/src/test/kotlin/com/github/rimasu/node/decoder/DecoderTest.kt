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
package com.github.rimasu.node.decoder

import com.github.michaelbull.result.*
import com.github.rimasu.node.decoder.CodePointType.*
import com.github.rimasu.node.types.*
import com.github.rimasu.text.Position
import com.github.rimasu.text.Region
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class DecoderTest {

    private lateinit var text: String
    private lateinit var result: Result<Node, DecoderError>

    private fun givenText(text: String) {
        this.text = text
    }

    private fun whenDecoded() {
        result = Decoder.parse(text)
    }

    private fun checkNode(func: (Node) -> Unit) {
        val actual = result.getOrElse { fail(it.toString()) }
        func(actual)
    }

    private fun thenParseFailsWith(expectedPosition: Position,
                                   expectedTypes: List<CodePointType> = emptyList()) {
        val actual = result.getErrorOrElse { fail(it.toString()) }
        assertEquals(expectedPosition, actual.position)
        when (actual) {
            is InvalidSyntax -> assertEquals(expectedTypes, actual.expectedTypes)
        }
    }

    @Test
    fun emptyListAtBeginningOfDocument() {
        givenText("[]")
        whenDecoded()
        checkNode { value ->
            assertEquals(listNode {}, value)
            assertEquals(Region(1, 1, 1, 2), value.anchor)
        }
    }

    @Test
    fun emptyListAtBeginningOfDocumentWithLeadingWhiteSpace() {
        givenText("\n []")
        whenDecoded()
        checkNode { value ->
            assertEquals(listNode {}, value)
            assertEquals(Region(2, 2, 2, 3), value.anchor)
        }
    }

    @Test
    fun emptyListAtBeginningOfDocumentWithIntermediateWhiteSpace() {
        givenText("[\n ]")
        whenDecoded()
        checkNode { value ->
            assertEquals(listNode {}, value)
            assertEquals(Region(1, 1, 2, 2), value.anchor)
        }
    }

    @Test
    fun unquotedValueInList() {
        givenText("[abcd]")
        whenDecoded()
        checkNode { value ->
            assertEquals(listNode { add("abcd") }, value)
            assertEquals(Region(1, 1, 1, 6), value.anchor)
        }

        checkNode { value ->
            value as ListNode
            with(value[1].getOrElse { fail(it.toString()) }) {
                assertEquals("abcd".asNode(), this)
                assertEquals(Region(1, 2, 1, 5), this.anchor)
            }
        }
    }

    @Test
    fun unquotedValuesInList() {
        givenText("[abcd efgh]")
        whenDecoded()

        checkNode { value ->
            assertEquals(listNode { add("abcd"); add("efgh") }, value)
            assertEquals(Region(1, 1, 1, 11), value.anchor)
        }

        checkNode { value ->
            value as ListNode
            with(value[1].getOrElse { fail(it.toString()) }) {
                assertEquals("abcd".asNode(), this)
                assertEquals(Region(1, 2, 1, 5), this.anchor)
            }

            with(value[2].getOrElse { fail(it.toString()) }) {
                assertEquals("efgh".asNode(), this)
                assertEquals(Region(1, 7, 1, 10), this.anchor)
            }
        }
    }

    @Test
    fun quotedValueInList() {
        givenText("[\"abcd\"]")
        whenDecoded()
        checkNode { value ->
            assertEquals(listNode { add("abcd".asNode()) }, value)
            assertEquals(Region(1, 1, 1, 8), value.anchor)

            value as ListNode
            with(value[1].getOrElse { fail(it.toString()) }) {
                assertEquals("abcd".asNode(), this)
                assertEquals(Region(1, 2, 1, 7), this.anchor)
            }
        }
    }

    @Test
    fun quotedAndEscapedValueInList() {
        givenText("[\"ab|\"cd\"]")
        whenDecoded()
        checkNode { value ->
            assertEquals(listNode { add("ab\"cd") }, value)
            assertEquals(Region(1, 1, 1, 10), value.anchor)
            value as ListNode
            with(value[1].getOrElse { fail(it.toString()) }) {
                assertEquals("ab\"cd".asNode(), this)
                assertEquals(Region(1, 2, 1, 9), this.anchor)
            }
        }
    }

    @Test
    fun quotedValuesInList() {
        givenText("[\"abcd\" \"efgh\"]")
        whenDecoded()
        checkNode { value ->
            assertEquals(listNode { add("abcd"); add("efgh") }, value)
            assertEquals(Region(1, 1, 1, 15), value.anchor)
            value as ListNode
            with(value[1].getOrElse { fail(it.toString()) }) {
                assertEquals("abcd".asNode(), this)
                assertEquals(Region(1, 2, 1, 7), this.anchor)
            }
            with(value[2].getOrElse { fail(it.toString()) }) {
                assertEquals("efgh".asNode(), this)
                assertEquals(Region(1, 9, 1, 14), this.anchor)
            }
        }
    }

    @Test
    fun listInList() {
        givenText("[[]]")
        whenDecoded()
        checkNode { value ->
            assertEquals(listNode { add(listNode {}) }, value)
            assertEquals(Region(1, 1, 1, 4), value.anchor)
            value as ListNode
            with(value[1].getOrElse { fail(it.toString()) }) {
                assertEquals(listNode {}, this)
                assertEquals(Region(1, 2, 1, 3), this.anchor)
            }
        }
    }

    @Test
    fun structInList() {
        givenText("[()]")
        whenDecoded()
        checkNode { value ->
            assertEquals(listNode { add(structNode {}) }, value)
            assertEquals(Region(1, 1, 1, 4), value.anchor)
            value as ListNode
            with(value[1].getOrElse { fail(it.toString()) }) {
                assertEquals(structNode {}, this)
                assertEquals(Region(1, 2, 1, 3), this.anchor)
            }
        }
    }

    @Test
    fun structAtBeginningOfDocument() {
        givenText("()")
        whenDecoded()
        checkNode { value ->
            assertEquals(structNode {}, value)
            assertEquals(Region(1, 1, 1, 2), value.anchor)
        }
    }

    @Test
    fun emptyStructAtBeginningOfDocumentWithLeadingWhiteSpace() {
        givenText("\n ()")
        whenDecoded()
        checkNode { value ->
            assertEquals(structNode {}, value)
            assertEquals(Region(2, 2, 2, 3), value.anchor)
        }
    }

    @Test
    fun emptyStructAtBeginningOfDocumentWithIntermediateWhiteSpace() {
        givenText("(\n )")
        whenDecoded()
        checkNode { value ->
            assertEquals(structNode {}, value)
            assertEquals(Region(1, 1, 2, 2), value.anchor)
        }
    }

    @Test
    fun unquotedValuesInStruct() {
        givenText("(a=1 b =2 c  =3 d= 4 e=  5)")
        whenDecoded()
        checkNode { value ->
            assertEquals(structNode {
                add("a", "1")
                add("b", "2")
                add("c", "3")
                add("d", "4")
                add("e", "5")
            }, value)
            assertEquals(Region(1, 1, 1, 27), value.anchor)

            value as StructNode
            with(value["a"].getOrElse { fail(it.toString()) }) {
                assertEquals("1".asNode(), this)
                assertEquals(Region(1, 4, 1, 4), this.anchor)
            }

            with(value["b"].getOrElse { fail(it.toString()) }) {
                assertEquals("2".asNode(), this)
                assertEquals(Region(1, 9, 1, 9), this.anchor)
            }

            with(value["c"].getOrElse { fail(it.toString()) }) {
                assertEquals("3".asNode(), this)
                assertEquals(Region(1, 15, 1, 15), this.anchor)
            }

            with(value["d"].getOrElse { fail(it.toString()) }) {
                assertEquals("4".asNode(), this)
                assertEquals(Region(1, 20, 1, 20), this.anchor)
            }

            with(value["e"].getOrElse { fail(it.toString()) }) {
                assertEquals("5".asNode(), this)
                assertEquals(Region(1, 26, 1, 26), this.anchor)
            }
        }
    }

    @Test
    fun quotedValuesInStruct() {
        givenText("(a=\"1 \" b =\" 2\" )")
        whenDecoded()
        checkNode { value ->
            assertEquals(structNode {
                add("a", "1 ")
                add("b", " 2")
            }, value)
            assertEquals(Region(1, 1, 1, 17), value.anchor)

            value as StructNode
            with(value["a"].getOrElse { fail(it.toString()) }) {
                assertEquals("1 ".asNode(), this)
                assertEquals(Region(1, 4, 1, 7), this.anchor)
            }

            with(value["b"].getOrElse { fail(it.toString()) }) {
                assertEquals(" 2".asNode(), this)
                assertEquals(Region(1, 12, 1, 15), this.anchor)
            }
        }
    }

    @Test
    fun structInStruct() {
        givenText("(a=() )")
        whenDecoded()
        checkNode { value ->
            assertEquals(structNode {
                add("a", structNode {})
            }, value)
            assertEquals(Region(1, 1, 1, 7), value.anchor)

            value as StructNode
            with(value["a"].getOrElse { fail(it.toString()) }) {
                assertEquals(structNode {}, this)
                assertEquals(Region(1, 4, 1, 5), this.anchor)
            }

        }
    }

    @Test
    fun listInStruct() {
        givenText("(a=[] )")
        whenDecoded()
        checkNode { value ->
            assertEquals(structNode {
                add("a", listNode {})
            }, value)
            assertEquals(Region(1, 1, 1, 7), value.anchor)

            value as StructNode
            with(value["a"].getOrElse { fail(it.toString()) }) {
                assertEquals(listNode {}, this)
                assertEquals(Region(1, 4, 1, 5), this.anchor)
            }
        }
    }

    @Test
    fun quotedValueAtRoot() {
        givenText("a")
        whenDecoded()
        thenParseFailsWith(
                expectedPosition = Position(1, 1),
                expectedTypes = listOf(OPEN_STRUCT, OPEN_LIST, WHITE_SPACE)
        )
    }

    @Test
    fun closeStructInList() {
        givenText("[)")
        whenDecoded()
        thenParseFailsWith(
                expectedPosition = Position(1, 2),
                expectedTypes = listOf(NORMAL, QUOTE, OPEN_STRUCT, OPEN_LIST, CLOSE_LIST, WHITE_SPACE)
        )
    }

    @Test
    fun closeStructInListWithIntermediateWhiteSpace() {
        givenText("[\n )")
        whenDecoded()
        thenParseFailsWith(
                expectedPosition = Position(2, 2),
                expectedTypes = listOf(NORMAL, QUOTE, OPEN_STRUCT, OPEN_LIST, CLOSE_LIST, WHITE_SPACE)
        )
    }

    @Test
    fun incompleteList() {
        givenText("[\n ")
        whenDecoded()
        thenParseFailsWith(
                expectedPosition = Position(2, 1)
        )
    }

    @Test
    fun closeListInStruct() {
        givenText("(]")
        whenDecoded()
        thenParseFailsWith(
                expectedPosition = Position(1, 2),
                expectedTypes = listOf(NORMAL, CLOSE_STRUCT, WHITE_SPACE)
        )
    }

    @Test
    fun closeListInStructWithIntermediateWhiteSpace() {
        givenText("(\n ]")
        whenDecoded()
        thenParseFailsWith(
                expectedPosition = Position(2, 2),
                expectedTypes = listOf(NORMAL, CLOSE_STRUCT, WHITE_SPACE)
        )
    }

    @Test
    fun IncompleteStruct() {
        givenText("(\n ")
        whenDecoded()
        thenParseFailsWith(
                expectedPosition = Position(2, 1)
        )
    }

    @Test
    fun incompleteField() {
        givenText("( a \n ")
        whenDecoded()
        thenParseFailsWith(
                expectedPosition = Position(2, 1)
        )
    }

    @Test
    fun openListBeforeField() {
        givenText("( ( ")
        whenDecoded()
        thenParseFailsWith(
                expectedPosition = Position(1, 3),
                expectedTypes = listOf(NORMAL, CLOSE_STRUCT, WHITE_SPACE)
        )
    }

    @Test
    fun openListDuringFieldName() {
        givenText("( a( ")
        whenDecoded()
        thenParseFailsWith(
                expectedPosition = Position(1, 4),
                expectedTypes = listOf(NORMAL, ASSIGNMENT, WHITE_SPACE)
        )
    }

    @Test
    fun openListBeforeAssignment() {
        givenText("( a ( ")
        whenDecoded()
        thenParseFailsWith(
                expectedPosition = Position(1, 5),
                expectedTypes = listOf(ASSIGNMENT, WHITE_SPACE)
        )
    }

    @Test
    fun closeStructBeforeAssignment() {
        givenText("( a ) ")
        whenDecoded()
        thenParseFailsWith(
                expectedPosition = Position(1, 5),
                expectedTypes = listOf(ASSIGNMENT, WHITE_SPACE)
        )
    }

    @Test
    fun closeStructAfterAssignment() {
        givenText("( a =)")
        whenDecoded()
        thenParseFailsWith(
                expectedPosition = Position(1, 6),
                expectedTypes = listOf(NORMAL, QUOTE, OPEN_STRUCT, OPEN_LIST, WHITE_SPACE)
        )
    }

    @Test
    fun closeStructAfterAssignmentWithIntermediateWhitespace() {
        givenText("( a = )\n ")
        whenDecoded()
        thenParseFailsWith(
                expectedPosition = Position(1, 7),
                expectedTypes = listOf(NORMAL, QUOTE, OPEN_STRUCT, OPEN_LIST, WHITE_SPACE)
        )
    }

    @Test
    fun openStructBeforeAssignment() {
        givenText("( a [\"")
        whenDecoded()
        thenParseFailsWith(
                expectedPosition = Position(1, 5),
                expectedTypes = listOf(ASSIGNMENT, WHITE_SPACE)
        )
    }

    @Test
    fun closeListBeforeAssignment() {
        givenText("( a ]\n ")
        whenDecoded()
        thenParseFailsWith(
                expectedPosition = Position(1, 5),
                expectedTypes = listOf(ASSIGNMENT, WHITE_SPACE)
        )
    }

    @Test
    fun closeListAfterAssignment() {
        givenText("( a =]\n ")
        whenDecoded()
        thenParseFailsWith(
                expectedPosition = Position(1, 6),
                expectedTypes = listOf(NORMAL, QUOTE, OPEN_STRUCT, OPEN_LIST, WHITE_SPACE)
        )
    }

    @Test
    fun closeListAfterAssignmentWithIntermediateWhitespace() {
        givenText("( a = ]\n ")
        whenDecoded()
        thenParseFailsWith(
                expectedPosition = Position(1, 7),
                expectedTypes = listOf(NORMAL, QUOTE, OPEN_STRUCT, OPEN_LIST, WHITE_SPACE)
        )
    }

    @Test
    fun unquotedValueBeforeAssignment() {
        givenText("( a a\n ")
        whenDecoded()
        thenParseFailsWith(
                expectedPosition = Position(1, 5),
                expectedTypes = listOf(ASSIGNMENT, WHITE_SPACE)
        )
    }

    @Test
    fun quotedValueBeforeAssignment() {
        givenText("( a \" ")
        whenDecoded()
        thenParseFailsWith(
                expectedPosition = Position(1, 5),
                expectedTypes = listOf(ASSIGNMENT, WHITE_SPACE)
        )
    }
}