import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageProcessing {

	File inputFile, outputFile;
	BufferedImage inputImage, outputImage;
	int width;
	int height;
	double mean;

	ImageProcessing(String inputFilename, String outputFilename) {
		inputFile = new File(inputFilename);
		outputFile = new File(outputFilename);
		outputFile.deleteOnExit();
		
		try {
			inputImage = ImageIO.read(inputFile);
			outputImage = inputImage;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		width = inputImage.getWidth();
		height = inputImage.getHeight();
		mean = 0;
	}

	public void process() {
		bw();
		mean();
		threshold();
		makeTransparent();
		save();
	}


	private void mean() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				mean = mean + new Color(inputImage.getRGB(j, i)).getRed();
			}
		}
		mean = mean / (width * height);
	}
	
	private void bw() {
		try {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					Color c = new Color(inputImage.getRGB(j, i));
					int red = (int) (c.getRed() * 0.299);
					int green = (int) (c.getGreen() * 0.587);
					int blue = (int) (c.getBlue() * 0.114);
					outputImage.setRGB(j, i, new Color(red + green + blue, red
							+ green + blue, red + green + blue).getRGB());
				}
			}
		} catch (Exception e) {
		}
	}

	private void threshold() {
		try {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					Color c = new Color(inputImage.getRGB(j, i));
					if (c.getRed() < mean)
						outputImage.setRGB(j, i, Color.BLACK.getRGB());
					else
						outputImage.setRGB(j, i, Color.WHITE.getRGB());
				}
			}
		} catch (Exception e) {
		}
	}
	
	private void makeTransparent() {
		java.awt.Image image = makeColorTransparent(outputImage, Color.WHITE);
		outputImage = imageToBufferedImage(image);
	}


	private java.awt.Image makeColorTransparent(BufferedImage im,
			final Color color) {
		RGBImageFilter filter = new RGBImageFilter() {

			// the color we are looking for... Alpha bits are set to opaque
			public int markerRGB = color.getRGB() | 0xFF000000;

			public final int filterRGB(int x, int y, int rgb) {
				if ((rgb | 0xFF000000) == markerRGB) {
					// Mark the alpha bits as zero - transparent
					return 0x00FFFFFF & rgb;
				} else {
					// nothing to do
					return rgb;
				}
			}
		};

		ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

	private BufferedImage imageToBufferedImage(java.awt.Image image) {

		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
				image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bufferedImage.createGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();

		return bufferedImage;

	}
	
	private void save() {
		try {
			ImageIO.write(outputImage, "png", outputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
