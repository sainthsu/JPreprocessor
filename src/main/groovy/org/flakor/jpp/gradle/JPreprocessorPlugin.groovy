package org.flakor.jpp.gradle

import org.gradle.BuildAdapter
import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.file.FileTree
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.invocation.Gradle
import org.gradle.internal.reflect.Instantiator

import javax.inject.Inject

/**
 * a gradle plugin used to preprocess java source code
 */
class JPreprocessorPlugin implements Plugin<Project> {
    protected Instantiator instantiator
    private Project project;
    JPPExtension extension;

    @Inject
    JPreprocessorPlugin(Instantiator instantiator) {
        this.instantiator = instantiator;
    }

    @Override
    void apply(Project project) {
        this.project = project

        JavaPreprocessTask preprocessTask = project.task(type:JavaPreprocessTask,JavaPreprocessTask.NAME)

        extension = project.extensions.create("jpp",JPPExtension,(ProjectInternal) project, instantiator)

        def projectAdapter = [
                javaPreprocess:preprocessTask,
                projectsEvaluated: { Gradle gradle ->
                    preprocessTask.with {
                        encode = extension.encode
                        defineFile = extension.defineFile
                        defines = new ArrayList<Define>()
                        extension.defines.each {k,v->
                            defines.add(new Define(k.toString(),v.toString()))
                        }

                        extension.sourceSetsContainer.each {
                            println 'sourceSetName:' + it.name
                        }

                        sourceTree = extension.sourceSetsContainer
                        destDir = extension.destDir
                    }
                }
        ] as BuildAdapter

        project.gradle.addBuildListener(projectAdapter)
    }

}
