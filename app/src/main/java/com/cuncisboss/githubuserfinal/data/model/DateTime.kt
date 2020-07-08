package com.cuncisboss.githubuserfinal.data.model

data class DateTime(
    var hour: String,
    var minute: String
) {
    override fun toString(): String {
        return "$hour:$minute"
    }
}