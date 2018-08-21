
/**
 * Assignment 1
 * Submitted by: 
 * Matan Chibotero. 	ID# 204076962
 * Liel Levi 	ID# 307983320
 * Yoni Amikam	ID# 311532774
 */
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.BitSet;
import javax.imageio.ImageIO;

public class PngLossyEncoderDecoder {
	public PngLossyEncoderDecoder() {
		// TODO Auto-generated constructor stub
	}

	public void Compress(String src, int LevelSelected) throws IOException {
		double minEntropy = 0, Entropyprev = 0, Entropytop = 0, Entropyave = 0, EntropyaveLossy = 0, Entropymid = 0,
				EntropymidLossy = 0, EntropyprevLossy = 0, EntropytopLossy = 0;
		BufferedImage imgBuffer = ImageIO.read(new File(src));
		File file = new File(src);
		InputStream input = new FileInputStream(file);
		byte[] bytes = new byte[(int) file.length()];
		input.read(bytes, 0, bytes.length);// Reads the image into an array of bytes
		byte[][] pxtop = new byte[2][imgBuffer.getWidth() * ((int) bytes[28] / 8)];// bytes[28]=deep pixel in bmp//2
																					// rows of pixels
		byte[] pxprev = new byte[imgBuffer.getWidth() * ((int) bytes[28] / 8)];// Row pixels
		byte[] pxprevnew = new byte[imgBuffer.getWidth() * ((int) bytes[28] / 8)];
		byte[] pxtopnew = new byte[imgBuffer.getWidth() * ((int) bytes[28] / 8)];
		byte[] pxavenew = new byte[imgBuffer.getWidth() * ((int) bytes[28] / 8)];
		byte[] pxmidnew = new byte[imgBuffer.getWidth() * ((int) bytes[28] / 8)];
		byte[] pxprevnewL = new byte[imgBuffer.getWidth() * ((int) bytes[28] / 8)];// Lossy
		byte[] pxtopnewL = new byte[imgBuffer.getWidth() * ((int) bytes[28] / 8)];// Lossy
		byte[] pxavenewL = new byte[imgBuffer.getWidth() * ((int) bytes[28] / 8)];// Lossy
		byte[] pxmidnewL = new byte[imgBuffer.getWidth() * ((int) bytes[28] / 8)];// Lossy
		int[] colorpx = new int[256];// Frequency of colors
		int[] colorpxtotal = new int[256];// Frequency of total colors
		byte[] pixels = new byte[imgBuffer.getWidth() * imgBuffer.getHeight() * ((int) bytes[28] / 8)];// Pixel array of
																										// image
		for (int i = 0; i < pixels.length; i++) {// The first 54 bytes in the bmp image are information about the image
			pixels[i] = bytes[i + 54];
		}
		File file2 = new File(src.substring(0, src.length() - 3) + "LYM-prev");// Intermediate file
		OutputStream out = new FileOutputStream(file2);
		byte choose = 0;// The conversion chosen by the algorithm
		byte[] beforepx = new byte[54];
		for (int i = 0; i < 54; i++) {
			beforepx[i] = bytes[i];
		}
		out.write(beforepx);// Write to the first 54 bytes
		for (int i = 0; i < imgBuffer.getHeight(); i++) {
			if (i == 0) {// first row
				for (int j = 0; j < imgBuffer.getWidth() * ((int) bytes[28] / 8); j++) {
					pxprev[j] = pixels[i * imgBuffer.getWidth() * ((int) bytes[28] / 8) + j];

				}
				Entropyprev = PngPrev(((int) bytes[28] / 8), pxprev, pxprevnew, colorpxtotal, i);// In the first row,
																									// the algorithm
																									// allows conversion
																									// only by the type
																									// of the previous
																									// pixel
				choose = 1;
				out.write(choose);// Write the option I have selected so that the decoding can be restored to the
									// correct image.
				out.write(pxprevnew);
			} else {// other rows
				for (int j = 0; j < imgBuffer.getWidth() * ((int) bytes[28] / 8); j++) {// A loop for filling the
																						// current row
					pxprev[j] = pixels[i * imgBuffer.getWidth() * ((int) bytes[28] / 8) + j];
					if (pxprev[j] < 0)
						colorpx[pxprev[j] + 256]++;
					else
						colorpx[pxprev[j]]++;
				}
				for (int j = 0; j < 256; j++) {// Entropy for row without conversions
					if (colorpx[j] != 0 || colorpxtotal[j] != 0) {
						minEntropy += ((double) (colorpx[j] + colorpxtotal[j]) / ((i + 1) * pxprev.length))
								* ((Math.log(((double) (colorpx[j] + colorpxtotal[j]) / ((i + 1) * pxprev.length)))
										/ Math.log(2)))
								* -1;
					}
				}
				for (int j = 0; j < imgBuffer.getWidth() * ((int) bytes[28] / 8); j++) {// A loop for filling the row
																						// above
					pxtop[0][j] = pixels[(i) * imgBuffer.getWidth() * ((int) bytes[28] / 8) + j];
				}
				choose = 0;
				Entropyprev = PngPrev(((int) bytes[28] / 8), pxprev, pxprevnew, colorpxtotal, i);
				if (Entropyprev < minEntropy) {
					choose = 1;
					minEntropy = Entropyprev;
				}
				Entropytop = PngTop(((int) bytes[28] / 8), pxtop, pxtopnew, colorpxtotal, i);
				if (Entropytop < minEntropy) {
					choose = 2;
					minEntropy = Entropytop;
				}
				Entropyave = PngAve(((int) bytes[28] / 8), pxtop, pxavenew, colorpxtotal, i);
				if (Entropyave < minEntropy) {
					choose = 3;
					minEntropy = Entropyave;
				}
				Entropymid = PngMid(((int) bytes[28] / 8), pxtop, pxmidnew, colorpxtotal, i);
				if (Entropymid < minEntropy) {
					choose = 4;
					minEntropy = Entropymid;
				}

				if (LevelSelected != 0) {// Lossy
					EntropyprevLossy = PngPrevLossy(((int) bytes[28] / 8), pxprev, pxprevnewL, colorpxtotal, i,
							(100 / LevelSelected));
					if (EntropyprevLossy < minEntropy) {
						choose = 5;
						minEntropy = EntropyprevLossy;
					}
					EntropytopLossy = PngTopLossy(((int) bytes[28] / 8), pxtop, pxtopnewL, colorpxtotal, i,
							(100 / LevelSelected));
					if (EntropytopLossy < minEntropy) {
						choose = 6;
						minEntropy = EntropytopLossy;
					}
					EntropyaveLossy = PngAveLossy(((int) bytes[28] / 8), pxtop, pxavenewL, colorpxtotal, i,
							(100 / LevelSelected));
					if (EntropyaveLossy < minEntropy) {
						choose = 7;
						minEntropy = EntropyaveLossy;
					}
					EntropymidLossy = PngMidLossy(((int) bytes[28] / 8), pxtop, pxmidnewL, colorpxtotal, i,
							(100 / LevelSelected));
					if (EntropymidLossy < minEntropy) {
						choose = 8;
						minEntropy = EntropymidLossy;
					}
				}
				out.write(choose);
				if (choose == 0) {
					out.write(pxprev);
					for (int j = 0; j < pxprev.length; j++) {
						colorpxtotal[pxprev[j] & 0xFF]++;// Update the frequency of colors
					}
				} else if (choose == 1) {
					out.write(pxprevnew);
					for (int j = 0; j < pxprevnew.length; j++) {
						colorpxtotal[pxprevnew[j] & 0xFF]++;
					}
				} else if (choose == 2) {
					out.write(pxtopnew);
					for (int j = 0; j < pxtopnew.length; j++) {
						colorpxtotal[pxtopnew[j] & 0xFF]++;
					}
				} else if (choose == 3) {
					out.write(pxavenew);
					for (int j = 0; j < pxavenew.length; j++) {
						colorpxtotal[pxavenew[j] & 0xFF]++;
					}
				} else if (choose == 4) {
					out.write(pxmidnew);
					for (int j = 0; j < pxmidnew.length; j++) {
						colorpxtotal[pxmidnew[j] & 0xFF]++;
					}
				} else if (choose == 5) {
					out.write(pxprevnewL);
					for (int j = 0; j < pxprevnewL.length; j++) {
						colorpxtotal[pxprevnewL[j] & 0xFF]++;
					}
				} else if (choose == 6) {
					out.write(pxtopnewL);
					for (int j = 0; j < pxtopnewL.length; j++) {
						colorpxtotal[pxtopnewL[j] & 0xFF]++;
					}
				} else if (choose == 7) {
					out.write(pxavenewL);
					for (int j = 0; j < pxavenewL.length; j++) {
						colorpxtotal[pxavenewL[j] & 0xFF]++;
					}
				} else if (choose == 8) {
					out.write(pxmidnewL);
					for (int j = 0; j < pxmidnewL.length; j++) {
						colorpxtotal[pxmidnewL[j] & 0xFF]++;
					}
				}

			}
			if (choose >= 0 && choose < 5) {// Without lossy
				for (int j = 0; j < imgBuffer.getWidth() * ((int) bytes[28] / 8); j++) {// Fill in the row above for the
																						// next round in the loop
					pxtop[1][j] = pixels[(i) * imgBuffer.getWidth() * ((int) bytes[28] / 8) + j];
				}
			} else if (choose == 5) {
				for (int j = 0; j < imgBuffer.getWidth() * ((int) bytes[28] / 8); j++) {// Fill in the row above for the
																						// next round in the loop
					if (j < ((int) bytes[28] / 8))// First bytes in a row//bytes[28]=deep pixel in bmp.
						pxtop[1][j] = pixels[(i) * imgBuffer.getWidth() * ((int) bytes[28] / 8) + j];// unchanged
					else {
						pxtop[1][j] = (byte) ((pxtop[1][j - ((int) bytes[28] / 8)] & 0xFF) + (pxprevnewL[j] & 0xFF));// Change
																														// by
																														// conversion
					}
				}
			} else if (choose == 6) {
				for (int j = 0; j < imgBuffer.getWidth() * ((int) bytes[28] / 8); j++) {
					if (j < ((int) bytes[28] / 8))
						pxtop[1][j] = pixels[(i) * imgBuffer.getWidth() * ((int) bytes[28] / 8) + j];
					else {
						pxtop[1][j] = (byte) ((pxtop[1][j] & 0xFF) + (pxtopnewL[j] & 0xFF));
					}
				}
			} else if (choose == 7) {
				for (int j = 0; j < imgBuffer.getWidth() * ((int) bytes[28] / 8); j++) {
					if (j < ((int) bytes[28] / 8))
						pxtop[1][j] = pixels[(i) * imgBuffer.getWidth() * ((int) bytes[28] / 8) + j];
					else {
						pxtop[1][j] = (byte) (((pxtop[1][j] & 0xFF) + (pxtop[1][j - ((int) bytes[28] / 8)] & 0xFF)) / 2
								+ (pxavenewL[j] & 0xFF));
					}
				}
			} else if (choose == 8) {
				byte[] temppx = new byte[pxtop[1].length];// Temp that saves the pixel row above
				for (int k = 0; k < temppx.length; k++)
					temppx[k] = pxtop[1][k];
				for (int j = 0; j < imgBuffer.getWidth() * ((int) bytes[28] / 8); j++) {
					if (j < ((int) bytes[28] / 8))
						pxtop[1][j] = pixels[(i) * imgBuffer.getWidth() * ((int) bytes[28] / 8) + j];
					else {
						pxtop[1][j] = (byte) (((pxtop[1][j] & 0xFF) + (pxtop[1][j - ((int) bytes[28] / 8)] & 0xFF)
								- (temppx[j - ((int) bytes[28] / 8)] & 0xFF)) + (pxmidnewL[j] & 0xFF));
					}
				}
			}

			minEntropy = 0;// reset
			for (int j = 0; j < 256; j++) {// reset
				colorpx[j] = 0;
			}
		}
		byte[] afterpx = new byte[bytes.length - 54
				- imgBuffer.getWidth() * imgBuffer.getHeight() * ((int) bytes[28] / 8)];// Bytes are at the end of the
																						// image after array pixels
		for (int i = imgBuffer.getWidth() * imgBuffer.getHeight() * ((int) bytes[28] / 8) + 54; i < bytes.length; i++) {
			afterpx[i - (imgBuffer.getWidth() * imgBuffer.getHeight() * ((int) bytes[28] / 8) + 54)] = bytes[i];
		}
		out.write(afterpx);
		HufmannEncoderDecoder Huffmann = new HufmannEncoderDecoder();
		String[] Address = new String[2];
		Address[0] = src.substring(0, src.length() - 3) + "LYM-prev";
		String[] AddressCompressed = new String[2];
		AddressCompressed[0] = src.substring(0, src.length() - 3) + "LYM";// Final LYM file
		Huffmann.CompressWithArray(Address, AddressCompressed);
		out.close();
		file2.delete();
		input.close();
	}

