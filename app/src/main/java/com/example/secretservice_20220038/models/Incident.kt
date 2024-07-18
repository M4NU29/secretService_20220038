package com.example.secretservice_20220038.models

import io.realm.kotlin.types.RealmObject

class Incident: RealmObject {
    var title: String = ""
    var date: String = ""
    var description: String = ""
    var image: ByteArray? = null
    var audioPath: String? = null
}
