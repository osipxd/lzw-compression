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

/**
 * Stream that can read codes that can't fit to one byte.
 *
 * @param stream Source stream
 * @param codeLength Determines code size. Can't be higher than [bufferSize]
 */
class CodeInputStream(
        private val stream: InputStream,
        codeLength: Int
) : BufferedCodeStream(codeLength) {

    companion object {
        private const val EOF = -1

    }

    fun read(): Int {
        if (buffer == EOF) return EOF

        fillBuffer()
        return readCodeFromBuffer()
    }

    private fun fillBuffer() {
        while (bufferedBits < codeLength) {
            val byte = stream.read()
            if (byte == EOF) {
                bufferedBits = EOF
                return
            }

            buffer = buffer or (byte shl bufferedBits)
            bufferedBits += Bytes.BITS_IN_BYTE
        }
    }

    private fun readCodeFromBuffer(): Int {
        if (bufferedBits == EOF) {
            val lastBuffer = buffer
            this.buffer = EOF
            return lastBuffer
        }

        val code = buffer and mask
        buffer = buffer ushr codeLength
        bufferedBits -= codeLength

        return code
    }
}

fun CodeInputStream.consumeEach(consume: (Int) -> Unit) {
    var value = this.read()
    while (value != -1) {
        consume(value)
        value = this.read()
    }
}