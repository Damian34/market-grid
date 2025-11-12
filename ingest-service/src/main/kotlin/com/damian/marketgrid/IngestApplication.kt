package com.damian.marketgrid

import io.quarkus.runtime.Quarkus
import io.quarkus.runtime.annotations.QuarkusMain

@QuarkusMain
class IngestApplication

fun main(args: Array<String>) {
    Quarkus.run(*args)
}
