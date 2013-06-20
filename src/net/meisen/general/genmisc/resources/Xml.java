package net.meisen.general.genmisc.resources;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Helper methods to work with Xml files
 * 
 * @author pmeisen
 * 
 */
public class Xml {

	/**
	 * Method to clone a <code>Document</code>.
	 * 
	 * @param doc
	 *          the <code>Document</code> to be cloned
	 * 
	 * @return the clone of the <code>Document</code>
	 */
	public static Document cloneDocument(final Document doc) {
		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		final DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
		} catch (final ParserConfigurationException e) {
			// should never happen
			throw new IllegalStateException(
					"Reached an invalid state, please check!", e);
		}
		final Node originalRoot = doc.getDocumentElement();

		final Document copiedDocument = db.newDocument();
		final Node copiedRoot = copiedDocument.importNode(originalRoot, true);
		copiedDocument.appendChild(copiedRoot);

		return copiedDocument;
	}

	/**
	 * Creates a string out of the passed <code>Document</code>.
	 * 
	 * @param doc
	 *          the <code>Document</code> to create the string of
	 * 
	 * @return the <code>Document</code> as string
	 */
	public static String createString(final Document doc) {
		final StringWriter writer = new StringWriter();
		transform(doc, new StreamResult(writer));
		return writer.getBuffer().toString();
	}

	/**
	 * Creates a byte-array out of the <code>Document</code>.
	 * 
	 * @param doc
	 *          the <code>Document</code> to create the byte-array from
	 * 
	 * @return the created byte-array
	 */
	public static byte[] createByteArray(final Document doc) {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		transform(doc, new StreamResult(bos));
		return bos.toByteArray();
	}

	/**
	 * Helper method used to transform a <code>Document</code> into the specified
	 * <code>StreamResult</code>.
	 * 
	 * @param doc
	 *          the <code>Document</code> to be transformed
	 * @param result
	 *          the <code>StreamResult</code> to transform the
	 *          <code>Document</code> to
	 */
	protected static void transform(final Document doc, final StreamResult result) {
		final TransformerFactory tf = TransformerFactory.newInstance();

		try {
			final Transformer transformer = tf.newTransformer();
			transformer.transform(new DOMSource(doc), result);
		} catch (final TransformerConfigurationException e) {
			// should never happen
			throw new IllegalStateException(
					"Reached an invalid state, please check!", e);
		} catch (final TransformerException e) {
			throw new IllegalArgumentException(
					"The passed document cannot be transformed!", e);
		}
	}
}
