package com.abhishek.moshigsonperftest

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TestModel(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val email: String,
    val gender: String,
    val ip_address: String,
    val avatar: String?,
    val active: Boolean,
    val age: Int
)