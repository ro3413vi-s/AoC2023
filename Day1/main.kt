import java.io.File

var acumulate: Long = 0
var numbers = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9",
    "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
var numberMap: Map<String, String> = mapOf("one" to "1", "two" to "2", "three" to "3", "four" to "4", "five" to "5",
    "six" to "6", "seven" to "7", "eight" to "8", "nine" to "9")

fun main() {
    var infile = File("res/input1.txt")
    infile.forEachLine{ findNumbers(it) }
    println(acumulate)
}

fun findNumbers(line: String) {
    var first: String = line.findAnyOf(strings = numbers, ignoreCase = true)?.second ?: ""
    var last: String = line.findLastAnyOf(strings = numbers, ignoreCase = true)?.second ?: ""
    first = numberMap.get(first) ?: first
    last = numberMap.get(last) ?: last
    try {
        acumulate += (first + last).toInt()
    } catch ( e: Exception) {
        println("no element in " + line)
    }
}