	public double PngMidLossy(int deep, byte[][] bytes, byte[] newpx, int[] colorpxtotal, int k, int size)
			throws IOException {
		int[] colorpx = new int[256];
		double Entropy = 0;
		byte pxprev = 0;// The previous pixel with a lossy
		int count = 0;// The number of pixels lost
		for (int j = 0; j < deep; j++) {
			for (int i = 0; i < newpx.length; i = i + deep) {
				if (i < deep) {// The first pixels in a row
					newpx[i + j] = bytes[0][i + j];
					pxprev = bytes[0][i + j];
				} else {
					if (((bytes[0][i + j] & 0xFF)
							- ((bytes[1][i + j] & 0xFF) + (pxprev & 0xFF) - (bytes[1][i + j - deep] & 0xFF))) < size
							&& ((bytes[0][i + j] & 0xFF) - ((bytes[1][i + j] & 0xFF) + (pxprev & 0xFF)
									- (bytes[1][i + j - deep] & 0xFF))) > -size) {// If the difference in the range of
																					// size
						newpx[i + j] = (byte) ((bytes[0][i + j] & 0xFF)
								- ((bytes[1][i + j] & 0xFF) + (pxprev & 0xFF) - (bytes[1][i + j - deep] & 0xFF)));
						pxprev = bytes[0][i + j];
					} else if (((bytes[0][i + j] & 0xFF)
							- ((bytes[1][i + j] & 0xFF) + (pxprev & 0xFF) - (bytes[1][i + j - deep] & 0xFF))) < size) {
						newpx[i + j] = (byte) -size;// The value is most similar to a pixel
						pxprev = (byte) (((bytes[1][i + j] & 0xFF) + (pxprev & 0xFF) - (bytes[1][i + j - deep] & 0xFF))
								- (size));
						count++;
					} else {
						newpx[i + j] = (byte) size;// The value is most similar to a pixel
						pxprev = (byte) (((bytes[1][i + j] & 0xFF) + (pxprev & 0xFF) - (bytes[1][i + j - deep] & 0xFF))
								+ (size));
						count++;
					}
				}
				if (newpx[i] < 0)
					colorpx[newpx[i] + 256]++;
				else
					colorpx[newpx[i]]++;
			}
		}
		for (int i = 0; i < 256; i++) {// Entropy calculation
			if (colorpx[i] != 0 || colorpxtotal[i] != 0) {
				Entropy += ((double) (colorpx[i] + colorpxtotal[i]) / ((k + 1) * newpx.length))
						* ((Math.log(((double) (colorpx[i] + colorpxtotal[i]) / ((k + 1) * newpx.length)))
								/ Math.log(2)))
						* -1;
			}
		}

		if (size == 100) {
			if (count > newpx.length * 0.1) {// Check if the loss is not too large
				Entropy = 999999998;
			} else {
				if (count > newpx.length * 0.2) {// Check if the loss is not too large
					Entropy = 999999998;
				}
			}
		}
		return Entropy;

	}

