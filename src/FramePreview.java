import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.DefaultFormatter;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.exceptions.BadPasswordException;

@SuppressWarnings("serial")
class FramePreview extends JFrame {

	JLabel imageLabel;
	Image imagePDF;
	JTextField txtPage;
	JSpinner scaleSpinner;
	mouseListener mouselistener;
	keyListener keylistener;

	Document document;
	PdfDecoder pdfdecoder;
	int pageNumber;
	
	FramePreview(Document doc) {

		super("Click to sign");
		
		// GUI
		mouselistener = new mouseListener(this);
		keylistener = new keyListener(this);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel p0 = new JPanel();
		add(p0);
		p0.setLayout(new BorderLayout());

		imageLabel = new JLabel();
		imageLabel.addMouseListener(mouselistener);

		p0.add(imageLabel, BorderLayout.CENTER);

		JPanel p1 = new JPanel(); p1.setLayout(new GridLayout(2, 1));
		p0.add(p1, BorderLayout.NORTH);
		

		JPanel p10 = new JPanel(); p10.setLayout(new GridLayout(1, 3));
		JButton back = new JButton("<<<");
		back.setActionCommand(keylistener.BACK);
		back.addActionListener(keylistener);
		p10.add(back);

		txtPage = new JTextField();
		txtPage.setHorizontalAlignment(JTextField.CENTER);
		p10.add(txtPage);

		JButton next = new JButton(">>>");
		next.setActionCommand(keylistener.NEXT);
		next.addActionListener(keylistener);
		p10.add(next);
		
		p1.add(p10);
		
		JPanel p11 = new JPanel(); p11.setLayout(new GridLayout(1, 2));
		JButton undo = new JButton("UNDO");
		undo.setActionCommand(keylistener.UNDO);
		undo.addActionListener(keylistener);
		p11.add(undo);

	    SpinnerNumberModel scaleSpinnerModel;
	    Integer current = 100;
	    Integer min = 1;
	    Integer max = 200;
	    Integer step = 1;
	    scaleSpinnerModel = new SpinnerNumberModel(current, min, max, step);
	    scaleSpinner = new JSpinner(scaleSpinnerModel);
	    JComponent comp = scaleSpinner.getEditor();
	    JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
	    DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
	    formatter.setCommitsOnValidEdit(true);
		p11.add(scaleSpinner);

		JButton redo = new JButton("REDO");
		redo.setActionCommand(keylistener.REDO);
		redo.addActionListener(keylistener);
		p11.add(redo);
		
		p1.add(p11);

		
		try {
			document = doc;
			pdfdecoder = new PdfDecoder();	
			pageNumber = 0;
			previewPDF();
			setShownPage(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	
	public void previewPDF() throws IOException {

		try {
			
			pdfdecoder.openPdfFile(document.getCurrentVersion());
			pageNumber = pdfdecoder.getPageCount();
			
			imagePDF = pdfdecoder.getPageAsImage(document.getPage());
			imageLabel.setIcon(new ImageIcon(imagePDF));
			Graphics graphics = imagePDF.getGraphics();
			graphics.drawRoundRect(100, 100,30, 50, 5, 5);
			imageLabel.setIcon(new ImageIcon(imagePDF));
			
			pdfdecoder.closePdfFile();
			
			repaint();
			pack();
		} catch (PdfException e) {
			e.printStackTrace();
		}
		
	}

	public void setShownPage(int p) {
		try {
			document.setPage(p);
			txtPage.setText(document.getPage() + "/" + getPageNumber());
			previewPDF();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getShownPage() {
		return document.getPage();
	}

	public int getPageNumber() {
		return pageNumber;
	}
	
	public void sign(double x, double y, Integer scale) {
		try {
			document.sign(x,y, scale);
			previewPDF();
		} catch (BadPasswordException e) {
			JOptionPane.showOptionDialog(null, "Document protected with password", Constants.appTitle, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
			e.printStackTrace();
			dispose();
		} catch (DocumentException e) {
			JOptionPane.showOptionDialog(null, "Error during document signing", Constants.appTitle, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
			e.printStackTrace();
			dispose();
		} catch (IOException e) {
			e.printStackTrace();
			dispose();
		}
	}

	public void undo() {
		try {
			document.undo();
			previewPDF();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void redo() {
		try {
			document.redo();
			previewPDF();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
class mouseListener implements MouseListener {
	FramePreview window;

	mouseListener(FramePreview frame) {
		window = frame;
	}

	public void mouseClicked(MouseEvent arg0) {
		window.sign(arg0.getX(),window.imageLabel.getHeight() - arg0.getY() + 0.5*(window.imagePDF.getHeight(window)-window.imageLabel.getHeight()), (Integer)window.scaleSpinner.getValue());

	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {

	}

	public void mouseReleased(MouseEvent arg0) {

	}
	
}

class keyListener implements ActionListener {

	FramePreview window;
	final String BACK = "BACK";
	final String NEXT = "NEXT";
	final String UNDO = "UNDO";
	final String REDO = "REDO";

	keyListener(FramePreview frame) {
		window = frame;
	}

	public void actionPerformed(ActionEvent arg0) {
		
		if (arg0.getActionCommand().equals(BACK))
			BACK();
		
		if (arg0.getActionCommand().equals(NEXT))
			NEXT();
		
		if (arg0.getActionCommand().equals(UNDO))
			UNDO();
		
		if (arg0.getActionCommand().equals(REDO))
			REDO();
	}

	public void BACK() {
		if (window.getShownPage() - 1 > 0) {
			window.setShownPage(window.getShownPage() - 1);
			try {
				window.previewPDF();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void NEXT() {
		if (window.getPageNumber() > window.getShownPage()) {
			window.setShownPage(window.getShownPage() + 1);
			try {
				window.previewPDF();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public void UNDO() {
		window.undo();	
	}
	
	public void REDO() {
		window.redo();
	}
}
}