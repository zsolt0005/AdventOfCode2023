fun main() {
    val input = readInput("Day01")

    Day01().part1(input).printlnWithPrefix("Part1")
    Day01().part2(input).printlnWithPrefix("Part2")
}

class Day01
{
    private val digitToNumberMap = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9"
    )

    fun part1(input: List<String>): Int
    {
        val numbers = mutableListOf<Int>()

        input.forEach { line ->
            val lineNumbers = line
                .filter { it.isDigit() }
                .map { it.digitToInt() }

            val first = lineNumbers.first()
            val last = lineNumbers.last()
            numbers.add("$first$last".toInt())
        }

        return numbers.sum()
    }

    fun part2(input: List<String>): Int
    {
        val cleared = input.map { prepareLine(it) }

        return part1(cleared)
    }

    private fun prepareLine(line: String): String
    {
        var clearedLine = ""

        for (i in line.indices)
        {
            val char = line[i]

            if(char.isDigit())
            {
                clearedLine += "$char"
                continue
            }

            val sub = line.substring(i)
            for ((digit, number) in digitToNumberMap)
            {
                if(sub.startsWith(digit))
                {
                    clearedLine += number
                    break
                }
            }
        }

        return clearedLine
    }
}