package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import synchroniser.SynchronisingOptions;

public class GUI extends JFrame {

	private static final long serialVersionUID = 2941318999657277463L;

	public int filesChanged, filesFailed;

	private JLabel parentL, childL, syncMode, changedFiles, failedFiles;
	private JTextField parent, child;
	private JButton parentB, childB, sync;
	private JFileChooser jfc;
	private JComboBox<String> jcb;

	public GUI() {

		jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		// Bau der JComboBox zur Auswahl des Sync-Modus
		jcb = new JComboBox<String>();
		jcb.setSize(200, 20);
		jcb.setLocation(120, 120);

		String[] jcbList = { "Änderungsdatum", "Alle", "Inhaltliche Änderung" };

		for (String current : jcbList)
			jcb.addItem(current);

		add(jcb);

		// Bau der GUI
		buildWindow();
		buildLabels();
		buildTextfields();
		buildButtons();

		setVisible(true);

	}

	private void buildWindow() {
		// Bau des JFrames

		setSize(400, 300);
		setTitle("FolderSyncer");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		requestFocus();
		setLayout(null);

	}

	private void buildLabels() {
		// Bau der einzelnen Labels im JFrame

		parentL = new JLabel("Startverzeichnis:");
		parentL.setSize(120, 20);
		parentL.setLocation(20, 20);
		add(parentL);

		childL = new JLabel("Zielverzeichnis:");
		childL.setSize(100, 20);
		childL.setLocation(20, 70);
		add(childL);

		syncMode = new JLabel("Sync-Modus:");
		syncMode.setSize(100, 20);
		syncMode.setLocation(20, 120);
		add(syncMode);

	}

	private void buildTextfields() {
		// Bau der einzelnen Textfelder im JFrame

		// Textfeld für das Startverzeichnis
		parent = new JTextField();
		parent.setSize(200, 20);
		parent.setLocation(120, 20);
		add(parent);

		// Textfeld für das Zielverzeichnis
		child = new JTextField();
		child.setSize(200, 20);
		child.setLocation(120, 70);
		add(child);

	}

	private void buildButtons() {
		// Bau der einzelnen Buttons im JFrame

		// Button um das Startverzeichnis auszuwählen
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

		// Button um das Zielverzeichnis auszuwählen
		childB = new JButton();
		childB.setSize(20, 20);
		childB.setLocation(340, 70);
		childB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// Wenn noch kein Startverzeichnis gewählt wurde, zeigt der jfc das
				// Standardverzeichnis,
				// sonst das dem Startverzeichnis übergeordnete Verzeichnis
				if (!parent.getText().equals(""))
					jfc.setCurrentDirectory(new File(parent.getText()).getParentFile());
				else
					jfc.setCurrentDirectory(new File(child.getText()).getParentFile());

				int selectVal = jfc.showOpenDialog(null);

				if (selectVal == JFileChooser.APPROVE_OPTION) {
					child.setText(jfc.getSelectedFile().getAbsolutePath());
				}

			}
		});
		add(childB);

		// Button um die Synchronisierung zu beginnen
		sync = new JButton("Synchronisieren");
		sync.setSize(150, 20);
		sync.setLocation(125, 240);
		sync.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// Anzahl der Dateien, die geändert wurden &
				// bei der ein Fehler während der Synchronisierung auftrat
				filesChanged = 0;
				filesFailed = 0;

				switch (String.valueOf(jcb.getSelectedItem())) {

				case ("Änderungsdatum"):
					SynchronisingOptions.syncWithModifiedTime(parent.getText(), child.getText());
					break;

				case ("Alle"):
					SynchronisingOptions.syncAll(parent.getText(), child.getText());
					break;

				case ("Inhaltliche Änderung"):
					SynchronisingOptions.syncWithContentChange(parent.getText(), child.getText());
					break;

				}

				// Leeren von den Labels
				if (changedFiles != null)
					changedFiles.setText("");

				if (failedFiles != null)
					failedFiles.setText("");

				if (filesChanged == new File(child.getText()).listFiles().length) {
					// Anzahl der geänderten Dateien == der Anzahl der Dateien im Zielverzeichnis

					changedFiles = new JLabel("Es wurden alle Dateien geändert.", SwingConstants.CENTER);
					changedFiles.setForeground(new Color(17, 204, 0));

					changedFiles.setLocation(0, 160);

				} else {

					if (filesChanged > 0) {
						// Es wurden Dateien geändert, aber nicht alle

						changedFiles = new JLabel(String.format("%s von %s Dateien geändert.", filesChanged,
								new File(child.getText()).listFiles().length), SwingConstants.CENTER);
						changedFiles.setForeground(new Color(17, 204, 0));
					} else {
						// Es wurden keine Dateien geändert

						changedFiles = new JLabel("Es wurden keine Dateien geändert.", SwingConstants.CENTER);
						changedFiles.setForeground(new Color(240, 200, 0)); // gelb
					}
					
					changedFiles.setLocation(0, 160);

					if (filesFailed == new File(child.getText()).listFiles().length) {
						// Beim Synchronisieren jeder Datei ist ein Fehler aufgetreten

						failedFiles = new JLabel("Keine Datei konnte geändert werden", SwingConstants.CENTER);

						failedFiles.setForeground(Color.RED);
						failedFiles.setFont(new Font("Tahoma", Font.PLAIN, 12));
						failedFiles.setSize(getWidth(), 20);
						failedFiles.setLocation(0, 180);
						add(failedFiles);

					} else if (filesFailed > 0) {
						// Synchronisierung einiger Dateien ist fehl geschlagen

						failedFiles = new JLabel(String.format("%s von %s Dateien konnten nicht geändert werden",
								filesFailed, new File(child.getText()).listFiles().length), SwingConstants.CENTER);

						failedFiles.setForeground(Color.RED);
						failedFiles.setFont(new Font("Tahoma", Font.PLAIN, 12));
						failedFiles.setSize(getWidth(), 20);
						failedFiles.setLocation(0, 180);
						add(failedFiles);

						changedFiles.setLocation(0, 150);

					}

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

	}

}