	public double PngAveLossy(int deep, byte[][] bytes, byte[] newpx, int[] colorpxtotal, int k, int size)
			throws IOException {
		int[] colorpx = new int[256];
		double Entropy = 0;
		byte pxprev = 0;// The previous pixel with a lossy
		int count = 0;
		for (int j = 0; j < deep; j++) {
			for (int i = 0; i < newpx.length; i = i + deep) {
				if (i < deep) {
					newpx[i + j] = bytes[0][i + j];
					pxprev = bytes[0][i + j];
				} else {
					if (((bytes[0][i + j] & 0xFF) - ((bytes[1][i + j] & 0xFF) + (pxprev & 0xFF)) / 2) < size
							&& ((bytes[0][i + j] & 0xFF - (bytes[1][i + j] & 0xFF + (pxprev & 0xFF)) / 2) > -size)) {
						newpx[i + j] = (byte) ((bytes[0][i + j] & 0xFF)
								- (((bytes[1][i + j] & 0xFF) + (pxprev & 0xFF)) / 2));
						pxprev = bytes[0][i + j];
					} else if (((bytes[0][i + j] & 0xFF) - ((bytes[1][i + j] & 0xFF + (pxprev & 0xFF)) / 2)) < size) {
						newpx[i + j] = (byte) -size;
						pxprev = (byte) (((pxprev & 0xFF) + (bytes[1][i + j] & 0xFF)) / 2 - (size));
						count++;
					} else {
						newpx[i + j] = (byte) +size;
						pxprev = (byte) (((pxprev & 0xFF) + (bytes[1][i + j] & 0xFF)) / 2 + (size));
						count++;
					}
				}
				if (newpx[i + j] < 0)
					colorpx[newpx[i + j] + 256]++;
				else
					colorpx[newpx[i + j]]++;

			}
		}
		for (int i = 0; i < 256; i++) {
			if (colorpx[i] != 0 || colorpxtotal[i] != 0) {
				Entropy += ((double) (colorpx[i] + colorpxtotal[i]) / ((k + 1) * newpx.length))
						* ((Math.log(((double) (colorpx[i] + colorpxtotal[i]) / ((k + 1) * newpx.length)))
								/ Math.log(2)))
						* -1;
			}
		}
		if (size == 100) {
			if (count > newpx.length * 0.1) {
				Entropy = 999999998;
			} else {
				if (count > newpx.length * 0.2) {
					Entropy = 999999998;
				}
			}
		}
		return Entropy;

	}

