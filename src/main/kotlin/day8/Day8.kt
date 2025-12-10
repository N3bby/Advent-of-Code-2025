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

fun List<JunctionBox>.getConnectionsOrderedByClosest(): List<Pair<JunctionBox, JunctionBox>> {
    val junctionBoxPairs = cartesianProduct(this, this).removeDuplicates()
    return junctionBoxPairs.sortedBy { (first, second) -> first.distanceFrom(second) }
}

fun getCircuits(junctionBoxes: List<JunctionBox>, connectionsToMake: Int): List<List<JunctionBox>> {
    val nShortestConnections = junctionBoxes.getConnectionsOrderedByClosest()
        .take(connectionsToMake)

    val unionFind = unionFindOf(nShortestConnections)
    val circuits = unionFind.getGroups(junctionBoxes)

    return circuits
}

fun getLastConnectionThatCompletesFullCircuit(junctionBoxes: List<JunctionBox>): Pair<JunctionBox, JunctionBox> {
    val connections = junctionBoxes.getConnectionsOrderedByClosest()
    val unionFind = unionFindOf<JunctionBox>()

    for (connection in connections) {
        unionFind.union(connection)
        if(unionFind.getGroups(junctionBoxes).size == 1) return connection
    }

    throw Error("Unable to close circuit")
}

fun List<Circuit>.ofSize(size: Int): List<Circuit> = filter { it.size == size }
