package net.meisen.general.genmisc.resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

import net.meisen.general.genmisc.types.Streams;

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
	 * Creates a {@code Document} from the specified {@code xml}. Returns the
	 * created {@code Document} if possible, otherwise - i.e. if the creation
	 * failed - {@code null}.
	 * 
	 * @param xml
	 *            the bytes which make up the xml
	 * @param namespaceAware
	 *            {@code true} to be namespace aware within the document,
	 *            otherwise {@code false}, see
	 *            {@link DocumentBuilderFactory#setNamespaceAware(boolean)}
	 * 
	 * @return the created {@code Document} or {@code null} if an error occurred
	 * 
	 * @see Document
	 */
	public static Document createDocument(final byte[] xml,
			final boolean namespaceAware) {
		final InputStream bais = new ByteArrayInputStream(xml);
		final Document doc = createDocument(bais, namespaceAware);

		Streams.closeIO(bais);

		return doc;
	}

	/**
	 * Creates a {@code Document} from the specified {@code xml}. Returns the
	 * created {@code Document} if possible, otherwise - i.e. if the creation
	 * failed - {@code null}.
	 * 
	 * @param xml
	 *            the {@code InputStream} to read the xml from
	 * @param namespaceAware
	 *            {@code true} to be namespace aware within the document,
	 *            otherwise {@code false}, see
	 *            {@link DocumentBuilderFactory#setNamespaceAware(boolean)}
	 * 
	 * @return the created {@code Document} or {@code null} if an error occurred
	 * 
	 * @see Document
	 */
	public static Document createDocument(final InputStream xml,
			final boolean namespaceAware) {

		// get a factory to create a builder
		final DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		docFactory.setNamespaceAware(namespaceAware);

		try {

			// get the builder to build a document and build it
			final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			final Document doc = docBuilder.parse(xml);

			// make sure the InputStream is closed
			Streams.closeIO(xml);

			return doc;
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Method to clone a <code>Document</code>.
	 * 
	 * @param doc
	 *            the <code>Document</code> to be cloned
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
	 *            the <code>Document</code> to create the string of
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
	 *            the <code>Document</code> to create the byte-array from
	 * 
	 * @return the created byte-array
	 */
	public static byte[] createByteArray(final Document doc) {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		transform(doc, new StreamResult(bos));
		return bos.toByteArray();
	}

	/**
	 * Helper method used to transform a <code>Document</code> into the
	 * specified <code>StreamResult</code>.
	 * 
	 * @param doc
	 *            the <code>Document</code> to be transformed
	 * @param result
	 *            the <code>StreamResult</code> to transform the
	 *            <code>Document</code> to
	 */
	protected static void transform(final Document doc,
			final StreamResult result) {
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
