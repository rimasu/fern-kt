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
import com.fasterxml.jackson.core.io.JsonEOFException
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.rimasu.node.types.*
import com.github.rimasu.text.Position
import com.github.rimasu.text.Region

/**
 * Responsible for decoding a node structure from a json document.
 */
class JacksonDecoder(private val jsonFactory: JsonFactory = JsonFactory()) {

    /**
     * Decode document from json encoded as string.
     */
    fun decode(jsonText: String): Result<Node, ParseError> {
        return decode(jsonFactory.createParser(jsonText))
    }

    /**
     * Decode document from existing parser. Most useful if node structure
     * has been embedded in a json document.
     */
    fun decode(parser: JsonParser): Result<Node, ParseError> {
        val start = textPosition(parser)
        parser.nextToken()
        return decodeNode(start, parser)
    }

    /**
     * Decode the current current token as a node.
     */
    private fun decodeNode(start: Position, parser: JsonParser): Result<Node, ParseError> {
        return when (parser.currentToken) {
            JsonToken.START_OBJECT -> decodeStruct(start, parser)
            JsonToken.START_ARRAY -> decodeList(start, parser)
            JsonToken.VALUE_STRING -> Ok(parser.valueAsString.asNode())
            JsonToken.VALUE_NUMBER_INT -> Ok(parser.valueAsLong.asNode())
            JsonToken.VALUE_NUMBER_FLOAT -> Ok(parser.valueAsDouble.asNode())
            JsonToken.VALUE_TRUE -> Ok(true.asNode())
            JsonToken.VALUE_FALSE -> Ok(false.asNode())
            JsonToken.VALUE_NULL -> Ok(NullNode())
            else -> return Err(ParseError(start))
        }
    }

    /**
     * Start of struct has been encountered. Need to parse fields (if any) and then end object.
     */
    private fun decodeStruct(start: Position, parser: JsonParser): Result<Node, ParseError> {
        try {
            val parts = mutableMapOf<String, Node>()
            while (parser.nextToken() == JsonToken.FIELD_NAME) {
                val label = parser.currentName
                val valueStart = textPosition(parser)
                parser.nextToken()
                val nodeResult = decodeNode(valueStart, parser)
                when (nodeResult) {
                    is Ok -> parts[label] = nodeResult.value
                    is Err -> return nodeResult
                }
            }

            return if (parser.currentToken == JsonToken.END_OBJECT) {
                Ok(StructNode(parts))
            } else {
                Err(ParseError(textPosition(parser)))
            }
        } catch (e: JsonEOFException) {
            return Err(ParseError(start))
        }
    }

    private fun buildAnchor(parser: JsonParser, start: Position): Region {
        return Region(
                start,
                Position(parser.currentLocation.lineNr,
                Math.max(1, parser.currentLocation.columnNr-1))
        )
    }

    /**
     * Start of list has been encountered. Need to parse values (if any) and then end array.
     */
    private fun decodeList(startPos: Position, parser: JsonParser): Result<Node, ParseError> {
        val parts = mutableListOf<Node>()
        try {
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                val nodeResult = decodeNode(textPosition(parser), parser)
                when (nodeResult) {
                    is Ok -> parts.add(nodeResult.value)
                    is Err -> return nodeResult
                }
            }
            return Ok(ListNode(parts, buildAnchor(parser, startPos)))
        } catch (e: JsonEOFException) {
            return Err(ParseError(startPos))
        }
    }

    private fun textPosition(parser: JsonParser) : Position {
        val location = parser.currentLocation
        return Position(location.lineNr, location.columnNr)
    }
}

