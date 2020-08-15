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

	public static void syncWithModifiedTime(String parent, String child) {

		File parentF = new File(parent);
		File childF = new File(child);

		for (File current : parentF.listFiles()) {

//			System.out.println("Checking " + current.getName());

			if (!(Arrays.asList(childF.list()).contains(current.getName()))) {

				File temp = new File(child + "\\" + current.getName());

				try {
					temp.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}

			for (File current2 : childF.listFiles()) {

				if (current.getName().equals(current2.getName())) {

					if (current.lastModified() != current2.lastModified()) {

						try {

							Files.copy(current.toPath(), current2.toPath(), StandardCopyOption.REPLACE_EXISTING);
//							System.out.println("Copying of " + current.getName() + " succeeded!");

							Main.getGui().filesChanged++;

						} catch (IOException e1) {

//							System.out.println("Copying of " + current.getName() + " failed!");
							e1.printStackTrace();

							Main.getGui().filesFailed++;

						}

					}

				}

			}

		}

	}

	public static void syncAll(String parent, String child) {

		File parentF = new File(parent);
		File childF = new File(child);

		for (File current : parentF.listFiles()) {

//			System.out.println("Checking " + current.getName());

			if (!(Arrays.asList(childF.list()).contains(current.getName()))) {

				File temp = new File(child + "\\" + current.getName());

				try {
					temp.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}

			for (File current2 : childF.listFiles()) {

				if (current.getName().equals(current2.getName())) {

					try {

						Files.copy(current.toPath(), current2.toPath(), StandardCopyOption.REPLACE_EXISTING);
//						System.out.println("Copying of " + current.getName() + " succeeded!");

						Main.getGui().filesChanged++;

					} catch (IOException e1) {

//						System.out.println("Copying of " + current.getName() + " failed!");
						e1.printStackTrace();

						Main.getGui().filesFailed++;

					}

				}

			}

		}

	}

	public static void syncWithContentChange(String parent, String child) {

		File parentF = new File(parent);
		File childF = new File(child);

		FileInputStream in1 = null, in2 = null;

		for (File current : parentF.listFiles()) {

//			System.out.println("Checking " + current.getName());

			if (!(Arrays.asList(childF.list()).contains(current.getName()))) {

				File temp = new File(child + "\\" + current.getName());

				try {
					temp.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}

			for (File current2 : childF.listFiles()) {

				if (current.getName().equals(current2.getName())) {

					try {

						in1 = new FileInputStream(current);
						in2 = new FileInputStream(current2);

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

					int c1 = 0, c2 = 0;

					try {

						while (c1 != -1 && c2 != -1) {

							if (c1 != c2)
								break;

							c1 = in1.read();
							c2 = in2.read();

						}

						in1.close();
						in2.close();

					} catch (IOException e) {
						e.printStackTrace();
					}

					if (c1 != c2) {

						try {

							Files.copy(current.toPath(), current2.toPath(), StandardCopyOption.REPLACE_EXISTING);
//							System.out.println("Copying of " + current.getName() + " succeeded!");

							Main.getGui().filesChanged++;

						} catch (IOException e1) {

//							System.out.println("Copying of " + current.getName() + " failed!");
							e1.printStackTrace();

							Main.getGui().filesFailed++;

						}

					}

				}

			}

		}

	}

}
