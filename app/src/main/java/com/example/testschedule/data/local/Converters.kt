package com.example.testschedule.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.testschedule.data.local.entity.account.group.GroupEntity
import com.example.testschedule.data.local.entity.account.profile.AccountProfileEntity
import com.example.testschedule.data.local.entity.account.mark_book.MarkBookEntity
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

    @TypeConverter
    fun listOfReferenceToJson(meaning: List<AccountProfileEntity.Reference>): String =
        jsonParser.toJson(
            meaning,
            object : TypeToken<ArrayList<AccountProfileEntity.Reference>>() {}.type
        ) ?: ""

    @TypeConverter
    fun listOfReferenceFromJson(json: String): List<AccountProfileEntity.Reference>? =
        jsonParser.fromJson<ArrayList<AccountProfileEntity.Reference>>(
            json,
            object : TypeToken<ArrayList<AccountProfileEntity.Reference>>() {}.type
        )

    @TypeConverter
    fun listOfSkillToJson(meaning: List<AccountProfileEntity.Skill>): String =
        jsonParser.toJson(
            meaning,
            object : TypeToken<ArrayList<AccountProfileEntity.Skill>>() {}.type
        ) ?: ""

    @TypeConverter
    fun listOfSkillFromJson(json: String): List<AccountProfileEntity.Skill>? =
        jsonParser.fromJson<ArrayList<AccountProfileEntity.Skill>>(
            json,
            object : TypeToken<ArrayList<AccountProfileEntity.Skill>>() {}.type
        )

    @TypeConverter
    fun listOfGroupInfoToJson(meaning: List<GroupEntity.GroupInfoStudent>): String =
        jsonParser.toJson(
            meaning,
            object : TypeToken<ArrayList<GroupEntity.GroupInfoStudent>>() {}.type
        ) ?: ""

    @TypeConverter
    fun listOfGroupInfoFromJson(json: String): List<GroupEntity.GroupInfoStudent>? =
        jsonParser.fromJson<ArrayList<GroupEntity.GroupInfoStudent>>(
            json,
            object : TypeToken<ArrayList<GroupEntity.GroupInfoStudent>>() {}.type
        )

    @TypeConverter
    fun studentGroupCuratorToJson(meaning: GroupEntity.StudentGroupCurator?): String =
        jsonParser.toJson(
            meaning,
            object : TypeToken<GroupEntity.StudentGroupCurator?>() {}.type
        ) ?: ""

    @TypeConverter
    fun studentGroupCuratorFromJson(json: String): GroupEntity.StudentGroupCurator? =
        jsonParser.fromJson<GroupEntity.StudentGroupCurator?>(
            json,
            object : TypeToken<GroupEntity.StudentGroupCurator?>() {}.type
        )
    @TypeConverter
    fun markBookMapToJson(meaning: Map<Int, MarkBookEntity.Semester>): String =
        jsonParser.toJson(
            meaning,
            object : TypeToken<Map<Int, MarkBookEntity.Semester>>() {}.type
        ) ?: ""

    @TypeConverter
    fun markBookMapFromJson(json: String): Map<Int, MarkBookEntity.Semester>? =
        jsonParser.fromJson<Map<Int, MarkBookEntity.Semester>>(
            json,
            object : TypeToken<Map<Int, MarkBookEntity.Semester>>() {}.type
        )



}