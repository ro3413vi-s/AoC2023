import java.io.File

var moduleMap: MutableMap<String, Module> = mutableMapOf()
enum class moduleType{
    FLIP_FLOP, CONJUNCTION, BROADCAST
}
data class Module(val type: moduleType, val name: String, var destinations: MutableList<String>)

var pulseList: MutableList<Pulse> = mutableListOf()
enum class pulseType {
    LOW_PULSE, HIGH_PULSE
}
data class Pulse(var from: String, var to: String, var type: pulseType)


var flipFlopStates: MutableMap<String,Boolean> = mutableMapOf()
var conjunctionStates: MutableMap<String,MutableMap<String,pulseType>> = mutableMapOf()

var lowPulseCounter: Long = 0L
var highPulseCounter: Long = 0L

var lcmVals: MutableList<Pair<String,Long>> = mutableListOf()

fun main() {
    var inFile = File("input.txt")
    var inputs = inFile.readLines().takeWhile{ !it.isEmpty() }
    inputs.forEach{ parseIndata(it) }
    fixAllConjunctions()
    pressButtonRepeatedly(1000)
    println(lowPulseCounter*highPulseCounter)
    // Part 2 - set range to 1..10000
    pressButtonRepeatedly(4000)
    println(lcmVals)
    println(findLCMOfListOfNumbers(lcmVals.map{it.second+1000})) // add 1000 since we already pressed the button 1000 times for part 1
}

fun pressButtonRepeatedly(times: Long) {
    for(i in 1L..times) {
        if(pulseList.isEmpty()){
            pressButton()
        }
        var temp: MutableList<Pulse> = mutableListOf()
        while(!pulseList.isEmpty()) {
            for(pulse in pulseList) {
                if(pulse.type == pulseType.LOW_PULSE) {
                    lowPulseCounter++
                } else {
                    highPulseCounter++
                }
                if(!moduleMap.keys.contains(pulse.to)) {
                    // skip e.g output module
                    continue
                }

                var mod = moduleMap.get(pulse.to)!!
                if(mod.type == moduleType.FLIP_FLOP) {
                    if(pulse.type != pulseType.HIGH_PULSE) {
                        flipFlopStates[mod.name] = !flipFlopStates.get(mod.name)!!
                        for(dest in mod.destinations) {
                            temp.add(Pulse(mod.name, dest, if (flipFlopStates[mod.name]!!) pulseType.HIGH_PULSE else pulseType.LOW_PULSE))
                        }
                    }
                } else if(mod.type == moduleType.CONJUNCTION) {
                    if(mod.name == "gf" && pulse.type == pulseType.HIGH_PULSE) {
                        if(lcmVals.filter{ it.first == pulse.from }.isEmpty()){
                            lcmVals.add(Pair(pulse.from, i))
                        }
                    }
                    conjunctionStates[mod.name]!![pulse.from] = pulse.type
                    for(dest in mod.destinations) {
                        if(conjunctionStates[mod.name]!!.values.all { it == pulseType.HIGH_PULSE}) {
                            temp.add(Pulse(mod.name, dest, pulseType.LOW_PULSE))
                        } else {
                            temp.add(Pulse(mod.name, dest, pulseType.HIGH_PULSE))
                        }
                    }
                }
            }
            pulseList.clear()
            pulseList.addAll(temp)
            temp.clear()
        }
    }
}

// Helpers

fun fixAllConjunctions() {
    for (key in conjunctionStates.keys) {
        for (n in moduleMap.values.filter{ it.destinations.contains(key) }.map{ it.name }) {
            conjunctionStates[key]!!.put(n, pulseType.LOW_PULSE)
        }
    }
}

fun parseIndata(input: String) {
    var mod = input.split(" -> ")
    var dest = mod[1].split(", ").toMutableList()
    if (mod[0].contains('%')) {
        var name = mod[0].drop(1)
        moduleMap.put(name, Module(moduleType.FLIP_FLOP, name, dest))
        flipFlopStates.put(name, false)
    }else if (mod[0].contains('&')) {
        var name = mod[0].drop(1)
        moduleMap.put(name, Module(moduleType.CONJUNCTION, name, dest))
        conjunctionStates.put(name, mutableMapOf())
    } else {
        var name = mod[0]
        moduleMap.put(name, Module(moduleType.BROADCAST, name, dest))
    }
}

fun pressButton() {
    val tBroadcast = moduleMap.get("broadcaster")!!
    lowPulseCounter++ // button -> broadcast module
    tBroadcast.destinations.forEach { pulseList.add(Pulse(tBroadcast.name, it, pulseType.LOW_PULSE)) }
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