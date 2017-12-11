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
 * Stream that can read/write codes that can't fit to one byte.
 *
 * @param codeLength Determines code size. Can't be higher than [bufferSize]
 */
abstract class BufferedCodeStream(
        protected val codeLength: Int
) {

    /**
     * Mask that used to fit code to given [codeLength].
     */
    protected val codeMask = Bytes.mask(codeLength)

    /**
     * Size of [buffer] in bits.
     */
    protected val bufferSize = Bytes.BITS_IN_INT

    /**
     * Buffer to store part of code that can't fit in byte.
     * Used Int. It can store maximum 64 bits.
     * @see [bufferSize]
     */
    protected var buffer = 0

    /**
     * Num of used bits in [buffer].
     */
    protected var bufferedBits = 0


    /**
     * Validates input params.
     */
    init {
        if (codeLength > bufferSize) {
            throw IllegalArgumentException("Code length $codeLength is more than buffer size.")
        }
    }

    protected fun putToBuffer(data: Int, length: Int, mask: Int = Bytes.mask(length)) {
        val trimmedData = data and mask
        buffer = buffer or (trimmedData shl bufferedBits)
        bufferedBits += length
    }

    protected fun getFromBuffer(length: Int, mask: Int = Bytes.mask(length)): Int {
        val data = buffer and mask
        buffer = buffer ushr length
        bufferedBits -= length

        return data
    }
}