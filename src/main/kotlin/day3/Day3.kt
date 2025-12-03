package day3

import ext.pow

typealias BatteryBank = List<Int>

fun parseBatteryBanks(input: String): List<BatteryBank> = input
    .lines()
    .map { line ->
        line.toCharArray().map { it.digitToInt() }
    }

fun BatteryBank.findLargestJoltage(maxTurnedOn: Int = 2): Long {
    val options = dropLast(maxTurnedOn - 1)
    val max = options.max()
    val maxIdx = options.indexOf(max)

    return if (maxTurnedOn == 1) {
        max.toLong()
    } else {
        val remainingSearchSpace = drop(maxIdx + 1)
        max.toLong() * 10L.pow(maxTurnedOn - 1) + remainingSearchSpace.findLargestJoltage(maxTurnedOn - 1)
    }
}

fun List<BatteryBank>.findLargestTotalJoltage(maxTurnedOn: Int = 2): Long =
    sumOf { it.findLargestJoltage(maxTurnedOn) }
