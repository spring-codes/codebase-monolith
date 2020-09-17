package com.cheroliv.calculator

import java.util.*


class Calculator {

    companion object {
        private val OPS = listOf("-", "+", "*", "/")
    }

    private val stack: Deque<Number?> = LinkedList()


    fun value(): Number? {
        return stack.last
    }


    fun push(arg: Any?) {
        when {
            OPS.contains(arg) -> {
                var `val`: Double? = null
                val y = stack.removeLast()
                val x = when {
                    stack.isEmpty() -> 0
                    else -> stack.removeLast()!!
                }
                when {
                    y != null -> {
                        when (arg) {
                            "-" -> `val` = x.toDouble() - y.toDouble()
                            "+" -> `val` = x.toDouble() + y.toDouble()
                            "*" -> `val` = x.toDouble() * y.toDouble()
                            "/" ->`val` = x.toDouble() / y.toDouble()
                        }
                    }
                }
                push(`val`)
            }
            else -> stack += arg as Number?
        }
    }

}