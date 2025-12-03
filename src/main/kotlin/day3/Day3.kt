package day3

import ext.maxWithIndex
import ext.pow

typealias BatteryBank = List<Long>

fun parseBatteryBanks(input: String): List<BatteryBank> = input
    .lines()
    .map { line ->
        line.toCharArray().map { it.digitToInt().toLong() }
    }

fun BatteryBank.findLargestJoltage(maxTurnedOn: Int = 2): Long {
    val options = dropLast(maxTurnedOn - 1)
    val (max, maxIdx) = options.maxWithIndex()

    return if (maxTurnedOn == 1) {
        max
    } else {
        val remainingOptions = drop(maxIdx + 1)
        max * 10L.pow(maxTurnedOn - 1) + remainingOptions.findLargestJoltage(maxTurnedOn - 1)
    }
}

fun List<BatteryBank>.findLargestTotalJoltage(maxTurnedOn: Int = 2): Long =
    sumOf { it.findLargestJoltage(maxTurnedOn) }
