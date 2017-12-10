/*
 * This file is part of lzw-compression, licensed under the MIT License (MIT).
 *
 * Copyright (c) Osip Fatkullin <osip.fatkullin@gmail.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ru.endlesscode.lzw.io

import ru.endlesscode.lzw.util.Bytes


class CodeOutputStream(
        private val stream: OutputStream,
        private val codeLength: Int
) {

    private val mask = Bytes.mask(codeLength)
    private val bufSize = Bytes.BITS_IN_INT

    private var buf = 0
    private var bufUsedBits = 0

    init {
        if (codeLength > bufSize) {
            throw IllegalArgumentException("Code length $codeLength is more than buffer size.")
        }
    }

    fun write(code: Int) {
        val bufferedCode = (code and mask) shl (bufUsedBits)
        buf = buf or bufferedCode
        bufUsedBits += codeLength

        writeBuffer()
    }

    private fun writeBuffer() {
        while (bufUsedBits >= Bytes.BITS_IN_BYTE) {
            writeNextByte()
            buf = buf ushr Bytes.BITS_IN_BYTE
            bufUsedBits -= Bytes.BITS_IN_BYTE
        }
    }

    fun flush() {
        if (buf != 0) {
            writeNextByte()
        }

        stream.flush()
    }

    private fun writeNextByte() {
        stream.write(buf and Bytes.BYTE_MASK)
    }
}
