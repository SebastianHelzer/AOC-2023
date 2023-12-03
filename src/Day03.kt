fun main() {

    fun getDigitsAndParts(input: List<String>): Pair<List<Triple<Int, Int, Char>>, List<Triple<Int, Int, Char>>> {
        val digitsAndParts = input.mapIndexed { row, s ->
            s.mapIndexed { column, c ->
                Triple(row, column, c)
            }
        }.flatten().filter { it.third != '.' }

        return digitsAndParts.partition { it.third.isDigit() }
    }

    fun positionsAroundPosition(position: Pair<Int, Int>): List<Pair<Int, Int>> {
        val (row, column) = position
        return listOf(
            row + 1 to column,
            row + 0 to column,
            row - 1 to column,
            row + 1 to column + 1,
            row + 0 to column + 1,
            row - 1 to column + 1,
            row + 1 to column - 1,
            row + 0 to column - 1,
            row - 1 to column - 1,
        )
    }

    fun getNumbersAdjacentToParts(numbers: List<Pair<List<Pair<Int, Int>>, Int>>, input: List<Triple<Int, Int, Char>>) : List<Int> {
        return numbers.filter {
            val positionsToCheck = it.first.map { position ->
                positionsAroundPosition(position)
            }.flatten().toSet()

            positionsToCheck.any { (row, colomn) ->
                input.firstOrNull { it.first == row && it.second == colomn }?.third?.let {
                    !it.isDigit() && it != '.'
                } ?: false
            }
        }.map { it.second }
    }

    fun mapDigitsToNumbers(digits: List<Triple<Int, Int, Char>>): List<Pair<List<Pair<Int, Int>>, Int>> {
        return digits.fold(listOf()) { acc, triple ->
            val row = triple.first
            val column = triple.second
            val digit = triple.third.digitToInt()
            val lastEntry = acc.lastOrNull()
            val lastPosition = lastEntry?.first?.lastOrNull()
            lastPosition?.let { (lastRow, lastColumn) ->
                if (lastRow == row && lastColumn == column - 1) {
                    val newValue = lastEntry.second * 10 + digit
                    return@fold acc.dropLast(1).plus(lastEntry.first.plus(row to column) to newValue)
                }
            }

            acc.plus(listOf(triple.first to triple.second) to triple.third.digitToInt())
        }
    }

    fun part1(input: List<String>): Int {
        val (digits, parts) = getDigitsAndParts(input)
        val numbers = mapDigitsToNumbers(digits)
        val filteredNumbers = getNumbersAdjacentToParts(numbers, parts)
        return filteredNumbers.sum()
    }

    fun getGearRatios(numbers: List<Pair<List<Pair<Int, Int>>, Int>>, parts: List<Triple<Int, Int, Char>>): List<Int> {
        return parts.filter { it.third == '*' }.mapNotNull {
            val positionsToCheck = positionsAroundPosition(it.first to it.second)
            val closeNumbers = positionsToCheck.mapNotNull { position ->
                numbers.firstOrNull { it.first.contains(position) }
            }.toSet().map { it.second }
            if (closeNumbers.size == 2) {
                closeNumbers.first() * closeNumbers.last()
            } else {
                null
            }
        }
    }

    fun part2(input: List<String>): Int {
        val (digits, parts) = getDigitsAndParts(input)
        val numbers = mapDigitsToNumbers(digits)
        val gearRatios = getGearRatios(numbers, parts)
        return gearRatios.sum()
    }

    println(part1(readInput("Day03_test")))
    println(part1(readInput("Day03")))
    println(part2(readInput("Day03_test")))
    println(part2(readInput("Day03")))
}