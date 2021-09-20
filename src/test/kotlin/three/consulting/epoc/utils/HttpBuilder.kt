package three.consulting.epoc.utils

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

fun jsonPostEntity(jsonFileName: String): HttpEntity<String> {
    val jsonString = jsonReader(jsonFileName)
    val httpHeaders = HttpHeaders()
    httpHeaders.contentType = MediaType.APPLICATION_JSON
    return HttpEntity(jsonString, httpHeaders)
}
