package org.flakor.jpp.gradle

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.internal.reflect.Instantiator
import org.gradle.util.ConfigureUtil

import java.util.concurrent.ConcurrentHashMap

/**
 * a extension for JPP plugin
 */
class JPPExtension {
    private ConcurrentHashMap<String,String> defines
    Project project

    String defineFile
    String destDir
    String encode

    /**
     * The source sets container.
     */
    final NamedDomainObjectContainer<JPPSourceSet> sourceSetsContainer

    JPPExtension(ProjectInternal project, Instantiator instantiator) {
        this.project = project
        sourceSetsContainer = project.container(JPPSourceSet,new JPPSourceSetFactory(instantiator, project.fileResolver))
    }

    String getDefineFile() {
        return defineFile
    }

    void setDefineFile(String defineFile) {
        this.defineFile = defineFile
    }

    String getDestDir() {
        return destDir
    }

    void setDestDir(String destDir) {
        this.destDir = destDir
    }

    String getEncode() {
        return encode
    }

    void setEncode(String encode) {
        this.encode = encode
    }

    void sourceSets(Action<NamedDomainObjectContainer<JPPSourceSet>> action) {
        action.execute(sourceSetsContainer)
    }

    NamedDomainObjectContainer<JPPSourceSet> getSourceSets() {
        sourceSetsContainer
    }

    void defines(Action<Map<String,String>> action) {
        action.execute(defines)
    }

    void defines(Closure closure) {
        ConfigureUtil.configure(closure,defines)
    }
}