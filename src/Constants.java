import java.io.File;

public final class Constants {
	// General
	public final static String appTitle = "PDFSigner";
	public final static String appAuthor = "Giovanni Pecoraro";
	public final static String appVersion = "1.0";
	public final static String appReleaseDate = "2021-10-07";
	public final static String appLicense = "MIT";
	
	// Configuration
	public final static String configurationFile = "config.xml";
	public final static String configurationField = "configuration";
	public final static String pdfConfigurationField = "default_pdf";
	public final static String signatureConfigurationField = "default_signature";
	public final static String outputConfigurationField = "default_output";
	
	// Temporary files
	public final static String temporaryFolder = System.getProperty("java.io.tmpdir") + File.separatorChar + "pdf_signer" + File.separatorChar;
	public final static String temporarySignatureFilename = Constants.temporaryFolder + "FRMPROC";
	
}
