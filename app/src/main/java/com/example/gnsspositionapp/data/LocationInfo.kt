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
        private val dateFormatter = DateTimeFormatter.ofPattern("HH時mm分ss秒")
        private const val METER_YARD_RATIO = 1.09361f
    }
    val yardAccuracy : Float
        get() = accuracy * METER_YARD_RATIO

    val yardDiff : Float?
        get() = (diff?: 0f ) * METER_YARD_RATIO

    val time: String
        get() = date.format(dateFormatter)

    fun toCSVFormat() = "$name,$date,$latitude,$longitude,$accuracy,$altitude"
}