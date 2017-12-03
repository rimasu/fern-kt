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

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.expect
import com.github.rimasu.node.types.ListNode
import com.github.rimasu.node.types.Node
import com.github.rimasu.node.types.StructNode
import com.github.rimasu.node.types.asNode
import com.github.rimasu.text.Position
import com.github.rimasu.text.Region
import com.winterbe.expekt.should
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.fail

class DecoderTest {

    @Nested
    inner class ParseEmptyListAtBeginningOfDocument: ParseOk(
            text = "[]"
    ) {
        override fun checkNode(value: Node) {
            value.should.equal(listNode {})
            value.anchor.should.equal(Region(1,1,1,2))
        }
    }

    @Nested
    inner class ParseEmptyListAtBeginningOfDocumentWithLeadingWhiteSpace: ParseOk(
            text = "\n []"
    ) {
        override fun checkNode(value: Node) {
            value.should.equal(listNode {})
            value.anchor.should.equal(Region(2,2,2,3))
        }
    }

    @Nested
    inner class ParseEmptyListAtBeginningOfDocumentWithIntermediateWhiteSpace: ParseOk(
            text = "[\n ]"
    ) {
        override fun checkNode(value: Node) {
            value.should.equal(listNode {})
            value.anchor.should.equal(Region(1, 1, 2, 2))
        }
    }

    @Nested
    inner class ParseUnquotedValueInList: ParseOk(
            text = "[abcd]"
    ) {
        override fun checkNode(value: Node) {
            value.should.equal(listNode { add("abcd") })
            value.anchor.should.equal(Region(1, 1, 1, 6))
            value as ListNode
            with(value[1].expect("should exist")) {
                should.equal("abcd".asNode())
                anchor.should.equal(Region(1, 2, 1, 5))
            }
        }
    }

    @Nested
    inner class ParseUnquotedValuesInList: ParseOk(
            text = "[abcd efgh]"
    ) {
        override fun checkNode(value: Node) {
            value.should.equal(listNode { add("abcd"); add("efgh") })
            value.anchor.should.equal(Region(1, 1, 1, 11))

            value as ListNode
            with(value[1].expect("should exist")) {
                should.equal("abcd".asNode())
                anchor.should.equal(Region(1, 2, 1, 5))
            }

            with(value[2].expect("should exist")) {
                should.equal("efgh".asNode())
                anchor.should.equal(Region(1, 7, 1, 10))
            }
        }
    }

    @Nested
    inner class ParseQuotedValueInList: ParseOk(
            text = "[\"abcd\"]"
    ) {
        override fun checkNode(value: Node) {
            value.should.equal(listNode { add("abcd") })
            value.anchor.should.equal(Region(1, 1, 1, 8))
            value as ListNode
            with(value[1].expect("should exist")) {
                should.equal("abcd".asNode())
                anchor.should.equal(Region(1, 2, 1, 7))
            }
        }
    }

    @Nested
    inner class ParseQuotedAndEscapedValueInList: ParseOk(
            text = "[\"ab|\"cd\"]"
    ) {
        override fun checkNode(value: Node) {
            value.should.equal(listNode { add("ab\"cd") })
            value.anchor.should.equal(Region(1, 1, 1, 10))
            value as ListNode
            with(value[1].expect("should exist")) {
                should.equal("ab\"cd".asNode())
                anchor.should.equal(Region(1, 2, 1, 9))
            }
        }
    }

    @Nested
    inner class ParseQuotedValuesInList: ParseOk(
            text = "[\"abcd\" \"efgh\"]"
    ) {
        override fun checkNode(value: Node) {
            value.should.equal(listNode { add("abcd"); add("efgh") })
            value.anchor.should.equal(Region(1, 1, 1, 15))
            value as ListNode
            with(value[1].expect("should exist")) {
                should.equal("abcd".asNode())
                anchor.should.equal(Region(1, 2, 1, 7))
            }
            with(value[2].expect("should exist")) {
                should.equal("efgh".asNode())
                anchor.should.equal(Region(1, 9, 1, 14))
            }
        }
    }

    @Nested
    inner class ParseListInList: ParseOk(
            text = "[[]]"
    ) {
        override fun checkNode(value: Node) {
            value.should.equal(listNode { add(listNode {}) })
            value.anchor.should.equal(Region(1, 1, 1, 4))
            value as ListNode
            with(value[1].expect("should exist")) {
                should.equal(listNode {})
                anchor.should.equal(Region(1, 2, 1, 3))
            }
        }
    }

