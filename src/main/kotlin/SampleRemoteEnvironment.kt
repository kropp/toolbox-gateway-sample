package toolbox.gateway.sample

import com.jetbrains.toolbox.gateway.EnvironmentVisibilityState
import com.jetbrains.toolbox.gateway.RemoteProviderEnvironment
import com.jetbrains.toolbox.gateway.environments.EnvironmentContentsView
import com.jetbrains.toolbox.gateway.states.StateConsumer
import java.util.concurrent.CompletableFuture

class SampleRemoteEnvironment(
    private val environment: EnvironmentDTO
) : RemoteProviderEnvironment {
    override fun getId(): String = environment.id
    override fun getName(): String = environment.name

    override fun addStateListener(consumer: StateConsumer) {
    }

    override fun removeStateListener(consumer: StateConsumer) {
    }

    override fun getContentsView(): CompletableFuture<EnvironmentContentsView> {
        return CompletableFuture.completedFuture(SampleEnvironmentContentsView())
    }

    override fun setVisible(visibilityState: EnvironmentVisibilityState) {
    }
}