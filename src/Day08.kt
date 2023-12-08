fun main() {
    val input = readInput("Day08")

    Day08().part1(input).printlnWithPrefix("Part1")
    Day08().part2(input).printlnWithPrefix("Part2")
}

class Day08
{
    fun part1(lines: List<String>): Int
    {
        val gameData = prepareGameData(lines)

        val instructions = gameData.first
        val nodes = gameData.second

        var steps = 0
        var currentNode = "AAA"

        while (true)
        {
            val instruction = instructions[steps % instructions.length]
            currentNode = if(instruction == 'L') nodes[currentNode]!!.first else nodes[currentNode]!!.second

            steps++

            if(currentNode == "ZZZ") break
        }

        return steps
    }

    fun part2(lines: List<String>): Long
    {
        val gameData = prepareGameData(lines)

        val instructions = gameData.first
        val nodes = gameData.second

        var steps = 0
        var currentNodes = nodes.keys.filter { it.endsWith("A") }
        val minSteps = LongArray(currentNodes.size) { -1 }

        while (minSteps.any { it < 0 })
        {
            currentNodes = when(instructions[steps % instructions.length])
            {
                'L' -> currentNodes.map { nodes[it]!!.first }
                else -> currentNodes.map { nodes[it]!!.second }
            }

            steps++

            currentNodes.forEachIndexed { index, node ->
                if(node.last() == 'Z' && minSteps[index] < 0)
                {
                    minSteps[index] = steps.toLong()
                }
            }
            if(steps % 10000000 == 0) println("Steps: $steps")
        }

        var result = 1L
        for (num in minSteps) {
            result = lcm(result, num)
        }

        return result
    }

    private fun gcd(a: Long, b: Long): Long {
        var num1 = a
        var num2 = b
        while (num1 != num2) {
            if (num1 > num2) {
                num1 -= num2
            } else {
                num2 -= num1
            }
        }
        return num1
    }

    private fun lcm(a: Long, b: Long) = a * b / gcd(a, b)

    private fun prepareGameData(lines: List<String>): Pair<String, Map<String, Pair<String, String>>>
    {
        val instructions = lines.first()
        val nodeDefinitions = lines.drop(2).map { it.replace(" ", "") }

        val nodesData = mutableMapOf<String, Pair<String, String>>()
        nodeDefinitions.forEach {
            val nodeName = it.substringBefore("=")

            val lrNodes = it.substringAfter("=")
                .replace("(", "")
                .replace(")", "")
                .split(",")

            nodesData[nodeName] = Pair(lrNodes.first(), lrNodes.last())
        }

        return instructions to nodesData
    }
}