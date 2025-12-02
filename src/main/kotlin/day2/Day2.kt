package day2

typealias ProductId = Long
typealias ProductIdRange = LongRange

fun ProductId.isInvalidProductIdPart1(): Boolean =
    Regex("^(.+)\\1$").matches(this.toString())

fun ProductId.isInvalidProductIdPart2(): Boolean =
    Regex("^(.+)\\1+$").matches(this.toString())

fun parseProductIds(input: String): List<ProductId> =
    input
        .split(",")
        .map { parseProductIdRange(it) }
        .flatten()

fun parseProductIdRange(range: String): ProductIdRange =
    range
        .split("-")
        .let { (start, end) -> return start.toLong()..end.toLong() }
