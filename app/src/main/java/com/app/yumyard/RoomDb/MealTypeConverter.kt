package com.app.yumyard.RoomDb

import androidx.room.TypeConverters


@TypeConverters
class MealTypeConverter
{
    @androidx.room.TypeConverter
    fun fromAnyToString(attributes: Any?):String {
        if (attributes == null){
            return ""
        }
        return attributes as String
    }

    @androidx.room.TypeConverter
    fun stringToAny(attributes: String?):Any{
        if (attributes==null){
            return ""
        }
        return attributes
    }
}