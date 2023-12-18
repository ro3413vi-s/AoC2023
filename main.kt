import java.io.File

var digList: MutableList<Dig> = mutableListOf()
data class Dig(var dir: Char, var length: Int, var col: String)

var digMap: MutableMap<Int,MutableSet<Pair<Int,String>>> = mutableMapOf()

fun main() {
    var inFile = File("input.txt")
    inFile.forEachLine{ 
        var ins = it.split(' ')
        digList.add(Dig(ins[0].first(), ins[1].toInt(), ins[2])) 
    }
    println(digList)
    var prev = Pair(0,0)
    for (dig in digList) {
        when(dig.dir) {
            'R' -> repeat(dig.length) { 
                if (!digMap.containsKey(prev.first)) digMap.put(prev.first, mutableSetOf())
                digMap.get(prev.first)?.add(Pair(prev.second,dig.col)) 
                prev = Pair(prev.first, prev.second+1)
            }
            'L' -> repeat(dig.length) { 
                if (!digMap.containsKey(prev.first)) digMap.put(prev.first, mutableSetOf())
                digMap.get(prev.first)?.add(Pair(prev.second,dig.col)) 
                prev = Pair(prev.first, prev.second-1)
            }
            'U' -> repeat(dig.length) { 
                if (!digMap.containsKey(prev.first)) digMap.put(prev.first, mutableSetOf())
                digMap.get(prev.first)?.add(Pair(prev.second,dig.col)) 
                prev = Pair(prev.first-1, prev.second)
            }
            'D' -> repeat(dig.length) { 
                if (!digMap.containsKey(prev.first)) digMap.put(prev.first, mutableSetOf())
                digMap.get(prev.first)?.add(Pair(prev.second,dig.col)) 
                prev = Pair(prev.first+1, prev.second)
            }
            else -> println("ERROR no such direction")
        }
    }
    println(digMap)
    var size = 0
    for(key in digMap.keys){
        var row: List<Int> = digMap[key]?.map{it.first}!!.sorted()
        size += row.last()+1 - row.first()
    }
    println(size)
}