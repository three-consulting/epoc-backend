package three.consulting.epoc.utils

import java.io.File
import java.nio.charset.Charset

fun jsonReader(fileName: String, baseLocation: String = "src/test/resources/"): String {
    val location = baseLocation + fileName
    return File(location).readText(Charset.defaultCharset())
}
