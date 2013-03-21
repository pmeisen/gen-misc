package net.meisen.general.genmisc.resources;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.meisen.general.genmisc.types.Files;
import net.meisen.general.genmisc.types.Objects;

/**
 * Determines the origin and type of a resource and the absolute path to
 * retrieve the resource
 * 
 * @author pmeisen
 * 
 */
public class ResourceInfo {

	/**
	 * Marker used within a resource to mark a resource to be stored within a jar
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

	/**
	 * Collects information about a resource. The resource can be located on the
	 * file-system or relative on the class-path or the file-system, whereby later
	 * has higher priority
	 * 
	 * @param path
	 *          the path of to the resource
	 * @param isFile
	 *          <code>true</code> if the path represents a file, otherwise
	 *          <code>false</code>
	 */
	public ResourceInfo(final String path, final boolean isFile) {
		this(path, null, isFile);
	}

	/**
	 * Collects information about a resource. The resource can be located on the
	 * file-system or relative on the class-path or the file-system, whereby later
	 * has higher priority
	 * 
	 * @param path
	 *          the relative path to the resource
	 * @param rootPath
	 *          the root path used for the resource <code>true</code> if the path
	 *          represents a file, otherwise <code>false</code>
	 * @param isFile
	 *          <code>true</code> if the path represents a file, otherwise
	 *          <code>false</code>
	 */
	public ResourceInfo(final String path, final String rootPath,
			final boolean isFile) {
		final String root = rootPath == null ? "" : rootPath;

		// check if the file exists
		final File file = "".equals(root) ? new File(path) : new File(root, path);

		// we have an path to a jar which marks a file within the jar
		final String lowerRoot = root.toLowerCase();
		if (path.toLowerCase().contains(INJARMARKER)
				|| lowerRoot.contains(INJARMARKER)
				|| lowerRoot.toLowerCase().endsWith(".jar")) {

			// if we have a root lets put it together
			final String urlPath;
			if (lowerRoot.endsWith(".jar") || lowerRoot.contains(INJARMARKER)) {
				urlPath = root + (root.endsWith(".jar") ? "!" : "")
						+ (path.endsWith("/") || path.endsWith("\\") ? "" : "/") + path;
			} else {
				urlPath = root + path;
			}

			// trust the path and just format it correctly
			final URL url = transformToJarPath(urlPath);

			if (url != null) {

				// set the defined stuff
				setUrl(url, isFile);
			} else {
				throw new IllegalArgumentException("The path '" + urlPath
						+ "' cannot be interpreted as resource path.");
			}
		}
		// check if its a directory or a file which exists and is absolute
		else if (file.exists() && file.canRead()
				&& ((isFile && file.isFile()) || (!isFile && file.isDirectory()))) {

			// set the directory information
			setFile(file, isFile);
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
			transformedClassPath = transformPathToClassPath(transformedClassPath,
					isFile);

			// check if the file is in a jar or on the class-path
			final URL url = ResourceInfo.class.getClassLoader().getResource(
					transformedClassPath);

			if (url != null) {
				final String protocol = url.getProtocol();

				// it is within a file
				if ("file".equalsIgnoreCase(protocol)) {
					setFile(new File(url.getFile()), isFile);
				}
				// it is within a jar
				else if ("jar".equalsIgnoreCase(protocol)) {
					setUrl(url, isFile);
				} else {
					throw new IllegalStateException(
							"This error should never been thrown - invalid protocol '"
									+ protocol + "' for path '" + path + "'");
				}
			} else {
				setEmpty();
			}
		}
	}

