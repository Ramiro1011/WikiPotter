package com.example.wikipotter.ui

interface FilterListener {
    fun onFilterApplied(houses: List<String>, roles: List<String>)
    fun clearFilters()
    fun setFilters(houses: Map<String, Boolean>, roles: Map<String, Boolean>)
    fun getHouseFilters(): Map<String, Boolean>
    fun getRolesFilters(): Map<String, Boolean>
    fun seeFavorites()
}