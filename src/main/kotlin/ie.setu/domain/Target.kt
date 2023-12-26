package ie.setu.domain

import org.joda.time.DateTime

data class Target (var id: Int,
                   var targetSleep: Double,
                   var targetBmi: Double,
                   var date: DateTime,
                   var userId: Int)
