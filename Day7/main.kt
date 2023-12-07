import java.io.File

data class Hand(var hand: String, var bid: Int)
var hands: ArrayList<Hand> = arrayListOf()

val cards: List<Char> = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
val cards2: List<Char> = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

fun main() {
    var inFile = File("res/input.txt")
    inFile.forEachLine{ hands.add(Hand(it.split(' ')[0], it.split(' ')[1].toInt())) }
    var mainComparator = compareBy<Hand>{ type(it) }
    hands.sortWith(mainComparator.thenByDescending{cardValue(it, 0, cards)}.thenByDescending{cardValue(it, 1, cards)}
        .thenByDescending{cardValue(it, 2, cards)}.thenByDescending{cardValue(it, 3, cards)}.thenByDescending{cardValue(it, 4, cards)})
    println(hands.mapIndexed{index, it -> (index+1)*it.bid}.sum())
    
    var mainComparator2 = compareBy<Hand>{ type2(it) }
    hands.sortWith(mainComparator2.thenByDescending{cardValue(it, 0, cards2)}.thenByDescending{cardValue(it, 1, cards2)}
        .thenByDescending{cardValue(it, 2, cards2)}.thenByDescending{cardValue(it, 3, cards2)}.thenByDescending{cardValue(it, 4, cards2)})
    println(hands.mapIndexed{index, it -> (index+1)*it.bid}.sum())
}

fun cardValue(hand: Hand, index: Int, cardValues: List<Char>): Int {
    return cardValues.indexOf(hand.hand[index])
}

fun parseHand(hand: String, cardList: List<Char>): MutableList<Pair<Char,Int>> {
    var temp = mutableListOf<Pair<Char,Int>>()
    for( card in cardList ) {
        temp.add(Pair(card, hand.count({ it == card })))
    }
    return temp
}

// Part 1

fun type(hand: Hand): Int {
        var cards = parseHand(hand.hand, cards)
        if(cards.any({ it.second == 5 })){
            return 7
        } else if(cards.any({ it.second == 4})) {
            return 6
        } else if(cards.any({ it.second == 3}) && cards.any({ it.second == 2 })) {
            return 5
        } else if(cards.any({ it.second == 3})) {
            return 4
        } else if(cards.filter{ it.second == 2 }.size == 2) {
            return 3
        } else if(cards.any({ it.second == 2})) {
            return 2
        } else {
            return 1
        }
}

// Part 2

fun type2(hand: Hand): Int {
        var cards = parseHand(hand.hand, cards2)
        var jokers = cards[cards.lastIndex].second
        cards = cards.dropLast(1).toMutableList()
        if(cards.any({ it.second + jokers == 5 })) {
            return 7
        } else if(cards.any({ it.second + jokers == 4})) {
            return 6
        } else if((cards.any({ it.second == 3}) && cards.any({ it.second == 2 })) ||
                  (jokers == 1 && cards.filter{ it.second == 2 }.size == 2)) {
            return 5
        } else if(cards.any({ it.second + jokers == 3})) {
            return 4
        } else if(cards.filter{ it.second == 2 }.size == 2) {
            return 3
        } else if(cards.any({ it.second + jokers == 2 })) {
            return 2
        } else {
            return 1
        }
}