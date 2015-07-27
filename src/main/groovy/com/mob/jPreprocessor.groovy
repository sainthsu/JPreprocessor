package com.mob

import org.gradle.api.Project
import org.gradle.api.Plugin

class jPreprocessor implements Plugin<Project> {
    void apply(Project target) {
        target.task('hello', type: GreetingTask)
    }
}
