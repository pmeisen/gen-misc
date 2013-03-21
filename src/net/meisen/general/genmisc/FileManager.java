package net.meisen.general.genmisc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import net.meisen.general.genmisc.types.Files;

/**
 * Used to create some files at a specific destination while testing
 * 
 * @author pmeisen
 * 
 */
public class FileManager {

	private ArrayList<File> createdFiles = new ArrayList<File>();

	/**
	 * Helper class used to create a file which will be deleted after test run
	 * 
	 * @param dir
	 *            the directory to create the file at
	 * @return the {@link File} which was created
	 * @throws IOException
	 *             if the file could not be created
	 */
	public File createFile(final String dir) throws IOException {
		final File file = new File(dir, UUID.randomUUID().toString());
		file.createNewFile();

		createdFiles.add(file);

		return file;
	}

	/**
	 * Helper to create an empty directory within a specific directory. The
	 * directory should not be filled.
	 * 
	 * @param dir
	 *            the directory to create a directory in
	 * @return the created directory
	 * @throws IOException
	 *             if the file could not be created
	 */
	public File createDir(final String dir) throws IOException {
		final File file = new File(dir, UUID.randomUUID().toString());
		file.mkdir();

		createdFiles.add(file);

		return file;
	}

	/**
	 * Removes all the files created with this <code>FileManager</code>
	 */
	public void cleanUp() {
		int oldSize = -1;

		while (createdFiles.size() > 0) {
			final ArrayList<File> notDeleted = new ArrayList<File>();

			for (final File file : createdFiles) {
				if (!file.delete() && !Files.deleteDir(file)) {
					notDeleted.add(file);
				}
			}
			createdFiles = notDeleted;

			// check if something changed
			int newSize = createdFiles.size();
			if (oldSize == newSize) {
				System.err.println("The following files cannot be deleted:");
				for (final File file : createdFiles) {
					System.err.println("- " + file.getAbsolutePath());
				}
				
				// stop here
				break;
			} else {
				oldSize = newSize;
			}
		}

		// remove all
		createdFiles.clear();
	}
}
