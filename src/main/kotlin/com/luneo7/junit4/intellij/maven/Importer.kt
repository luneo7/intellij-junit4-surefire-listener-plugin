package com.luneo7.junit4.intellij.maven

import com.intellij.openapi.externalSystem.service.project.IdeModifiableModelsProvider
import com.intellij.openapi.module.Module
import org.jetbrains.idea.maven.importing.MavenImporter
import org.jetbrains.idea.maven.importing.MavenRootModelAdapter
import org.jetbrains.idea.maven.project.MavenProject
import org.jetbrains.idea.maven.project.MavenProjectChanges
import org.jetbrains.idea.maven.project.MavenProjectsProcessorTask
import org.jetbrains.idea.maven.project.MavenProjectsTree
import com.luneo7.junit4.intellij.state.ListenersConfig

class Importer : MavenImporter("org.apache.maven.plugins", "maven-surefire-plugin") {

    override fun preProcess(
        module: Module?,
        mavenProject: MavenProject?,
        changes: MavenProjectChanges?,
        modifiableModelsProvider: IdeModifiableModelsProvider?
    ) {
    }

    override fun process(
        modifiableModelsProvider: IdeModifiableModelsProvider?,
        module: Module?,
        rootModel: MavenRootModelAdapter?,
        mavenModel: MavenProjectsTree?,
        mavenProject: MavenProject?,
        changes: MavenProjectChanges?,
        mavenProjectToModuleName: MutableMap<MavenProject, String>?,
        postTasks: MutableList<MavenProjectsProcessorTask>?
    ) {
        if (module != null) {
            val service = module.getService(ListenersConfig::class.java)

            if (mavenProject != null) {
                val surefire = mavenProject.plugins
                    .first { m -> "maven-surefire-plugin" == m.artifactId }

                if (surefire != null &&
                    surefire.configurationElement != null &&
                    surefire.configurationElement!!.children != null
                ) {

                    val listener = surefire.configurationElement!!
                        .children
                        .filter { e -> e.name == "properties" }
                        .flatMap { e -> e.children }
                        .filter { e -> e.children.any { e1 -> e1.name == "name" && e1.value == "listener" } }
                        .flatMap { e -> e.children.filter { e1 -> e1.name == "value" }.map { e1 -> e1.value } }
                        .first()

                    if (listener != null) {
                        service.state.listeners = listener.split(",").map { e -> e.trim() }
                        return
                    }
                }
            }
            service.state.listeners = emptyList()
        }
    }
}