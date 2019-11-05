package com.febaisi.listenableworkerexample.ui

import java.text.SimpleDateFormat
import java.util.*

fun convertTime(longDate: Long) = SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Date(longDate))
