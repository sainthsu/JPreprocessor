package org.flakor.jpp

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.file.FileTree

class jPreprocessor implements Plugin<Project> {
    def File baseDir;
    def String buildDir;
    def FileTree srcTree;

    void apply(Project target) {

    }
}
