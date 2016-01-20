package org.flakor.jpp.gradle

import org.gradle.api.file.FileTreeElement
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.file.DefaultSourceDirectorySet
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.specs.Spec
import org.gradle.util.ConfigureUtil
import org.gradle.util.GUtil

/**
 * Created by xusq on 2016/1/20.
 */
class DefaultJPPSourceSet implements JPPSourceSet{

    private final String name
    private final SourceDirectorySet javaSource
    private final SourceDirectorySet allJavaSource
    private final SourceDirectorySet javaResources
    private final String displayName
    private final SourceDirectorySet allSource

    public DefaultJPPSourceSet(String name, FileResolver fileResolver) {
        this.name = name
        displayName = GUtil.toWords(name)

        String javaSrcDisplayName = String.format("%s Java source", displayName)

        javaSource = new DefaultSourceDirectorySet(javaSrcDisplayName, fileResolver)
        javaSource.getFilter().include("**/*.java")

        allJavaSource = new DefaultSourceDirectorySet(javaSrcDisplayName, fileResolver)
        allJavaSource.getFilter().include("**/*.java")
        allJavaSource.source(javaSource)

        String javaResourcesDisplayName = String.format("%s Java resources", displayName);
        javaResources = new DefaultSourceDirectorySet(javaResourcesDisplayName, fileResolver);
        javaResources.getFilter().exclude(new Spec<FileTreeElement>() {
            @Override
            public boolean isSatisfiedBy(FileTreeElement element) {
                return javaSource.contains(element.getFile());
            }
        });

        String allSourceDisplayName = String.format("%s source", displayName);
        allSource = new DefaultSourceDirectorySet(allSourceDisplayName, fileResolver);
        allSource.source(javaResources);
        allSource.source(javaSource);
    }

    public String toString() {
        return String.format("source set %s", getDisplayName());
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    String getName() {
        return name
    }

    @Override
    SourceDirectorySet getJava() {
        return javaSource
    }

    @Override
    JPPSourceSet java(Closure configureClosure) {
        ConfigureUtil.configure(configureClosure,getJava())
        return this
    }

    @Override
    SourceDirectorySet getAllJava() {
        return allJavaSource
    }

    @Override
    SourceDirectorySet getAllSource() {
        return allSource
    }

    @Override
    JPPSourceSet setRoot(String path) {
        javaSource.setSrcDirs(Collections.singletonList(path + "/java"));
        javaResources.setSrcDirs(Collections.singletonList(path + "/resources"));
        return this
    }
}
