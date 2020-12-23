package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import main.Main;

public class Language {

	public HashMap<String, String> langs;

	// gibt alle Sprachen ausgeschrieben zurück
	public HashMap<String, String> getLanguages() {
		langs = new HashMap<>();

		for (File file : new File("files/languages").listFiles()) {

			try {
				BufferedReader bfr = new BufferedReader(new FileReader(file));
				String line;
				String[] texts;

				while ((line = bfr.readLine()) != null) {
					if (line.startsWith("language")) {

						texts = line.split(":");
						langs.put(texts[1].strip(), file.getName());

						bfr.close();
						break;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return langs;
	}

	private HashMap<String, Object> getTexts(String language) {
		File lang = new File("files/languages/" + getLanguages().get(language)); // Datei von der Sprache
		HashMap<String, Object> Texts = new HashMap<>();

		try {
			BufferedReader bfr = new BufferedReader(new FileReader(lang, StandardCharsets.UTF_8));
			String line;
			String[] texts;

			while ((line = bfr.readLine()) != null) {
				texts = line.split(":");
				if (texts.length == 2)
					texts[1] = texts[1].trim(); // da am Anfang ein Leerzeichen ist

				switch (texts[0]) {

				case "language":
					Texts.put(texts[0], texts[1]);
					break;

				case "windows":
					HashMap<String, String> nestedMap = new HashMap<>();

					line = bfr.readLine();
					texts = line.split(":");
					texts[0] = texts[0].trim(); // da die Zeile eingerückt ist
					texts[1] = texts[1].trim();

					nestedMap.put(texts[0], texts[1]);

					Texts.put("windows", nestedMap);
					break;

				case "texts":
					HashMap<String, String> nestedMap2 = new HashMap<>();

					for (int i = 0; i < 6; i++) {
						line = bfr.readLine();
						texts = line.split(":");
						texts[0] = texts[0].trim();
						texts[1] = texts[1].trim();

						nestedMap2.put(texts[0], texts[1]);
					}

					Texts.put("texts", nestedMap2);
					break;

				case "modes":
					HashMap<String, String> nestedMap3 = new HashMap<>();

					for (int i = 0; i < 3; i++) {
						line = bfr.readLine();
						texts = line.split(":");
						texts[0] = texts[0].trim();
						texts[1] = texts[1].trim();

						nestedMap3.put(texts[0], texts[1]);
					}

					Texts.put("modes", nestedMap3);
					break;
				
				case "messages":
					HashMap<String, String> nestedMap4 = new HashMap<>();
					
					for (int i = 0; i < 15; i++) {

						if (i == 3 || i == 9) {
							bfr.readLine();
							continue;
						}
							
						
						line = bfr.readLine();
						texts = line.split(":");
						texts[0] = texts[0].trim();
						texts[1] = texts[1].trim();
						
						nestedMap4.put(texts[0], texts[1]);
					}
					
					Texts.put("messages", nestedMap4);
					break;

				}

			}

			bfr.close();
			return Texts;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public String getText(String path) {
		HashMap<String, Object> Texts = getTexts(Main.getConfig().getCurrentLanguage());
		String[] pathParts = path.split("\\.");
		String current;
		HashMap<String, Object> currentMap = Texts;
		
		for (int i = 0; i < pathParts.length; i++) {
			
			current = pathParts[i];
			pathParts[i] = null;

			if (pathParts[pathParts.length - 1] == null) {
				return (String) currentMap.get(current);
			} else {
				currentMap = (HashMap<String, Object>) currentMap.get(current);
			}
		}
		
		return null;

	}

}
