package net.meisen.general.genmisc.resources;

import net.meisen.general.genmisc.types.Files;
import net.meisen.general.genmisc.types.Objects;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Determines the origin and type of a resource and the absolute path to
 * retrieve the resource
 *
 * @author pmeisen
 */
public class ResourceInfo {

    /**
     * Marker used within a resource to mark a resource to be stored within a
     * jar
     */
    public final static String INJARMARKER = ".jar!";

    /**
     * the type of the resource
     */
    private ResourceType type;

    /**
     * the jar the resource is stored in
     */
    private String jarPath;
    /**
     * the path within a jar the resource is stored at
     */
    private String inJarPath;

    /**
     * the full path of the resource, used to access the resource
     */
    private String fullPath;

    private String rootPath;

    /**
     * Collects information about a resource. The resource can be located on the
     * file-system or relative on the class-path or the file-system, whereby
     * later has higher priority
     *
     * @param path   the path of to the resource
     * @param isFile <code>true</code> if the path represents a file, otherwise
     *               <code>false</code>
     */
    public ResourceInfo(final String path, final boolean isFile) {
        this(path, null, isFile);
    }

    /**
     * Collects information about a resource. The resource can be located on the
     * file-system or relative on the class-path or the file-system, whereby
     * later has higher priority
     *
     * @param path     the relative path to the resource
     * @param rootPath the root path used for the resource
     * @param isFile   <code>true</code> if the path represents a file, otherwise
     *                 <code>false</code>
     */
    public ResourceInfo(final String path, final String rootPath, final boolean isFile) {
        final String root = rootPath == null ? "" : rootPath;

        // check if the file exists
        final File rootDir;
        final File file;
        if ("".equals(root)) {
            rootDir = null;
            file = new File(path);
        } else if (Files.isInDirectory(path, rootPath)) {
            rootDir = new File(root);
            file = new File(path);
        } else {
            rootDir = new File(root);
            file = new File(root, path);
        }

        // we have an path to a jar which marks a file within the jar
        if (path.contains(INJARMARKER) || root.contains(INJARMARKER) || root.endsWith(".jar")) {

            // if we have a root lets put it together
            final String urlPath;
            if (root.endsWith(".jar") || root.contains(INJARMARKER)) {
                urlPath = root
                        + (root.endsWith(".jar") ? "!" : "")
                        + (path.endsWith("/") || path.endsWith("\\") ? "" : "/")
                        + path;
            } else {
                urlPath = root + path;
            }

            // trust the path and just format it correctly
            final URL url = transformToJarPath(urlPath);

            if (url != null) {

                // set the defined stuff
                setUrl(url, rootDir, isFile);
            } else {
                throw new IllegalArgumentException("The path '" + urlPath + "' cannot be interpreted as resource path.");
            }
        }
        // check if its a directory or a file which exists and is absolute
        else if (file.exists()
                && file.canRead()
                && ((isFile && file.isFile()) || (!isFile && file.isDirectory()))) {

            // set the directory information
            setFile(file, rootDir, isFile);
        }
        // it's not a file and not a full-qualified path, so it must be on the
        // class-path
        else {
            String transformedClassPath = root;

            if (!root.endsWith("/")) {
                transformedClassPath += "/";
            }
            transformedClassPath += path;

            // get the class-path value
            transformedClassPath = transformPathToClassPath(transformedClassPath, isFile);

            // check if the file is in a jar or on the class-path
            final URL url = ResourceInfo.class.getClassLoader().getResource(transformedClassPath);

            if (url != null) {
                final String protocol = url.getProtocol();

                // it is within a file
                if ("file".equalsIgnoreCase(protocol)) {
                    File f;
                    try {
                        f = new File(url.toURI());
                    } catch (final URISyntaxException e) {

                        /*
                         * let's try the URL getFile instead, that's the fall-back
                         * nothing more
                         */
                        f = new File(url.getFile());
                    }

                    setFile(f, rootDir, isFile);
                }
                // it is within a jar
                else if ("jar".equalsIgnoreCase(protocol)) {
                    setUrl(url, rootDir, isFile);
                } else {
                    throw new IllegalStateException("This error should never been thrown - " +
                                    "invalid protocol '" + protocol + "' for path '" + path + "'");
                }
            } else {
                setEmpty();
            }
        }
    }

