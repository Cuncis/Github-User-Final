package com.cuncisboss.consumerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cuncisboss.consumerapp.repository.ApiGithubRepository

@Suppress("UNCHECKED_CAST")
class ApiGithubViewModelFactory(private val repository: ApiGithubRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ApiGithubViewModel(
            repository
        ) as T
    }
}