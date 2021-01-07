package com.luneo7.junit4.intellij.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@State(name = "ListenersConfig")
public class ListenersConfig implements PersistentStateComponent<ListenersConfig.PersistentState> {

    private PersistentState persistentState = new PersistentState();

    @Override
    public @Nullable ListenersConfig.PersistentState getState() {
        return persistentState;
    }

    @Override
    public void loadState(@NotNull PersistentState state) {
        persistentState = state;

    }

    public static class PersistentState {
        private List<String> listeners = Collections.emptyList();

        public final List<String> getListeners() {
            return this.listeners;
        }

        public final void setListeners(List<String> listeners) {
            this.listeners = listeners;
        }
    }
}
