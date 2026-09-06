# Keep line numbers so release crashes can be retraced with mapping.txt.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Gson reads these fields reflectively. Their JSON names must stay stable after
# obfuscation, including values persisted by Room type converters.
-keepattributes Signature
-keep,allowobfuscation class com.google.gson.reflect.TypeToken
-keep,allowobfuscation class * extends com.google.gson.reflect.TypeToken

-keep,allowobfuscation class com.example.testschedule.data.remote.dto.**
-keepclassmembers class com.example.testschedule.data.remote.dto.** {
    <fields>;
}

-keep,allowobfuscation class com.example.testschedule.data.local.entity.**
-keepclassmembers class com.example.testschedule.data.local.entity.** {
    <fields>;
}

-keep,allowobfuscation class com.example.testschedule.domain.model.schedule.ScheduleModel**
-keepclassmembers class com.example.testschedule.domain.model.schedule.ScheduleModel** {
    <fields>;
}
