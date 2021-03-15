package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import main.Main;

public class Config {

	public String getCurrentLanguage() {

		try {
			BufferedReader bfr = new BufferedReader(new FileReader("files/config.yml"));
			String line;

			while ((line = bfr.readLine()) != null) {
				if (line.startsWith("current_lang")) {

					bfr.close();
					return line.split(":")[1].strip();
				}
			}

			bfr.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public void setCurrentLanguage(String newLanguage) {

		try {
			BufferedReader bfr = new BufferedReader(new FileReader("files/config.yml"));
			StringBuffer inputBuffer = new StringBuffer();
			String line;
			String[] texts;

			while ((line = bfr.readLine()) != null) {

				if (line.startsWith("current_lang:")) {

					texts = line.split(":");
					line = texts[0] + ": " + newLanguage;
					inputBuffer.append(line);
					inputBuffer.append('\n');

				}

			}

			bfr.close();

			FileOutputStream fileOut = new FileOutputStream("files/config.yml");
			fileOut.write(inputBuffer.toString().getBytes());
			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

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

	private HashMap<String, String> getTexts(String language) {
		File lang = new File("files/languages/" + getLanguages().get(language)); // Datei von der Sprache
		HashMap<String, String> Texts = new HashMap<>();

		try {
			BufferedReader bfr = new BufferedReader(new FileReader(lang, StandardCharsets.UTF_8));
			String line;
			String path = "";
			int oldTabs = 0, newTabs = 0;
			String[] texts;

			while ((line = bfr.readLine()) != null) {

				texts = line.split(":");

				if (texts.length == 2)
					texts[1] = texts[1].trim(); // da am Anfang ein Leerzeichen ist

				newTabs = countFrontTabs(line);
				
				texts[0] = texts[0].trim();
				
				if (newTabs == 0)
					path = "";

				if (newTabs <= oldTabs) { // letzten Teil von path neu machen

					if (!path.equals(""))
						path = path.replace(path.subSequence(indexOfLastDot(path) + 1, path.length()), texts[0]);
					else
						path += texts[0];

				} else // neuen Teil zum path hinzufügen
					path += "." + texts[0];

				if (texts.length > 1) // == 2
					Texts.put(path, texts[1]);

				oldTabs = newTabs;
				
			}

			bfr.close();
			return Texts;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getText(String path) {
		return getTexts(Main.getConfig().getCurrentLanguage()).get(path);
	}

	private int countFrontTabs(String word) {
		int result = 0;
		String wordPart = "";

		for (char c : word.toCharArray()) {

			wordPart += c;

			if (wordPart.equals("    ")) {
				result++;
				wordPart = "";
			} else if (wordPart.chars().allMatch(Character::isLetter))
				break;

		}

		return result;
	}

	private int indexOfLastDot(String word) {
		int index = 0;

		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == '.')
				index = i;
		}

		return index;
	}
	
}
