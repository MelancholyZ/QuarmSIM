package model;

import controller.Controller;

/**
 * Mob class
 */
public class Mob {
    private int level, charSheetAC, itemAC = 0, spellAC = 0;
    private int MR, CR, DR, PR, FR, numEntriesInAttributes, bodyType;
    private String mobDBStart, mobName, mobsTable, mobAttributes;
    private int spellbonusesAGI = 0, itembonusesAGI = 0;

    /**
     * Default constructor for a mob
     */
    public Mob(String mobName) {
        setMobName(mobName);
        setMobDB();
        setMobsTable();
        setMobAttributes();
        setEntries();
        level = getIntElement(2);
        bodyType = getIntElement(5);
        MR = getIntElementReverseIndex(57);
        CR = getIntElementReverseIndex(56);
        DR = getIntElementReverseIndex(55);
        FR = getIntElementReverseIndex(54);
        PR = getIntElementReverseIndex(53);
        charSheetAC = getIntElementReverseIndex(49);

        //displayMobStats();
    }

    /**
     * Displays mob attributes
     */
    public void displayMobStats() {
        System.out.println("Name: " + mobName);
        System.out.println("Level: " + level);
        System.out.println("Mitigation: " + getMitigation());
        System.out.println("MR: " + MR + ", CR: " + CR + ", DR: " + DR + ", PR: " + PR + ", FR: " + FR);

    }

    /**
     * Gets the integer value at the index of Mob table
     * @param index index of element
     * @return int value at the index
     */
    public int getIntElement(int index) {
        String tempString, tempString2, tempString3;
        int endIndex;
        tempString = mobAttributes.substring(Controller.ordinalIndexOf(mobAttributes, ",", index));
        tempString2 = tempString.substring(1,100);
        endIndex = tempString2.indexOf(",");
        tempString3 = tempString2.substring(0, endIndex);
        return Integer.parseInt(tempString3);
    }

    /**
     * Gets the integer value at the index of Mob table
     * @param index index of element
     * @return int value at the index
     */
    public int getIntElementReverseIndex(int index) {
        String tempString, tempString2, tempString3;
        int endIndex;
        tempString = mobAttributes.substring(Controller.ordinalIndexOf(mobAttributes, ",", numEntriesInAttributes - index));
        tempString2 = tempString.substring(1,100);
        endIndex = tempString2.indexOf(",");
        tempString3 = tempString2.substring(0, endIndex);
        return Integer.parseInt(tempString3);
    }

    /**
     * Setter method to set the mob name
     * @param mobName the name of the mob to set
     */
    public void setMobName(String mobName) {
        this.mobName = mobName;
    }

    /**
     * Logic that governs mob mitigation
     * @return mob mitigation
     */
    public int getMitigation() {
        int mit;

        if (level < 15) {
            mit = level * 3;

            if (level < 3)
                mit += 2;
        } else {
            if (Controller.getExpansion().equals("PlanesEQ"))
                mit = 200;
            else
                mit = level * 41 / 10 - 15;
        }

        if (mit > 200)
            mit = 200;

        if (mit == 200 && charSheetAC > 200)
            mit = charSheetAC;

        mit += (4 * itemAC / 3) + (spellAC / 4);
        if (mit < 1)
            mit = 1;
        return mit;
    }

    /**
     * Setter method for shortening the table to the specified mob
     */
    public void setMobsTable() {
        mobDBStart = mobsTable.substring(mobsTable.indexOf(mobName));
    }

    /**
     * Setter method for setting the mob table
     */
    public void setMobDB() {
        mobsTable = Controller.grabTableInDB("npc_types", Controller.getDb());
    }

    /**
     * Setter method that gets the full segment of mob attributes for a single mob
     */
    public void setMobAttributes() {
        mobAttributes = mobDBStart.substring(0, mobDBStart.indexOf("),"));
    }

    /**
     * Getter method for mob magic resistance
     * @return mob magic resistance
     */
    public int getMR() {
        return MR;
    }

    /**
     * Getter method for mob cold resistance
     * @return mob cold resistance
     */
    public int getCR() {
        return CR;
    }

    /**
     * Getter method for mob disease resistance
     * @return mob disease resistance
     */
    public int getDR() {
        return DR;
    }

    /**
     * Getter method for mob poison resistance
     * @return mob poison resistance
     */
    public int getPR() {
        return PR;
    }

    /**
     * Getter method for mob fire resistance
     * @return mob fire resistance
     */
    public int getFR() {
        return FR;
    }

    /**
     * Setter method for the number of attributes of a mob
     */
    public void setEntries() {
        numEntriesInAttributes = Controller.numCommas(mobAttributes);
    }

    /**
     * Getter method for body type of mob
     * @return the body type of the mob
     */
    public int getBodyType() {
        return bodyType;
    }

    /**
     * Checks if successful hit
     * @param cs character sheet
     * @param skillInUse melee skill in use
     * @return true if hit
     */
    public boolean avoidanceCheck(CharacterSheet cs, String skillInUse) {

        int toHit = cs.getToHit(skillInUse);
        int avoidance = getAvoidance();
        int toHitPct = 0;
        int avoidancePct = 0;
        // Hit Chance percent modifier
        // Disciplines: Evasive, Precision, Deadeye, Trueshot, Charge
        // TODO Buffs: the Eagle Eye line 1: Effect type: Increase Chance to Hit with Archery by 40
//        toHitPct = attacker->itembonuses.HitChanceEffect[skillInUse] +
//                attacker->spellbonuses.HitChanceEffect[skillInUse] +
//                        attacker->aabonuses.HitChanceEffect[skillInUse] +
//                                attacker->itembonuses.HitChanceEffect[EQ::skills::HIGHEST_SKILL + 1] +
//            attacker->spellbonuses.HitChanceEffect[EQ::skills::HIGHEST_SKILL + 1] +
//            attacker->aabonuses.HitChanceEffect[EQ::skills::HIGHEST_SKILL + 1];
        if(skillInUse.equals("Archery")) {
            toHitPct = 40;
        }

        // Avoidance chance percent modifier
        // TODO: Disciplines: Evasive, Precision, Voiddance, Fortitude
        //avoidancePct = defender->spellbonuses.AvoidMeleeChanceEffect + defender->itembonuses.AvoidMeleeChanceEffect;

        toHit = toHit * (100 + toHitPct) / 100;
        avoidance = avoidance * (100 + avoidancePct) / 100;

        double hitChance;
        toHit += 10;
        avoidance += 10;

        if (toHit * 1.21 > avoidance) {
            hitChance = 1.0 - avoidance / (toHit * 1.21 * 2.0);
        }
        else {
            hitChance = toHit * 1.21 / (avoidance * 2.0);
        }
        return Controller.random((float) hitChance);
    }

    /**
     * Calculates mob avoidance
     * @return mob avoidance
     */
    public int getAvoidance() {
        int avoidance = level * 9 + 5;

        if (level <= 50 && avoidance > 400)
            avoidance = 400;
        else if (avoidance > 460)
            avoidance = 460;

        avoidance += (spellbonusesAGI + itembonusesAGI) * 22 / 100;
        //avoidance += bonusAvoidance; ?????????????????????
        if (avoidance < 1)
            avoidance = 1;

        return avoidance;
    }

    /**
     * Getter method to get mob level
     * @return mob level
     */
    public int getLevel() {
        return level;
    }
}
