import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

@SuppressWarnings("serial")
public class FrameMenu extends JFrame {

	private Listener listener;

	private JMenuBar menuBar;
	private JMenu helpMenu;
	private JMenuItem aboutMenuItem;
	
	private JPanel panelPDF;
	private JButton buttonBrowsePDF;
	private JTextField txtBrowsePDF;
	private JPanel panelSignature;
	private JButton buttonBrowseSignature;
	private JTextField txtBrowseSignature;
	
	private JPanel panelSave;
	private JButton buttonBrowseSave;
	private JTextField txtBrowseSave;
	
	private JPanel panelButtons;
	private JButton buttonSign;
	private JButton buttonSave;
	private JButton buttonExit;

	FrameMenu() {
		super(Constants.appTitle);
		
		// UI Initialization
		setSize(400, 400);
		getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		listener = new Listener(this);
		
		// Menu Bar
		menuBar = new JMenuBar();
	    setJMenuBar(menuBar);
	    helpMenu = new JMenu("?");
	    menuBar.add(helpMenu);
	    aboutMenuItem = new JMenuItem("About");
	    helpMenu.add(aboutMenuItem);
	    aboutMenuItem.setActionCommand(listener.ABOUT);
	    aboutMenuItem.addActionListener(listener);
	    
		// Spacer
	    add(new JPanel());
	    
		// PDF File Panel
		TitledBorder titledBorderPDF = new TitledBorder("PDF File");
		panelPDF = new JPanel();
		panelPDF.setBorder(titledBorderPDF);
		panelPDF.setLayout(new GridLayout(2, 1));
 		txtBrowsePDF = new JTextField(); 
 		panelPDF.add(txtBrowsePDF);
		buttonBrowsePDF = new JButton("Browse");
		buttonBrowsePDF.setActionCommand(listener.BROWSEPDF);
		buttonBrowsePDF.addActionListener(listener);
		panelPDF.add(buttonBrowsePDF);
		add(panelPDF);

		// Spacer
	    add(new JPanel());
	    
		// Signature Image Panel
		TitledBorder titledBorderSignature = new TitledBorder("Signature Image");
		panelSignature = new JPanel();
		panelSignature.setBorder(titledBorderSignature);
		panelSignature.setLayout(new GridLayout(2, 1));
		txtBrowseSignature = new JTextField();
		panelSignature.add(txtBrowseSignature);
		buttonBrowseSignature = new JButton("Browse");
		buttonBrowseSignature.setActionCommand(listener.BROWSESIGN);
		buttonBrowseSignature.addActionListener(listener);
		panelSignature.add(buttonBrowseSignature);
		add(panelSignature);
		
		// Spacer
	    add(new JPanel());
	    
		// Output Panel
		TitledBorder titledBorderOutput = new TitledBorder("PDF Destination Folder");
		panelSave = new JPanel();
		panelSave.setBorder(titledBorderOutput);
		panelSave.setLayout(new GridLayout(2, 1));
		txtBrowseSave = new JTextField(); 
		panelSave.add(txtBrowseSave);
		buttonBrowseSave = new JButton("Browse"); 
		buttonBrowseSave.addActionListener(listener); 
		buttonBrowseSave.setActionCommand(listener.BROWSESAVE); 
		panelSave.add(buttonBrowseSave);
		add(panelSave);
		
		// Spacer
	    add(new JPanel());
	    
		// Buttons Panel
		panelButtons = new JPanel(); 
		panelButtons.setLayout(new GridLayout(1,3));
		buttonSign = new JButton("SIGN"); 
		buttonSign.addActionListener(listener); 
		buttonSign.setActionCommand(listener.SIGN); 
		panelButtons.add(buttonSign);
		buttonSave = new JButton("SAVE"); 
		buttonSave.addActionListener(listener); 
		buttonSave.setActionCommand(listener.SAVE); 
		panelButtons.add(buttonSave);
		buttonExit = new JButton("EXIT"); 
		buttonExit.addActionListener(listener); 
		buttonExit.setActionCommand(listener.EXIT); 
		panelButtons.add(buttonExit);
		add(panelButtons);
		
		// Configuration loading
		ConfigurationManager configurationManager = new ConfigurationManager();
		txtBrowsePDF.setText(configurationManager.getParameter(Constants.pdfConfigurationField));
		txtBrowseSignature.setText(configurationManager.getParameter(Constants.signatureConfigurationField));
		txtBrowseSave.setText(configurationManager.getParameter(Constants.outputConfigurationField));

		setResizable(false);
		setVisible(true);
	}
	
	void setFilepathPDF(String FilepathPDF) {
		txtBrowsePDF.setText(FilepathPDF);
	}
	
	String getFilepathPDF() {
		return txtBrowsePDF.getText();
	}
	
	void setFilepathSignature(String FilepathSignature) {
		txtBrowseSignature.setText(FilepathSignature);
	}
	
	String getFilepathSignature() {
		return txtBrowseSignature.getText();
	}
	
	void setFilepathSave(String FilepathSave) {
		txtBrowseSave.setText(FilepathSave);
	}
	
	String getFilepathSave() {
		return txtBrowseSave.getText();
	}
}

class Listener implements ActionListener {

	FrameMenu window;

