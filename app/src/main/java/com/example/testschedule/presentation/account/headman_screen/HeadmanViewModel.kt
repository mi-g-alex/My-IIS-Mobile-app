package com.example.testschedule.presentation.account.headman_screen

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.account.headman.create_omissions.HeadmanGetOmissionsModel
import com.example.testschedule.domain.use_case.account.headman.create_omissions.GetOmissionsByDateUseCase
import com.example.testschedule.domain.use_case.account.headman.create_omissions.SaveOmissionsByDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HeadmanViewModel @Inject constructor(
    private val getOmissionsByDateUseCase: GetOmissionsByDateUseCase,
    private val saveOmissionsByDateUseCase: SaveOmissionsByDateUseCase
) : ViewModel() {
    val isLoading = mutableStateOf(false)
    val errorText = mutableStateOf("")

    private val cal = Calendar.getInstance().apply {
        GregorianCalendar(
            get(Calendar.YEAR),
            get(Calendar.MONTH),
            get(Calendar.DAY_OF_MONTH),
            0, 0, 0
        )
    }

    val selectedDate = mutableStateOf<DatePickerState>(DatePickerState(
        yearRange = ((cal.get(GregorianCalendar.YEAR) - 10)..
                (cal.get(GregorianCalendar.YEAR) + 10)),
        initialDisplayedMonthMillis = cal.timeInMillis,
        initialDisplayMode = DisplayMode.Picker,
        initialSelectedDateMillis = cal.timeInMillis,
        locale = Locale.getDefault()
    ))

    private val lastRequiredDate = mutableStateOf("")
    private val lrd = mutableStateOf(Date())

    val lessonsList = mutableStateListOf<HeadmanGetOmissionsModel.LessonModel>()
    val selectedOmissions = mutableStateMapOf<Int, MutableMap<Int, Int>>()
    val cnt = mutableIntStateOf(0)

    val savedLessons = mutableIntStateOf(-1)

    val isSaving = mutableStateOf(false)

    fun getOmissionsByDate(date: Date?) {
        val tmpDate = date?.let {
            SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault()
            ).format(it) + "T00:00:00.000Z"
        } ?: lastRequiredDate.value
        lessonsList.clear()
        cnt.intValue = 0
        lastRequiredDate.value = tmpDate
        if (date != null) {
            lrd.value = date
        }
        selectedOmissions.clear()
        getOmissionsByDateUseCase(tmpDate).onEach { res ->
            when (res) {
                is Resource.Success -> {
                    if (lastRequiredDate.value == res.data?.date) {
                        lessonsList.clear()
                        lessonsList.addAll(res.data.lessons)
                    }
                    isLoading.value = false
                    errorText.value = ""
                }

                is Resource.Loading -> {
                    isLoading.value = true
                    errorText.value = ""
                }

                is Resource.Error -> {
                    isLoading.value = false
                    errorText.value = res.message.toString()
                }
            }
        }.launchIn(viewModelScope)
    }

    fun saveOmissions(
        onSuccess: (lessonId: Int) -> Unit,
        onError: (lessonId: Int) -> Unit
    ) {
        savedLessons.intValue = 0
        val tmpOmissions = selectedOmissions.toMap()
        for (lessons in tmpOmissions) {
            saveOmissionsByDateUseCase(lessons.key, lessons.value).onEach { res ->
                when (res) {
                    is Resource.Success -> {
                        savedLessons.intValue++
                        isSaving.value = false
                        onSuccess(lessons.key)
                    }

                    is Resource.Loading -> {
                        isSaving.value = true
                    }
                    is Resource.Error -> {
                        savedLessons.intValue++
                        isSaving.value = false
                        onError(lessons.key)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}