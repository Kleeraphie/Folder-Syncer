package synchroniser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import main.Main;

public class SynchronisingOptions {

	/**
	 * @param parent
	 *            Der Pfad des Startverzeichnisses
	 * @param child
	 *            Der Pfad des Zielverzeichnisses
	 */
	public static void syncWithModifiedTime(String parent, String child) {

		File parentF = new File(parent);
		File childF = new File(child);

		for (File current : parentF.listFiles()) {
			// Jede Datei im Startverzeichnis auf Vorhandensein im Zielverzeichnis prüfen

			// System.out.println("Checking " + current.getName());

			if (!(Arrays.asList(childF.list()).contains(current.getName()))) {
				// Zielverzeichnis enthält nicht die gerade untersuchte Datei

				// Erstellen einer temporären Datei mit späterem Namen
				// damit späterer Kopiervorgang möglich ist
				File temp = new File(child + "\\" + current.getName());

				try {
					temp.createNewFile();
				} catch (IOException e) {
					// Temporäre Datei konnte nicht erstellt werden
					// Vorgang wird für diese Datei abgebrochen &
					// und als fehlgeschlagen gewertet

					e.printStackTrace();
					Main.getGui().filesFailed++;
					continue;
				}

			}

			for (File current2 : childF.listFiles()) {

				if (current.getName().equals(current2.getName())) {
					// zu untersuchende Datei und temporäre Datei gefunden

					if (current.lastModified() != current2.lastModified()) {
						// Änderungsdaten der Dateien unterscheiden sich

						try {
							// Temporäre Datei durch Datei aus dem Startverzeichnis ersetzen
							// Vorgang für diese Datei beendet &
							// als Erfolg gewertet

							Files.copy(current.toPath(), current2.toPath(), StandardCopyOption.REPLACE_EXISTING);
							// System.out.println("Copying of " + current.getName() + " succeeded!");

							Main.getGui().filesChanged++;

						} catch (IOException e) {
							// Ersetzen der temporären Datei fehlgeschlagen
							// Vorgang wird für diese Datei abgebrochen &
							// und als fehlgeschlagen gewertet

							// System.out.println("Copying of " + current.getName() + " failed!");
							e.printStackTrace();
							Main.getGui().filesFailed++;
						}

					}

				}

			}

		}

	}

	/**
	 * @param parent
	 *            Der Pfad des Startverzeichnisses
	 * @param child
	 *            Der Pfad des Zielverzeichnisses
	 */
	public static void syncAll(String parent, String child) {

		File parentF = new File(parent);
		File childF = new File(child);

		for (File current : parentF.listFiles()) {
			// Jede Datei im Startverzeichnis auf Vorhandensein im Zielverzeichnis prüfen

			// System.out.println("Checking " + current.getName());

			if (!(Arrays.asList(childF.list()).contains(current.getName()))) {
				// Zielverzeichnis enthält nicht die gerade untersuchte Datei

				// Erstellen einer temporären Datei mit späterem Namen
				// damit späterer Kopiervorgang möglich ist
				File temp = new File(child + "\\" + current.getName());

				try {
					temp.createNewFile();
				} catch (IOException e) {
					// Temporäre Datei konnte nicht erstellt werden
					// Vorgang wird für diese Datei abgebrochen &
					// und als fehlgeschlagen gewertet

					e.printStackTrace();
					Main.getGui().filesFailed++;
					continue;
				}

			}

			for (File current2 : childF.listFiles()) {

				if (current.getName().equals(current2.getName())) {
					// zu untersuchende Datei und temporäre Datei gefunden

					try {
						// Temporäre Datei durch Datei aus dem Startverzeichnis ersetzen
						// Vorgang für diese Datei beendet &
						// als Erfolg gewertet

						Files.copy(current.toPath(), current2.toPath(), StandardCopyOption.REPLACE_EXISTING);
						// System.out.println("Copying of " + current.getName() + " succeeded!");

						Main.getGui().filesChanged++;

					} catch (IOException e) {
						// Ersetzen der temporären Datei fehlgeschlagen
						// Vorgang wird für diese Datei abgebrochen &
						// und als fehlgeschlagen gewertet

						// System.out.println("Copying of " + current.getName() + " failed!");
						e.printStackTrace();
						Main.getGui().filesFailed++;

					}

				}

			}

		}

	}

	/**
	 * @param parent
	 *            Der Pfad des Startverzeichnisses
	 * @param child
	 *            Der Pfad des Zielverzeichnisses
	 */
	public static void syncWithContentChange(String parent, String child) {

		File parentF = new File(parent);
		File childF = new File(child);

		FileInputStream in1 = null, in2 = null;

		for (File current : parentF.listFiles()) {
			// Jede Datei im Startverzeichnis auf Vorhandensein im Zielverzeichnis prüfen

			// System.out.println("Checking " + current.getName());

			if (!(Arrays.asList(childF.list()).contains(current.getName()))) {
				// Zielverzeichnis enthält nicht die gerade untersuchte Datei

				// Erstellen einer temporären Datei mit späterem Namen
				// damit späterer Kopiervorgang möglich ist
				File temp = new File(child + "\\" + current.getName());

				try {
					temp.createNewFile();
				} catch (IOException e) {
					// Temporäre Datei konnte nicht erstellt werden
					// Vorgang wird für diese Datei abgebrochen &
					// und als fehlgeschlagen gewertet

					e.printStackTrace();
					Main.getGui().filesFailed++;
					continue;
				}

			}

			for (File current2 : childF.listFiles()) {

				if (current.getName().equals(current2.getName())) {
					// zu untersuchende Datei und temporäre Datei gefunden

					try {

						in1 = new FileInputStream(current);
						in2 = new FileInputStream(current2);

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

					int c1 = 0, c2 = 0;

					try {
						// Überprüfen auf Unterschiede in den beiden Dateien

						while (c1 != -1 && c2 != -1) {

							if (c1 != c2) // Unterschied gefunden
								break;

							c1 = in1.read();
							c2 = in2.read();

						}

						in1.close();
						in2.close();

					} catch (IOException e) {
						// Fehler beim Überprüfen auf Unterschiede in den beiden Dateien
						// Vorgang wird für diese Datei abgebrochen &
						// und als fehlgeschlagen gewertet

						e.printStackTrace();
						Main.getGui().filesFailed++;
						continue;
					}

					if (c1 != c2) {

						try {
							// Temporäre Datei durch Datei aus dem Startverzeichnis ersetzen
							// Vorgang für diese Datei beendet &
							// als Erfolg gewertet

							Files.copy(current.toPath(), current2.toPath(), StandardCopyOption.REPLACE_EXISTING);
							// System.out.println("Copying of " + current.getName() + " succeeded!");

							Main.getGui().filesChanged++;

						} catch (IOException e1) {
							// Ersetzen der temporären Datei fehlgeschlagen
							// Vorgang wird für diese Datei abgebrochen &
							// und als fehlgeschlagen gewertet

							// System.out.println("Copying of " + current.getName() + " failed!");
							e1.printStackTrace();
							Main.getGui().filesFailed++;

						}

					}

				}

			}

		}

	}

}
