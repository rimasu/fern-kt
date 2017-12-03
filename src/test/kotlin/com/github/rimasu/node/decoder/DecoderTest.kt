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
import com.github.rimasu.node.types.ListNode
import com.github.rimasu.node.types.Node
import com.github.rimasu.node.types.StructNode
import com.github.rimasu.node.types.asNode
import com.winterbe.expekt.should
import org.junit.jupiter.api.Test

class DecoderTest {

    @Test
    fun parse1() {
        parse("[]").should.equal(
                Ok(
                        ListNode(emptyList())
                )
        )
    }

    @Test
    fun parse2() {
        parse("aoeuaoeu").should.equal(
                Err(
                        Unit
                )

        )
    }

    @Test
    fun parse3() {
        parse("[a b 34.2]").should.equal(
                Ok(
                        listNode {
                            add("a")
                            add("b")
                            add("34.2")
                        }
                )
        )
    }

    @Test
    fun parse4() {
        parse("[[a b][3]]").should.equal(
                Ok(
                        listNode {
                            add(listNode {
                                add("a")
                                add("b")
                            })
                            add(listNode {
                                add("3")
                            })
                        }
                )

        )
    }

    @Test
    fun parse5() {
        parse("()").should.equal(
                Ok(
                        StructNode(emptyMap())
                )
        )
    }

    @Test
    fun parse6() {
        parse("(a=5)").should.equal(
                Ok(
                        StructNode(mapOf("a" to 5.asNode()))
                )
        )
    }

    @Test
    fun parse7() {
        parse("(a= 5)").should.equal(
                Ok(
                        StructNode(mapOf("a" to 5.asNode()))
                )
        )
    }

    @Test
    fun parse8() {
        parse("(a =5)").should.equal(
                Ok(
                        StructNode(mapOf("a" to 5.asNode()))
                )
        )
    }

    @Test
    fun parse9() {
        parse("( a=5 b=8)").should.equal(
                Ok(
                        StructNode(
                                mapOf(
                                        "a" to 5.asNode(),
                                        "b" to 8.asNode()
                                ))
                )
        )
    }

    @Test
    fun parse10() {
        parse("( a=5 b=[])").should.equal(
                Ok(
                        StructNode(
                                mapOf(
                                        "a" to 5.asNode(),
                                        "b" to listNode { }
                                ))
                )
        )
    }

    @Test
    fun parse11() {
        parse("(a=5 b=())").should.equal(
                Ok(
                        StructNode(
                                mapOf(
                                        "a" to 5.asNode(),
                                        "b" to StructNode(emptyMap())
                                ))
                )
        )
    }

    @Test
    fun parse12() {
            parse("[\"a b\"\" a \"]").should.equal(
                    Ok(
                            listNode {
                                add("a b")
                                add(" a ")
                            }
                    )
            )
    }

    @Test
    fun parse13() {
        parse("[\" |\" \"]").should.equal(
                Ok(
                        listNode {
                            add(" \" ")
                        }
                )
        )
    }

    private fun parse(s: String): Result<Node, Unit> {
        return Decoder.parse(s)
    }
}