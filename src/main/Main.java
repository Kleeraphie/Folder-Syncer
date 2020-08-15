package main;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import gui.GUI;

/**
 * Dieses Programm synchronisiert den Inhalt von zwei Ordnern. Dabei kann man
 * einstellen, ob es alle Dateien, nur die Dateien mit einem neuerem
 * Änderungsdatum & alle neuen Dateien oder die Dateien, wo sich der Inhalt
 * geändert hat & alle neuen Dateien in den anderen Ordner kopieren und ggf.
 * ersetzen soll.
 * 
 * @author Raphael Kleebaum
 * @version 1.0.1
 * @since 31.03.2020
 *
 */

public class Main {

	public static GUI gui;

	public static void main(String[] args) {

		// Setzen des LookAndFeels des Programms auf das LookAndFeel des Systems
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		gui = new GUI();
	}

	/**
	 * 
	 * @return Gibt die Instanz der GUI zurück
	 */
	public static GUI getGui() {
		return gui;
	}

}
