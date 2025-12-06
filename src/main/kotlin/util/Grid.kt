package util

import ext.paddedLines
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.math.ceil

data class Bounds(val width: Int, val height: Int) {

    val positions = sequence<Position> {
        for (x in 0 until width) {
            for (y in 0 until height) {
                yield(Position(x, y))
            }
        }
    }

    val topLeft = Position(0, 0)
    val bottomRight = Position(width - 1, height - 1)

    val quadrants: List<Region>
        get() = listOf(
            Region(0, width / 2, 0, height / 2),
            Region(0, width / 2, ceil(height / 2.0).toInt(), height),
            Region(ceil(width / 2.0).toInt(), width, 0, height / 2),
            Region(ceil(width / 2.0).toInt(), width, ceil(height / 2.0).toInt(), height),
        )

    fun contains(position: Position): Boolean {
        return position.x in 0 until width &&
                position.y in 0 until height
    }

    fun loop(position: Position): Position {
        val x = position.x % width
        val y = position.y % height
        return Position(
            if (x < 0) width + x else x,
            if (y < 0) height + y else y
        )
    }
}

data class Region(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int) {
    val width get() = maxX - minX
    val height get() = maxY - minY

    fun contains(position: Position): Boolean {
        return position.x in minX until maxX &&
                position.y in minY until maxY
    }
}

/**
 * Grid with origin defined in the top left corner
 */
data class Grid<T> private constructor(val rows: List<MutableList<T>>) {

    val width = rows[0].size
    val height = rows.size

    val columns: List<List<T>>
        get() {
            return (0..<width).map { column -> rows.map { it[column] } }
        }

    val bounds get() = Bounds(width, height)

    val positions = sequence {
        for (x in 0..<width) {
            for (y in 0..<height) {
                yield(Position(x, y))
            }
        }
    }

    fun isInBounds(position: Position): Boolean = bounds.contains(position)

    fun getAtPosition(position: Position): T {
        return rows[position.y][position.x]
    }

    fun setAtPosition(position: Position, value: T) {
        rows[position.y][position.x] = value
    }

    fun getPositionsWhere(predicate: (position: Position) -> Boolean): List<Position> {
        val matchingCells = mutableListOf<Position>()

        positions.forEach { position ->
            if (predicate(position)) matchingCells.add(position)
        }

        return matchingCells
    }

    fun getPositionsWhereValue(predicate: (value: T) -> Boolean): List<Position> {
        return getPositionsWhere { position -> predicate(getAtPosition(position)) }
    }

    fun insertRow(index: Int, row: List<T>): Grid<T> {
        if (row.size != width) throw IllegalArgumentException("Inserted row (width ${row.size}) must be the same width as the grid (width $width)")
        return Grid.from(rows.take(index) + listOf(row) + rows.drop(index))
    }

    fun insertColumn(index: Int, column: List<T>): Grid<T> {
        if (column.size != height) throw IllegalArgumentException("Inserted column (height ${column.size}) must be the same height as the grid (height $height)")
        return Grid.from(
            rows.mapIndexed { rowIndex, row ->
                row.take(index) + listOf(column[rowIndex]) + row.drop(index)
            }
        )
    }

    fun invertRows(): Grid<T> {
        return Grid(rows.reversed())
    }

    fun print(): String {
        return print { null }
    }

    fun toCsv(): String {
        return rows
            .map { row -> row.joinToString(", ") }
            .joinToString("\n")
    }

    fun print(highlighter: (Position) -> T?): String {
        val stringBuilder = StringBuilder()
        for (y in 0 until height) {
            for (x in 0 until width) {
                val position = Position(x, y)
                val value = getAtPosition(position)
                val highlighted = highlighter(position)
                stringBuilder.append(highlighted ?: value)
            }
            stringBuilder.append("\n")
        }
        return stringBuilder.toString()
    }

    companion object {
        fun <T> from(rows: List<List<T>>): Grid<T> {
            return Grid(rows.map { it.toMutableList() })
        }

        fun fromString(input: String): Grid<Char> {
            val rows = input.paddedLines().map { it.toList() }
            return from(rows)
        }

        fun <T> fromString(input: String, transform: (Char) -> T): Grid<T> {
            val rows = input.paddedLines().map { line -> line.toCharArray().map(transform) }
            return from(rows)
        }

        fun <T> generate(bounds: Bounds, generator: (Position) -> T): Grid<T> {
            val rows = mutableListOf<MutableList<T>>()
            for (y in 0..<bounds.height) {
                rows.add(mutableListOf())
                for (x in 0..<bounds.width) {
                    rows[y].add(generator(Position(x, y)))
                }
            }
            return Grid(rows)
        }
    }
}

