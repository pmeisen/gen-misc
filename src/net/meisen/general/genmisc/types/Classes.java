package net.meisen.general.genmisc.types;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Utility class for classes
 * 
 * @author pmeisen
 * 
 */
public class Classes {

	/**
	 * Determines the class of the generic type of the generic object.
	 * 
	 * @param genericObject
	 *            an instance of the generic class
	 * 
	 * @return the {@link Class} of the generic type of the generic object
	 *         passed
	 */
	public static Class<?> getGenericClass(final Object genericObject) {
		return getGenericClass(genericObject.getClass());
	}

	/**
	 * Determines the class of the generic type of the generic class.
	 * 
	 * @param genericClass
	 *            the generic class
	 * 
	 * @return the {@link Class} of the generic type of the generic object
	 *         passed
	 */
	public static Class<?> getGenericClass(final Class<?> genericClass) {
		Class<?> clazz = ((Class<?>) ((ParameterizedType) genericClass
				.getGenericSuperclass()).getActualTypeArguments()[0]);
		return clazz;
	}

	/**
	 * Determines the class specified by the {@code className}.
	 * 
	 * @param className
	 *            the name of the class to be generated
	 * 
	 * @return the {@link Class} associated to this name or <code>null</code> if
	 *         the class could not be found
	 */
	public static Class<?> getClass(final String className) {
		return getClass(null, className, true);
	}

	/**
	 * Determines the class specified by the {@code className}.
	 * 
	 * @param className
	 *            the name of the class to be generated
	 * @param expected
	 *            {@code true} if the class is expected to be available,
	 *            otherwise {@code false}; if the first is the case an
	 *            error message is printed if the class cannot be found
	 * 
	 * @return the {@link Class} associated to this name or <code>null</code> if
	 *         the class could not be found
	 */
	public static Class<?> getClass(final String className,
			final boolean expected) {
		return getClass(null, className, expected);
	}

	/**
	 * Determines the class specified by the {@code className}.
	 * 
	 * @param loader
	 *            the <code>ClassLoader</code> to be used to load the class
	 * @param className
	 *            the name of the class to be generated
	 * 
	 * @return the {@link Class} associated to this name or <code>null</code> if
	 *         the class could not be found
	 */
	public static Class<?> getClass(final ClassLoader loader,
			final String className) {
		return getClass(loader, className, true);
	}

	/**
	 * Determines the class specified by the {@code className}.
	 * 
	 * @param loader
	 *            the <code>ClassLoader</code> to be used to load the class
	 * @param className
	 *            the name of the class to be generated
	 * @param expected
	 *            {@code true} if the class is expected to be available,
	 *            otherwise {@code false}; if the first is the case an
	 *            error message is printed if the class cannot be found
	 * 
	 * @return the {@link Class} associated to this name or <code>null</code> if
	 *         the class could not be found
	 */
	public static Class<?> getClass(final ClassLoader loader, String className,
			final boolean expected) {
		className = (className != null ? className.trim() : null);

		try {
			if (loader == null) {
				return Class.forName(className);
			} else {
				return Class.forName(className, true, loader);
			}
		} catch (final Exception e) {
			if (expected) {
				System.err.println("Cannot generate the specified class '"
						+ className + "'");
			}
			return null;
		}
	}

	/**
	 * Reads all the direct classes of the passed package using the current
	 * <code>ClassLoader</code>
	 * 
	 * @param packageName
	 *            the package to look for, must be using "/" instead of "."
	 * 
	 * @return the classes of the package in the jar
	 * 
	 * @throws FileNotFoundException
	 *             if the jar cannot be accessed
	 * @throws IOException
	 *             if some IO-operation fails
	 * @throws ClassNotFoundException
	 *             if the class cannot be found
	 */
	public static Set<Class<?>> getClasses(final String packageName)
			throws IOException, ClassNotFoundException {
		final ClassLoader loader = Thread.currentThread()
				.getContextClassLoader();

		return getClasses(loader, packageName);
	}

