package org.flakor.jpp.gradle

import org.gradle.BuildAdapter
import org.gradle.api.Project
import org.gradle.api.Plugin
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

        extension = project.extensions.create("jpp",JPPExtension,this, (ProjectInternal) project, instantiator)

        def projectAdapter = [
                javaPreprocess:preprocessTask,
                projectsEvaluated: { Gradle gradle ->
                    preprocessTask.with {
                        destDir = extension.destDir
                        encode = extension.encode
                        defineFile = extension.defineFile
                        List<JavaPreprocessTask.Define> defines = new ArrayList<JavaPreprocessTask.Define>()
                        extension.defines.each {k,v->
                            defines.add(new JavaPreprocessTask.Define(k,v))
                        }
                        sourceTree = extension.sourceSetsContainer.getByName("main").allSource.asFileTree
                    }
                }
        ] as BuildAdapter

        project.gradle.addBuildListener(projectAdapter)
    }

}
