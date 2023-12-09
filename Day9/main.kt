import java.io.File

fun main() {
    var inFile = File("res/input.txt")
    var lines = inFile.readLines()
    var numbers = lines.map{ extrapolate(it.split(' ')) }
    println(numbers.sum())
    var startNumbers = lines.map{ extrapolate(it.split(' ').reversed()) }
    println(startNumbers.sum())
}

fun extrapolate(input: List<String>): Int {
    var diff = input.map{it.toInt()}.toMutableList()
    diff = getDiffs(diff)
    return diff.last()
}

fun getDiffs(input: MutableList<Int>) : MutableList<Int> {
    if(input.filter{it != 0}.isEmpty()) {
        return input
    }
    var diffList: MutableList<Int> = mutableListOf()
    for (i in 1..input.lastIndex) {
        diffList.add(input[i] - input[i-1])
    }
    var below = getDiffs(diffList)
    input.add(input.last() + below.last())
    return input
}