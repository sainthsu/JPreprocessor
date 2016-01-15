package org.flakor.jpp.gradle

import org.gradle.api.file.SourceDirectorySet

/**
 * A {@code JPPSourceSet} represents a logical group of Java source .
 */
public interface JPPSourceSet {
    /**
     * Returns the name of this source set.
     *
     * @return The name. Never returns null.
     */
    String getName()

    /**
     * Returns the Java source which is to be compiled by the Java compiler into the class output
    * directory.
    *
    * @return the Java source. Never returns null.
    */
    SourceDirectorySet getJava();

    /**
     * Configures the Java source for this set.
     *
     * <p>The given closure is used to configure the {@link SourceDirectorySet} which contains the
     * Java source.
     *
     * @param configureClosure The closure to use to configure the Java source.
     * @return this
     */
    JPPSourceSet java(Closure configureClosure);

    /**
     * All Java source files for this source set. This includes, for example, source which is
     * directly compiled, and source which is indirectly compiled through joint compilation.
     *
     * @return the Java source. Never returns null.
     */
    SourceDirectorySet getAllJava();

    /**
     * All source files for this source set.
     *
     * @return the source. Never returns null.
     */
    SourceDirectorySet getAllSource();

    /**
    * Sets the root of the source sets to a given path.
    *
    * All entries of the source set are located under this root directory.
    *
    * @param path the root directory.
    * @return this
    */
    JPPSourceSet setRoot(String path);

}