package com.luneo7.junit4.intellij.listeners

import com.intellij.rt.execution.junit.IDEAJUnitListenerEx
import org.junit.runner.Description
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunListener

class JUnitListener : IDEAJUnitListenerEx {

    companion object {
        @JvmStatic
        val PARAMETER_NAME = "jlisteners"
    }

    private var listeners: List<Any?> = emptyList()

    init {
        val property = System.getProperty(PARAMETER_NAME)
        if (property != null) {
            val split = property.split(",")
            listeners = split.map { c ->
                try {
                    Class.forName(c).newInstance()
                } catch (e: ClassNotFoundException) {
                    println("Class $c was not found, not listener will be used for that class")
                    null
                }
            }
        }
    }

    override fun testStarted(className: String?, methodName: String?) {
        listeners.forEach { l ->
            if (l is RunListener) {
                l.testStarted(Description.createTestDescription(className, methodName))
            }
        }
    }

    override fun testFinished(className: String?, methodName: String?, succeed: Boolean) {
        if (succeed) {
            testFinished(className!!, methodName!!)
        } else {
            listeners.forEach { l ->
                if (l is RunListener) {
                    l.testFailure(Failure(Description.createTestDescription(className, methodName), RuntimeException()))
                }
            }
        }
    }

    override fun testFinished(className: String, methodName: String) {
        listeners.forEach { l ->
            if (l is RunListener) {
                l.testFinished(Description.createTestDescription(className, methodName))
            }
        }

    }

    override fun testRunStarted(name: String?) {
        listeners.forEach { l ->
            if (l is RunListener) {
                l.testRunStarted(Description.createSuiteDescription(name))
            }
        }
    }

    override fun testRunFinished(name: String?) {
        listeners.forEach { l ->
            if (l is RunListener) {
                l.testRunFinished(org.junit.runner.Result())
            }
        }
    }
}