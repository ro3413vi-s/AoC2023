import java.io.File

var inputs: MutableList<String> = mutableListOf()
var hashMap: MutableMap<Int,MutableList<Pair<String, Int>>> = mutableMapOf()

fun main() {
    var inFile = File("input.txt")
    var acc = 0
    inFile.forEachLine{it.split(',').forEach{ acc += hashAlgorithm(it) }}
    println(acc)
    inFile.forEachLine{it.split(',').forEach{ hashMapThis(it) }}
    println(calculateHashMap())
}

fun hashAlgorithm(input: String): Int {
    var current = 0
    for(c in input) {
        current += c.code
        current *= 17
        current = current % 256
    }
    return current
}

fun hashMapThis(input: String) {
    if(input.contains('=')) {
        // insert
        var inHash = input.split('=')
        var hash = hashAlgorithm(inHash[0])
        if(hashMap.keys.contains(hash)) {
            if (hashMap[hash]?.any{ it.first == inHash[0]} ?: false) {
                hashMap[hash]!![hashMap[hash]!!.indexOfFirst{ it.first == inHash[0]}] = Pair(inHash[0], inHash[1].toInt())
            } else {
                hashMap[hash]?.add(Pair(inHash[0], inHash[1].toInt()))
            }
        } else {
            hashMap.put(hash, mutableListOf<Pair<String,Int>>(Pair(inHash[0], inHash[1].toInt())))
        }
    } else {
        // remove
        var inHash = input.split('-')
        var hash = hashAlgorithm(inHash[0])
        if(hashMap.keys.contains(hash) && hashMap[hash]?.any{ it.first == inHash[0]} ?: false) {
            hashMap[hash]?.removeAt(hashMap[hash]?.indexOfFirst{ it -> (it.first == inHash[0]) } ?: -1)
        }
    }
}

fun calculateHashMap(): Int {
    var accumulate = 0
    for (key in hashMap.keys) {
        if (hashMap[key]!!.isEmpty()) continue
        for (index in 0..(hashMap[key]!!.lastIndex)) {
            accumulate += (key+1) * (index+1) * hashMap[key]!![index].second
        }
    }
    return accumulate
}