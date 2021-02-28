package com.example.mvvmnewsapp.db

import androidx.room.TypeConverter
import com.example.mvvmnewsapp.models.Source
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return Gson().toJson(source)
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Gson().fromJson(name, Source::class.java)
    }
}