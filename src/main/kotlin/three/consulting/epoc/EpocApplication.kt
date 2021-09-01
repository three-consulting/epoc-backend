package three.consulting.epoc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EpocApplication

fun main(args: Array<String>) {
    runApplication<EpocApplication>(*args)
}
