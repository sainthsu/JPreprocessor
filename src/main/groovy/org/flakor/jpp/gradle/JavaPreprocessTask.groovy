package org.flakor.jpp.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.FileUtils

import java.util.concurrent.ConcurrentHashMap


/**
 * javaPreprocess task
 */
class JavaPreprocessTask extends DefaultTask {
    static final String NAME = 'javaPreprocess'
    static final String GROUP = 'build'
    static final String DESCRIPTION = 'Preprocess macros in java source code'
    static final String PLUGIN_VERSION = '0.1.0'

    List<Define> defines;

    @Input
    @Optional
    String baseDir

    @Input
    @Optional
    String destDir

    @Input
    @Optional
    String encode

    @Input
    @Optional
    String defineFile

    ConcurrentHashMap<String,FileTree> sourceMap = ConcurrentHashMap.newInstance();
    private MacrosParser parser;

    Properties releaseProps
    {
        group = GROUP
        description = DESCRIPTION
    }

    @TaskAction
    void javaPreprocess() {
        logger.info('Gradle jPreprocessor Plugin version: ' + PLUGIN_VERSION)
        parseDefine()
        parser = new MacrosParser(defines)
        parseSrc()
    }

    void setBaseDir(String baseDir) {
        if (baseDir == null || baseDir.length() == 0) return
        File file = new File(project.projectDir.absolutePath,baseDir)
        if (file.exists()) {
            this.baseDir = file.absolutePath
        }
    }

    void setDestDir(String buildDir) {
        if (buildDir == null || buildDir.length() == 0) return
        this.destDir = baseDir + File.separator + buildDir
    }

    void setSourceTree(NamedDomainObjectContainer<JPPSourceSet> sourceSetsContainer) {
        sourceSetsContainer.each{
            sourceMap.put(it.name,it.allSource as FileTree)
        }
        if (baseDir == null || baseDir.length() == 0) {
            baseDir = project.projectDir.absolutePath
        }
    }

    void parseDefine() {
        if (!defineFile) return

        if (defines == null) {
            defines = new ArrayList<Define>()
        }
        File file = new File(defineFile)
        if (!file.exists()) {
            throw FileNotFoundException
        }

        file.eachLine { line ->
            def l = line.trim()
            int i
            if (l.length() > 0 ){
                i = l.lastIndexOf("=")
                def n = l.substring(0, i).trim()
                def v = l.substring(i + 1).trim()
                def d = new Define(n,v)
                defines.add(d)
            }
        }
    }

    private int parseSrc() {
        int num = 0
        if (!destDir) {
            destDir = baseDir + File.separator + 'dest'
        }
        println 'baseDir:' + baseDir
        println 'destDir:' + destDir
        FileTree sourceTree = sourceMap.get('main')
        sourceTree.each {
            if (it.isFile() && it.getName().endsWith(".java")) {
                String relativePath = it.absolutePath - baseDir
                String absolutePath = destDir + relativePath
                println 'current file:' + it.absolutePath
                println 'target file:' + absolutePath
                File buildFile = new File(absolutePath)
                if (!buildFile.parentFile.exists()) {
                    buildFile.parentFile.mkdirs()
                }
                if (buildFile.exists()) {
                    buildFile.delete()
                }
                buildFile.createNewFile()
                owner.copyFile(it,buildFile)
                num++
            }
        }

        return num
    }

    public void copyFile(File srcFile, File destFile) {
        if(encode == null || encode.size() == 0) {
            encode = 'UTF-8'
        }
        if (srcFile != null && destFile != null
                && srcFile.exists() && destFile.exists()
                && srcFile.isFile() && destFile.isFile()) {
            int lineCount = 0;
            String line = null;
            try {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(srcFile), encode);
                BufferedReader br = new BufferedReader(isr);
                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(destFile), encode);
                BufferedWriter bw = new BufferedWriter(osw);
                line = br.readLine();
                while (line != null) {
                    lineCount++;
                    String resLine = parser.parseLine(line);
                    if (!resLine.trim().startsWith("//#")) {
                        bw.append(resLine + "\n");
                    }
                    line = br.readLine();
                }
                bw.flush();
                bw.close();
                br.close();
            } catch (Exception ex) {
                System.err.println("File: " + srcFile.getPath());
                System.err.println("Line: " + line.trim());
                System.err.println("Line Number: " + lineCount + "\n");
                ex.printStackTrace();
            }
        }
    }
}
