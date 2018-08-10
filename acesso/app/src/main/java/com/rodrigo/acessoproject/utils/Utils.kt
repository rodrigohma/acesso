package com.rodrigo.acessoproject.utils

import java.util.*

object Utils {

    private const val ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm"

    /**
     * Method to generate random text name.
     */
    fun generateName(): String {
        val random = Random()
        val sb = StringBuilder(20)
        for (i in 0 until 20)
            sb.append(ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)])
        return sb.toString()
    }

}