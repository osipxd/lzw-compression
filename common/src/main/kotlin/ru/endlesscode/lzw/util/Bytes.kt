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

package ru.endlesscode.lzw.util

object Bytes {
    const val BYTE_MASK = 0xFF
    const val BYTES_IN_INT = 4
    const val BITS_IN_BYTE = 8
    const val BITS_IN_INT = BITS_IN_BYTE * BYTES_IN_INT

    const val HEX_CHARS = "0123456789ABCDEF"

    fun longMask(bits: Int): Long = mask(bits).unsignedToLong()

    fun mask(bits: Int): Int = (1 shl bits) - 1
}

fun Byte.unsignedToInt(): Int = this.toInt() and 0xFF

fun Int.unsignedToLong(): Long = this.toLong() and 0xFFFFFFFF

fun ByteArray.toHexString(): String {
    val hexChars = CharArray(size * 3)
    for (i in indices) {
        val v = this[i].unsignedToInt()
        val j = i * 3
        hexChars[j] = Bytes.HEX_CHARS[v ushr 0x04]
        hexChars[j + 1] = Bytes.HEX_CHARS[v and 0x0F]
        hexChars[j + 2] = ' '
    }

    return hexChars.joinToString(separator = "").trimEnd()
}