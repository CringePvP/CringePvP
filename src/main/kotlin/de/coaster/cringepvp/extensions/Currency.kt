package de.coaster.cringepvp.extensions

import de.moltenKt.core.extension.math.round
import kotlin.math.pow

class Currency(var value: Double, var abbreviationIndex: Int) {
    operator fun compareTo(other: Currency): Int {
        return if(this.abbreviationIndex == other.abbreviationIndex) {
            this.value.compareTo(other.value)
        } else {
            this.abbreviationIndex.compareTo(other.abbreviationIndex)
        }
    }

    val display get() = "${value.round(2).toString().replace(".0", "").replace(".00", "")} ${getAbbreviation(abbreviationIndex)}"

    override fun toString(): String {
        return display
    }

    init {
        while (value > 1000) {
            value /= 1000
            abbreviationIndex++
        }
    }

    companion object {
        fun convertBefore(valueLong: Long, abbreviationIndexParam: Int): Currency {
            var value = valueLong
            var abbreviationIndex = abbreviationIndexParam
            while (value > 1000) {
                value /= 1000
                abbreviationIndex++
            }
            return Currency(value.toDouble(), abbreviationIndex)
        }
    }
}


var fixedAbbrivations = listOf("", "K", "M", "B", "T")
val alphabeticAbbreviations = listOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z")

fun getAbbreviation(index: Int): String {
    return if (index < fixedAbbrivations.size) {
        fixedAbbrivations[index]
    } else {
        val sb = StringBuilder()
        val alphabethicIndex = (index - fixedAbbrivations.size)

        var indieces = listOf<Int>()
        var tempValue = alphabethicIndex

        // Convert the index to a list of indices smaller than 26
        while (tempValue >= 26) {
            indieces += tempValue % 26
            tempValue /= 26
        }
        indieces += tempValue

        indieces.reversed().forEach {
            sb.append(alphabeticAbbreviations[it])
        }
        sb.toString()
    }
}

infix fun Int.abbreviate(index: Int) = Currency(this.toDouble(), index)
infix fun Long.abbreviate(index: Int) = Currency.convertBefore(this, index)
infix fun Double.abbreviate(index: Int) = Currency(this, index)

infix operator fun Currency.plus(other: Currency): Currency {
    if (this.abbreviationIndex != other.abbreviationIndex) {
        // Convert one of the currencies to the one with the higher index
        val lowerValue = if (this.abbreviationIndex < other.abbreviationIndex) this else other
        val higherValue = if (this.abbreviationIndex > other.abbreviationIndex) this else other
        val higherIndex = if (this.abbreviationIndex > other.abbreviationIndex) this.abbreviationIndex else other.abbreviationIndex
        val lowerIndex = if (this.abbreviationIndex < other.abbreviationIndex) this.abbreviationIndex else other.abbreviationIndex

        val difference = higherIndex - lowerIndex
        // Convert the lower value to the higher value
        val convertedValue = lowerValue.value / 1000.0.pow(difference)

        return Currency(convertedValue + higherValue.value, higherIndex)
    }
    return Currency(this.value + other.value, this.abbreviationIndex)
}

infix operator fun Currency.minus(other: Currency): Currency {
    if (this.abbreviationIndex != other.abbreviationIndex) {
        // Convert one of the currencies to the one with the higher index
        val lowerValue = if (this.abbreviationIndex < other.abbreviationIndex) this else other
        val higherValue = if (this.abbreviationIndex > other.abbreviationIndex) this else other
        val higherIndex = if (this.abbreviationIndex > other.abbreviationIndex) this.abbreviationIndex else other.abbreviationIndex
        val lowerIndex = if (this.abbreviationIndex < other.abbreviationIndex) this.abbreviationIndex else other.abbreviationIndex

        val difference = higherIndex - lowerIndex
        // Convert the lower value to the higher value
        val convertedValue = lowerValue.value / 1000.0.pow(difference)

        return Currency(higherValue.value - convertedValue, higherIndex)
    }
    return Currency(this.value - other.value, this.abbreviationIndex)
}

infix operator fun Currency.times(other: Double): Currency {
    return Currency(this.value * other, this.abbreviationIndex)
}

infix operator fun Currency.times(other: Long): Currency {
    return Currency(this.value * other, this.abbreviationIndex)
}

infix operator fun Currency.times(other: Int): Currency {
    return Currency(this.value * other, this.abbreviationIndex)
}

infix operator fun Currency.div(other: Double): Currency {
    return Currency(this.value / other, this.abbreviationIndex)
}

infix operator fun Currency.div(other: Long): Currency {
    return Currency(this.value / other, this.abbreviationIndex)
}

infix operator fun Currency.div(other: Int): Currency {
    return Currency(this.value / other, this.abbreviationIndex)
}


fun main() {
    val currency = Currency(324.0, 0)
    val otherVal = 123.0 abbreviate 1

    val result = currency + otherVal
    println("${currency.display} + ${otherVal.display} = ${result.display}")

    val result2 = otherVal - currency
    println("${otherVal.display} - ${currency.display} = ${result2.display}")

    val result3 = currency * 123000.0
    println("${currency.display} * 123000 = ${result3.display}")
}