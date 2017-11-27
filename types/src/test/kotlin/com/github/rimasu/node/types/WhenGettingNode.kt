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

import com.github.michaelbull.result.Result
import com.winterbe.expekt.should

abstract class WhenGettingNode<T>(protected val node: Node) {
    protected abstract val result: Result<T, NodeError>

    protected fun assertIncompatibleValue() {
        assertErr(result) {
            it.should.instanceof(IncompatibleValue::class.java)
        }
    }

    protected fun assertValueRetrieved(expected: T) {
        assertOk(result) {
            it.should.equal(expected)
        }
    }
}

abstract class WhenGettingNodeAsString(node: Node) : WhenGettingNode<String>(node) {
    override val result = node.asString()
}

abstract class WhenGettingNodeAsInt(node: Node) : WhenGettingNode<Int>(node) {
    override val result = node.asInt()
}

abstract class WhenGettingNodeAsLong(node: Node) : WhenGettingNode<Long>(node) {
    override val result = node.asLong()
}

abstract class WhenGettingNodeAsDouble(node: Node) : WhenGettingNode<Double>(node) {
    override val result = node.asDouble()
}

abstract class WhenGettingNodeAsFloat(node: Node) : WhenGettingNode<Float>(node) {
    override val result = node.asFloat()
}

abstract class WhenGettingNodeAsBoolean(node: Node) : WhenGettingNode<Boolean>(node) {
    override val result = node.asBoolean()
}

abstract class WhenGettingNodeAsStruct(node: Node) : WhenGettingNode<StructNode>(node) {
    override val result = node.asStruct()
}

abstract class WhenGettingNodeAsList(node: Node): WhenGettingNode<ListNode>(node)  {
    override val result = node.asList()
}