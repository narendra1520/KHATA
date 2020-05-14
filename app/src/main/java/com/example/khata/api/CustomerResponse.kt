package com.example.khata.api

import com.example.khata.pojo.Customer


class CustomerResponse(val isError: Boolean, val message: String, var data: List<Customer>)