	final String ABOUT = "ABOUT";
	final String BROWSEPDF = "BROWSEPDF";
	final String BROWSESIGN = "BROWSESIGN";
	final String BROWSESAVE = "BROWSESAVE";
	final String SIGN = "SIGN";
	final String SAVE = "SAVE";
	final String EXIT = "EXIT";

	Listener(FrameMenu frame) {
		window = frame;
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals(ABOUT))
			ABOUT();
		
		if (arg0.getActionCommand().equals(BROWSEPDF))
			BROWSEPDF();
		
		if (arg0.getActionCommand().equals(BROWSESIGN))
			BROWSESIGN();
		
		if (arg0.getActionCommand().equals(BROWSESAVE))
			BROWSESAVE();
		
		if (arg0.getActionCommand().equals(SIGN))
			SIGN();
		
		if (arg0.getActionCommand().equals(SAVE))
			SAVE();
			
		if (arg0.getActionCommand().equals(EXIT))
			EXIT();
	}

	private void ABOUT() {
		String aboutMessage = "<html>" +
				  			  "<b>" + Constants.appTitle + "</b><br>" +
				  			  "<br>" +
							  "<b>Author</b>: <spacer>" + Constants.appAuthor + "<br>" +
							  "<b>Version</b>: " + Constants.appVersion + "<br>" +
							  "<b>Date</b>: " + Constants.appReleaseDate + "<br>" +
							  "<b>License</b>: " + Constants.appLicense + "<br>" +							
							  "</html>";
		
		JLabel label = new JLabel(aboutMessage);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
		JOptionPane.showOptionDialog(null, label, "About", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
	}
	
	private void BROWSEPDF() {
		try {
			JFileChooser fileChooser = new JFileChooser();
			
			if (!window.getFilepathPDF().equals("")) {
				fileChooser.setCurrentDirectory(new File(window.getFilepathPDF()).getParentFile());
			}
			
			fileChooser.setFileFilter(new PDFFileFilter());
			int n = fileChooser.showOpenDialog(new JPanel());
			if (n == JFileChooser.APPROVE_OPTION) {
				window.setFilepathPDF(fileChooser.getSelectedFile().getAbsolutePath());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void BROWSESIGN() {
		try {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new ImageFilter());
			fileChooser.setCurrentDirectory(new File(window.getFilepathSignature()).getParentFile());
			int n = fileChooser.showOpenDialog(new JPanel());
			if (n == JFileChooser.APPROVE_OPTION) {
				window.setFilepathSignature(fileChooser.getSelectedFile().getAbsolutePath());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void BROWSESAVE() {
		try {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);;
			int n = fileChooser.showOpenDialog(new JPanel());
			if (n == JFileChooser.APPROVE_OPTION) {
				window.setFilepathSave(fileChooser.getSelectedFile().getAbsolutePath());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void SIGN() {
		if (window.getFilepathPDF().equals("")) {
			JOptionPane.showMessageDialog(null,"Please select a PDF file to be signed", Constants.appTitle, JOptionPane.ERROR_MESSAGE);
		} else if (window.getFilepathSignature().equals("")){
			JOptionPane.showMessageDialog(null,"Please select a signature image", Constants.appTitle, JOptionPane.ERROR_MESSAGE);
		} else {
			createDirectory(new File(Constants.temporaryFolder));
			new ImageProcessing(window.getFilepathSignature(), Constants.temporarySignatureFilename).process();
			new FramePreview((new Document(window.getFilepathPDF(), Constants.temporarySignatureFilename)).getDocument());
		}
	}

	private void SAVE() {
		try {
			String filename = new File(window.getFilepathPDF()).getName();
			new FileCopy(window.getFilepathPDF(), window.getFilepathSave() + File.separatorChar + filename);			
			//new File(window.getFilepathPDF()).delete();
			JOptionPane.showOptionDialog(null, "Correctly saved", Constants.appTitle, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showOptionDialog(null, "Error during saving", Constants.appTitle, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
		}
	}

	private void EXIT() {
		// Export current configuration
		ConfigurationManager configurationManager = new ConfigurationManager();
		configurationManager.setParameter(Constants.pdfConfigurationField, window.getFilepathPDF());
		configurationManager.setParameter(Constants.signatureConfigurationField, window.getFilepathSignature());
		configurationManager.setParameter(Constants.outputConfigurationField, window.getFilepathSave());
		configurationManager.saveConfiguration();
		//
		
		// Delete temporary files
        deleteDirectory( new File(Constants.temporaryFolder));
		//
		
		System.exit(0);
	}
	
	private void createDirectory(File file) {
		file.mkdir();
	}
	
	private void deleteDirectory(File file) {
		if (!file.exists())
			return;
					
        for (File subfile : file.listFiles()) {
        	  
            if (subfile.isDirectory()) {
                deleteDirectory(subfile);
            }
  
            subfile.delete();
        }
	}
}

class PDFFileFilter extends FileFilter {

	public boolean accept(File file) {
		if (file.isDirectory())
			return true;

		String fname = file.getName().toLowerCase();
		return fname.endsWith("pdf");
	}

	public String getDescription() {
		return "PDF File";
	}
}

class ImageFilter extends FileFilter {

	public boolean accept(File file) {
		if (file.isDirectory())
			return true;

		String fname = file.getName().toLowerCase();
		return fname.endsWith("bmp") || fname.endsWith("png") || fname.endsWith("jpg");
	}

	public String getDescription() {
		return "BMP/PNG Image";
	}
}