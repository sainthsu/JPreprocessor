package org.flakor.jpp.gradle

import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.internal.file.DefaultSourceDirectorySet
import org.gradle.api.internal.file.FileResolver
import org.gradle.internal.reflect.Instantiator

/**
 * Factory to create JPPSourceSet object using an {@link Instantiator} to add
 * the DSL methods.
 */
public class JPPSourceSetFactory implements NamedDomainObjectFactory<JPPSourceSet> {

    private final Instantiator instantiator;
    private final FileResolver fileResolver;

    public JPPSourceSetFactory(Instantiator instantiator,
                                   FileResolver fileResolver) {
        this.instantiator = instantiator;
        this.fileResolver = fileResolver;
    }

    @Override
    public JPPSourceSet create(String name) {
        return instantiator.newInstance(DefaultJPPSourceSet.class, name, fileResolver);
    }
}
