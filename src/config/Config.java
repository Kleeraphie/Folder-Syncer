package config;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;

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

}
