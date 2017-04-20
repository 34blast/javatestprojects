package rmscott.pdfgeneration;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

public class CreatePDFFromXML {

	/**
	 * Method that will convert the given XML to PDF
	 * 
	 * @throws IOException
	 * @throws FOPException
	 * @throws TransformerException
	 */
	public void convertToPDF() throws IOException, FOPException, TransformerException {

		InputStream xslInputSteam = this.getClass().getClassLoader().getResourceAsStream("employees.xsl");
		InputStream xmlInputSteam = this.getClass().getClassLoader().getResourceAsStream("employees.xml");
		StreamSource xmlSource = new StreamSource(xmlInputSteam);
		// create an instance of fop factory
		FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
		// a user agent is needed for transformation
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		// Setup output
		OutputStream out;
		out = new java.io.FileOutputStream("./target/outputfiles/employee.pdf");

		try {
			// Construct fop with desired output format
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

			// Setup XSLT
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(xslInputSteam));

			// Resulting SAX events (the generated FO) must be piped through to
			// FOP
			Result res = new SAXResult(fop.getDefaultHandler());

			// Start XSLT transformation and FOP processing
			// That's where the XML is first transformed to XSL-FO and then
			// PDF is created
			transformer.transform(xmlSource, res);
		} finally {
			out.close();
			xslInputSteam.close();
			xmlInputSteam.close();
		}
	} // end of convertToPDF

	/**
	 * This method will convert the given XML to XSL-FO
	 * 
	 * @throws IOException
	 * @throws FOPException
	 * @throws TransformerException
	 */
	public void convertToFO() throws IOException, FOPException, TransformerException {
		InputStream xslInputSteam = this.getClass().getClassLoader().getResourceAsStream("employees.xsl");
		InputStream xmlInputSteam = this.getClass().getClassLoader().getResourceAsStream("employees.xml");

		StreamSource xmlSource = new StreamSource(xmlInputSteam);

		// a user agent is needed for transformation
		/* FOUserAgent foUserAgent = fopFactory.newFOUserAgent(); */
		// Setup output
		OutputStream out;

		out = new java.io.FileOutputStream("./target/outputfiles/temp.txt");
		// out = new java.io.FileOutputStream("temp.txt");

		try {
			// Setup XSLT
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(xslInputSteam));

			// Resulting SAX events (the generated FO) must be piped through to
			// FOP
			// Result res = new SAXResult(fop.getDefaultHandler());

			Result res = new StreamResult(out);

			// Start XSLT transformation and FOP processing
			// File created here
			transformer.transform(xmlSource, res);
		} finally {
			out.close();
			xslInputSteam.close();
			xmlInputSteam.close();
		}

	} // end of convertToFO

	public static void main(String[] args) {

		System.out.println("CreatePDFFromXML.main() starting execution ");
		System.out.println();

		CreatePDFFromXML CreatePDFFromXML = new CreatePDFFromXML();
		try {
			CreatePDFFromXML.convertToFO();
			CreatePDFFromXML.convertToPDF();
		} catch (FOPException fopEx) {
			System.err.println("FOPException caught " + fopEx);
			fopEx.printStackTrace();
		} catch (IOException ioEx) {
			System.err.println("TransformerException caught " + ioEx);
			ioEx.printStackTrace();
		} catch (TransformerException transFormEx) {
			System.err.println("TransformerException caught " + transFormEx);
			transFormEx.printStackTrace();
		}

		System.out.println();
		System.out.println("CreatePDFFromXML.main() stopping execution ");

	} // end of main

}