	public double PngTopLossy(int deep, byte[][] bytes, byte[] newpx, int[] colorpxtotal, int k, int size)
			throws IOException {

		int[] colorpx = new int[256];
		double Entropy = 0;
		int count = 0;
		for (int i = 0; i < newpx.length; i++) {
			if (i < deep) {
				newpx[i] = bytes[0][i];
			} else {
				if (((bytes[0][i] & 0xFF) - (bytes[1][i] & 0xFF)) < size
						&& ((bytes[0][i] & 0xFF) - (bytes[1][i] & 0xFF) > -size)) {
					newpx[i] = (byte) ((bytes[0][i] & 0xFF) - (bytes[1][i] & 0xFF));
				} else if (((bytes[0][i] & 0xFF) - (bytes[1][i] & 0xFF)) < size) {
					newpx[i] = (byte) -size;
					count++;
				} else {
					newpx[i] = (byte) size;
					count++;
				}
			}
			if (newpx[i] < 0)
				colorpx[newpx[i] + 256]++;
			else
				colorpx[newpx[i]]++;
		}
		for (int i = 0; i < 256; i++) {
			if (colorpx[i] != 0 || colorpxtotal[i] != 0) {
				Entropy += ((double) (colorpx[i] + colorpxtotal[i]) / ((k + 1) * newpx.length))
						* ((Math.log((double) (colorpx[i] + colorpxtotal[i]) / ((k + 1) * newpx.length)) / Math.log(2)))
						* -1;
			}
		}
		if (size == 100) {
			if (count > newpx.length * 0.1) {
				Entropy = 999999998;
			} else {
				if (count > newpx.length * 0.2) {
					Entropy = 999999998;
				}
			}
		}
		return Entropy;

	}