fun Grid<Char>.matchesKernelAtPosition(position: Position, kernel: Grid<Char>, wildcard: Char = '.'): Boolean {
    return kernel.positions.all { kernelPosition ->
        if (kernel.getAtPosition(kernelPosition) == wildcard) {
            true
        } else {
            val positionToCheck = position + kernelPosition.toOffset()
            isInBounds(positionToCheck) && getAtPosition(positionToCheck) == kernel.getAtPosition(kernelPosition)
        }
    }
}

fun Grid<Int>.matchesKernelAtPosition(position: Position, kernel: Grid<Int>): Boolean {
    return kernel.positions.all { kernelPosition ->
        val positionToCheck = position + kernelPosition.toOffset()
        isInBounds(positionToCheck) && getAtPosition(positionToCheck) == kernel.getAtPosition(kernelPosition)
    }
}

fun <T, R> Grid<T>.map(mapper: (Position, T) -> R): Grid<R> {
    return Grid.generate(bounds) { position ->
        mapper(position, getAtPosition(position))
    }
}

val blockDetectionKernel = Grid.from(
    listOf(
        listOf(1, 1, 1),
        listOf(1, 1, 1),
        listOf(1, 1, 1)
    )
)

val horizontalEdgeDetectionKernel = Grid.from(
    listOf(
        listOf(-1, -1, -1),
        listOf(0, 0, 0),
        listOf(1, 1, 1)
    )
)
val verticalEdgeDetectionKernel = Grid.from(
    listOf(
        listOf(-1, 0, 1),
        listOf(-1, 0, 1),
        listOf(-1, 0, 1)
    )
)

fun Grid<Int>.convolution(kernel: Grid<Int>): Grid<Int> {
    return map { gridPosition, value ->
        kernel.positions.sumOf { kernelPosition ->
            val positionToCheck = gridPosition + kernelPosition.toOffset()
            if (isInBounds(positionToCheck)) {
                getAtPosition(positionToCheck) * kernel.getAtPosition(kernelPosition)
            } else {
                0
            }
        }
    }
}

fun Grid<Int>.convolution(kernels: List<Grid<Int>>): Grid<Int> {
    val convolutedGrids = kernels.map { convolution(it) }
    return map { position, _ ->
        convolutedGrids.maxOf { it.getAtPosition(position) }
    }
}

fun Grid<Int>.totalAbsValue(): Int {
    return positions.sumOf { abs(getAtPosition(it)) }
}

fun Grid<Int>.renderToImage(file: File) {
    val image = BufferedImage(bounds.width, bounds.height, TYPE_INT_RGB)
    val white = Color(255, 255, 255)
    val black = Color(0, 0, 0)

    positions.forEach {
        if (getAtPosition(it) > 0) {
            image.setRGB(it.x, it.y, black.rgb)
        } else {
            image.setRGB(it.x, it.y, white.rgb)
        }
    }

    ImageIO.write(image, "png", file)
}

fun <T> Grid<T>.getContiguousGroups(): Set<Set<Position>> {
    val visited: MutableSet<Position> = HashSet(width * height)
    val groups = mutableSetOf<Set<Position>>()

    for (position in positions) {
        if (visited.contains(position)) continue

        val group = getContiguousGroup(position, getAtPosition(position))
        groups.add(group)
        visited.addAll(group)
    }

    return groups
}

fun <T> Grid<T>.getContiguousGroup(
    position: Position,
    value: T,
    visited: MutableSet<Position> = mutableSetOf<Position>(),
): Set<Position> {
    visited.add(position)

    val positionsToVisit = position.directNeightbours
        .filter { isInBounds(it) }
        .filter { getAtPosition(it) == value }
        .filter { !visited.contains(it) }

    positionsToVisit.forEach { positionToVisit -> getContiguousGroup(positionToVisit, value, visited) }

    return visited
}

fun <T> Grid<T>.getSubGrid(region: Region): Grid<T> =
    Grid.generate(Bounds(region.width, region.height)) {
        getAtPosition(it + Offset(region.minX, region.minY))
    }
