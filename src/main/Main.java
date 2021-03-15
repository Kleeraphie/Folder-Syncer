package main;

import javax.swing.UIManager;

import config.Config;
import gui.GUI;

/**
 * Dieses Programm synchronisiert den Inhalt von zwei Ordnern. Dabei kann man
 * einstellen, ob es alle Dateien, nur die Dateien mit einem neuerem
 * �nderungsdatum & alle neuen Dateien oder die Dateien, wo sich der Inhalt
 * ge�ndert hat & alle neuen Dateien in den anderen Ordner kopieren und ggf.
 * ersetzen soll.
 * 
 * @author Raphael Kleebaum
 * @version 1.2.0
 * @since 31.03.2020
 *
 */

public class Main {

	public static GUI gui;
	private static Config lang;
	private static Config config;
	public static String currentLang;

	public static void main(String[] args) {

		// Setzen des LookAndFeels des Programms auf das LookAndFeel des Systems
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		lang = new Config();
		config = new Config();
		
		gui = new GUI();
	}

	/**
	 * 
	 * @return Gibt die Instanz der GUI zur�ck
	 */
	public static GUI getGui() {
		return gui;
	}
	
	public static Config getLanguage() {
		return lang;
	}
	
	public static Config getConfig() {
		return config;
	}

}
