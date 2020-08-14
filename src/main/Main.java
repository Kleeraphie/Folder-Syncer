package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	static JFrame window;
	static JFileChooser jfc;
	static JTextField parent, child;
	static JLabel parentL, childL, syncMode, changedFiles, failedFiles;
	static JButton parentB, childB, sync;
	static JComboBox<String> jcb;
	static int filesChanged, filesFailed;

	public static void main(String[] args) {
		new Main();
	}

	public Main() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		setSize(400, 300);
		setTitle("FolderSyncer");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		requestFocus();
		setLayout(null);

		jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		parentL = new JLabel("Startverzeichnis:");
		parentL.setSize(120, 20);
		parentL.setLocation(20, 20);
		add(parentL);

		childL = new JLabel("Zielverzeichnis:");
		childL.setSize(100, 20);
		childL.setLocation(20, 70);
		add(childL);

		parent = new JTextField();
		parent.setSize(200, 20);
		parent.setLocation(120, 20);
		add(parent);

		child = new JTextField();
		child.setSize(200, 20);
		child.setLocation(120, 70);
		add(child);

		parentB = new JButton();
		parentB.setSize(20, 20);
		parentB.setLocation(340, 20);
		parentB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jfc.setCurrentDirectory(new File(parent.getText()).getParentFile());
				int selectVal = jfc.showOpenDialog(null);

				if (selectVal == JFileChooser.APPROVE_OPTION) {
					parent.setText(jfc.getSelectedFile().getAbsolutePath());
				}

			}
		});
		add(parentB);

		childB = new JButton();
		childB.setSize(20, 20);
		childB.setLocation(340, 70);
		childB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jfc.setCurrentDirectory(new File(child.getText()).getParentFile());
				int selectVal = jfc.showOpenDialog(null);

				if (selectVal == JFileChooser.APPROVE_OPTION) {
					child.setText(jfc.getSelectedFile().getAbsolutePath());
				}

			}
		});
		add(childB);

		syncMode = new JLabel("Sync-Modus:");
		syncMode.setSize(100, 20);
		syncMode.setLocation(20, 120);
		add(syncMode);

		jcb = new JComboBox<String>();
		jcb.setSize(200, 20);
		jcb.setLocation(120, 120);

		String[] jcbList = { "Änderungsdatum", "Alle", "Inhaltliche Änderung" };

		for (String current : jcbList)
			jcb.addItem(current);

		add(jcb);

		sync = new JButton("Synchronisieren");
		sync.setSize(150, 20);
		sync.setLocation(125, 240);
		sync.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				filesChanged = 0;
				filesFailed = 0;

				switch (String.valueOf(jcb.getSelectedItem())) {

				case ("Änderungsdatum"):
					syncWithModifiedTime();
					break;

				case ("Alle"):
					syncAll();
					break;

				case ("Inhaltliche Änderung"):
					syncWithContentChange();
					break;

				}
				
				if (changedFiles != null)
					changedFiles.setText("");
				
				if (failedFiles != null)
					failedFiles.setText("");

				if (filesChanged == new File(child.getText()).listFiles().length) {

					changedFiles = new JLabel("Es wurden alle Dateien geändert.", SwingConstants.CENTER);
					changedFiles.setForeground(new Color(17, 204, 0));
					
				} else if (filesChanged > 0) {
					changedFiles = new JLabel(String.format("%s von %s Dateien geändert.", filesChanged,
							new File(child.getText()).listFiles().length), SwingConstants.CENTER);
					changedFiles.setForeground(new Color(17, 204, 0));
				} else {
					changedFiles = new JLabel("Es wurden keine Dateien geändert.", SwingConstants.CENTER);
					changedFiles.setForeground(new Color(240, 200, 0));
				}

				changedFiles.setLocation(0, 160);

				if (filesFailed > 0) {

					failedFiles = new JLabel(String.format("%s von %s Dateien konnten nicht geändert werden",
							filesFailed, new File(child.getText()).listFiles().length), SwingConstants.CENTER);
					
					failedFiles.setForeground(Color.RED);
					failedFiles.setFont(new Font("Tahome", Font.PLAIN, 12));
					failedFiles.setSize(getWidth(), 20);
					failedFiles.setLocation(0, 180);
					add(failedFiles);
					
					changedFiles.setLocation(0, 150);

				}

				changedFiles.setFont(new Font("Tahoma", Font.PLAIN, 12));
				changedFiles.setSize(getWidth(), 20);
				add(changedFiles);

				// JFrame neu laden, damit neues JPanel auch angezeigt wird
				invalidate();
				validate();
				repaint();

			}
		});
		add(sync);

		setVisible(true);

	}

	private void syncWithModifiedTime() {

		File parentF = new File(parent.getText());
		File childF = new File(child.getText());

		for (File current : parentF.listFiles()) {

			System.out.println("Checking " + current.getName());

			if (!(Arrays.asList(childF.list()).contains(current.getName()))) {

				File temp = new File(child.getText() + "\\" + current.getName());

				try {
					temp.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}

			for (File current2 : childF.listFiles()) {

				if (current.getName().equals(current2.getName())) {

					if (current.lastModified() != current2.lastModified()) {

						try {

							Files.copy(current.toPath(), current2.toPath(), StandardCopyOption.REPLACE_EXISTING);
							System.out.println("Copying of " + current.getName() + " succeeded!");

							filesChanged++;

						} catch (IOException e1) {

							System.out.println("Copying of " + current.getName() + " failed!");
							e1.printStackTrace();

						}

					}

				}

			}

		}

	}

	private void syncAll() {

		File parentF = new File(parent.getText());
		File childF = new File(child.getText());

		for (File current : parentF.listFiles()) {

			System.out.println("Checking " + current.getName());

			if (!(Arrays.asList(childF.list()).contains(current.getName()))) {

				File temp = new File(child.getText() + "\\" + current.getName());

				try {
					temp.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}

			for (File current2 : childF.listFiles()) {

				if (current.getName().equals(current2.getName())) {

					try {

						Files.copy(current.toPath(), current2.toPath(), StandardCopyOption.REPLACE_EXISTING);
						System.out.println("Copying of " + current.getName() + " succeeded!");

						filesChanged++;

					} catch (IOException e1) {

						System.out.println("Copying of " + current.getName() + " failed!");
						e1.printStackTrace();

					}

				}

			}

		}

	}

	private void syncWithContentChange() {

		File parentF = new File(parent.getText());
		File childF = new File(child.getText());

		FileInputStream in1 = null, in2 = null;

		for (File current : parentF.listFiles()) {

			System.out.println("Checking " + current.getName());

			if (!(Arrays.asList(childF.list()).contains(current.getName()))) {

				File temp = new File(child.getText() + "\\" + current.getName());

				try {
					temp.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}

			for (File current2 : childF.listFiles()) {

				if (current.getName().equals(current2.getName())) {

					try {

						in1 = new FileInputStream(current);
						in2 = new FileInputStream(current2);

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

					int c1 = 0, c2 = 0;

					try {

						while (c1 != -1 && c2 != -1) {

							if (c1 != c2)
								break;

							c1 = in1.read();
							c2 = in2.read();

						}

						in1.close();
						in2.close();

					} catch (IOException e) {
						e.printStackTrace();
					}

					if (c1 != c2) {

						try {

							Files.copy(current.toPath(), current2.toPath(), StandardCopyOption.REPLACE_EXISTING);
							System.out.println("Copying of " + current.getName() + " succeeded!");

							filesChanged++;

						} catch (IOException e1) {

							System.out.println("Copying of " + current.getName() + " failed!");
							e1.printStackTrace();

						}

					}

				}

			}

		}

	}

}
