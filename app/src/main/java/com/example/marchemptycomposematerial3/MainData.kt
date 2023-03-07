package com.example.marchemptycomposematerial3

import kotlinx.serialization.Serializable


@Serializable
data class Yelp(val businesses: List<Business> = emptyList(), val region : Region = Region(), val total : Int = 0)

@Serializable
data class Business(val id: String, val image_url: String, val name: String, val phone: String)

@Serializable
data class Region(val center: Center = Center())

@Serializable
data class Center(val latitude: Double = 27.96 , val longitude: Double = -82.45)
