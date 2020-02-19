package io.github.dnloop.noteapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tag(
    @PrimaryKey(autoGenerate = true)  var id : Int,
    var name : String
)
