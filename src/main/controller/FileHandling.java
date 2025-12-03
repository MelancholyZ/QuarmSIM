package main.controller;

import java.io.*;

/**
 * This class has two main methods. It uses readFromFile to read the contents
 * of a specified file, and writeToFile to write the provided contents to
 * the specified file.
 * @see FileInputStream https://docs.oracle.com/javase/8/docs/api/java/io/FileInputStream.html
 * @author Shawn Quillin
 * @version 1.0, 13 JULY 2025
 */
public class FileHandling {
    public static long fileWriterPosition = 0;

    /**
     * Default constructor
     */
    public FileHandling(){
    }

    /**
     * This method attempts to open the file of the specified argument,
     * fileName, and if successful, it reads the contents of the file and
     * return it in the form of a string.
     * @param fileName the name of the file to be read
     * @return fileContents.toString() the contents of the file
     */
    public String readFromFile (String fileName) {
        StringBuilder fileContents = new StringBuilder();
        int c;

        //Attempts to open the file
        try (BufferedInputStream bis = new BufferedInputStream(new DataInputStream(new FileInputStream(fileName)))) {
            //read from file and checking for EoF
            while((c = bis.read()) !=-1) {
                //Reads the contents of the file
                fileContents.append((char) c);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return fileContents.toString();
    }

    /**
     * This method attempts to open a file by the specified argument fileName,
     * and if successful, writes the contents of the string fileContents
     * to the file using a buffered output stream.
     * @param fileName the name of the file to be written to
     * @param fileContents the contents of the file to be written
     */
    public void writeToFile(String fileName, String fileContents) {
        //Attempts to open the file and write to it
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
            raf.seek(fileWriterPosition);

            try (BufferedOutputStream bos = new BufferedOutputStream((new FileOutputStream(raf.getFD())))) {
                bos.write(fileContents.getBytes());
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }

        fileWriterPosition += fileContents.length();
    }

    public void convertDBToShort(String fullDBPath) {

        String fullDB = readFromFile(fullDBPath);
        StringBuilder newDB = new StringBuilder();
        String[] tablesToGrab = new String[]{"aa_actions","aa_effects", "altadv_vars", "items` (", "npc_types",
                                "skill_caps` (", "spells_en` (", "spells_new", "skill_difficulty"};

        for (String s : tablesToGrab) {
            newDB.append(Controller.grabTableInDB(s, fullDB));
            newDB.append("\n");
        }

        newDB.append("CREATE TABLE 'classID'\n");
        newDB.append("Warrior 1,\n");
        newDB.append("Cleric 2,\n");
        newDB.append("Paladin 3,\n");
        newDB.append("Ranger 4,\n");
        newDB.append("ShadowKnight 5,\n");
        newDB.append("Druid 6,\n");
        newDB.append("Monk 7,\n");
        newDB.append("Bard 8,\n");
        newDB.append("Rogue 9,\n");
        newDB.append("Shaman 10,\n");
        newDB.append("Necromancer 11,\n");
        newDB.append("Wizard 12,\n");
        newDB.append("Mage 13,\n");
        newDB.append("Enchanter 14,\n");
        newDB.append("Beastlord 15,\n");
        newDB.append("CREATE TABLE\n");

        writeToFile("resources/quarm_shortdb.sql", newDB.toString());
    }

    public void clearFile(String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
        } catch (IOException e) {
            System.err.println("Error clearing file: " + e.getMessage());
        }
    }
}
