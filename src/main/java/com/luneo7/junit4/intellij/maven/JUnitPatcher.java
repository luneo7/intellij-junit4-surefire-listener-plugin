package com.luneo7.junit4.intellij.maven;

import com.intellij.execution.configurations.JavaParameters;
import com.intellij.openapi.module.Module;
import com.luneo7.junit4.intellij.state.ListenersConfig;
import org.jetbrains.annotations.Nullable;


public class JUnitPatcher extends com.intellij.execution.JUnitPatcher {
    private static final String PARAMETER_NAME = "jlisteners";

    @Override
    public void patchJavaParameters(@Nullable Module module, JavaParameters javaParameters) {
        if (module != null) {
            ListenersConfig service = module.getService(ListenersConfig.class);
            if (service != null && service.getState() != null && javaParameters != null && javaParameters.getVMParametersList() != null) {
                if (!service.getState().getListeners().isEmpty()) {
                    javaParameters.getVMParametersList().addProperty(PARAMETER_NAME, String.join(",", service.getState().getListeners()));
                } else {
                    javaParameters.getVMParametersList().addProperty(PARAMETER_NAME, "");
                }
            }
        }
    }
}
