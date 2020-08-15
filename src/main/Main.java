package main;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import gui.GUI;

public class Main {

	public static GUI gui;

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		gui = new GUI();
	}

	public static GUI getGui() {
		return gui;
	}

}
