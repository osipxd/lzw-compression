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

package ru.endlesscode.lzw

import org.junit.Before
import org.junit.Test
import ru.endlesscode.lzw.io.InputStream
import ru.endlesscode.lzw.io.OutputStream
import java.io.ByteArrayInputStream
import kotlin.test.assertEquals

class LzwCompressorTest {

    private lateinit var compressor: Compressor

    @Before
    fun setUp() {
        this.compressor = LzwCompressor()
    }

    @Test
    fun compressShouldWorksRight() {
        val inputStream = InputStream(ByteArrayInputStream("abacabadabacabae".toByteArray()))
        val output = mutableListOf<Int>()
        val outputStream = OutputStream(object : java.io.OutputStream() {
            override fun write(b: Int) {
                output += b
            }


        })

        compressor.compress(inputStream, outputStream)

        assertEquals(arrayListOf(0, 1, 0, 2, 5, 0, 3, 9, 8, 6, 4), output)
    }
}