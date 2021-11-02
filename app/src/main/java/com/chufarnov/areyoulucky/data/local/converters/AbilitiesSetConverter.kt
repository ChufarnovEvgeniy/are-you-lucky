package com.chufarnov.areyoulucky.data.local.converters

import androidx.room.TypeConverter
import com.chufarnov.areyoulucky.domain.entities.AbilitiesSetEntity
import com.google.gson.Gson

class AbilitiesSetConverter {
    @TypeConverter
    fun fromAbilitiesSet(json: String): AbilitiesSetEntity {
        return Gson().fromJson(json, AbilitiesSetEntity::class.java)
    }

    @TypeConverter
    fun toAbilitiesSet(abilitiesSet: AbilitiesSetEntity): String {
        return Gson().toJson(abilitiesSet)
    }
}