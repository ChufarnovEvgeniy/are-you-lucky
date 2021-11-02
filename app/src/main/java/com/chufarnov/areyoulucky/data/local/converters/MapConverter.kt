package com.chufarnov.areyoulucky.data.local.converters

import androidx.room.TypeConverter
import com.chufarnov.areyoulucky.domain.entities.MapEntity
import com.google.gson.Gson

class MapConverter {
    @TypeConverter
    fun fromMap(json: String): MapEntity {
        return Gson().fromJson(json, MapEntity::class.java)
    }

    @TypeConverter
    fun toMap(map: MapEntity): String {
        return Gson().toJson(map)
    }
}