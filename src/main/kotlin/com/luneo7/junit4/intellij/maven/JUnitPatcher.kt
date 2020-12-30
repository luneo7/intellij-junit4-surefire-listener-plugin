package com.luneo7.junit4.intellij.maven

import com.intellij.execution.configurations.JavaParameters
import com.intellij.openapi.module.Module
import com.luneo7.junit4.intellij.state.ListenersConfig

class JUnitPatcher : com.intellij.execution.JUnitPatcher() {

    companion object {
        @JvmStatic
        val PARAMETER_NAME = "jlisteners"
    }

    override fun patchJavaParameters(module: Module?, javaParameters: JavaParameters?) {
        if (module != null) {
            val service = module.getService(ListenersConfig::class.java)
            if (service.state.listeners.isNotEmpty()) {
                javaParameters?.vmParametersList?.addProperty(PARAMETER_NAME, service.state.listeners.joinToString(","))
            } else {
                javaParameters?.vmParametersList?.addProperty(PARAMETER_NAME, "")
            }
        }
    }
}
