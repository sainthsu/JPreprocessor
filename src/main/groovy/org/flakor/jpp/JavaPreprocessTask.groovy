package org.flakor.jpp

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction


/**
 * Created by xusq on 2016/1/14.
 */
class JavaPreprocessTask extends DefaultTask {
    static final static String NAME = 'javaPreprocess'
    static final static String GROUP = 'build'
    static final String DESCRIPTION = 'Preprocess macros in java source code'
    static final String API_URL_DEFAULT = 'https://jpp.flakor.org'

    @Input
    String baseDir

    @Input
    String srcDir

    @Input
    @Optional
    String buildDir

    Properties releaseProps
    {
        group = GROUP
        description = DESCRIPTION
    }

    @TaskAction
    void javaPreprocess() {

    }

}
