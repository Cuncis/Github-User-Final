package com.cuncisboss.githubuserfinal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cuncisboss.githubuserfinal.repository.ApiGithubRepository

@Suppress("UNCHECKED_CAST")
class ApiGithubViewModelFactory(private val repository: ApiGithubRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ApiGithubViewModel(
            repository
        ) as T
    }
}