package ie.setu.domain

import org.joda.time.DateTime

data class Achievement (var id: Int,
                  var name:String,
                  var rank: Int,
                  var date: DateTime,
                  var userId: Int
)