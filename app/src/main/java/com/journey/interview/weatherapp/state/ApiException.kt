package com.journey.interview.weatherapp.state

class ApiException(var code: Int, override var message: String) : RuntimeException()