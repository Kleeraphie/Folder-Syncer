package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import synchroniser.SynchronisingOptions;

public class GUI extends JFrame {

	private static final long serialVersionUID = 2941318999657277463L;

	public int filesChanged, filesFailed;
	private JTextField parent, child;
	private JFileChooser jfc;
	private JComboBox<String> jcb;
	private GridBagConstraints c;

	public GUI() {

		// Bau der GUI
		buildWindow();
		buildLabels();
		buildTextfields();
		buildButtons();

		pack();
		setVisible(true);

	}

	private void buildWindow() {
		// Bau des JFrames

		c = new GridBagConstraints();

		setSize(400, 300);
		setTitle("FolderSyncer");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		requestFocus();
		setLayout(new GridBagLayout());

	}

	private void buildLabels() {
		// Bau der einzelnen Labels im JFrame

		JLabel parentL, childL, syncMode;

		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 25, 5, 5);

		c.insets.top = 25;

		parentL = new JLabel("Startverzeichnis:");
		add(parentL, c);

		c.gridy = 1;
		c.insets.top = 0;

		childL = new JLabel("Zielverzeichnis:");
		add(childL, c);

		c.gridy = 2;
		syncMode = new JLabel("Sync-Modus:");
		add(syncMode, c);

	}

	// TODO: JCB ist 1px kürzer als TextFields (sichtbar)
	private void buildTextfields() {
		// Bau der einzelnen Textfelder im JFrame

		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 5, 0);

		c.insets.top = 25;

		// Textfeld für das Startverzeichnis
		parent = new JTextField();
		parent.setColumns(15);
		add(parent, c);

		// Textfeld für das Zielverzeichnis
		c.gridy = 1;
		c.insets.top = 0;

		child = new JTextField();
		child.setColumns(15);
		add(child, c);

		c.gridx = 1;
		c.gridy = 2;

		jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		// Bau der JComboBox zur Auswahl des Sync-Modus
		jcb = new JComboBox<String>();

		String[] jcbList = { "Änderungsdatum", "Alle", "Inhaltliche Änderung" };

		for (String current : jcbList)
			jcb.addItem(current);

		add(jcb, c);

	}

	private void buildButtons() {
		// Bau der einzelnen Buttons im JFrame

		JButton parentB, childB, sync, settings;
		Image icon;

		c.insets = new Insets(0, 5, 5, 25);

		c.gridx = 2;
		c.gridy = 0;
		c.insets.top = 25;

		// Button um das Startverzeichnis auszuwählen
		parentB = new JButton();
		parentB.setPreferredSize(new Dimension(20, 20));
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
		add(parentB, c);

		c.gridy = 1;
		c.insets.top = 0;

		// Button um das Zielverzeichnis auszuwählen
		childB = new JButton();
		childB.setPreferredSize(new Dimension(20, 20));
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
		add(childB, c);

		// Icon für die Pfad-Buttons laden und setzen
		try {
			icon = ImageIO.read(new File("images/directory.png"));

			parentB.setIcon(new ImageIcon(icon));
			childB.setIcon(new ImageIcon(icon));

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(15, 0, 25, 3);
		c.gridwidth = 3;

		// Button um die Synchronisierung zu beginnen
		sync = new JButton("Synchronisieren");
		sync.addActionListener(new ActionListener() {

			// TODO: vllt. klug returns setzen um rechenaufwand zu minimieren
			@Override
			public void actionPerformed(ActionEvent e) {

				if (checkEntries()) // Alle Pfade sind korrekt und nicht gleich
					sync();

			}
		});
		add(sync, c);

		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 1;
		
		settings = new JButton();
		settings.setPreferredSize(new Dimension(32,32));
		settings.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new Settings();
				
			}
		});
		add(settings, c);
		
		// Icon für den Einstellungen-Button laden und setzen
		try {
			icon = ImageIO.read(new File("images/settings.png"));

			settings.setIcon(new ImageIcon(icon));

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	private boolean checkEntries() {
		if (parent.getText().equals("")) {

			String message = "Im Feld \"Startverzeichnis\" muss ein Pfad angegeben sein!";

			JOptionPane.showMessageDialog(this, message, "Fehler!", JOptionPane.ERROR_MESSAGE);

			return false;

		} else if (child.getText().equals("")) {

			String message = "Im Feld \"Zielverzeichnis\" muss ein Pfad angegeben sein!";

			JOptionPane.showMessageDialog(this, message, "Fehler!", JOptionPane.ERROR_MESSAGE);

			return false;
		}

		if (!new File(parent.getText()).exists()) {
			String message = "Das Startverzeichnis existiert nicht!";

			JOptionPane.showMessageDialog(this, message, "Fehler!", JOptionPane.ERROR_MESSAGE);

			return false;
		} else if (!new File(child.getText()).exists()) {

			String message = "Das Zielverzeichnis existiert nicht!";

			JOptionPane.showMessageDialog(this, message, "Fehler!", JOptionPane.ERROR_MESSAGE);

			return false;
		}

		if (parent.getText().equals(child.getText())) {

			String message = "Das Start- und Zielverzeichnis sind identisch!";

			JOptionPane.showMessageDialog(this, message, "Fehler!", JOptionPane.ERROR_MESSAGE);

			return false;
		}

		return true;
	}

	private void sync() {

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

		showMessages();

	}

	private void showMessages() {

		if (filesChanged == new File(child.getText()).listFiles().length) {
			// Anzahl der geänderten Dateien == der Anzahl der Dateien im Zielverzeichnis

			String message = "Es wurden alle Dateien geändert.";

			JOptionPane.showMessageDialog(null, message, "Erfolg!", JOptionPane.INFORMATION_MESSAGE);

		} else {

			if (filesChanged > 0) {
				// Es wurden Dateien geändert, aber nicht alle

				String message = String.format("%s von %s Dateien geändert. Der Rest war gleich.", filesChanged,
						new File(child.getText()).listFiles().length);

				JOptionPane.showMessageDialog(null, message, "Warnung!", JOptionPane.WARNING_MESSAGE);

			} else {
				// Es wurden keine Dateien geändert

				String message = "Es wurden keine Dateien geändert, da alle Dateien gleich sind.";

				JOptionPane.showMessageDialog(null, message, "Warnung!", JOptionPane.WARNING_MESSAGE);

			}

			if (filesFailed == new File(child.getText()).listFiles().length) {
				// Beim Synchronisieren jeder Datei ist ein Fehler aufgetreten

				String message = "Keine Datei konnte aufgrund eines Fehlers geändert werden.";

				JOptionPane.showMessageDialog(null, message, "Fehler!", JOptionPane.ERROR_MESSAGE);

			} else if (filesFailed > 0) {
				// Synchronisierung einiger Dateien ist fehl geschlagen

				String message = String.format(
						"%s von %s Dateien konnten aufgrund eines Fehlers nicht geändert werden.", filesFailed,
						new File(child.getText()).listFiles().length);

				JOptionPane.showMessageDialog(null, message, "Fehler!", JOptionPane.ERROR_MESSAGE);

			}

		}

	}

}
