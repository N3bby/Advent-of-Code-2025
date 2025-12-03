package day3

typealias BatteryBank = List<Int>

fun parseBatteryBanks(input: String): List<BatteryBank> = input
    .lines()
    .map { line ->
        line.toCharArray().map { it.digitToInt() }
    }

fun BatteryBank.findLargestJoltage(): Int {
    val firstOption = dropLast(1).max()
    val firstOptionIdx = indexOf(firstOption)
    val remainingSecondOptions = drop(firstOptionIdx + 1)
    val secondOption = remainingSecondOptions.max()

    return firstOption * 10 + secondOption
}

fun List<BatteryBank>.findLargestTotalJoltage(): Int = sumOf { it.findLargestJoltage() }