	public double PngPrevLossy(int deep, byte[] bytes, byte[] newpx, int[] colorpxtotal, int k, int size)
			throws IOException {
		// byte[] newpx=new byte[newpx.length];
		int[] colorpx = new int[256];
		double Entropy = 0;
		byte x = 0;
		int count = 0;
		for (int j = 0; j < deep; j++) {
			for (int i = 0; i < bytes.length; i = i + deep) {
				if (i < deep) {
					newpx[i + j] = bytes[i + j];
					x = bytes[i + j];
				} else {
					if (((bytes[i + j] & 0xFF) - (x & 0xFF)) < size && ((bytes[i + j] & 0xFF) - (x & 0xFF) > -size)) {
						newpx[i + j] = (byte) ((bytes[i + j] & 0xFF) - (x & 0xFF));
						x = (byte) ((bytes[i + j] & 0xFF));
					} else if ((bytes[i + j] & 0xFF) - (x & 0xFF) < size) {
						newpx[i + j] = (byte) -size;
						x = (byte) ((x & 0xFF) - size);
						count++;
					} else {
						newpx[i + j] = (byte) size;
						x = (byte) ((x & 0xFF) + size);
						count++;
					}
				}
				if (newpx[i + j] < 0)
					colorpx[newpx[i + j] + 256]++;
				else
					colorpx[newpx[i + j]]++;
			}
		}

		for (int i = 0; i < 256; i++) {
			if (colorpx[i] != 0 || colorpxtotal[i] != 0) {
				Entropy += ((double) (colorpx[i] + colorpxtotal[i]) / ((k + 1) * newpx.length))
						* ((Math.log((double) (colorpx[i] + colorpxtotal[i]) / ((k + 1) * newpx.length)) / Math.log(2)))
						* -1;
			}
		}
		if (size == 100) {
			if (count > newpx.length * 0.1) {
				Entropy = 999999998;
			} else {
				if (count > newpx.length * 0.2) {
					Entropy = 999999998;
				}
			}
		}
		return Entropy;
	}

