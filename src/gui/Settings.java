package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

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
		setLocationRelativeTo(Main.getGui());
		setVisible(true);

	}

	private void buildWindow() {
		// Bau des JFrames

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.VERTICAL;
		
		setTitle(Main.getConfig().getText("windows.settings"));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		requestFocus();
		setLayout(new GridBagLayout());

	}

	
	private void buildLabels() {

		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(55, 75, 25, 5); // TODO: 55 damit label mittig von jcb ist; müsste auch anders gehen

		JLabel lang = new JLabel(Main.getConfig().getText("texts.language") + ":");
		add(lang, c);

	}

	private void buildTextfields() {

		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 1;
		c.insets = new Insets(50, 0, 20, 75);
		
		langs = new JComboBox<String>();
		Set<String> langList = Main.getConfig().getLanguages().keySet();

		for (String current : langList)
			langs.addItem(current);

		// ausgewählte Sprache auf aktuelle Sprache setzen
		langs.setSelectedItem(Main.getConfig().getCurrentLanguage());

		add(langs, c);

	}

	private void buildButtons() {

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.insets = new Insets(0, 0, 50, 0);

		JButton save = new JButton(Main.getConfig().getText("texts.save"));
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.getConfig().setCurrentLanguage((String) langs.getSelectedItem());
				Main.getGui().reload();
				dispose();
			}
		});
		
		add(save, c);

	}

}
