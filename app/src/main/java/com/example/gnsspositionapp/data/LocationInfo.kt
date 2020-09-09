package com.example.gnsspositionapp.data

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter


data class LocationInfo(
    val name : String,
    val date : LocalDateTime,
    val latitude : Double,
    val longitude : Double,
    val accuracy : Float,
    val diff : Float?,
    val altitude : Double
){

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("HH時mm分ss秒")
    }

    private val latitudeFormatted : String
        get() = "%.2f".format(latitude)

    private val longitudeFormatted : String
        get() = "%.2f".format(longitude)

    private val accuracyFormatted : String
        get() = "%.2f".format(accuracy)

    val yard : Float?
        get() = (diff?: 0f )*1.09361f

    val time: String
        get() = date.format(formatter)

    fun toCSVFormat() = "$name,$date,$latitudeFormatted,$longitudeFormatted,$accuracyFormatted,$accuracy,$altitude"
}