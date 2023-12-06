import java.io.File

var times: MutableList<Long> = mutableListOf()
var distances: MutableList<Long> = mutableListOf()

var oneTime: Long = 0
var oneDistance: Long = 0

fun main() {
    var inFile = File("res/input.txt")
    var lines = inFile.readLines()
    times.addAll(lines[0].split(':')[1].trim().split(' ').filter{ !it.isEmpty() }.map{ it.toLong() })
    distances.addAll(lines[1].split(':')[1].trim().split(' ').filter{ !it.isEmpty() }.map{ it.toLong() })
    println(times)
    println(distances)
    var res: Long = 1
    for (i in 0..times.lastIndex){
        res = res * searchTimes(times[i], distances[i])
    }
    println(res)
    oneTime = times.map{ it.toString() }.reduce{a, it -> a + it}.toLong()
    oneDistance = distances.map{ it.toString() }.reduce{a, it -> a + it}.toLong()
    println(oneTime)
    println(oneDistance)
    println(searchTimes(oneTime, oneDistance))
}

fun searchTimes(time: Long, distance: Long): Long {
    var timeRange = 0..time
    var wins: Long = 0
    for (i in timeRange) {
        var score = calculateScore(i, time-i)
        //var highScore = calculateScore(time-i, i)
        if (score > distance) {
            wins++
        }
    }
    return wins
}

fun calculateScore(speed: Long, time: Long): Long {
    return speed*time
}