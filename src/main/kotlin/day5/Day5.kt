package day5

typealias FreshIngredients = List<LongRange>
typealias AvailableIngredients = List<Long>

fun parseFreshIngredientsAndAvailableIngredients(input: String): Pair<FreshIngredients, AvailableIngredients> {
    val (freshIngredientRanges, availableIngredients) = input.split("\n\n")

    return Pair(
        freshIngredientRanges.lines().map { line ->
                val (start, end) = line.split("-").map { it.toLong() }
                start .. end
            },
        availableIngredients.lines().map { it.toLong() }
    )
}

fun FreshIngredients.contains(availableIngredient: Long): Boolean {
    return any { range -> availableIngredient in range }
}