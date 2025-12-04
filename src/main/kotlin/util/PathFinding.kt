package util

import kotlin.collections.plus
import kotlin.math.min

typealias Path = List<Position>

fun findShortestPath(start: Position, end: Position, bounds: Bounds, blockedTiles: Set<Position>): Path? {
    val nodes = bounds.positions.filter { !blockedTiles.contains(it) }.toSet()
    val distances = computeDistancesFromStart(start, nodes)

    @Suppress("DuplicatedCode")
    fun getShortestPath(visited: Path = listOf(end)): Path? {
        if (visited.last() == start) return visited

        val neighbourClosestToStart = visited.last().directNeightbours
            .filter { nodes.contains(it) }
            .filter { !visited.contains(it) }
            .minByOrNull { distances[it]!! }

        if (neighbourClosestToStart == null) return null

        return getShortestPath(visited + neighbourClosestToStart)
    }

    return getShortestPath()
}

fun computeDistancesFromStart(start: Position, nodes: Set<Position>): Map<Position, Int> {
    val distances = mutableMapOf<Position, Int>()
    nodes.forEach { distances[it] = Int.MAX_VALUE }
    distances[start] = 0

    val unvisited = nodes.toMutableSet()
    while (true) {
        if (unvisited.isEmpty() || unvisited.all { distances[it] == Int.MAX_VALUE }) break

        val currentNode = unvisited.minBy { position -> distances[position]!! }
        currentNode.directNeightbours
            .filter { unvisited.contains(it) }
            .forEach { neighbour ->
                distances[neighbour] = min(
                    distances[neighbour]!!,
                    distances[currentNode]!! + 1
                )
            }
        unvisited.remove(currentNode)
    }

    return distances
}
