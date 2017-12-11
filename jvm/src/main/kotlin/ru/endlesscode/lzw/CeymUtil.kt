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

import picocli.CommandLine
import ru.endlesscode.lzw.io.InputStream
import ru.endlesscode.lzw.io.OutputStream
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.Callable


fun main(args: Array<String>) {
    val commandLine = CommandLine(CeymUtil())
            .registerConverter(Path::class.java, { Paths.get(it) })

    commandLine.parseWithHandler(CommandLine.RunAll(), System.err, *args)
}


@CommandLine.Command(
        name = "ceym",
        description = ["Util for packing and unpacking files with LZW algorithm"],
        footer = ["Licensed under MIT."]
)
class CeymUtil : Callable<Unit> {

    companion object {
        private const val EXT_PACKED = ".ceym"
        private const val EXT_UNPACKED = ".u"
    }

    @CommandLine.Parameters(index = "0", description = ["The file to pack or unpack"])
    private lateinit var input: Path

    @CommandLine.Option(names = ["-u", "--unpack"], description = ["Use util in unpack mode"])
    private var unpackMode = false

    @CommandLine.Option(names = ["-o", "--output"], description = ["Name of output file"])
    private var output: Path? = null

    @CommandLine.Option(
            names = ["-C", "-code-length"],
            description = ["Code length in bits (by default - 12). You can try to change this value to increase compression rate."]
    )
    private var codeLength: Int = 12

    @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = ["Show this help message and exit"])
    private var helpRequested = false

    override fun call() {
        input = input.toAbsolutePath()

        try {
            Validation.shouldExists(input)

            if (unpackMode) {
                onUnpackMode()
            } else {
                onPackMode()
            }
        } catch (e: Exception) {
            println("[ERROR] ${e.message}")
        }
    }

    private fun onPackMode() {
        if (output == null) {
            output = input.parent.resolve("${input.fileName}$EXT_PACKED")
        }

        doWork()
    }

    private fun onUnpackMode() {
        if (output == null) {
            output = input.parent.resolve(input.fileName.toString().replace(EXT_PACKED, EXT_UNPACKED))
        }

        doWork()
    }

    private fun doWork() {
        val bis = BufferedInputStream(Files.newInputStream(input))
        val bos = BufferedOutputStream(Files.newOutputStream(output))

        bis.use { bos.use {
            val compressor = LzwCompressor(codeLength)
            if (unpackMode) {
                compressor.decompress(InputStream(bis), OutputStream(bos))
            } else {
                compressor.compress(InputStream(bis), OutputStream(bos))
            }
        }}
    }
}
