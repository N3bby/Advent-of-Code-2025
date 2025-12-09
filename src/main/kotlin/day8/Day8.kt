package day8

import util.Position3D
import util.cartesianProduct
import util.removeDuplicates
import util.unionFindOf

typealias JunctionBox = Position3D
typealias Circuit = List<JunctionBox>

fun parseJunctionBoxes(input: String): List<JunctionBox> = input
    .lines()
    .map { it.split(",").map(String::toInt) }
    .map { (x, y, z) -> Position3D(x, y, z) }

fun getCircuits(junctionBoxes: List<JunctionBox>, connectionsToMake: Int): List<List<JunctionBox>> {
    val junctionBoxPairs = cartesianProduct(junctionBoxes, junctionBoxes).removeDuplicates()

    val nShortestConnections = junctionBoxPairs
        .sortedBy { (first, second) -> first.distanceFrom(second) }
        .take(connectionsToMake)

    val unionFind = unionFindOf(nShortestConnections)
    val circuits = unionFind.getGroups(junctionBoxes)

    return circuits
}

fun List<Circuit>.ofSize(size: Int): List<Circuit> = filter { it.size == size }
