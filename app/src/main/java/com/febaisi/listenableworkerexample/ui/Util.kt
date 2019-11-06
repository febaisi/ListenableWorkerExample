package com.febaisi.listenableworkerexample.ui

import java.text.SimpleDateFormat
import java.util.*

fun convertTime(longDate: Long): String = SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Date(longDate))