	public double PngMid(int deep, byte[][] bytes, byte[] newpx, int[] colorpxtotal, int k) throws IOException {
		int[] colorpx = new int[256];
		double Entropy = 0;
		for (int i = 0; i < newpx.length; i++) {
			if (i < deep) {
				newpx[i] = bytes[0][i];
			} else {
				newpx[i] = (byte) ((bytes[0][i] & 0xFF)
						- ((bytes[1][i] & 0xFF) + (bytes[0][i - deep] & 0xFF) - (bytes[1][i - deep] & 0xFF)));
			}
			if (newpx[i] < 0)
				colorpx[newpx[i] + 256]++;
			else
				colorpx[newpx[i]]++;
		}
		for (int i = 0; i < 256; i++) {
			if (colorpx[i] != 0 || colorpxtotal[i] != 0) {
				Entropy += ((double) (colorpx[i] + colorpxtotal[i]) / ((k + 1) * newpx.length))
						* ((Math.log((double) (colorpx[i] + colorpxtotal[i]) / ((k + 1) * newpx.length)) / Math.log(2)))
						* -1;
			}
		}

		return Entropy;

	}

	public double PngTop(int deep, byte[][] bytes, byte[] newpx, int[] colorpxtotal, int k) throws IOException {

		int[] colorpx = new int[256];
		double Entropy = 0;
		for (int i = 0; i < newpx.length; i++) {
			if (i < deep) {
				newpx[i] = bytes[0][i];
			} else {
				newpx[i] = (byte) ((bytes[0][i] & 0xFF) - (bytes[1][i] & 0xFF));
			}
			if (newpx[i] < 0)
				colorpx[newpx[i] + 256]++;
			else
				colorpx[newpx[i]]++;
		}
		for (int i = 0; i < 256; i++) {
			if (colorpx[i] != 0 || colorpxtotal[i] != 0) {
				Entropy += ((double) (colorpx[i] + colorpxtotal[i]) / ((k + 1) * newpx.length))
						* ((Math.log((double) (colorpx[i] + colorpxtotal[i]) / ((k + 1) * newpx.length)) / Math.log(2)))
						* -1;
			}
		}

		return Entropy;

	}

