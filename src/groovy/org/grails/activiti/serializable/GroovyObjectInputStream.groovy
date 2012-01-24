package org.grails.activiti.serializable

/**
 *
 * @author <a href='mailto:pais@exigenservices.com'>Ilya Drabenia</a>
 *
 * @since 5.8.2
 */
class GroovyObjectInputStream extends ObjectInputStream {
    private ClassLoader myClassLoader;

    public GroovyObjectInputStream(ClassLoader myClassLoader) throws IOException, SecurityException {
        super();
        this.myClassLoader = myClassLoader;
    }

    public GroovyObjectInputStream(InputStream inp, ClassLoader myClassLoader) throws IOException {
        super(inp);
        this.myClassLoader = myClassLoader;
    }

    @Override
    protected Class resolveClass(ObjectStreamClass desc) throws IOException,
            ClassNotFoundException {
        String name = desc.getName();
        return Class.forName(name, false, new GroovyClassLoader(myClassLoader));
    }

}
