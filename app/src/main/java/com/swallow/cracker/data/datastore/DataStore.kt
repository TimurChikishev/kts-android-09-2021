package com.swallow.cracker.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


private const val DATASTORE_SETTINGS = "DATASTORE_SETTINGS"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_SETTINGS)
