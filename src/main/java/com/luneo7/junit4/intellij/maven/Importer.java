package com.luneo7.junit4.intellij.maven;

import com.intellij.openapi.externalSystem.service.project.IdeModifiableModelsProvider;
import com.intellij.openapi.module.Module;
import com.luneo7.junit4.intellij.state.ListenersConfig;
import org.jdom.Element;
import org.jetbrains.idea.maven.importing.MavenImporter;
import org.jetbrains.idea.maven.importing.MavenRootModelAdapter;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectChanges;
import org.jetbrains.idea.maven.project.MavenProjectsProcessorTask;
import org.jetbrains.idea.maven.project.MavenProjectsTree;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Importer extends MavenImporter {

    public Importer() {
        super("org.apache.maven.plugins", "maven-surefire-plugin");
    }

    @Override
    public void preProcess(Module module,
                           MavenProject mavenProject,
                           MavenProjectChanges changes,
                           IdeModifiableModelsProvider modifiableModelsProvider) {
        // no-op
    }

    @Override
    public void process(IdeModifiableModelsProvider modifiableModelsProvider,
                        Module module,
                        MavenRootModelAdapter rootModel,
                        MavenProjectsTree mavenModel,
                        MavenProject mavenProject,
                        MavenProjectChanges changes,
                        Map<MavenProject, String> mavenProjectToModuleName,
                        List<MavenProjectsProcessorTask> postTasks) {
        if (module == null) {
            return;
        }

        ListenersConfig service = module.getService(ListenersConfig.class);

        if (mavenProject != null) {
            List<String> listeners = mavenProject.getPlugins()
                                                 .stream()
                                                 .filter(m -> "maven-surefire-plugin".equals(m.getArtifactId()))
                                                 .findFirst()
                                                 .flatMap(mavenPlugin -> {
                                                     if (mavenPlugin.getConfigurationElement() != null && mavenPlugin.getConfigurationElement()
                                                                                                                     .getChildren() != null) {
                                                         return mavenPlugin.getConfigurationElement()
                                                                           .getChildren()
                                                                           .stream()
                                                                           .filter(e -> "properties".equals(e.getName()))
                                                                           .flatMap(e -> e.getChildren()
                                                                                          .stream())
                                                                           .filter(e -> e.getChildren()
                                                                                         .stream()
                                                                                         .anyMatch(e1 -> "name".equals(e1.getName()) &&
                                                                                                 "listener".equals(e1.getValue())))
                                                                           .flatMap(e -> e.getChildren()
                                                                                          .stream()
                                                                                          .filter(e1 -> "value".equals(e1.getName()))
                                                                                          .map(Element::getValue))
                                                                           .findFirst();


                                                     }
                                                     return Optional.empty();
                                                 })
                                                 .map(l -> Arrays.stream(l.split(","))
                                                                 .map(String::trim)
                                                                 .filter(e -> !e.isEmpty())
                                                                 .collect(Collectors.toList()))
                                                 .orElse(Collections.emptyList());

            service.getState().setListeners(listeners);
        }
    }
}
