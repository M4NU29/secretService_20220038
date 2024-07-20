package com.example.secretservice_20220038.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Incident: RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var title: String = ""
    var date: String = ""
    var description: String = ""
    var image: ByteArray? = null
    var audioPath: String? = null
}
