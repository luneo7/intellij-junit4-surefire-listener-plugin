package com.luneo7.junit4.intellij.state

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State

@State(
    name = "ListenersConfig"
)
open class ListenersConfig : PersistentStateComponent<ListenersConfig.PersistentState> {
    var persistentState: PersistentState = PersistentState()

    override fun getState(): PersistentState {
        return persistentState
    }

    override fun loadState(state: PersistentState) {
        persistentState = state
    }

    class PersistentState {
        var listeners = emptyList<String>()
    }
}