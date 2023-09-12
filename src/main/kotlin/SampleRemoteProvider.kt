package toolbox.gateway.sample

import com.jetbrains.toolbox.gateway.ProviderVisibilityState
import com.jetbrains.toolbox.gateway.RemoteEnvironmentConsumer
import com.jetbrains.toolbox.gateway.RemoteProvider
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.okio.decodeFromBufferedSource
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import java.net.URI
import kotlin.time.Duration.Companion.seconds

class SampleRemoteProvider(
    private val httpClient: OkHttpClient,
    private val consumer: RemoteEnvironmentConsumer,
    coroutineScope: CoroutineScope,
) : RemoteProvider {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        coroutineScope.launch {
            val request = Request.Builder()
                .get()
                .url("https://kropp.dev/gateway.json")
//                .cacheControl(CacheControl.FORCE_NETWORK)
                .build()
            while (true) {
                try {
                    logger.debug("Updating remote environments for Sample Plugin")
                    val response = httpClient.newCall(request).await()
                    val body = response.body ?: continue
                    val dto = Json.decodeFromBufferedSource(EnvironmentsDTO.serializer(), body.source())
                    try {
                        consumer.consumeEnvironments(dto.environments.map { SampleRemoteEnvironment(it) })
                    } catch (_: CancellationException) {
                        logger.debug("Environments update cancelled")
                        break
                    }
                } catch (e: Exception) {
                    logger.warn("Failed to retrieve environments: ${e.message}")
                }
                // only for demo purposes!
                delay(3.seconds)
            }
        }
    }

    override fun close() {}

    override fun getName(): String = "Sample Provider"
    override fun getSvgIcon(): ByteArray {
        return this::class.java.getResourceAsStream("/icon.svg")?.readAllBytes() ?: byteArrayOf()
    }

    override fun canCreateNewEnvironments(): Boolean = true
    override fun isSingleEnvironment(): Boolean = false

    override fun setVisible(visibilityState: ProviderVisibilityState) {}

    override fun addEnvironmentsListener(listener: RemoteEnvironmentConsumer) {}
    override fun removeEnvironmentsListener(listener: RemoteEnvironmentConsumer) {}

    override fun handleUri(uri: URI) {
        logger.debug("External request: {}", uri)
    }
}
