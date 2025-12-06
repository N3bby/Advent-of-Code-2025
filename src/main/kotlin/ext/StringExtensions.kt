package ext

fun String.splitAtIndex(index: Int): Pair<String, String> {
    return Pair(this.substring(0, index), this.substring(index))
}

fun String.splitInTwo(): Pair<String, String> {
    return splitAtIndex(this.length / 2)
}

fun String.transpose(): String {
    val lines = lines()
    return (0..lines.maxOf { it.length })
        .map { index -> lines.map { line -> line.getOrNull(index) ?: ' ' }.joinToString("") }
        .joinToString("\n")
}

fun String.reversedPerLine(): String {
    return lines()
        .map { it.reversed() }
        .joinToString("\n")
}

fun String.hasSubstringAt(index: Int, substring: String): Boolean {
    return substring(startIndex = index).startsWith(substring)
}

fun String.paddedLines(): List<String> {
    val maxLineLength = lines().maxOf { it.length }
    return lines()
        .map { it.padEnd(maxLineLength) }
}
