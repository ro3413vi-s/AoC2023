import java.io.File

lateinit var LRinput: String
data class Node(var left: String, var right: String)
var nodes: MutableMap<String,Node> = mutableMapOf()

var nodeCycles: MutableMap<String, MutableSet<Pair<String,Long>>> = mutableMapOf()

fun main() {
    var inFile = File("res/input.txt")
    var inputs = inFile.readLines().toMutableList()
    LRinput = inputs[0]
    inputs.drop(2).forEach{ createNode(it) }
    // Part 1
    var current = Pair("AAA", 0)
    while (current.first != "ZZZ"){
        current  = traversePath(index = 0, current.first, current.second)
    }
    println(current)
    // Part 2
    var starts = nodes.keys.filter{ it[2] == 'A' }
    for (start in starts) {
        nodeCycles.put(start, mutableSetOf())
        var currentC: Pair<String,Long> = Pair(start, 0L)
        var visited: MutableList<String> = mutableListOf()
        while (!visited.contains(currentC.first)){
            visited.add(currentC.first)
            currentC = findCycle(index = 0, currentC.first, currentC.second, start)
        }
    }
    var cycles = nodeCycles.values.map{it.first().second}
    println(findLCMOfListOfNumbers(cycles))
}

fun createNode(input: String) {
    var t = input.split(" = ")
    val name = t[0]
    var node = t[1].split(',')
    nodes.put(name, Node(node[0].drop(1).trim(),node[1].dropLast(1).trim()))
}

// Part 1

// returns pair<Current node name, number of steps>
fun traversePath(index: Int, current: String, steps: Int): Pair<String,Int> {
    if (current == "ZZZ" || index == LRinput.length) {
        return Pair(current, steps)
    }

    var cNode: Node = nodes.getValue(current)
    return traversePath(index+1, if (LRinput[index] == 'L') cNode.left else cNode.right, steps+1)   
}

// Part 2

fun findCycle(index: Int, current: String, steps: Long, startNode: String): Pair<String,Long> {
    if (current[2] == 'Z') {
        nodeCycles[startNode]!!.add(Pair(current,steps))
    }
    if(index == LRinput.length) {
        return Pair(current, steps)
    }
    var cNode: Node = nodes.getValue(current)
    return findCycle(index+1, if (LRinput[index] == 'L') cNode.left else cNode.right, steps+1, startNode)
}

fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
    var result = numbers[0]
    for (i in 1 until numbers.size) {
        result = findLCM(result, numbers[i])
    }
    return result
}
