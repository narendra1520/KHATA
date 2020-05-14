package com.example.khata.api

import com.example.khata.pojo.Transact

class TransactionResponse(val isError: Boolean, val message: String, var data: List<Transact>)
