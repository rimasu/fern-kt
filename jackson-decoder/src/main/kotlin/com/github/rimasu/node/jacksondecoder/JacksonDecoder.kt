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

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.rimasu.node.types.Node
import com.github.rimasu.node.types.StructNode

/**
 * Responsible for decoding a node structure from a json document.
 */
class JacksonDecoder(private val jsonFactory: JsonFactory = JsonFactory()) {

    /**
     * Decode document from existing parser. Most useful if node structure
     * has been embedded in a json document.
     */
    fun decode(parser: JsonParser) : Result<Node, ParseError> {
        val nextToken = parser.nextToken()
        return when (nextToken) {
            JsonToken.START_OBJECT -> decodeStruct(parser)
            else -> Err(ParseError())
        }
    }

    /**
     * Start of struct has been encountered. Need to parse fields (if any) and then end object.
     */
    private fun decodeStruct(parser: JsonParser): Result<Node, ParseError> {
        val parts = mutableMapOf<String, Node>()
        var next = parser.nextToken()
        while(next != JsonToken.END_OBJECT) {
            return Err(ParseError())
        }
        return Ok(StructNode(parts))
    }

    /**
     * Decode document from json encoded as string.
     */
    fun decode(jsonText: String) : Result<Node, ParseError> {
        return decode(jsonFactory.createParser(jsonText))
    }
}

