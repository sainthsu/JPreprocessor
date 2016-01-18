package org.flakor.jpp.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction


/**
 * javaPreprocess task
 */
class JavaPreprocessTask extends DefaultTask {
    static final String NAME = 'javaPreprocess'
    static final String GROUP = 'build'
    static final String DESCRIPTION = 'Preprocess macros in java source code'

    private List<Define> defines;

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

    FileTree sourceTree;
    private MacrosParser parser;

    Properties releaseProps
    {
        group = GROUP
        description = DESCRIPTION
    }

    @TaskAction
    void javaPreprocess() {
        logger.info("Gradle jPreprocessor Plugin version: $pluginVersion")
        parser = new MacrosParser()
        parseDefine()
        parseSrc()
    }

    void setSrcDir(String srcDir) {
        this.srcDir = srcDir
    }

    void setBuildDir(String buildDir) {
        this.buildDir = buildDir
    }

    void setSourceTree(NamedDomainObjectContainer<JPPSourceSet> sourceSetsContainer) {
        def sourceSet = sourceSetsContainer.findByName("main")
        sourceTree = sourceSet.allSource as FileTree
        baseDir = project.absoluteProjectPath()

    }

    void parseDefine() {
        if (!defineFile) return

        if (defines == null) {
            defines = new ArrayList<Define>()
        }
        File file = new File(defineFile)
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
        sourceTree.each {
            if (it.isFile()) {
                if (!it.parentFile.exists()) {
                    it.parentFile.mkdirs()
                }
                String relativePath = it.absolutePath - project.absoluteProjectPath()
                String absolutePath = buildDir + relativePath
                File buildFile = new File(absolutePath)
                buildFile.createNewFile()
            }
        }

        for (String dir : srcDir) {
            if (dir != null && buildDir != null) {
                String srcDirStr = baseDir.getPath() + File.separatorChar + dir;
                String buildDirStr = baseDir.getPath() + File.separatorChar + buildDir;
                File[] srcFileList = listFile(new File(srcDirStr), "java");
                File defineFile = new File(srcDirStr + File.separatorChar + ".define");
                if (defineFile.exists()) {
                    File[] fileList = new File[srcFileList.length + 1];
                    fileList[0] = defineFile;
                    System.arraycopy(srcFileList, 0, fileList, 1, srcFileList.length);
                    srcFileList = fileList;
                }

                try {
                    for (int i = 0; i < srcFileList.length; i++) {
                        String srcFileStr = srcFileList[i].getPath();
                        File buildFile = new File (buildDirStr
                                + srcFileStr.substring(srcDirStr.length()));
                        if (!buildFile.getParentFile().exists()) {
                            buildFile.getParentFile().mkdirs();
                        }
                        if (buildFile.exists()) {
                            buildFile.delete();
                        }
                        buildFile.createNewFile();
                        copyFile(srcFileList[i], buildFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return srcFileList.length;
            }
        }
        return 0;
    }

    private File[] listFile(File folder, String ext) {
        if (folder.isDirectory()) {
            File[] children = folder.listFiles();
            Vector<File> fileList = new Vector<File>();
            for (int i = 0; i < children.length; i++) {
                if (children[i].isDirectory()) {
                    File[] childList = listFile(children[i], ext);
                    for (int j = 0; j < childList.length; j++) {
                        fileList.add(childList[j]);
                    }
                } else if (children[i].getName().endsWith("." + ext)) {
                    fileList.add(children[i]);
                }
            }
            File[] res = new File[fileList.size()];
            fileList.copyInto(res);
            return res;
        }
        return new File[0];
    }

    private void copyFile(File srcFile, File destFile) {
        if (srcFile != null && destFile != null
                && srcFile.exists() && destFile.exists()
                && srcFile.isFile() && destFile.isFile()) {
            int lineCount = 0;
            String line = null;
            try {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(srcFile), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(destFile), "UTF-8");
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

    class Define {
        private String name;
        private String value;

        Define(String n,String v) {
            name = n
            value = v
        }

        void setName(String name) {
            this.name = name
        }

        void setValue(String value) {
            this.value = value
        }
    }
}
