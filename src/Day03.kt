fun main() {
    val input = readInput("Day03")

    Day03().part1(input).printlnWithPrefix("Part1")
    Day03().part2(input).printlnWithPrefix("Part2")
}

class Day03
{
    fun part1(lines: List<String>): Int
    {
        val numbersData = parseLines(lines)

        return numbersData.sumOf {
            if(isNumberAdjacentToASymbol(it, lines)) it.number else 0
        }
    }

    fun part2(lines: List<String>): Int
    {
        val numbersData = parseLines(lines)

        val starsData = mutableListOf<StarData>()
        lines.forEachIndexed { index, line ->
            val starPositions = findAllStarsPositions(line)
            if (starPositions.isEmpty()) return@forEachIndexed

            starPositions.forEach {
                starsData.add(StarData(index, it))
            }
        }

        return starsData.sumOf {
            val adjacentNumbers = findAllAdjacentNumbersForPosition(it, numbersData)
            if(adjacentNumbers.size != 2) return@sumOf 0

            adjacentNumbers.first() * adjacentNumbers.last()
        }
    }

    private fun parseLines(lines: List<String>): List<NumberData>
    {
        val numbersData = mutableListOf<NumberData>()

        val regex = "([0-9]+)".toRegex()
        lines.forEachIndexed { index, line ->
            val matches = regex.findAll(line)

            matches.forEach { match ->
                val number = match.value.toInt()
                val startIndex = match.range.first
                val endIndex = match.range.last

                numbersData.add(NumberData(index, startIndex, endIndex, number))
            }
        }

        return numbersData
    }

    private fun isNumberAdjacentToASymbol(numberData: NumberData, lines: List<String>): Boolean
    {
        val line = lines[numberData.lineIndex]
        val lineAbove = lines.getOrNull(numberData.lineIndex - 1)
        val lineUnder = lines.getOrNull(numberData.lineIndex + 1)

        // Same line :: startIndex -1 and endIndex +1
        if(line.getOrNull(numberData.startIndex - 1) != null && isSymbol(line[numberData.startIndex - 1])) return true
        if(line.getOrNull(numberData.endIndex + 1) != null && isSymbol(line[numberData.endIndex + 1])) return true

        for (i in (numberData.startIndex -1) .. (numberData.endIndex +1))
        {
            // line above :: startIndex -1 to endIndex +1
            if(lineAbove?.getOrNull(i) != null && isSymbol(lineAbove[i])) return true

            // line under :: startIndex -1 to endIndex +1
            if(lineUnder?.getOrNull(i) != null && isSymbol(lineUnder[i])) return true
        }

        return false
    }

    private fun findAllAdjacentNumbersForPosition(starData: StarData, numbersData: List<NumberData>): List<Int>
    {
        val adjacentNumbers = mutableListOf<Int>()

        val numbersInLine = numbersData.filter { it.lineIndex == starData.lineIndex }
        val numbersInLineAbove = numbersData.filter { it.lineIndex == starData.lineIndex - 1 }
        val numbersInLineUnder = numbersData.filter { it.lineIndex == starData.lineIndex + 1 }

        numbersInLine.forEach {
            if(starData.position - 1 == it.endIndex || starData.position + 1 == it.startIndex)
                adjacentNumbers.add(it.number)
        }

        numbersInLineAbove.forEach {
            val numberRange = (it.startIndex -1) .. (it.endIndex +1)
            if(numberRange.contains(starData.position)) adjacentNumbers.add(it.number)
        }

        numbersInLineUnder.forEach {
            val numberRange = (it.startIndex -1) .. (it.endIndex +1)
            if(numberRange.contains(starData.position)) adjacentNumbers.add(it.number)
        }

        return adjacentNumbers
    }

    private fun findAllStarsPositions(line: String): List<Int>
    {
        val indices = mutableListOf<Int>()

        var index = line.indexOf('*')
        while (index >= 0) {
            indices.add(index)
            index = line.indexOf('*', index + 1)
        }

        return indices
    }

    private fun isSymbol(char: Char) = !char.isDigit() && char != '.'
}

data class NumberData(
    val lineIndex: Int,
    val startIndex: Int,
    val endIndex: Int,
    val number: Int
)

data class StarData(
    val lineIndex: Int,
    val position: Int
)