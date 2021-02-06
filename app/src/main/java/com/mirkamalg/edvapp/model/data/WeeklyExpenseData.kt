package com.mirkamalg.edvapp.model.data

/**
 * Created by Mirkamal on 02 February 2021
 */
data class WeeklyExpenseData(
    val favoriteMarket: String?,
    val favoriteGoods: List<String>,
    val expensesOfWeekdays: List<Double>
)
