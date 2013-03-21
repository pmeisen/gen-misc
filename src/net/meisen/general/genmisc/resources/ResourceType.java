package net.meisen.general.genmisc.resources;

/**
 * Different types of resources (i.e. files or directories)
 * 
 * @author pmeisen
 * 
 */
public enum ResourceType {
	/**
	 * a path which exists on the file-system
	 */
	FILE_SYSTEM_PATH, 
	/**
	 * a file which exists on the file-system
	 */
	FILE_SYSTEM_FILE, 
	/**
	 * a file within a jar file
	 */
	IN_JAR_FILE, 
	/**
	 * a path which can be resolved through a jar
	 */
	IN_JAR_PATH;
}