    @Nested
    inner class ParseStructInList: ParseOk(
            text = "[()]"
    ) {
        override fun checkNode(value: Node) {
            value.should.equal(listNode { add(structNode {}) })
            value.anchor.should.equal(Region(1, 1, 1, 4))
            value as ListNode
            with(value[1].expect("should exist")) {
                should.equal(structNode {})
                anchor.should.equal(Region(1, 2, 1, 3))
            }
        }
    }


    @Nested
    inner class ParseEmptyStructAtBeginningOfDocument: ParseOk(
            text = "()"
    ) {
        override fun checkNode(value: Node) {
            value.should.equal(structNode {})
            value.anchor.should.equal(Region(1,1,1,2))
        }
    }

    @Nested
    inner class ParseEmptyStructAtBeginningOfDocumentWithLeadingWhiteSpace: ParseOk(
            text = "\n ()"
    ) {
        override fun checkNode(value: Node) {
            value.should.equal(structNode {})
            value.anchor.should.equal(Region(2,2,2,3))
        }
    }

    @Nested
    inner class ParseEmptyStructAtBeginningOfDocumentWithIntermediateWhiteSpace: ParseOk(
            text = "(\n )"
    ) {
        override fun checkNode(value: Node) {
            value.should.equal(structNode {})
            value.anchor.should.equal(Region(1, 1, 2, 2))
        }
    }

    @Nested
    inner class ParseUnquotedValuesInStruct: ParseOk(
            text = "(a=1 b =2 c  =3 d= 4 e=  5)"
    ) {
        override fun checkNode(value: Node) {
            value.should.equal(structNode {
                add("a", "1")
                add("b", "2")
                add("c", "3")
                add("d", "4")
                add("e", "5")
            })
            value.anchor.should.equal(Region(1, 1, 1, 27))

            value as StructNode
            with(value["a"].expect("should exist")) {
                should.equal("1".asNode())
                anchor.should.equal(Region(1, 4, 1, 4))
            }

            with(value["b"].expect("should exist")) {
                should.equal("2".asNode())
                anchor.should.equal(Region(1, 9, 1, 9))
            }

            with(value["c"].expect("should exist")) {
                should.equal("3".asNode())
                anchor.should.equal(Region(1, 15, 1, 15))
            }

            with(value["d"].expect("should exist")) {
                should.equal("4".asNode())
                anchor.should.equal(Region(1, 20, 1, 20))
            }

            with(value["e"].expect("should exist")) {
                should.equal("5".asNode())
                anchor.should.equal(Region(1, 26, 1, 26))
            }
        }
    }

    @Nested
    inner class ParseQuotedValuesInStruct: ParseOk(
            text = "(a=\"1 \" b =\" 2\" )"
    ) {
        override fun checkNode(value: Node) {
            value.should.equal(structNode {
                add("a", "1 ")
                add("b", " 2")
            })
            value.anchor.should.equal(Region(1, 1, 1, 17))

            value as StructNode
            with(value["a"].expect("should exist")) {
                should.equal("1 ".asNode())
                anchor.should.equal(Region(1, 4, 1, 7))
            }

            with(value["b"].expect("should exist")) {
                should.equal(" 2".asNode())
                anchor.should.equal(Region(1, 12, 1, 15))
            }
        }
    }

    @Nested
    inner class ParseStructInStruct: ParseOk(
            text = "(a=() )"
    ) {
        override fun checkNode(value: Node) {
            value.should.equal(structNode {
                add("a", structNode {})
            })
            value.anchor.should.equal(Region(1, 1, 1, 7))

            value as StructNode
            with(value["a"].expect("should exist")) {
                should.equal(structNode {})
                anchor.should.equal(Region(1, 4, 1, 5))
            }

        }
    }

    @Nested
    inner class ParseListInStruct: ParseOk(
            text = "(a=[] )"
    ) {
        override fun checkNode(value: Node) {
            value.should.equal(structNode {
                add("a", listNode {})
            })
            value.anchor.should.equal(Region(1, 1, 1, 7))

            value as StructNode
            with(value["a"].expect("should exist")) {
                should.equal(listNode {})
                anchor.should.equal(Region(1, 4, 1, 5))
            }

        }
    }


    @Nested
    inner class ParsingQuotedValueAtRoot : ParseErr (
            text="a"
    ) {
        override fun checkError(error: DecoderError) {
            error.position.should.equal(Position(1,1 ))
        }
    }

    @Nested
    inner class ParsingCloseStructInList : ParseErr (
            text="[)"
    ) {
        override fun checkError(error: DecoderError) {
            error.position.should.equal(Position(1,2))
        }
    }

    @Nested
    inner class ParsingCloseStructInListWithIntermediateWhiteSpace : ParseErr (
            text="[\n )"
    ) {
        override fun checkError(error: DecoderError) {
            error.position.should.equal(Position(2,2))
        }
    }

