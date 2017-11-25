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

abstract class WhenGettingNodeAsStruct(node: Node) : WhenGettingNode<StructNode>(node) {
    override val result = node.asStruct()
}

abstract class WhenGettingNodeAsList(node: Node): WhenGettingNode<ListNode>(node)  {
    override val result = node.asList()
}