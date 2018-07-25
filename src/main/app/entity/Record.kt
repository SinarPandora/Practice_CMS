package app.entity

import java.io.Serializable

data class Record(
        var time: String,
        var user: String,
        var info: String,
        var state: String,
        var id: Int
) : Serializable
