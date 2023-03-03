package com.example.myapplicationwithauthorization


class Data : ArrayList<DataItem>()

data class DataItem(
    val answer: String,
    val category: String,
    val question: String
)