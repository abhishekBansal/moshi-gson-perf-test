package com.abhishek.moshigsonperftest

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.junit.Test
import java.io.File
import java.lang.reflect.Type
import java.net.URLDecoder

class MoshiGsonPerfTest {
    private val moshi = Moshi.Builder().build()
    private val gson = Gson()

    @Test
    fun measureGsonDeserialization() {
        val mockJson = getJson()
        measureAverage("GSON-D") {
            val listType: Type = object : TypeToken<List<TestModel>>() {}.type
            gson.fromJson(mockJson, listType)
        }
    }

    @Test
    fun measureGsonSerialization() {
        val mockJson = getJson()
        val listType: Type = object : TypeToken<List<TestModel>>() {}.type
        val list: List<TestModel> = gson.fromJson(mockJson, listType)

        measureAverage("GSON-S") {
            gson.toJson(list)
        }
    }

    @Test
    fun measureMoshiDeserialization() {
        val mockJson = getJson()
        measureAverage("MOSHI-D") {
            val adapter = moshi.adapter<List<TestModel>>(
                Types.newParameterizedType(
                    List::class.java,
                    TestModel::class.java
                )
            )
            adapter.fromJson(mockJson)
        }
    }

    @Test
    fun measureMoshiSerialization() {
        val mockJson = getJson()
        val listType: Type = object : TypeToken<List<TestModel>>() {}.type
        val list: List<TestModel> = gson.fromJson(mockJson, listType)

        measureAverage("MOSHI-S") {
            val adapter = moshi.adapter<List<TestModel>>(
                Types.newParameterizedType(
                    List::class.java,
                    TestModel::class.java
                )
            )
            adapter.toJson(list)
        }
    }

    private fun getJson(fileName: String = "mock_data.json"): String {
        val classLoader = javaClass.classLoader
        val url = URLDecoder.decode(classLoader?.getResource(fileName)?.file, "UTF-8")
        return File(url).readText()
    }

    private fun measure(tag: String, code: () -> Unit): Long {
        val start = System.currentTimeMillis()
        code()
        val time = System.currentTimeMillis() - start
//        println("$tag $time")
        return time
    }

    private fun measureAverage(tag: String, times: Int = 10, code: () -> Unit) {
        var time = 0L
        for (i in 1..times) {
            time += measure(tag, code)
        }

        print("$tag Average Time(ms): ${time/times}")
    }
}