    /**
     * Specifies the <code>File</code> of the <code>ResourceInfo</code>
     *
     * @param file   the <code>File</code> to be set for the
     *               <code>ResourceInfo</code>
     * @param root   the root of the file if one was specified
     * @param isFile <code>true</code> if the passed <code>File</code> is a file
     *               and not a directory, otherwise <code>false</code>
     */
    protected void setFile(final File file, final File root,
                           final boolean isFile) {

        if (file.exists()) {
            fullPath = Files.getCanonicalPath(file);
            rootPath = root == null ? null : Files.getCanonicalPath(root);
            jarPath = null;
            inJarPath = null;

            if (file.isDirectory() && !isFile) {
                type = ResourceType.FILE_SYSTEM_PATH;
            } else if (file.isFile() && isFile) {
                type = ResourceType.FILE_SYSTEM_FILE;
            } else {
                setEmpty();
            }
        } else {
            setEmpty();
        }
    }

    /**
     * Specifies an <code>URL</code> pointing to the resource to get the
     * <code>ResourceInfo</code> from
     *
     * @param url    the <code>URL</code> to get the <code>ResourceInfo</code> for
     * @param root   the root of the file if one was specified
     * @param isFile <code>true</code> if the passed <code>File</code> is a file
     *               and not a directory, otherwise <code>false</code>
     */
    protected void setUrl(final URL url, final File root, final boolean isFile) {

        // check if we have a valid url
        if (url == null) {
            setEmpty();
        } else {

            // get the path
            String fullyResolvedPath;
            try {
                fullyResolvedPath = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8.name());
                fullyResolvedPath = fullyResolvedPath.replaceFirst("^\\Qfile:", "");
                fullyResolvedPath = URLDecoder.decode(fullyResolvedPath, StandardCharsets.UTF_8.name());
                fullyResolvedPath = new File(fullyResolvedPath).getPath();
            } catch (final UnsupportedEncodingException e) {
                // fall-back to the path
                fullyResolvedPath = url.getPath();
            }
            final String protocol = url.getProtocol();

            if ("jar".equals(protocol)) {
                final String jarFilePath = fullyResolvedPath.substring(0, fullyResolvedPath.indexOf(INJARMARKER) +
                        INJARMARKER.length() - 1);
                final String pathInJar = fullyResolvedPath.substring(fullyResolvedPath.indexOf(INJARMARKER) +
                        INJARMARKER.length());
                final File jarFile = new File(jarFilePath);

                if (jarFile.exists()) {

                    // set the information
                    jarPath = Files.getCanonicalPath(jarFile);
                    inJarPath = transformPathToClassPath(pathInJar, isFile);
                    type = isFile ? ResourceType.IN_JAR_FILE : ResourceType.IN_JAR_PATH;
                    fullPath = url.toString();

                    // check if the jar file really contains the resource
                    try {
                        final JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8.name()));

                        // we create a regex used to match the name
                        final String pattern = "(?i)\\Q" + inJarPath + "\\E/?";

                        // Now we have to look within the entries, the function
                        // getJarEntry or getEntry would return a value, if it's
                        // available but the name wouldn't be the real name
                        // concerning the fact, if it's a directory or a file.
                        // The returned name is always the passed name, which
                        // might be incorrect concerning an ending / which is
                        // used to determine if the entry is a directory
                        JarEntry entry = null;
                        final Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            final JarEntry posEntry = entries.nextElement();
                            final String name = posEntry.getName();

                            if (name.matches(pattern)) {
                                entry = posEntry;
                                break;
                            }
                        }

                        if (entry == null) {
                            setEmpty();
                        } else if (entry.isDirectory() == isFile) {
                            setEmpty();
                        }
                    } catch (final IOException e) {
                        throw new IllegalStateException("Cannot read jar at '" + jarPath + "'", e);
                    }
                } else {
                    setEmpty();
                }
            } else if ("file".equals(protocol)) {
                final File file = new File(url.getFile());
                setFile(file, root, isFile);
            }
        }
    }

    /**
     * Resets all the information of the <code>ResourceInfo</code>
     */
    protected void setEmpty() {
        jarPath = null;
        inJarPath = null;
        fullPath = null;
        type = null;
    }

    /**
     * Used to transform a path with a .jar! marker in it, to a full qualified
     * URL, which can be used to retrieve data.
     *
     * @param path the path which should be transformed to the full qualified URL
     * @return the transformed URL, or <code>null</code> if the path cannot be
     * transformed to a Jar path, which means that the passed path is
     * not a valid path to a resource within a specified jar
     */
    protected static URL transformToJarPath(final String path) {

        // only paths with the ! marker can be used
        if (!path.contains(INJARMARKER)) {
            return null;
        }

        // check how we can get the jar-path
        URL url;
        String urlPath = path;
        try {
            url = new URL(path);
        } catch (final MalformedURLException e) {

            // try to use it as file
            try {
                url = (new File(path)).toURI().toURL();
                url = new URL(URLDecoder.decode(url.toString(), StandardCharsets.UTF_8.name()));
            } catch (final UnsupportedEncodingException | MalformedURLException innerException) {
                // we have no other choice
                url = null;
            }
        }
        urlPath = url == null ? urlPath : url.toString();

        // make sure that the path is a valid url
        String modPath = urlPath.replace('\\', '/');
        if (!urlPath.startsWith("jar:")) {
            if (!urlPath.startsWith("file:")) {
                if (!urlPath.startsWith("/")) {
                    modPath = "/" + modPath;
                }

                modPath = "file:" + modPath;
            }

            modPath = "jar:" + modPath;
        }

        URL dirURL;

        // there can be a:
        // - file: or
        // - jar:file:
        // protocol specified
        try {
            dirURL = new URL(modPath);
        } catch (final MalformedURLException e) {
            dirURL = null;
        }

        return dirURL;
    }

    /**
     * Transform a path to a valid class-path (i.e. without any '\', no leading
     * "/", ...), and no URL encoding.
     *
     * @param path   the path to be transformed
     * @param isFile <code>true</code> if the passed path points to a file,
     *               otherwise <code>false</code>
     * @return the transformed path which can be used as class-path
     */
    protected static String transformPathToClassPath(final String path,
                                                     final boolean isFile) {
        final String canBaseDir = Files.getCanonicalPath(System.getProperty("user.dir"));
        String classPath;

        // make sure we don't have a absolute path for the classloader
        if (path.startsWith("/") || path.startsWith("\\")) {
            classPath = path.substring(1);
        } else {
            classPath = path;
        }

        if (classPath.startsWith("./")) {
            classPath = classPath.substring(2);
        } else if (classPath.startsWith(".\\")) {
            classPath = classPath.substring(2);
        }

        // resolve it through canonicalization
        final String canPath = Files.getCanonicalPath(classPath);

        if (canPath == null) {
            // we have to try the passed path
            classPath = path;
        } else if (canPath.startsWith(canBaseDir)) {
            classPath = canPath.substring(canBaseDir.length());
        } else {
            classPath = canPath;
        }

        // do some changes to get a valid class-path
        // - replace "\" by "/"
        // - add the marker for files and/or resources
        classPath = classPath.replace("\\", "/");
        if (classPath.endsWith("/")) {
            if (isFile) {
                classPath = classPath.substring(0, classPath.length() - 1);
            }
        } else if (!isFile) {
            classPath += "/";
        }

        return classPath;
    }

    /**
     * Returns the type of the resource
     *
     * @return the type of the resource
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * The path to the jar file this resource is contained in. The method will
     * return <code>null</code> if the resource is a file located on the
     * file-system.
     *
     * @return the path to the jar file this resource is contained in
     */
    public String getJarPath() {
        return jarPath;
    }

    /**
     * The path to the resource within the jar-file. The method will return
     * <code>null</code> if the resource is a file located on the file-system.
     * The path is not URL encoded, i.e. special characters like white-spaces
     * are resolved.
     *
     * @return the path of the resource within the jar file
     */
    public String getInJarPath() {
        return inJarPath;
    }

    /**
     * The absolute path to the resource, which can be a URL using the "!"
     * annotations for resources located in a jar or the canonical path of the
     * resource if its located on the file-system.
     *
     * @return the absolute path to the resource
     */
    public String getFullPath() {
        return fullPath;
    }

    /**
     * Get the root path which was used to create the {@code ResourceInfo}.
     *
     * @return the root path, can be {@code null}
     */
    public String getRootPath() {
        return rootPath;
    }

    /**
     * Gets the relative path of the resource to the rootPath.
     *
     * @return the relative path to the root
     */
    public String getRelativePathToRoot() {
        return rootPath == null ? fullPath : fullPath.replaceFirst("^\\Q"
                + rootPath + "\\E", "");
    }

    /**
     * Existence check for a specific resource
     *
     * @return <code>true</code> if the resource exists, otherwise
     * <code>false</code>
     */
    public boolean exists() {
        return type != null;
    }

    /**
     * Checks if the resource is a file
     *
     * @return <code>true</code> if the resource is a file-resource, otherwise
     * <code>false</code>, i.e. its a directory
     */
    public boolean isFile() {
        if (type == ResourceType.FILE_SYSTEM_FILE || type == ResourceType.IN_JAR_FILE) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return getFullPath();
    }

    @Override
    public boolean equals(final Object object) {

        if (!(object instanceof ResourceInfo)) {
            return false;
        } else {
            final ResourceInfo resInfo = (ResourceInfo) object;

            return Objects.equals(resInfo.type, type)
                    && Objects.equals(resInfo.inJarPath, inJarPath)
                    && Objects.equals(resInfo.jarPath, jarPath)
                    && Objects.equals(resInfo.fullPath, fullPath);
        }
    }

    @Override
    public int hashCode() {
        return Objects.generateHashCode(7, 13, inJarPath, jarPath, fullPath,
                type);
    }

    /**
     * Transform a <code>Collection</code> of <code>String</code> instances into
     * a <code>Collection</code> of <code>ResourceInfo</code> instances.
     *
     * @param fileNames the <code>Collection</code> to be transformed
     * @return the transformed <code>Collection</code>, the order may be changed
     */
    public static Collection<ResourceInfo> transformFromFileNameCollection(
            final Collection<? extends String> fileNames) {
        return transformFromFileNameCollection((String) null, fileNames);
    }

    /**
     * Transform a <code>Collection</code> of <code>String</code> instances into
     * a <code>Collection</code> of <code>ResourceInfo</code> instances.
     *
     * @param rootPath  the root of all the files
     * @param fileNames the <code>Collection</code> to be transformed
     * @return the transformed <code>Collection</code>, the order may be changed
     */
    public static Collection<ResourceInfo> transformFromFileNameCollection(
            final File rootPath, final Collection<? extends String> fileNames) {
        return transformFromFileNameCollection(
                Files.getCanonicalPath(rootPath), fileNames);
    }

    /**
     * Transform a <code>Collection</code> of <code>String</code> instances into
     * a <code>Collection</code> of <code>ResourceInfo</code> instances.
     *
     * @param rootPath  the root of all the files
     * @param fileNames the <code>Collection</code> to be transformed
     * @return the transformed <code>Collection</code>, the order may be changed
     */
    public static Collection<ResourceInfo> transformFromFileNameCollection(
            final String rootPath, final Collection<? extends String> fileNames) {
        final List<File> files = new ArrayList<>();

        // transform the list to files
        if (fileNames != null) {
            files.addAll(fileNames.stream()
                    .map((Function<String, File>) File::new)
                    .collect(Collectors.toList()));
        }

        // use the other implementation
        return transformFromFileCollection(rootPath, files);
    }

    /**
     * Transform a <code>Collection</code> of <code>File</code> instances into a
     * <code>Collection</code> of <code>ResourceInfo</code> instances.
     *
     * @param files the <code>Collection</code> to be transformed
     * @return the transformed <code>Collection</code>, the order may be changed
     */
    public static Collection<ResourceInfo> transformFromFileCollection(
            final Collection<? extends File> files) {
        return transformFromFileCollection((String) null, files);
    }

    /**
     * Transform a <code>Collection</code> of <code>String</code> instances into
     * a <code>Collection</code> of <code>ResourceInfo</code> instances.
     *
     * @param rootPath the root of all the files
     * @param files    the <code>Collection</code> to be transformed
     * @return the transformed <code>Collection</code>, the order may be changed
     */
    public static Collection<ResourceInfo> transformFromFileCollection(
            final File rootPath, final Collection<? extends File> files) {
        return transformFromFileCollection(Files.getCanonicalPath(rootPath), files);
    }

    /**
     * Transform a <code>Collection</code> of <code>String</code> instances into
     * a <code>Collection</code> of <code>ResourceInfo</code> instances.
     *
     * @param rootPath the root of all the files
     * @param files    the <code>Collection</code> to be transformed
     * @return the transformed <code>Collection</code>, the order may be changed
     */
    public static Collection<ResourceInfo> transformFromFileCollection(
            final String rootPath, final Collection<? extends File> files) {
        final List<ResourceInfo> resInfos = new ArrayList<>();

        if (files != null) {
            files.stream()
                    .filter(file -> rootPath == null || Files.isInDirectory(file, rootPath))
                    .forEach(file -> {
                        final ResourceInfo resInfo = new ResourceInfo(
                                Files.getCanonicalPath(file), rootPath,
                                file.isFile());
                        resInfos.add(resInfo);
                    });
        }

        return resInfos;
    }
}
