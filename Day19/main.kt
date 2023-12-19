import java.io.File

var workflowMap: MutableMap<String,List<Rule>> = mutableMapOf()
data class Rule(var statement: String, var dest: String)

var partList: MutableList<xmas> = mutableListOf()
data class xmas(var x: Int, var m: Int, var a: Int, var s: Int)

var accepted: Long = 0L

var combinations: MutableMap<String,Long> = mutableMapOf(x to 1L, m to 1L, a to 1L, s to 1L)

fun main() {
    var inFile = File("tinput.txt")
    parseWorkflows(inFile)
    parseParts(inFile)
    //println(partList)

    // Part 1
    for(part in partList) {
        checkPart(part)
    }
    println(accepted)

    // Part 2
    workflowMap.forEach{ println(it)}
    var x = Pair(1,4000)
    var m = Pair(1,4000)
    var a = Pair(1,4000)
    var s = Pair(1,4000)
    var next = "in"
    while(true) {
        var workflow = workflowMap.get(next)!!
        next = checkPartInWorkflow2(x,m,a,s, workflow)
    }

}

fun checkPart(part: xmas) {
    var next = checkPartInWorkflow(part, workflowMap.get("in")!!)
    while(true){
        if (next == "A") {
            accepted += part.x + part.m + part.a + part.s
            return
        } else if( next == "R" ) {
            return
        } else {
            next = checkPartInWorkflow(part, workflowMap.get(next)!!)
        }
    }
}

fun checkPartInWorkflow(part: xmas, rules: List<Rule>): String {
    for (rule in rules) {
        if (rule.statement.isEmpty()) return rule.dest
        var rp = rule.statement.split('<', '>')
        var partVal = when(rp[0]) {
            "x" -> part.x
            "m" -> part.m
            "a" -> part.a
            "s" -> part.s
            else -> Int.MAX_VALUE
        }
        if('<' in rule.statement) {
            if (partVal < rp[1].toInt()) return rule.dest
        } else {
            if (partVal > rp[1].toInt()) return rule.dest
        }
    }
    return ""
}

// Part 2

fun checkPartInWorkflow2(x: IntRange, m: IntRange, a: IntRange, s: IntRange, rules: List<Rule>): String {
    for (rule in rules) {
        if (rule.statement.isEmpty()) return rule.dest
        var rp = rule.statement.split('<', '>')
        var partVal = when(rp[0]) {
            "x" -> x
            "m" -> m
            "a" -> a
            "s" -> s
            else -> Int.MAX_VALUE
        }
        if('<' in rule.statement) {
            if (partVal < rp[1].toInt()) return rule.dest
        } else {
            if (partVal > rp[1].toInt()) return rule.dest
        }
    }
    return ""
}


// Helper functions

fun parseWorkflows(inFile: File) {
    var inputs = inFile.readLines().takeWhile{ !it.isEmpty() }.toMutableList()
    for (input in inputs.map{ it.split('{', '}', ',').filter{ !it.isEmpty() }}) {
        var temp: MutableList<Rule> = mutableListOf()
        var t = input.subList(1, input.lastIndex).map{ it.split(':') }
        t.forEach{ temp.add(Rule(it[0], it[1]))}
        temp.add(Rule("", input.last())) // add final rule
        workflowMap.put(input[0], temp)
    }
}

fun parseParts(inFile: File) {
    var inputs = inFile.readLines().takeLastWhile{ !it.isEmpty() }.toMutableList()
    var temp: MutableList<List<Int>> = mutableListOf()
    inputs.forEach{ temp.add(it.trim{ it == '{' || it == '}' }.filter{ !(it in "xmas=") }.split(',').map{ it.toInt() }) }
    for (t in temp) {
        partList.add(xmas(t[0], t[1], t[2], t[3]))
    }
}