	/**
	 * Specifies the <code>File</code> of the <code>ResourceInfo</code>
	 * 
	 * @param file
	 *          the <code>File</code> to be set for the <code>ResourceInfo</code>
	 * @param isFile
	 *          <code>true</code> if the passed <code>File</code> is a file and
	 *          not a directory, otherwise <code>false</code>
	 */
	protected void setFile(final File file, final boolean isFile) {

		if (file.exists()) {
			fullPath = Files.getCanonicalPath(file);
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
	 * @param url
	 *          the <code>URL</code> to get the <code>ResourceInfo</code> for
	 * @param isFile
	 *          <code>true</code> if the passed <code>File</code> is a file and
	 *          not a directory, otherwise <code>false</code>
	 */
	protected void setUrl(final URL url, final boolean isFile) {

		// check if we have a valid url
		if (url == null) {
			setEmpty();
		} else {

			final String urlPath = url.getPath();
			final String protocol = url.getProtocol();

			if ("jar".equals(protocol)) {

				final String jarFilePath = urlPath.substring(0,
						urlPath.indexOf(INJARMARKER) + INJARMARKER.length() - 1);
				final String pathInJar = urlPath.substring(urlPath.indexOf(INJARMARKER)
						+ INJARMARKER.length());

				// get the jar file
				final URL jarURL;
				try {
					jarURL = new URL(jarFilePath);
				} catch (final MalformedURLException e) {
					throw new IllegalStateException("The jar-path '" + jarFilePath
							+ "' cannot be parsed", e);
				}
				final File jarFile;
				try {
					jarFile = new File(jarURL.toURI());
				} catch (final URISyntaxException e) {
					throw new IllegalStateException("The jar-path '" + jarFilePath
							+ "' cannot be parsed", e);
				}

				if (jarFile.exists()) {

					// set the information
					jarPath = Files.getCanonicalPath(jarFile);
					inJarPath = transformPathToClassPath(pathInJar, isFile);
					type = isFile ? ResourceType.IN_JAR_FILE : ResourceType.IN_JAR_PATH;
					fullPath = jarURL.toString() + "!/" + inJarPath;

					// check if the jar file really contains the resource
					try {
						final JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));

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
							}
						}

						// close the file
						jar.close();

						if (entry == null) {
							setEmpty();
						} else if (entry.isDirectory() == isFile) {
							setEmpty();
						}
					} catch (final UnsupportedEncodingException e) {
						throw new IllegalStateException("Cannot read jar at '" + jarPath
								+ "'", e);
					} catch (final IOException e) {
						throw new IllegalStateException("Cannot read jar at '" + jarPath
								+ "'", e);
					}
				} else {
					setEmpty();
				}
			} else if ("file".equals(protocol)) {
				final File file = new File(url.getFile());
				setFile(file, isFile);
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
	 * @param path
	 *          the path which should be transformed to the full qualified URL
	 * @return the transformed URL, or <code>null</code> if the path cannot be
	 *         transformed to a Jar path, which means that the passed path is not
	 *         a valid path to a resource within a specified jar
	 */
	protected static URL transformToJarPath(final String path) {

		// only paths with the ! marker can be used
		if (!path.contains(INJARMARKER)) {
			return null;
		}

		// make sure that the path is a valid url
		String modPath = path.replace('\\', '/');
		if (!path.startsWith("jar:")) {
			if (!path.startsWith("file:")) {
				if (!path.startsWith("/")) {
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
	 * "/", ...).
	 * 
	 * @param path
	 *          the path to be transformed
	 * @param isFile
	 *          <code>true</code> if the passed path points to a file, otherwise
	 *          <code>false</code>
	 * @return the transformed path which can be used as class-path
	 */
	protected static String transformPathToClassPath(final String path,
			final boolean isFile) {
		final String canBaseDir = Files.getCanonicalPath(System
				.getProperty("user.dir"));
		String classPath;

		// make sure we don't have a absolute path for the classloader
		if (path.startsWith("/")) {
			classPath = path.substring(1);
		} else if (path.startsWith("./")) {
			classPath = path.substring(2);
		} else if (path.startsWith(".\\")) {
			classPath = path.substring(2);
		} else {
			classPath = path;
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
	 * Existence check for a specific resource
	 * 
	 * @return <code>true</code> if the resource exists, otherwise
	 *         <code>false</code>
	 */
	public boolean exists() {
		return type != null;
	}

	/**
	 * Checks if the resource is a file
	 * 
	 * @return <code>true</code> if the resource is a file-resource, otherwise
	 *         <code>false</code>, i.e. its a directory
	 */
	public boolean isFile() {
		if (ResourceType.FILE_SYSTEM_FILE.equals(type)
				|| ResourceType.IN_JAR_FILE.equals(type)) {
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

		if (object instanceof ResourceInfo == false) {
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
		return Objects.generateHashCode(7, 13, inJarPath, jarPath, fullPath, type);
	}

	/**
	 * Transform a <code>Collection</code> of <code>String</code> instances into a
	 * <code>Collection</code> of <code>ResourceInfo</code> instances.
	 * 
	 * @param files
	 *          the <code>Collection</code> to be transformed
	 * 
	 * @return the transformed <code>Collection</code>, the order may be changed
	 */
	public static Collection<ResourceInfo> transformFromFileNameCollection(
			final Collection<? extends String> files) {
		final List<ResourceInfo> resInfos = new ArrayList<ResourceInfo>();

		// transform the list
		if (files != null) {
			for (final String fileName : files) {
				final File file = new File(fileName);
				final ResourceInfo resInfo = new ResourceInfo(fileName, null,
						file.isFile());

				resInfos.add(resInfo);
			}
		}

		return resInfos;

	}

	/**
	 * Transform a <code>Collection</code> of <code>File</code> instances into a
	 * <code>Collection</code> of <code>ResourceInfo</code> instances.
	 * 
	 * @param files
	 *          the <code>Collection</code> to be transformed
	 * 
	 * @return the transformed <code>Collection</code>, the order may be changed
	 */
	public static Collection<ResourceInfo> transformFromFileCollection(
			final Collection<? extends File> files) {
		final List<ResourceInfo> resInfos = new ArrayList<ResourceInfo>();

		// transform the list
		if (files != null) {
			for (final File file : files) {
				final ResourceInfo resInfo = new ResourceInfo(
						Files.getCanonicalPath(file), null, file.isFile());

				resInfos.add(resInfo);
			}
		}

		return resInfos;
	}
}
