package com.example.testschedule.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.testschedule.data.util.JsonParser
import com.example.testschedule.domain.model.schedule.ScheduleModel
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class Converters(
    private val jsonParser: JsonParser
) {

    @TypeConverter
    fun employeeInfoToJson(meaning: ScheduleModel.EmployeeInfo?): String =
        jsonParser.toJson(
            meaning,
            object : TypeToken<ScheduleModel.EmployeeInfo>() {}.type
        ) ?: ""

    @TypeConverter
    fun employeeInfoFromJson(json: String): ScheduleModel.EmployeeInfo? =
        jsonParser.fromJson<ScheduleModel.EmployeeInfo>(
            json,
            object : TypeToken<ScheduleModel.EmployeeInfo>() {}.type
        )

    @TypeConverter
    fun studentGroupInfoToJson(meaning: ScheduleModel.StudentGroupInfo?): String =
        jsonParser.toJson(
            meaning,
            object : TypeToken<ScheduleModel.StudentGroupInfo>() {}.type
        ) ?: ""

    @TypeConverter
    fun studentGroupInfoFromJson(json: String): ScheduleModel.StudentGroupInfo? =
        jsonParser.fromJson<ScheduleModel.StudentGroupInfo>(
            json,
            object : TypeToken<ScheduleModel.StudentGroupInfo>() {}.type
        )

    @TypeConverter
    fun listOfWeeksScheduleToJson(meaning: List<ScheduleModel.WeeksSchedule>): String =
        jsonParser.toJson(
            meaning,
            object : TypeToken<ArrayList<ScheduleModel.WeeksSchedule>>() {}.type
        ) ?: ""

    @TypeConverter
    fun listOfWeeksScheduleFromJson(json: String): List<ScheduleModel.WeeksSchedule> =
        jsonParser.fromJson<ArrayList<ScheduleModel.WeeksSchedule>>(
            json,
            object : TypeToken<ArrayList<ScheduleModel.WeeksSchedule>>() {}.type
        ) ?: emptyList()


    @TypeConverter
    fun listOfLessonsToJson(meaning: List<ScheduleModel.WeeksSchedule.Lesson>?): String =
        jsonParser.toJson(
            meaning,
            object : TypeToken<ArrayList<ScheduleModel.WeeksSchedule.Lesson>>() {}.type
        ) ?: ""

    @TypeConverter
    fun listOfLessonsFromJson(json: String): List<ScheduleModel.WeeksSchedule.Lesson>? =
        jsonParser.fromJson<ArrayList<ScheduleModel.WeeksSchedule.Lesson>>(
            json,
            object : TypeToken<ArrayList<ScheduleModel.WeeksSchedule.Lesson>>() {}.type
        )

    @TypeConverter
    fun listOfStringToJson(meaning: List<String>?): String =
        jsonParser.toJson(
            meaning,
            object : TypeToken<ArrayList<String>>() {}.type
        ) ?: ""

    @TypeConverter
    fun listOfStringFromJson(json: String): List<String>? =
        jsonParser.fromJson<ArrayList<String>>(
            json,
            object : TypeToken<ArrayList<String>>() {}.type
        )

}