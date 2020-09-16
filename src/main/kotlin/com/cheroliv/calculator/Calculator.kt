package com.cheroliv.calculator

import java.util.*


class Calculator {
    private val stack: Deque<Number?> = LinkedList()

    fun push(arg: Any?) {
        when {
            OPS.contains(arg) -> {
                val y = stack.removeLast()
                val x = when {
                    stack.isEmpty() -> 0
                    else -> stack.removeLast()!!
                }
                var `val`: Double? = null
                when {
                    y != null -> when (arg) {
                        "-" -> `val` = x.toDouble() - y.toDouble()
                        "+" -> `val` = x.toDouble() + y.toDouble()
                        "*" -> `val` = x.toDouble() * y.toDouble()
                        "/" ->`val` = x.toDouble() / y.toDouble()
                    }
                }
                push(`val`)
            }
            else -> stack += arg as Number?
        }
    }

    fun value(): Number? {
        return stack.last
    }

    companion object {
        private val OPS = listOf("-", "+", "*", "/")
    }
}