	public int ByteToInt(byte b) { // change the binary numbers to only positive binary numbers
		int ret = 0;
		BitSet set = BitSet.valueOf(new byte[] { b });
		for (int i = 0; i < 8; i++) {
			if (set.get(i))
				ret = (int) (ret + Math.pow(2, i));
		}
		return ret;
	}

	public double PngPrev(int deep, byte[] bytes, byte[] newpx, int[] colorpxtotal, int k) throws IOException {
		// byte[] newpx=new byte[bytes.length];
		int[] colorpx = new int[256];
		double Entropy = 0;
		for (int i = 0; i < newpx.length; i++) {
			if (i < deep) {
				newpx[i] = bytes[i];
			} else {
				newpx[i] = (byte) ((bytes[i] & 0xFF) - (bytes[i - deep] & 0xFF));
			}
			if (newpx[i] < 0)
				colorpx[newpx[i] + 256]++;
			else
				colorpx[newpx[i]]++;
		}
		for (int i = 0; i < 256; i++) {
			if (colorpx[i] != 0 || colorpxtotal[i] != 0) {
				Entropy += ((double) (colorpx[i] + colorpxtotal[i]) / ((k + 1) * newpx.length))
						* ((Math.log((double) (colorpx[i] + colorpxtotal[i]) / ((k + 1) * newpx.length)) / Math.log(2)))
						* -1;
			}
		}
		return Entropy;
	}

	public double PngAve(int deep, byte[][] bytes, byte[] newpx, int[] colorpxtotal, int k) throws IOException {

		int[] colorpx = new int[256];
		double Entropy = 0;
		for (int i = 0; i < newpx.length; i++) {
			if (i < deep) {
				newpx[i] = bytes[0][i];
			} else {
				newpx[i] = (byte) ((bytes[0][i] & 0xFF) - ((bytes[1][i] & 0xFF) + (bytes[0][i - deep] & 0xFF)) / 2);
			}
			if (newpx[i] < 0)
				colorpx[newpx[i] + 256]++;
			else
				colorpx[newpx[i]]++;
		}
		for (int i = 0; i < 256; i++) {
			if (colorpx[i] != 0 || colorpxtotal[i] != 0) {
				Entropy += ((double) (colorpx[i] + colorpxtotal[i]) / ((k + 1) * newpx.length))
						* ((Math.log((double) (colorpx[i] + colorpxtotal[i]) / ((k + 1) * newpx.length)) / Math.log(2)))
						* -1;
			}
		}
		return Entropy;

	}

