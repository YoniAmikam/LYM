
//Authors: Yoni Amikam, 311532774; Matan Chibotero, 204076962; Liel Levi, 307983320;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Label;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import java.awt.Toolkit;
import javax.swing.JRadioButton;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.awt.Insets;

public class LYMClass {

	private JFrame mainFrame;
	private JFrame viewFrame;
	private JFrame helpFrame;
	private JFrame infoFrame;
	private JFrame extractFrame;
	private JFrame selectViewFrame;
	private JFrame settingFrame;
	private Image img;
	TitledBorder title;
	private String dataPatch_1;
	private String dataPatch_2;
	private int choiceLevel = 0;
	private int choiceLevelCancel = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws IOException {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO: handle exception
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LYMClass window = new LYMClass();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void openWebpage(String urlString) {
		try {
			Desktop.getDesktop().browse(new URL(urlString).toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int byteToInt(byte[] bytes, int n, int m) {
		String currentByte = "";
		String sum = "";
		for (int i = n; i < m; i++) {
			if (bytes[i] < 0)
				currentByte += Integer.toBinaryString(bytes[i] + 256);
			else
				currentByte += Integer.toBinaryString(bytes[i]);
			while (currentByte.length() % 8 != 0) {
				currentByte = "0" + currentByte;
			}
			sum = currentByte + sum;
			currentByte = "";
		}
		int currentInt = 0;
		for (int i = sum.length() - 1; i > 0; i--) {
			if (sum.charAt(i) == '1')
				currentInt += Math.pow(2, sum.length() - 1 - i);
		}
		return currentInt;
	}

	/**
	 * Create the application.
	 */
	public LYMClass() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		PngLossyEncoderDecoder x = new PngLossyEncoderDecoder();
		mainFrame = new JFrame();
		mainFrame.setResizable(false);
		mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("lemon-icon.png")));
		mainFrame.setBounds(100, 100, 537, 459);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame.setLocation(dim.width / 2 - mainFrame.getSize().width / 2,
				dim.height / 2 - mainFrame.getSize().height / 2);
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setControlButtonsAreShown(false);
		mainFrame.getContentPane().setLayout(null); // before change

		// JButton info
		JButton btnInfo = new JButton("Info");
		btnInfo.setToolTipText("Show information");
		btnInfo.setHorizontalTextPosition(JButton.CENTER);
		btnInfo.setVerticalTextPosition(JButton.BOTTOM);
		btnInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File[] selectedFile = fileChooser.getSelectedFiles();
				FileInputStream input = null;
				if ((!(selectedFile.length == 0))) {
					long size = 0;
					String width = "";
					String height = "";
					String deep = "";
					int temp;
					for (int i = 0; i < selectedFile.length; i++) {

						size += selectedFile[i].length();
					}
					if (selectedFile.length == 1) {
						try {
							input = new FileInputStream(selectedFile[0]);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						byte[] Bytes = new byte[(int) selectedFile[0].length()];
						try {
							input.read(Bytes, 0, Bytes.length);
							input.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if (selectedFile[0].toString()
								.substring(selectedFile[0].toString().length() - 3, selectedFile[0].toString().length())
								.equals("LYM") == false) {
							temp = byteToInt(Bytes, 18, 22);
							width = Integer.toString(temp);
							temp = byteToInt(Bytes, 22, 26);
							height = Integer.toString(temp);
							temp = Bytes[28];
							deep = Integer.toString(temp);
						} else {
							width = "undefined";
							height = "undefined";
							deep = "undefined";
						}

					}
					infoFrame = new JFrame();
					infoFrame.getContentPane().setBackground(UIManager.getColor("text"));
					infoFrame.setBounds(100, 100, 300, 373);
					infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					infoFrame.setIconImage(
							Toolkit.getDefaultToolkit().getImage(getClass().getResource("lemon-icon.png")));
					infoFrame.getContentPane().setLayout(null);
					infoFrame.setLocation(mainFrame.getLocationOnScreen().x + 40,
							(mainFrame.getLocationOnScreen().y + 40));

					mainFrame.setEnabled(false);
					infoFrame.addWindowListener(new java.awt.event.WindowAdapter() {
						@Override
						public void windowClosing(java.awt.event.WindowEvent windowEvent) {
							mainFrame.setEnabled(true);
						}
					});

					Label lblLymArvhive = new Label("LYM arvhive");
					lblLymArvhive.setFont(new Font("Dialog", Font.BOLD, 13));
					lblLymArvhive.setBounds(115, 10, 80, 21);
					infoFrame.getContentPane().add(lblLymArvhive);

					JLabel lblTotalFiles = new JLabel("Total files");
					lblTotalFiles.setBounds(44, 51, 46, 14);
					infoFrame.getContentPane().add(lblTotalFiles);

					JLabel lblTotalSize = new JLabel("Total size");
					lblTotalSize.setBounds(44, 76, 46, 14);
					infoFrame.getContentPane().add(lblTotalSize);

					JLabel lblHeightPixel = new JLabel("height pixel");
					lblHeightPixel.setBounds(44, 114, 74, 14);
					infoFrame.getContentPane().add(lblHeightPixel);

					JLabel lblWidthPixel = new JLabel("width pixel");
					lblWidthPixel.setBounds(44, 139, 60, 14);
					infoFrame.getContentPane().add(lblWidthPixel);

					JLabel lblBits = new JLabel("deep bits ");
					lblBits.setBounds(44, 164, 74, 14);
					infoFrame.getContentPane().add(lblBits);

					JSeparator separator = new JSeparator();
					separator.setBounds(44, 102, 230, 2);
					infoFrame.getContentPane().add(separator);

					JLabel lblTotalFilesInput = new JLabel("" + selectedFile.length);
					lblTotalFilesInput.setHorizontalAlignment(SwingConstants.RIGHT);
					lblTotalFilesInput.setBounds(177, 51, 100, 14);
					infoFrame.getContentPane().add(lblTotalFilesInput);

					JLabel lblTotalSizeInput = new JLabel("" + size);
					lblTotalSizeInput.setHorizontalAlignment(SwingConstants.RIGHT);
					lblTotalSizeInput.setBounds(177, 76, 100, 14);
					infoFrame.getContentPane().add(lblTotalSizeInput);

					JLabel lblHeightPixelInput = new JLabel("" + height);
					lblHeightPixelInput.setHorizontalAlignment(SwingConstants.RIGHT);
					lblHeightPixelInput.setBounds(177, 114, 100, 14);
					infoFrame.getContentPane().add(lblHeightPixelInput);

					JLabel lblWidthPixelInput = new JLabel("" + width);
					lblWidthPixelInput.setHorizontalAlignment(SwingConstants.RIGHT);
					lblWidthPixelInput.setBounds(177, 139, 100, 14);
					infoFrame.getContentPane().add(lblWidthPixelInput);

					JLabel lblBitsInput = new JLabel("" + deep);
					lblBitsInput.setHorizontalAlignment(SwingConstants.RIGHT);
					lblBitsInput.setBounds(177, 164, 100, 14);
					infoFrame.getContentPane().add(lblBitsInput);

					JButton btnOk = new JButton("OK");
					btnOk.setBounds(94, 300, 74, 23);
					infoFrame.getContentPane().add(btnOk);

					btnOk.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							infoFrame.dispose();
							mainFrame.setEnabled(true);
							mainFrame.setVisible(true);
						}

					});

					JButton btnCancel = new JButton("Cancel");
					btnCancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							infoFrame.dispose();
							mainFrame.setEnabled(true);
							mainFrame.setVisible(true);
						}
					});
					btnCancel.setBounds(178, 300, 74, 23);
					infoFrame.getContentPane().add(btnCancel);

					infoFrame.setVisible(true);
					infoFrame.setResizable(false);
					mainFrame.setEnabled(false);

				}
			}
		});
		img = new ImageIcon(this.getClass().getResource("Info-icon (1).png")).getImage();
		btnInfo.setIcon(new ImageIcon(img));
		btnInfo.setBounds(201, -2, 65, 59);
		mainFrame.getContentPane().add(btnInfo);

		// JButton Add
		JButton btnAdd = new JButton("Add");
		btnAdd.setMnemonic(KeyEvent.VK_A);
		btnAdd.setToolTipText("Add files to archive");

		btnAdd.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				File[] selectedFile = fileChooser.getSelectedFiles();
				boolean type = true;
				for (int i = 0; i < selectedFile.length; i++) {
					String end = selectedFile[i].toString().substring(selectedFile[i].toString().length() - 3,
							selectedFile[i].toString().length());
					if (end.equals("bmp") == false && end.equals("BMP") == false) {
						type = false;
					}
				}
				if ((!(selectedFile.length == 0)) && (type)) {
					mainFrame.setResizable(false);
					mainFrame.setEnabled(false);

					for (int i = 0; i < selectedFile.length; i++) {

						try {
							x.Compress(selectedFile[i].toString(), choiceLevel);
							fileChooser.rescanCurrentDirectory();
						} catch (Exception exception) {
							exception.printStackTrace();
						}

					}
					mainFrame.setEnabled(true);
				}
			}
		});

		img = new ImageIcon(this.getClass().getResource("Button-Add-icon (1).png")).getImage();
		btnAdd.setIcon(new ImageIcon(img));
		btnAdd.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnAdd.setHorizontalTextPosition(SwingConstants.CENTER);
		btnAdd.setBounds(0, -2, 65, 59);
		mainFrame.getContentPane().add(btnAdd);
		fileChooser.setBounds(0, 55, 506, 328);
		fileChooser.setMultiSelectionEnabled(true);
		mainFrame.getContentPane().add(fileChooser);
		javax.swing.Action details = fileChooser.getActionMap().get("viewTypeDetails");
		details.actionPerformed(null);

		// JButton Delete
		JButton btnDelete = new JButton("Delete");
		btnDelete.setMnemonic(KeyEvent.VK_DELETE);
		btnDelete.setToolTipText("Delete files");
		img = new ImageIcon(this.getClass().getResource("Places-trash-full-icon (1).png")).getImage();
		btnDelete.setIcon(new ImageIcon(img));
		btnDelete.setVerticalAlignment(SwingConstants.BOTTOM);
		btnDelete.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnDelete.setHorizontalTextPosition(SwingConstants.CENTER);
		btnDelete.setBounds(138, -2, 65, 59);
		mainFrame.getContentPane().add(btnDelete);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "BMP", "LYM"));

		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Retrieve the selected files.
				File[] selectedFile = fileChooser.getSelectedFiles();
				if ((!(selectedFile.length == 0))) {
					int selectedAnswer = JOptionPane.showConfirmDialog(null,
							"Are you sure want to permanently delete this file?", "Confirm", JOptionPane.YES_NO_OPTION);

					for (int i = 0; i < selectedFile.length; i++) {
						try {
							if (selectedFile[i] != null) {
								if (selectedAnswer == JOptionPane.YES_OPTION) {
									Files.delete(selectedFile[i].toPath());
									fileChooser.rescanCurrentDirectory();
								}
							}
						} catch (Exception exception) {
							exception.printStackTrace();
						}
					}

				}
			}
		});

		JButton btnViewAndSave = new JButton("View & Save");
		btnViewAndSave.setMargin(new Insets(0, 14, 2, 14));
		btnViewAndSave.setAlignmentX(1.0f);
		btnViewAndSave.setToolTipText("View and Save files");
		btnViewAndSave.setMnemonic(KeyEvent.VK_L);
		btnViewAndSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewFrame = new JFrame();
				viewFrame.setBounds(100, 100, 826, 675);
				viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				viewFrame.setLocation(mainFrame.getLocationOnScreen().x - 80,
						(mainFrame.getLocationOnScreen().y - 120));
				viewFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("lemon-icon.png")));
				viewFrame.getContentPane().setLayout(null);
				viewFrame.setVisible(true);
				viewFrame.setResizable(false);

				mainFrame.setEnabled(false);
				viewFrame.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(java.awt.event.WindowEvent windowEvent) {
						mainFrame.setEnabled(true);
					}
				});

				// Labels container
				Border blackline = BorderFactory.createLineBorder(Color.gray);
				JLabel lblNewLabel = new JLabel("");
				lblNewLabel.setBounds(20, 82, 365, 507);
				viewFrame.getContentPane().add(lblNewLabel);
				title = BorderFactory.createTitledBorder(blackline, "Original Image");
				title.setTitleJustification(TitledBorder.CENTER);
				lblNewLabel.setBorder(title);

				JLabel lblImgDec = new JLabel("");
				lblImgDec.setBounds(422, 82, 365, 507);
				viewFrame.getContentPane().add(lblImgDec);
				title = BorderFactory.createTitledBorder(blackline, "Image Decompression");
				lblImgDec.setBorder(title);

				JLabel lblDecLevel = new JLabel("");
				lblDecLevel.setBounds(376, 11, 320, 42);
				viewFrame.getContentPane().add(lblDecLevel);
				title = BorderFactory.createTitledBorder(blackline, "Decomposition Level");
				lblDecLevel.setBorder(title);

				// Label container image parameters
				JLabel lblImgPara1 = new JLabel("");
				lblImgPara1.setBounds(456, 379, 309, 190);
				viewFrame.getContentPane().add(lblImgPara1);
				title = BorderFactory.createTitledBorder(blackline, "Image Parameters");
				lblImgPara1.setBorder(title);

				JLabel lblImgPara2 = new JLabel("");
				lblImgPara2.setBounds(54, 379, 309, 190);
				viewFrame.getContentPane().add(lblImgPara2);
				title = BorderFactory.createTitledBorder(blackline, "Image Parameters");
				lblImgPara2.setBorder(title);

				// Show image
				JLabel lblbgWhite1 = new JLabel("");
				lblbgWhite1.setOpaque(true);
				lblbgWhite1.setBackground(Color.WHITE);
				lblbgWhite1.setBounds(32, 104, 342, 264);
				viewFrame.getContentPane().add(lblbgWhite1);

				JLabel lblbgWhite_2 = new JLabel("");
				lblbgWhite_2.setOpaque(true);
				lblbgWhite_2.setBackground(Color.WHITE);
				lblbgWhite_2.setBounds(434, 104, 342, 264);
				viewFrame.getContentPane().add(lblbgWhite_2);

				// Text fields Image parameters
				JTextField textFieldHeight_1 = new JTextField();
				textFieldHeight_1.setColumns(10);
				textFieldHeight_1.setBounds(253, 407, 86, 20);
				textFieldHeight_1.setHorizontalAlignment(SwingConstants.RIGHT);
				viewFrame.getContentPane().add(textFieldHeight_1);

				JTextField textFieldWidth_1 = new JTextField();
				textFieldWidth_1.setColumns(10);
				textFieldWidth_1.setBounds(253, 433, 86, 20);
				textFieldWidth_1.setHorizontalAlignment(SwingConstants.RIGHT);
				viewFrame.getContentPane().add(textFieldWidth_1);

				JTextField textFieldSize_1 = new JTextField();
				textFieldSize_1.setColumns(10);
				textFieldSize_1.setBounds(253, 459, 86, 20);
				textFieldSize_1.setHorizontalAlignment(SwingConstants.RIGHT);
				viewFrame.getContentPane().add(textFieldSize_1);

				JTextField textFieldLocation_1 = new JTextField();
				textFieldLocation_1.setColumns(10);
				textFieldLocation_1.setBounds(253, 485, 86, 20);
				textFieldLocation_1.setHorizontalAlignment(SwingConstants.RIGHT);
				viewFrame.getContentPane().add(textFieldLocation_1);

				JTextField textFieldBitdepth_1 = new JTextField();
				textFieldBitdepth_1.setColumns(10);
				textFieldBitdepth_1.setBounds(253, 511, 86, 20);
				textFieldBitdepth_1.setHorizontalAlignment(SwingConstants.RIGHT);
				viewFrame.getContentPane().add(textFieldBitdepth_1);

				JTextField textFieldForamt_1 = new JTextField();
				textFieldForamt_1.setColumns(10);
				textFieldForamt_1.setBounds(253, 537, 86, 20);
				textFieldForamt_1.setHorizontalAlignment(SwingConstants.RIGHT);
				viewFrame.getContentPane().add(textFieldForamt_1);

				JTextField textFieldHeight_2 = new JTextField();
				textFieldHeight_2.setColumns(10);
				textFieldHeight_2.setBounds(659, 404, 86, 20);
				textFieldHeight_2.setHorizontalAlignment(SwingConstants.RIGHT);
				viewFrame.getContentPane().add(textFieldHeight_2);

				JTextField textFieldWidth_2 = new JTextField();
				textFieldWidth_2.setColumns(10);
				textFieldWidth_2.setBounds(659, 430, 86, 20);
				textFieldWidth_2.setHorizontalAlignment(SwingConstants.RIGHT);
				viewFrame.getContentPane().add(textFieldWidth_2);

				JTextField textFieldSize_2 = new JTextField();
				textFieldSize_2.setColumns(10);
				textFieldSize_2.setBounds(659, 456, 86, 20);
				textFieldSize_2.setHorizontalAlignment(SwingConstants.RIGHT);
				viewFrame.getContentPane().add(textFieldSize_2);

				JTextField textFieldLocation_2 = new JTextField();
				textFieldLocation_2.setColumns(10);
				textFieldLocation_2.setBounds(659, 482, 86, 20);
				textFieldLocation_2.setHorizontalAlignment(SwingConstants.RIGHT);
				viewFrame.getContentPane().add(textFieldLocation_2);

				JTextField textFieldBitdepth_2 = new JTextField();
				textFieldBitdepth_2.setColumns(10);
				textFieldBitdepth_2.setBounds(659, 508, 86, 20);
				textFieldBitdepth_2.setHorizontalAlignment(SwingConstants.RIGHT);
				viewFrame.getContentPane().add(textFieldBitdepth_2);

				JTextField textFieldForamt_2 = new JTextField();
				textFieldForamt_2.setColumns(10);
				textFieldForamt_2.setBounds(659, 534, 86, 20);
				textFieldForamt_2.setHorizontalAlignment(SwingConstants.RIGHT);
				viewFrame.getContentPane().add(textFieldForamt_2);

				// Radio buttons of level decomposition
				ButtonGroup group = new ButtonGroup();
				JRadioButton rdbtnLow = new JRadioButton("Low quality");
				rdbtnLow.setBounds(393, 26, 89, 23);
				viewFrame.getContentPane().add(rdbtnLow);
				group.add(rdbtnLow);
				rdbtnLow.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						choiceLevel = 2;
						rdbtnLow.setSelected(true);
					}
				});

				JRadioButton rdbtnHigh = new JRadioButton("High quality");
				rdbtnHigh.setBounds(493, 26, 89, 23);
				viewFrame.getContentPane().add(rdbtnHigh);
				group.add(rdbtnHigh);
				rdbtnHigh.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						choiceLevel = 1;
						rdbtnHigh.setSelected(true);
					}
				});

				JRadioButton rdbtnWithoutLoss = new JRadioButton("Without loss");
				rdbtnWithoutLoss.setBounds(593, 26, 89, 23);
				viewFrame.getContentPane().add(rdbtnWithoutLoss);
				group.add(rdbtnWithoutLoss);
				rdbtnWithoutLoss.setSelected(true);
				rdbtnWithoutLoss.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						choiceLevel = 0;
						rdbtnWithoutLoss.setSelected(true);
					}
				});

				// Option buttons
				JButton btnSave = new JButton("Save");
				btnSave.setEnabled(false);
				btnSave.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mainFrame.setVisible(true);
						viewFrame.dispose();
						mainFrame.setEnabled(true);

					}
				});
				btnSave.setBounds(267, 29, 89, 23);
				viewFrame.getContentPane().add(btnSave);

				JButton btnView = new JButton("View");
				btnView.setEnabled(false);
				btnView.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						viewFrame.setEnabled(false);
						try {
							if (rdbtnLow.isSelected())
								choiceLevel = 2;
							else if (rdbtnHigh.isSelected())
								choiceLevel = 1;
							else
								choiceLevel = 0;
							x.Compress(dataPatch_1, choiceLevel);
							fileChooser.rescanCurrentDirectory();
							x.Decompress(dataPatch_1.substring(0, dataPatch_1.toString().length() - 4) + ".LYM",
									dataPatch_2, dataPatch_1.substring(0,
											(dataPatch_1.toString().length() - 4) - dataPatch_2.length()));
							BufferedImage img = null;
							try {
								img = ImageIO.read(new File(
										dataPatch_1.substring(0, dataPatch_1.toString().length() - 4) + "-LYM.BMP"));
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							Image dimg = img.getScaledInstance(lblbgWhite_2.getHeight() + 78,
									lblbgWhite_2.getWidth() - 78, Image.SCALE_SMOOTH);
							ImageIcon image = new ImageIcon(dimg);
							lblbgWhite_2.setIcon(image);

						} catch (Exception exception) {
							exception.printStackTrace();
						}
						File file3 = new File(dataPatch_1.substring(0, dataPatch_1.toString().length() - 4) + ".LYM");
						File file4 = new File(
								dataPatch_1.substring(0, dataPatch_1.toString().length() - 4) + "-LYM.bmp");

						FileInputStream input = null;
						int temp;
						String size = "";
						String width = "";
						String height = "";
						String deep = "";

						try {
							input = new FileInputStream(file4);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						byte[] Bytes = new byte[(int) file4.length()];
						try {
							input.read(Bytes, 0, Bytes.length);
							input.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						temp = byteToInt(Bytes, 18, 22);
						width = Integer.toString(temp);
						temp = byteToInt(Bytes, 22, 26);
						height = Integer.toString(temp);
						temp = Bytes[28];
						deep = Integer.toString(temp);
						textFieldHeight_2.setText(height);
						textFieldWidth_2.setText(width);
						textFieldSize_2.setText(size);
						temp = (int) file3.length();
						size = Integer.toString(temp);
						textFieldSize_2.setText(size);

						textFieldLocation_2.setText(dataPatch_1.substring(0, dataPatch_2.length()));
						textFieldBitdepth_2.setText(deep);
						textFieldForamt_2.setText("lym");
						viewFrame.setEnabled(true);
						btnSave.setEnabled(true);

					}
				});
				btnView.setBounds(149, 29, 89, 23);
				viewFrame.getContentPane().add(btnView);

				JButton btnOpenImage = new JButton("Open image");
				btnOpenImage.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						selectViewFrame = new JFrame();
						selectViewFrame.setBounds(0, 0, 510, 500);
						selectViewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						selectViewFrame.getContentPane().setLayout(null);
						selectViewFrame.setLocation(mainFrame.getLocationOnScreen().x + 40,
								(mainFrame.getLocationOnScreen().y + 40));
						selectViewFrame.setVisible(true);
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setMultiSelectionEnabled(false);
						fileChooser.setAcceptAllFileFilterUsed(false);
						fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "BMP"));
						fileChooser.setControlButtonsAreShown(false);
						fileChooser.setBorder(new EmptyBorder(0, 0, 0, 0));

						fileChooser.setBounds(10, 10, 475, 400);
						selectViewFrame.getContentPane().add(fileChooser);

						viewFrame.setEnabled(false);
						selectViewFrame.addWindowListener(new java.awt.event.WindowAdapter() {
							@Override
							public void windowClosing(java.awt.event.WindowEvent windowEvent) {
								viewFrame.setEnabled(true);
							}
						});

						JButton btnNewButton = new JButton("Cancel");
						btnNewButton.setBounds(397, 425, 89, 23);
						btnNewButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								viewFrame.setVisible(true);
								selectViewFrame.dispose();
								viewFrame.setEnabled(true);
							}
						});
						selectViewFrame.getContentPane().add(btnNewButton);

						JButton btnOk = new JButton("OK");
						btnOk.setBounds(300, 425, 89, 23);
						selectViewFrame.getContentPane().add(btnOk);

						btnOk.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								File selectedFile = fileChooser.getSelectedFile();
								if (selectedFile != null) {

									try {
										dataPatch_1 = selectedFile.toString();
										dataPatch_2 = selectedFile.getName().substring(0,
												selectedFile.getName().length() - 4);
										BufferedImage img = null;
										try {
											img = ImageIO.read(new File(selectedFile.toString()));
										} catch (IOException e1) {
											e1.printStackTrace();
										}
										Image dimg = img.getScaledInstance(lblbgWhite1.getHeight() + 78,
												lblbgWhite1.getWidth() - 78, Image.SCALE_SMOOTH);
										ImageIcon image = new ImageIcon(dimg);
										lblbgWhite1.setIcon(image);
									} catch (Exception exception) {
										exception.printStackTrace();
									}
									FileInputStream input1 = null;
									String width = "";
									String height = "";
									String deep = "";
									String size = "";
									int temp;

									try {
										input1 = new FileInputStream(selectedFile);
									} catch (FileNotFoundException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									byte[] Bytes = new byte[(int) selectedFile.length()];
									try {
										input1.read(Bytes, 0, Bytes.length);
										input1.close();
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									temp = byteToInt(Bytes, 18, 22);
									width = Integer.toString(temp);
									temp = byteToInt(Bytes, 22, 26);
									height = Integer.toString(temp);
									temp = Bytes[28];
									deep = Integer.toString(temp);
									temp = byteToInt(Bytes, 2, 6);
									size = Integer.toString(temp);
									textFieldHeight_1.setText(height);
									textFieldWidth_1.setText(width);
									textFieldSize_1.setText(size);

									String y = selectedFile.getName().substring(0, selectedFile.getName().length() - 4);

									textFieldLocation_1.setText(selectedFile.toString().substring(0, y.length()));
									textFieldBitdepth_1.setText(deep);
									textFieldForamt_1.setText("bmp");
									lblbgWhite_2.setIcon(null);
									viewFrame.setVisible(true);
									selectViewFrame.dispose();
									viewFrame.setEnabled(true);
									btnView.setEnabled(true);
								}
							}
						});
					}
				});
				btnOpenImage.setBounds(20, 29, 103, 23);
				viewFrame.getContentPane().add(btnOpenImage);

				JButton btnCancel = new JButton("Cancel");
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (dataPatch_1 != null) {
							File file1 = new File(dataPatch_1.substring(0, dataPatch_1.length() - 3) + "LYM");
							File file2 = new File(dataPatch_1.substring(0, dataPatch_1.length() - 4) + "-LYM.BMP");

							file1.delete();
							file2.delete();
						}
						mainFrame.setVisible(true);
						viewFrame.dispose();
						mainFrame.setEnabled(true);
					}
				});
				btnCancel.setBounds(699, 600, 89, 23);
				viewFrame.getContentPane().add(btnCancel);

				// Labels Image parameters
				JLabel lblHeight_1 = new JLabel("Height of Image");
				lblHeight_1.setBounds(64, 410, 127, 14);
				viewFrame.getContentPane().add(lblHeight_1);

				JLabel lblWidth_1 = new JLabel("Width of Image");
				lblWidth_1.setBounds(64, 436, 127, 14);
				viewFrame.getContentPane().add(lblWidth_1);

				JLabel lblSize_1 = new JLabel("Size of Image");
				lblSize_1.setBounds(64, 462, 127, 14);
				viewFrame.getContentPane().add(lblSize_1);

				JLabel lblLocation_1 = new JLabel("Location of Image");
				lblLocation_1.setBounds(64, 488, 127, 14);
				viewFrame.getContentPane().add(lblLocation_1);

				JLabel lblBitdeph_1 = new JLabel("Bitdepth");
				lblBitdeph_1.setBounds(64, 514, 127, 14);
				viewFrame.getContentPane().add(lblBitdeph_1);

				JLabel lblForamt_1 = new JLabel("Foramt of Image");
				lblForamt_1.setBounds(64, 540, 127, 14);
				viewFrame.getContentPane().add(lblForamt_1);

				JLabel lblHeight_2 = new JLabel("Height of Image");
				lblHeight_2.setBounds(470, 407, 127, 14);
				viewFrame.getContentPane().add(lblHeight_2);

				JLabel lblWidth_2 = new JLabel("Width of Image");
				lblWidth_2.setBounds(470, 433, 127, 14);
				viewFrame.getContentPane().add(lblWidth_2);

				JLabel lblSize_2 = new JLabel("Size of Image");
				lblSize_2.setBounds(470, 459, 127, 14);
				viewFrame.getContentPane().add(lblSize_2);

				JLabel lblLocation_2 = new JLabel("Location of Image");
				lblLocation_2.setBounds(470, 485, 127, 14);
				viewFrame.getContentPane().add(lblLocation_2);

				JLabel lblBitdeph_2 = new JLabel("Bitdepth");
				lblBitdeph_2.setBounds(470, 511, 127, 14);
				viewFrame.getContentPane().add(lblBitdeph_2);

				JLabel lblForamt_2 = new JLabel("Foramt of Image");
				lblForamt_2.setBounds(470, 537, 127, 14);
				viewFrame.getContentPane().add(lblForamt_2);

			}

		});

		img = new ImageIcon(this.getClass().getResource("Look-icon (1).png")).getImage();
		btnViewAndSave.setIcon(new ImageIcon(img));
		btnViewAndSave.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnViewAndSave.setHorizontalTextPosition(SwingConstants.CENTER);
		btnViewAndSave.setBounds(262, -2, 97, 59);
		mainFrame.getContentPane().add(btnViewAndSave);

		// JButton Extract to
		JButton btnExtractTo = new JButton("Extract to");
		btnExtractTo.setToolTipText("Extract files");
		btnExtractTo.setMnemonic(KeyEvent.VK_E);
		img = new ImageIcon(this.getClass().getResource("Documents-icon (1).png")).getImage();
		btnExtractTo.setIcon(new ImageIcon(img));
		btnExtractTo.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnExtractTo.setHorizontalTextPosition(SwingConstants.CENTER);
		btnExtractTo.setBounds(63, -2, 81, 59);
		mainFrame.getContentPane().add(btnExtractTo);
		btnExtractTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File[] selectedFile = fileChooser.getSelectedFiles();
				boolean type = true;
				for (int i = 0; i < selectedFile.length; i++) {
					if ((selectedFile[i].toString()
							.substring(selectedFile[i].toString().length() - 3, selectedFile[i].toString().length())
							.equals("LYM") == false)) {
						type = false;
					}
				}
				if ((!(selectedFile.length == 0)) && type) {
					extractFrame = new JFrame();
					extractFrame.setResizable(false);
					extractFrame.setBounds(100, 100, 500, 430);
					extractFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					extractFrame.setIconImage(
							Toolkit.getDefaultToolkit().getImage(getClass().getResource("lemon-icon.png")));
					extractFrame.getContentPane().setLayout(null);
					extractFrame.setLocation(mainFrame.getLocationOnScreen().x + 40,
							(mainFrame.getLocationOnScreen().y + 40));

					mainFrame.setEnabled(false);
					extractFrame.addWindowListener(new java.awt.event.WindowAdapter() {
						@Override
						public void windowClosing(java.awt.event.WindowEvent windowEvent) {
							mainFrame.setEnabled(true);
						}
					});

					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					fileChooser.setControlButtonsAreShown(false);
					fileChooser.setBorder(new EmptyBorder(0, 0, 0, 0));
					fileChooser.setBounds(5, 5, 483, 345);
					extractFrame.getContentPane().add(fileChooser);
					JButton btnNewButton = new JButton("Cancel");
					btnNewButton.setBounds(400, 370, 89, 23);
					btnNewButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							mainFrame.setEnabled(true);
							mainFrame.setVisible(true);
							extractFrame.dispose();
						}
					});
					extractFrame.getContentPane().add(btnNewButton);
					JButton btnOk = new JButton("OK");
					btnOk.setBounds(300, 370, 89, 23);
					extractFrame.getContentPane().add(btnOk);
					btnOk.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if (fileChooser.getSelectedFile() != null) {
								for (int i = 0; i < selectedFile.length; i++) {
									extractFrame.dispose();
									mainFrame.setVisible(true);
									try {
										x.Decompress(selectedFile[i].toString(),
												selectedFile[i].getName().substring(0,
														selectedFile[i].getName().length() - 4),
												fileChooser.getSelectedFile().getAbsolutePath());

										fileChooser.rescanCurrentDirectory();
									} catch (Exception exception) {
										exception.printStackTrace();
									}
								}
								mainFrame.setEnabled(true);
							}
						}
					});

					extractFrame.setVisible(true);
				}
			}
		});

		// JMenu bar
		JMenuBar menuBar = new JMenuBar();
		mainFrame.setJMenuBar(menuBar);

		// JMenu
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		// JMenu items
		JMenuItem mntmSetting = new JMenuItem("Set setting files");
		mnFile.add(mntmSetting);
		mntmSetting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settingFrame = new JFrame();
				settingFrame.setBounds(100, 100, 365, 146);
				settingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				settingFrame.getContentPane().setLayout(null);
				settingFrame.setLocation(mainFrame.getLocationOnScreen().x + 80,
						(mainFrame.getLocationOnScreen().y + 80));
				settingFrame.setVisible(true);
				mainFrame.setEnabled(false);
				settingFrame.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(java.awt.event.WindowEvent windowEvent) {
						choiceLevel = choiceLevelCancel;
						mainFrame.setEnabled(true);
					}
				});
				Border blackline = BorderFactory.createLineBorder(Color.gray);
				JLabel lblDecLevel = new JLabel("");
				lblDecLevel.setBounds(10, 11, 320, 49);
				settingFrame.getContentPane().add(lblDecLevel);
				title = BorderFactory.createTitledBorder(blackline, "Decomposition Level");
				lblDecLevel.setBorder(title);
				choiceLevelCancel = choiceLevel;

				ButtonGroup groupSetting = new ButtonGroup();
				JRadioButton rdbtnLowSetting = new JRadioButton("Low quality");
				rdbtnLowSetting.setBounds(27, 26, 89, 23);
				settingFrame.getContentPane().add(rdbtnLowSetting);
				groupSetting.add(rdbtnLowSetting);
				rdbtnLowSetting.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						choiceLevel = 2;
						rdbtnLowSetting.setSelected(true);
					}
				});

				JRadioButton rdbtnHightSetting = new JRadioButton("High quality");
				rdbtnHightSetting.setBounds(127, 26, 89, 23);
				settingFrame.getContentPane().add(rdbtnHightSetting);
				groupSetting.add(rdbtnHightSetting);
				rdbtnHightSetting.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						choiceLevel = 1;
						rdbtnHightSetting.setSelected(true);
					}
				});

				JRadioButton rdbtnWithoutLossSetting = new JRadioButton("Without loss");
				rdbtnWithoutLossSetting.setBounds(227, 26, 89, 23);
				settingFrame.getContentPane().add(rdbtnWithoutLossSetting);
				groupSetting.add(rdbtnWithoutLossSetting);
				rdbtnWithoutLossSetting.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						choiceLevel = 0;
						rdbtnWithoutLossSetting.setSelected(true);
					}
				});

				if (choiceLevel == 1)
					rdbtnHightSetting.setSelected(true);
				else if (choiceLevel == 2)
					rdbtnLowSetting.setSelected(true);
				else if (choiceLevel == 0)
					rdbtnWithoutLossSetting.setSelected(true);

				JButton btnCancel = new JButton("Cancel");
				btnCancel.setBounds(250, 71, 89, 23);
				settingFrame.getContentPane().add(btnCancel);
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						choiceLevel = choiceLevelCancel;
						mainFrame.setVisible(true);
						settingFrame.dispose();
						mainFrame.setEnabled(true);
					}
				});

				JButton btnOk = new JButton("OK");
				btnOk.setBounds(147, 71, 89, 23);
				settingFrame.getContentPane().add(btnOk);
				btnOk.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mainFrame.setVisible(true);
						settingFrame.dispose();
						mainFrame.setEnabled(true);
					}
				});
			}
		});

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		// JMenu
		JMenu mnCommands = new JMenu("Commands");
		menuBar.add(mnCommands);

		// JMenu items
		JMenuItem mntmAdd = new JMenuItem("Add files to archive                                   Alt+A");
		mnCommands.add(mntmAdd);
		mntmAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAdd.doClick(0);
			}
		});

		JMenuItem mntmExtract = new JMenuItem("Extract to a specified folder                      Alt+E");
		mnCommands.add(mntmExtract);
		mntmExtract.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnExtractTo.doClick(0);

			}
		});

		JMenuItem mntmViewFiles = new JMenuItem("View files                                                     Alt+L");
		mnCommands.add(mntmViewFiles);
		mntmViewFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnViewAndSave.doClick(0);

			}
		});

		JSeparator separator_2 = new JSeparator();
		mnCommands.add(separator_2);

		JMenuItem mntmDelete = new JMenuItem("Delete files                                                  Alt+Del");
		mnCommands.add(mntmDelete);
		mntmDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDelete.doClick(0);
			}
		});

		// JMenu
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		// JMenu items
		JMenuItem mntmLymHomepage = new JMenuItem("LYM home page                      ");
		mnHelp.add(mntmLymHomepage);
		mntmLymHomepage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openWebpage("http://www.lymtechnologies.com/");
			}
		});

		JSeparator separator_1 = new JSeparator();
		mnHelp.add(separator_1);

		JMenuItem mntmAboutLym = new JMenuItem("About LYM...");
		mnHelp.add(mntmAboutLym);
		mntmAboutLym.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				helpFrame = new JFrame();
				helpFrame.setResizable(false);
				helpFrame.setBounds(100, 100, 450, 332);
				helpFrame.setLocation(mainFrame.getLocationOnScreen().x + 40, (mainFrame.getLocationOnScreen().y + 40));

				mainFrame.setEnabled(false);
				helpFrame.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(java.awt.event.WindowEvent windowEvent) {
						mainFrame.setEnabled(true);
					}
				});

				helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				helpFrame.getContentPane().setLayout(null);
				helpFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("lemon-icon.png")));
				JLabel lblImgLemon = new JLabel("");
				img = new ImageIcon(this.getClass().getResource("lemon-icon big.png")).getImage();
				lblImgLemon.setIcon(new ImageIcon(img));
				lblImgLemon.setBounds(28, 28, 64, 64);
				helpFrame.getContentPane().add(lblImgLemon);
				JLabel lblImgLym = new JLabel("");
				lblImgLym.setHorizontalAlignment(SwingConstants.TRAILING);
				lblImgLym.setVerticalAlignment(SwingConstants.BOTTOM);
				img = new ImageIcon(this.getClass().getResource("LogoMakr_1XO8H2.png")).getImage();
				lblImgLym.setIcon(new ImageIcon(img));
				lblImgLym.setFont(new Font("Serif", Font.BOLD, 20));
				lblImgLym.setBounds(102, 21, 386, 117);
				helpFrame.getContentPane().add(lblImgLym);
				JButton btnOk = new JButton("OK");
				btnOk.setBounds(300, 24, 111, 23);
				helpFrame.getContentPane().add(btnOk);
				btnOk.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						helpFrame.dispose();
						mainFrame.setEnabled(true);
						mainFrame.setVisible(true);
					}

				});
				JButton btnHomePage = new JButton("Home page");
				btnHomePage.setBounds(300, 58, 111, 23);
				helpFrame.getContentPane().add(btnHomePage);
				btnHomePage.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						openWebpage("http://www.lymtechnologies.com/");
					}
				});
				JLabel lblVersion = new JLabel("LYM 4.30 (" + System.getProperty("sun.arch.data.model") + "-bits)");
				lblVersion.setBounds(38, 108, 119, 14);
				helpFrame.getContentPane().add(lblVersion);

				JLabel lblCopyrightBy = new JLabel("Copyright 2002-2018 by Yoni Amikam, Matan Chibotero, Liel Levi ");
				lblCopyrightBy.setBounds(38, 134, 353, 14);
				helpFrame.getContentPane().add(lblCopyrightBy);

				JSeparator separator = new JSeparator();
				separator.setBounds(38, 194, 222, 2);
				helpFrame.getContentPane().add(separator);

				JSeparator separator_1 = new JSeparator();
				separator_1.setBounds(38, 269, 222, 2);
				helpFrame.getContentPane().add(separator_1);

				JLabel lblFree = new JLabel("free");
				lblFree.setBounds(38, 203, 46, 14);
				helpFrame.getContentPane().add(lblFree);
				helpFrame.setVisible(true);
			}

		});

	}
}
