import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class Document {

	String filename;
	String imageSignature;
	String[] temporaryFiles;
	PdfReader pdfReader;
	PdfStamper pdfStamper;
	int version;
	int lastVersion;
	Image image;

	FramePreview framePreview;
	int currentPage;
	
	Document(String pdf, String signatureFilepath) {

		filename = pdf;
		imageSignature = signatureFilepath;
		pdfReader = null;
		pdfStamper = null;
		version = 0;
		
		temporaryFiles = new String[256];
		temporaryFiles[version] = new String(filename);
		createTemporaryFile();
		
		setPage(1);
	}
	
	public Document getDocument() {
		return this;
	}

	public void sign(double x, double y, float scale) throws DocumentException, BadPasswordException, IOException {
		pdfReader = new PdfReader(new FileInputStream(temporaryFiles[version]));

		createTemporaryFile();
		pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(temporaryFiles[version]));

		PdfContentByte content;
		image = Image.getInstance(imageSignature);
		image.scalePercent(scale);
		content = pdfStamper.getOverContent(getPage());
		image.setAbsolutePosition(Math.round(x - image.getScaledWidth() / 2), Math.round(y - image.getScaledHeight() / 2));
		content.addImage(image);

		pdfStamper.close();
		pdfReader.close();
		saveFile();
	}

	public void undo() {
		if (version > 1) {
			version--;
			saveFile();
		}
	}

	public void redo() {
		if (version < lastVersion) {
			version++;
			saveFile();
		}
	}

	private void createTemporaryFile() {
		try {
			
			String tmpFile = Constants.temporaryFolder + "DOC" + (version+1) + ".tmp";
			version++;
			lastVersion = version;
			temporaryFiles[version] = new String(tmpFile);
			
			new FileCopy(temporaryFiles[version-1],temporaryFiles[version]);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getCurrentVersion() {
		return temporaryFiles[version];
	}
	
	public void setPage(int page) {
		currentPage = page;
	}
	
	public int getPage() {
		return currentPage;
	}
	
	public void saveFile() {
		try {
			pdfReader = new PdfReader(new FileInputStream(temporaryFiles[version]));
			pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(filename));
			pdfStamper.close();
			pdfReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
