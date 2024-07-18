package com.example.secretservice_20220038

import android.app.Application
import com.example.secretservice_20220038.models.Incident
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class App: Application() {
    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()
        realm = Realm.open(
            RealmConfiguration.create(
                setOf(Incident::class)
            )
        )
    }
}
