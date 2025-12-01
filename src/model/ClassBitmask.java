package model;

import java.util.ArrayList;

/**
 * Class bitmask class
 */
public class ClassBitmask {
    private int bitmask;
    private ArrayList<String> classes = new ArrayList<>();

    /**
     * This class converts the int bitmask into an ArrayList of strings of classes
     * @param bitMaskArg the bitmask to decode
     */
    public ClassBitmask(int bitMaskArg) {
        bitmask = bitMaskArg;
        convertBitmask();
    }

    /**
     * Getter method that gets the classes used by the bitmask
     * @return the classes of the bitmask
     */
    public ArrayList<String> getClasses() {
        return classes;
    }

    /**
     * Setter method that sets the classes from the bitmask
     * @param classes the classes of the bitmask
     */
    public void setClasses(ArrayList<String> classes) {
        this.classes = classes;
    }

    /**
     *Warrior 1
     *Cleric 2,
     *Paladin 4,
     *Ranger 8,
     *ShadowKnight 16,
     *Druid 32,
     *Monk 64,
     *Bard 128,
     *Rogue 256,
     *Shaman 512,
     *Necromancer 1024,
     *Wizard 2048,
     *Magician 4096,
     *Enchanter 8192,
     *Beastlord 16384,
     */
    public void convertBitmask() {
        if (bitmask >= 16384) {
            classes.add("Beastlord");
            bitmask -= 16384;
        }
        if (bitmask >= 8192) {
            classes.add("Enchanter");
            bitmask -= 8192;
        }
        if (bitmask >= 4096) {
            classes.add("Magician");
            bitmask -= 4096;
        }
        if (bitmask >= 2048) {
            classes.add("Wizard");
            bitmask -= 2048;
        }
        if (bitmask >= 1024) {
            classes.add("Necromancer");
            bitmask -= 1024;
        }
        if (bitmask >= 512) {
            classes.add("Shaman");
            bitmask -= 512;
        }
        if (bitmask >= 256) {
            classes.add("Rogue");
            bitmask -= 256;
        }
        if (bitmask >= 128) {
            classes.add("Bard");
            bitmask -= 128;
        }
        if (bitmask >= 64) {
            classes.add("Monk");
            bitmask -= 64;
        }
        if (bitmask >= 32) {
            classes.add("Druid");
            bitmask -= 32;
        }
        if (bitmask >= 16) {
            classes.add("ShadowKnight");
            bitmask -= 16;
        }
        if (bitmask >= 8) {
            classes.add("Ranger");
            bitmask -= 8;
        }
        if (bitmask >= 4) {
            classes.add("Paladin");
            bitmask -= 4;
        }
        if (bitmask >= 2) {
            classes.add("Cleric");
            bitmask -= 2;
        }
        if (bitmask >= 1) {
            classes.add("Warrior");
            bitmask -= 1;
        }
    }

    /**
     * Prints the contents of the classes ArrayList
     */
    public void listClasses() {
        for (int i = 0; i < classes.size(); i++) {
            System.out.print(classes.get(i));
            if(i != classes.size() - 1) {
                System.out.print(", ");
            }
        }
    }
}
