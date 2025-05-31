package io.meli.vpn.serializers.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import kotlinx.serialization.serializer
import java.io.InputStream
import java.io.OutputStream

class KotlinSerializationDataStore<T>(
    val json: Json,
    val serializer: KSerializer<T>,
    val default: () -> T,
) : Serializer<T> {
    override val defaultValue: T get() = default()

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(input: InputStream): T {
        return try {
            json.decodeFromStream(serializer, input)
        } catch (e: SerializationException) {
            default()
//            throw CorruptionException("cant decode this input", e)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun writeTo(t: T, output: OutputStream) {
        withContext(Dispatchers.IO) {
            json.encodeToStream(serializer, t, output)
        }
    }
}

inline fun <reified T> createDatastore(
    json: Json,
    context: Context,
    preferencesName: String,
    noinline default: () -> T,
): DataStore<T> {
    return DataStoreFactory.create(
        serializer = KotlinSerializationDataStore(
            json = json,
            serializer = serializer<T>(),
            default = {
                // no data found
                default()
            }
        ),
        produceFile = {
            context.preferencesDataStoreFile(preferencesName)
        },
        corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = {
                // exception thrown during decoding
                default()
            }
        ),
    )
}