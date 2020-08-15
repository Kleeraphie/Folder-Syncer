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
		
		jcb = new JComboBox<String>();
		jcb.setSize(200, 20);
		jcb.setLocation(120, 120);

		String[] jcbList = { "Änderungsdatum", "Alle", "Inhaltliche Änderung" };

		for (String current : jcbList)
			jcb.addItem(current);

		add(jcb);

		buildWindow();
		buildLabels();
		buildTextfields();
		buildButtons();
		
		setVisible(true);

	}

	private void buildWindow() {

		setSize(400, 300);
		setTitle("FolderSyncer");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		requestFocus();
		setLayout(null);

	}

	private void buildLabels() {

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

		parent = new JTextField();
		parent.setSize(200, 20);
		parent.setLocation(120, 20);
		add(parent);

		child = new JTextField();
		child.setSize(200, 20);
		child.setLocation(120, 70);
		add(child);
		
	}

	private void buildButtons() {

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
					SynchronisingOptions.syncWithModifiedTime(parent.getText(), child.getText());
					break;

				case ("Alle"):
					SynchronisingOptions.syncAll(parent.getText(), child.getText());
					break;

				case ("Inhaltliche Änderung"):
					SynchronisingOptions.syncWithContentChange(parent.getText(), child.getText());
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
		
	}

}
