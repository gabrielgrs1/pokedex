package com.gabrielgrs1.pokedex.domain.converters

import androidx.room.TypeConverter

class ListStringConverter {

    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(",")
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }
}
