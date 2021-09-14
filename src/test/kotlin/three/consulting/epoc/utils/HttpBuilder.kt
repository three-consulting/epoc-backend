package three.consulting.epoc.utils

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

fun jsonPostEntity(jsonLocation: String): HttpEntity<String> {
    val jsonString = jsonReader(jsonLocation)
    val httpHeaders = HttpHeaders()
    httpHeaders.contentType = MediaType.APPLICATION_JSON
    return HttpEntity(jsonString, httpHeaders)
}
