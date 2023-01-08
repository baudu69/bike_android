package fr.polytech.bike.data

import androidx.databinding.InverseMethod
import java.time.LocalDate

object Converter {
    @JvmStatic
    @InverseMethod("doubleToString")
    fun stringToDouble(value: String): Double {
        return value.toDouble()
    }

    @JvmStatic
    fun doubleToString(value: Double): String {
        return value.toString()
    }

    @JvmStatic
    @InverseMethod("localDateToString")
    fun stringToLocalDate(value: String): LocalDate {
        return try{ LocalDate.parse(value) } catch (e: Exception) { LocalDate.now() }
    }

    @JvmStatic
    fun localDateToString(value: LocalDate): String {
        return value.toString()
    }
}