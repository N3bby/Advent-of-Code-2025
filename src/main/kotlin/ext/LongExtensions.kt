package ext

import kotlin.math.pow

// This might not work for very large numbers
fun Long.pow(exponent: Int): Long = toDouble().pow(exponent).toLong()
