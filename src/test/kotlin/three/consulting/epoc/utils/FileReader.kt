package three.consulting.epoc.utils

import java.io.File
import java.nio.charset.Charset

fun jsonReader(location: String): String {
    return File(location).readText(Charset.defaultCharset())
}