	public void Decompress(String src, String name, String dest) throws IOException {
		HufmannEncoderDecoder Huffmann = new HufmannEncoderDecoder();
		String[] Address = new String[2];
		Address[0] = src;
		String[] AddressCompressed = new String[2];
		AddressCompressed[0] = src.substring(0, src.length() - 3) + "LYMM-prev";
		byte[] bytes = Huffmann.DecompressWithArray(Address, AddressCompressed);
		BufferedImage imgBuffer = ImageIO.read(new File(src.substring(0, src.length() - 3) + "LYMM-prev"));
		byte[][] pix = new byte[imgBuffer.getHeight()][imgBuffer.getWidth() * ((int) bytes[28] / 8)];
		byte[] beforpx = new byte[54];
		File file = new File(src.substring(0, src.length() - 3) + "LYMM-prev");
		file.delete();
		file = new File(dest + "\\" + name + "-LYM.bmp");
		OutputStream out = new FileOutputStream(file);
		for (int i = 0; i < 54; i++) {// 54 first bytes
			beforpx[i] = bytes[i];
		}
		out.write(beforpx);
		byte[] choose = new byte[imgBuffer.getHeight()];// Selection for each row
		for (int i = 0; i < imgBuffer.getHeight(); i++) {
			choose[i] = bytes[i * imgBuffer.getWidth() * ((int) bytes[28] / 8) + 54 + i];
			for (int j = 0; j < imgBuffer.getWidth() * ((int) bytes[28] / 8); j++) {
				pix[i][j] = bytes[i * imgBuffer.getWidth() * ((int) bytes[28] / 8) + j + i + 55];// The pixel matrix of
																									// the image
			}
		}
		byte[][] realpix = new byte[imgBuffer.getHeight()][imgBuffer.getWidth() * ((int) bytes[28] / 8)];// The real
																											// pixel
																											// matrix of
																											// the image
		for (int i = 0; i < pix.length; i++) {
			for (int j = 0; j < pix[0].length; j++) {
				if (j < ((int) bytes[28] / 8)) {// first row pixels
					realpix[i][j] = pix[i][j];
				} else {
					if (choose[i] == 0) {
						realpix[i][j] = (byte) (pix[i][j - ((int) bytes[28] / 8)] & 0xFF);
					} else if (choose[i] == 1 || choose[i] == 5) {
						realpix[i][j] = (byte) ((realpix[i][j - ((int) bytes[28] / 8)] & 0xFF) + (pix[i][j] & 0xFF));
					} else if (choose[i] == 2 || choose[i] == 6) {
						realpix[i][j] = (byte) ((realpix[i - 1][j] & 0xFF) + (pix[i][j] & 0xFF));
					} else if (choose[i] == 3 || choose[i] == 7) {
						realpix[i][j] = (byte) (((realpix[i - 1][j] & 0xFF)
								+ (realpix[i][j - ((int) bytes[28] / 8)] & 0xFF)) / 2 + (pix[i][j] & 0xFF));
					} else {
						realpix[i][j] = (byte) (((realpix[i - 1][j] & 0xFF)
								+ (realpix[i][j - ((int) bytes[28] / 8)] & 0xFF)
								- (realpix[i - 1][j - ((int) bytes[28] / 8)] & 0xFF)) + (pix[i][j] & 0xFF));
					}
				}
			}
		}

		int index = 0;
		byte[] writepix = new byte[imgBuffer.getHeight() * imgBuffer.getWidth() * ((int) bytes[28] / 8)];
		for (int i = 0; i < pix.length; i++) {
			for (int j = 0; j < pix[0].length; j++) {
				writepix[index] = realpix[i][j];
				index++;
			}
		}
		out.write(writepix);
		byte[] afterpx = new byte[bytes.length - 54
				- imgBuffer.getWidth() * imgBuffer.getHeight() * ((int) bytes[28] / 8) - imgBuffer.getHeight()];
		for (int i = imgBuffer.getWidth() * imgBuffer.getHeight() * ((int) bytes[28] / 8) + 54
				+ imgBuffer.getHeight(); i < bytes.length; i++) {
			afterpx[i - (imgBuffer.getWidth() * imgBuffer.getHeight() * ((int) bytes[28] / 8) + 54
					+ imgBuffer.getHeight())] = bytes[i];
		}
		out.write(afterpx);
		out.close();
	}
}
