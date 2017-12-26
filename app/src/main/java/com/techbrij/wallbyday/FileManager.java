package com.techbrij.wallbyday;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * The <b>FileManager</b> class is a static class used to save <b>File</b> from
 * <b>String</b> and load <b>File</b> as <b>String</b>.
 *
 * @version 1.0
 */
public class FileManager {

    /**
     * Load a <b>File</b> as a <b>String</b>.
     *
     * @param file
     *            The file to load
     * @return The File as a String
     */
    public static String readFromFile(final File file) {
        String ret = "";

        try {
            FileInputStream inputStream = new FileInputStream(file );

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }


    public static String[] readArrayFromFile(final File file) {
        ArrayList<String> ret = new ArrayList<String>();

        try {
            FileInputStream inputStream = new FileInputStream(file );

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ((receiveString = bufferedReader.readLine()) != null) {
                    ret.add(receiveString);
                }

                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret.toArray(new String[ret.size()]);
    }

    /**
     * Write a <b>String</b> as a <b>File</b>.
     *
     * @param file
     *            The File where the data will be written
     * @param data
     *            The data to write
     */
    public static void writeToFile(final File file, final String data) {
        new Thread() {
            @Override
            public void run() {
                try {
                    FileOutputStream outputStream = new FileOutputStream(file);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                            outputStream);
                    outputStreamWriter.write(data);
                    outputStreamWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.run();
    }

    public static void writeArrayToFile(final File file, final String[] data) {
        new Thread() {
            @Override
            public void run() {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    for (String item: data) {
                        if (item!=null) {
                            writer.write(item);
                        }
                        writer.newLine();
                    }
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.run();
    }

//Write Without any thread
    public static void writeArrayToFileSync(final File file, final String[] data) {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    for (String item: data) {
                        if (item!=null) {
                            writer.write(item);
                        }
                        writer.newLine();
                    }
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }

    /**
     * Private constructor of <b>FileManager</b>. <i>Static class should not be
     * instanced</i>
     */
    private FileManager() {
    }
}