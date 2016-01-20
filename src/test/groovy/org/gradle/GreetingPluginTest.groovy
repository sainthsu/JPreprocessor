package org.gradle

import org.flakor.jpp.gradle.JavaPreprocessTask
import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import static org.junit.Assert.*

class GreetingPluginTest {
    @Test
    public void greeterPluginAddsGreetingTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.plugins.apply 'gradle-jpp-plugin'

        assertTrue(project.tasks.hello instanceof JavaPreprocessTask)
    }
}
