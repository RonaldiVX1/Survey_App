package com.example.surveyapps.ui.others

sealed class Result {
    data class Success(val data: Any) : Result()
    data class Error(val error: String) : Result()
    object Loading : Result()
}