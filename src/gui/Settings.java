package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import main.Main;

public class Settings extends JFrame {

	private static final long serialVersionUID = 4755471415460680864L;

	private GridBagConstraints c;
	private JComboBox<String> langs;

	public Settings() {

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
		c.fill = GridBagConstraints.VERTICAL;

		setSize(400, 300);
		setTitle("Einstellungen");
		setLocationRelativeTo(Main.getGui());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		requestFocus();
		setLayout(new GridBagLayout());

	}

	private void buildLabels() {

		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(25, 25, 25, 5);

		JLabel lang = new JLabel("Sprache:");
		add(lang, c);

	}

	private void buildTextfields() {

		c.insets = new Insets(25, 0, 20, 25);
		c.gridx = 1;

		langs = new JComboBox<String>();
		// TODO: Sprachen aus den Dateien auslesen
		String[] langList = { "English", "Deutsch" };

		for (String current : langList)
			langs.addItem(current);

		add(langs, c);

	}

	private void buildButtons() {

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.insets = new Insets(0, 0, 25, 0);

		JButton save = new JButton("Speichern");
		add(save, c);

	}

}
