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
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import ru.endlesscode.lzw.io.InputStream
import ru.endlesscode.lzw.io.OutputStream
import ru.endlesscode.lzw.util.toHexString
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals


@RunWith(Parameterized::class)
class LzwCompressorTest(
        private val source: String,
        private val compressed: String
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<out Any>> {
            return listOf(
                    arrayOf("abacabadabacabae", "61 20 06 61 30 06 00 11 06 64 40 10 03 11 10 65")
            )
        }
    }

    private lateinit var compressor: Compressor

    @Before
    fun setUp() {
        this.compressor = LzwCompressor()
    }

    @Test
    fun compressShouldWorksRight() {
        val inputStream = InputStream(ByteArrayInputStream(source.toByteArray()))
        val baos = ByteArrayOutputStream()
        val outputStream = OutputStream(baos)

        compressor.compress(inputStream, outputStream)

        assertEquals(compressed, baos.toByteArray().toHexString())
    }

    @Test
    fun decompressShouldWorksRight() {
        val compressedBytes = compressed.split(" ")
                .map { it.toInt(16).toByte() }

        val inputStream = InputStream(ByteArrayInputStream(compressedBytes.toByteArray()))
        val baos = ByteArrayOutputStream()
        val outputStream = OutputStream(baos)

        compressor.decompress(inputStream, outputStream)

        assertEquals(source, baos.toString())
    }
}