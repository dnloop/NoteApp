package io.github.dnloop.noteapp.data

import java.sql.Timestamp

data class Note (
    var noteId : String,
    var title : String,
    var content : String,
    var category : Category,
    var tags : ArrayList<Tag>,
    var archived : Boolean,
    // CRUD values
    var deleted : Boolean,
    var createdAt : Timestamp,
    var modifiedAt : Timestamp,
    var deletedAt : Timestamp

)