	/**
	 * Reads all the direct classes of the passed package using the specified
	 * <code>ClassLoader</code>
	 * 
	 * @param loader
	 *            the <code>ClassLoader</code> used to look for
	 * @param packageName
	 *            the package to look for, must be using "/" instead of "."
	 * 
	 * @return the classes of the package in the jar
	 * 
	 * @throws FileNotFoundException
	 *             if the jar cannot be accessed
	 * @throws IOException
	 *             if some IO-operation fails
	 * @throws ClassNotFoundException
	 *             if the class cannot be found
	 */
	public static Set<Class<?>> getClasses(final ClassLoader loader,
			final String packageName) throws IOException,
			ClassNotFoundException {

		final Set<Class<?>> classes = new HashSet<Class<?>>();
		final String path = packageName.replace('.', '/');
		final Enumeration<URL> resources = loader.getResources(path);

		if (resources != null) {
			while (resources.hasMoreElements()) {
				String filePath = resources.nextElement().getFile();

				// WINDOWS HACK
				if (filePath.indexOf("%20") > 0)
					filePath = filePath.replaceAll("%20", " ");
				if (filePath != null) {
					if ((filePath.indexOf("!") > 0)
							& (filePath.indexOf(".jar") > 0)) {
						String jarPath = filePath.substring(0,
								filePath.indexOf("!")).substring(
								filePath.indexOf(":") + 1);

						// WINDOWS HACK
						if (jarPath.indexOf(":") >= 0) {
							jarPath = jarPath.substring(1);
						}
						classes.addAll(getFromJARFile(jarPath, path));
					} else {
						classes.addAll(getFromDirectory(new File(filePath),
								packageName));
					}
				}
			}
		}
		return classes;
	}

	/**
	 * Reads all the direct classes of the passed package in the specified
	 * directory
	 * 
	 * @param directory
	 *            the directory to look for the classes of the package
	 * @param packageName
	 *            the package to look for, must be using "/" instead of "."
	 * 
	 * @return the classes of the package in the jar
	 * 
	 * @throws ClassNotFoundException
	 *             if the class cannot be found
	 */
	public static Set<Class<?>> getFromDirectory(final File directory,
			final String packageName) throws ClassNotFoundException {
		final Set<Class<?>> classes = new HashSet<Class<?>>();
		if (!directory.exists()) {
			return classes;
		}

		// check the directory's content
		for (final String content : directory.list()) {
			final File file = new File(directory, content);
			if (file.exists() && file.isFile() && content.endsWith(".class")) {
				final String className = content.replaceAll("\\.class$", "");
				final String fullName = packageName + '.' + className;
				final Class<?> clazz = Class.forName(fullName);

				classes.add(clazz);
			}
		}
		return classes;
	}

	/**
	 * Reads all the direct classes of the passed package in the specified
	 * jar-file
	 * 
	 * @param jar
	 *            the jar-file to look for the classes of the package
	 * @param packageName
	 *            the package to look for, must be using "/" instead of "."
	 * 
	 * @return the classes of the package in the jar
	 * 
	 * @throws FileNotFoundException
	 *             if the jar cannot be accessed
	 * @throws IOException
	 *             if some IO-operation fails
	 * @throws ClassNotFoundException
	 *             if the class cannot be found
	 */
	public static Set<Class<?>> getFromJARFile(final String jar,
			final String packageName) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		final Set<Class<?>> classes = new HashSet<Class<?>>();
		final JarInputStream jarFile = new JarInputStream(new FileInputStream(
				jar));

		// iterate over the entries
		JarEntry jarEntry = jarFile.getNextJarEntry();
		while (jarEntry != null) {

			// check the classname if its needed
			String className = jarEntry.getName();
			if (className.matches("\\Q" + packageName + "\\E/?[^/]+\\.class")) {
				className = className.replaceAll("\\.class$", "");
				className = className.replace('/', '.');
				classes.add(Class.forName(className));
			}

			jarEntry = jarFile.getNextJarEntry();
		}

		return classes;
	}
}
