fun main() {
    val input = readInput("Day04")

    Day04().part1(input).printlnWithPrefix("Part1")
    Day04().part2(input).printlnWithPrefix("Part2")
}

class Day04
{
    fun part1(lines: List<String>): Int
    {
        var points = 0

        lines.forEach {
            val winningNumSize = getWinningNumbers(it)
            if(winningNumSize == 0) return@forEach

            var winningPoints = 1
            for (i in 1..< winningNumSize) winningPoints *= 2

            points += winningPoints
        }

        return points
    }

    fun part2(lines: List<String>): Int
    {
        val cardToCountMap = mutableMapOf<Int, Int>()
        lines.forEachIndexed { index, _ ->
            cardToCountMap[index + 1] = 1
        }

        lines.forEachIndexed { i, line ->
            val cardIndex = i + 1
            val carCount = cardToCountMap[cardIndex]!!

            val winningNumSize = getWinningNumbers(line)

            for (j in 1 .. winningNumSize)
            {
                cardToCountMap[cardIndex + j] = cardToCountMap[cardIndex + j]!! + (1 * carCount)
            }
        }

        return cardToCountMap.values.sum()
    }

    private fun getWinningNumbers(line: String): Int
    {
        val winnerNumbers = line
            .substringAfter(":")
            .substringBefore("|")
            .split(" ")
            .filter { num -> num.isNotEmpty() }
            .map { num -> num.toInt() }

        val yourNumbers = line
            .substringAfter("|")
            .split(" ")
            .filter { num -> num.isNotEmpty() }
            .map { num -> num.toInt() }

        return yourNumbers.filter { num -> winnerNumbers.contains(num) }.size
    }
}