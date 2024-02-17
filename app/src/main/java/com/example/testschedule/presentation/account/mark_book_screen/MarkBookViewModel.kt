package com.example.testschedule.presentation.account.mark_book_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.model.account.mark_book.MarkBookModel
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.example.testschedule.domain.use_case.account.mark_book.GetMarkBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarkBookViewModel @Inject constructor(
    private val db: UserDatabaseRepository,
    private val getMarkBookUseCase: GetMarkBookUseCase
) : ViewModel() {

    val isLoading = mutableStateOf(false)

    val errorText = mutableStateOf("")
    val markBook = mutableStateOf<MarkBookModel?>(null)

    init {
        getMarkBook()
    }


    private fun getMarkBook() {
        viewModelScope.launch {
            markBook.value = db.getMarkBook()
        }
        getMarkBookUseCase().onEach { res ->
            when (res) {
                is Resource.Success -> {
                    isLoading.value = false
                    markBook.value = res.data
                    errorText.value = ""
                }

                is Resource.Error -> {
                    isLoading.value = false
                    errorText.value = res.message.toString()
                    if (errorText.value == "WrongPassword") {
                        viewModelScope.launch {
                            db.deleteUserBasicData()
                        }
                    }
                }

                is Resource.Loading -> {
                    isLoading.value = true
                    errorText.value = ""
                }
            }
        }.launchIn(viewModelScope)
    }
}