    @Nested
    inner class ParsingIncompleteList : ParseErr (
            text="[\n "
    ) {
        override fun checkError(error: DecoderError) {
            error.position.should.equal(Position(2,1))
        }
    }




    @Nested
    inner class ParsingCloseListInStruct : ParseErr (
            text="(]"
    ) {
        override fun checkError(error: DecoderError) {
            error.position.should.equal(Position(1,2))
        }
    }

    @Nested
    inner class ParsingCloseListInStructWithIntermediateWhiteSpace : ParseErr (
            text="(\n ]"
    ) {
        override fun checkError(error: DecoderError) {
            error.position.should.equal(Position(2,2))
        }
    }

    @Nested
    inner class ParsingIncompleteStruct : ParseErr (
            text="(\n "
    ) {
        override fun checkError(error: DecoderError) {
            error.position.should.equal(Position(2,1))
        }
    }


    @Nested
    inner class ParsingIncompleteField : ParseErr (
            text="( a \n "
    ) {
        override fun checkError(error: DecoderError) {
            error.position.should.equal(Position(2,1))
        }
    }


    @Nested
    inner class ParsingOpenListBeforeField : ParseErr (
            text="( ( "
    ) {
        override fun checkError(error: DecoderError) {
            error.position.should.equal(Position(1,3))
        }
    }

    @Nested
    inner class ParsingOpenListDuringFieldName : ParseErr (
            text="( a( "
    ) {
        override fun checkError(error: DecoderError) {
            error.position.should.equal(Position(1,4))
        }
    }

    @Nested
    inner class ParsingOpenListBeforeAssignment : ParseErr (
            text="( a ( "
    ) {
        override fun checkError(error: DecoderError) {
            error.position.should.equal(Position(1,5))
        }
    }

    @Nested
    inner class ParsingCloseStructBeforeAssignment : ParseErr (
            text="( a ) "
    ) {
        override fun checkError(error: DecoderError) {
            error.position.should.equal(Position(1,5))
        }
    }

    @Nested
    inner class ParsingCloseStructAfterAssignment : ParseErr (
            text="( a =)"
    ) {
        override fun checkError(error: DecoderError) {
            error.position.should.equal(Position(1,6))
        }
    }

    @Nested
    inner class ParsingCloseStructAfterAssignmentWithIntermediateWhitespace : ParseErr (
            text="( a = )\n "
    ) {
        override fun checkError(error: DecoderError) {
            error.position.should.equal(Position(1,7))
        }
    }

    @Nested
    inner class ParsingOpenStructBeforeAssignment : ParseErr (
            text="( a [\""
    ) {
        override fun checkError(error: DecoderError) {
            error.position.should.equal(Position(1,5))
        }
    }

    @Nested
    inner class ParsingCloseListBeforeAssignment : ParseErr (
            text="( a ]\n "
    ) {
        override fun checkError(error: DecoderError) {
            error.position.should.equal(Position(1,5))
        }
    }

    @Nested
    inner class ParsingCloseListAfterAssignment : ParseErr (
            text="( a =]\n "
    ) {
        override fun checkError(error: DecoderError) {
            error.position.should.equal(Position(1,6))
        }
    }

    @Nested
    inner class ParsingCloseListAfterAssignmentWithIntermediateWhitespace : ParseErr (
            text="( a = ]\n "
    ) {
        override fun checkError(error: DecoderError) {
            error.position.should.equal(Position(1,7))
        }
    }

    @Nested
    inner class ParsingUnquotedValueBeforeAssignment : ParseErr (
            text="( a a\n "
    ) {
        override fun checkError(error: DecoderError) {
            error.position.should.equal(Position(1,5))
        }
    }

    @Nested
    inner class ParsingQuotedValueBeforeAssignment : ParseErr (
            text="( a \" "
    ) {
        override fun checkError(error: DecoderError) {
            error.position.should.equal(Position(1,5))
        }
    }

    abstract inner class ParseOk(text: String) {
        private val result = Decoder.parse(text)

        @Test
        fun resultIsOk() {
            when(result) {
                is Ok -> checkNode(result.value)
                is Err -> fail("Unexpected failure $result")
            }
        }

        abstract fun checkNode(value: Node)
    }

    abstract inner class ParseErr(text: String) {
        private val result = Decoder.parse(text)

        @Test
        fun resultIsError() {
            when(result) {
                is Ok -> fail("Unexpected ok $result")
                is Err -> checkError(result.error)
            }
        }

        abstract fun checkError(error: DecoderError)
    }


}