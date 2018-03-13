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
package com.github.rimasu.fern.types

import com.github.rimasu.fern.types.Fern.list
import com.github.rimasu.fern.types.Fern.struct
import org.junit.Test
import kotlin.test.assertEquals

class NodeDslTest {

    @Test
    fun canCreateTopLevelStructNode() {
        val value = struct {
            add("a", "b")
        }

        assertEquals(
                StructNode(
                        mapOf(
                                "a" to "b".asNode()
                        )
                ), value
        )
    }

    @Test
    fun canCreateStructInStruct() {
        val value = struct {
            struct("a") {
                add("a", "b")
            }
        }

        assertEquals(
                StructNode(
                        mapOf(
                                "a" to StructNode(
                                        mapOf(
                                                "a" to "b".asNode()
                                        )
                                )
                        )
                )
                , value
        )
    }

    @Test
    fun canCreateListInStruct() {
        val value = struct {
            list("a") {
                add("a")
            }
        }

        assertEquals(
                StructNode(
                        mapOf(
                                "a" to ListNode(
                                        listOf(
                                                "a".asNode()
                                        )
                                )
                        )
                )
                , value
        )
    }

    @Test
    fun canCreateTopLevelListNode() {
        val value = list {
            add("b")
        }

        assertEquals(
                ListNode(listOf("b".asNode())),
                value
        )
    }

    @Test
    fun canCreateStructInAList() {
        val value = list {
            struct {
                add("a", "b")
            }
        }

        assertEquals(
                ListNode(
                        listOf(
                                StructNode(
                                        mapOf(
                                                "a" to "b".asNode()
                                        )
                                )
                        )
                )
                , value
        )
    }

    @Test
    fun canCreateListInAList() {
        val value = list {
            list {
                add("a")
            }
        }

        assertEquals(
                ListNode(
                        listOf(
                                ListNode(
                                        listOf(
                                                "a".asNode()
                                        )
                                )
                        )
                )
                , value
        )
    }
}