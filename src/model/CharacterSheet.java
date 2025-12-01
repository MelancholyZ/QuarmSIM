package model;

import controller.Controller;
import controller.FileHandling;

/**
 * Character sheet class
 */
public class CharacterSheet {
    private static int level;
    private int classID;
    private static String characterClass;
    private int oneHandedBlunt, oneHandedSlashing, piercing, handToHand;
    private int twoHandedBlunt, twoHandedSlashing;
    private int abjuration, alteration, channeling, conjuration, divination, evocation, meditate;
    private int archery, backstab, bash, kick, throwing;
    private int defense, block, dodge, parry, riposte;
    private int doubleAttack, dualWieldSkill, offensePrimary, offenseSecondary, offenseSpecial, offenseRanged, offense;
    private int flyingKick, eagleStrike, tigerClaw, dragonPunch, roundKick;
    private int specializeAbjure, specializeAlteration, specializeConjuration, specializeDivination, specializeEvocation;
    private int singing, stringed, percussion, wind, brass;
    private int spellAttack;
    private int wornAttack;
    private static int haste;
    private int damageBonus;
    private int arrowDamage;
    private String skillIDs, skillCaps, classIDs, specialType, arrow;
    private int strength;
    private static int dexterity;
    private int agility;
    private int wisdom;
    private static int intelligence;
    private int charisma;
    private int stamina;
    private static Weapon primary;
    private Weapon secondary;
    private Weapon ranged;
    private String primaryWeapon, secondaryWeapon, rangedWeapon, special;
    private SpecialAttack specialAttack, rangedAttack;
    private int primarySkillCap, secondarySkillCap, specialSkillCap;
    private double archerySkillMod;
    private static double backstabSkillMod;
    private double amMultiplier;
    private float dualWieldChance;
    private boolean berserk;
    private int combatFury, flurry = 0, ambidexterity = 0;

    /**
     * Character sheet constructor
     * @param level level of the character
     * @param characterClass class of the character
     * @param primaryWeapon primary weapon
     * @param secondaryWeapon secondary weapon
     * @param rangedWeapon ranged weapon
     * @param special special attack
     * @param spellAttack attack from buff
     * @param wornAttack attack from items
     * @param haste total haste
     * @param stats stat totals
     * @param ambidexterity DW AA
     * @param am archery mastery AA and rooted damage modifier in one
     * @param cf combat fury AA
     * @param archerySkillMod skill mod for archery
     * @param backstabSkillMod skill mod for backstab
     * @param arrowDamage physical damage of the arrow
     * @param arrowElementalDamage elemental damage of the arrow
     * @param arrowElementalType elemental type of the arrow
     * @param berserk true if player is berserk
     * @param flurry warrior flurry AA
     * @param fh the fileHandler object
     */
    public CharacterSheet(int level, String characterClass, String primaryWeapon, String secondaryWeapon,
                          String rangedWeapon, String special, int spellAttack, int wornAttack, int haste,
                          int stats, int ambidexterity, double am, int cf, double archerySkillMod, double backstabSkillMod,
                          int arrowDamage, int arrowElementalDamage, int arrowElementalType, boolean berserk, int flurry,
                          FileHandling fh) {

        this.primaryWeapon = primaryWeapon;
        this.secondaryWeapon = secondaryWeapon;
        this.rangedWeapon = rangedWeapon;
        this.special = special;
        this.archerySkillMod = archerySkillMod;
        this.backstabSkillMod = backstabSkillMod;
        setLevel(level);
        setCharacterClass(characterClass);
        getSkillTables();
        setClassID(getClassID(characterClass));
        setStats(stats);
        setAmbidexterityAA(ambidexterity);
        setArcheryMastery(am);
        setDualWield();
        setDualWieldChance();
        setDoubleAttack();
        setDefense();
        setOffense();
        setHaste(haste);
        setWornAttack(wornAttack);
        setSpellAttack(spellAttack);
        setCombatFury(cf);

        if(characterClass.equals("Warrior")) {
            setFlurry(flurry);
            setBerserk(berserk);
        }

        if (!primaryWeapon.equals("none")) {
            primary = new Weapon(primaryWeapon, level, "primary", dualWieldChance);
            setPrimarySkillCap();
            setOffensePrimary();
            setDamageBonus();
            //displayWeaponStats(primary,fh, true);
        }
        if (!secondaryWeapon.equals("none")) {
            secondary = new Weapon(secondaryWeapon, level, "secondary", dualWieldChance);
            setSecondarySkillCap();
            setOffenseSecondary();
            //displayWeaponStats(secondary, fh, false);
        }
        if (!rangedWeapon.equals("none")) {
            ranged = new Weapon(rangedWeapon, level, "ranged", dualWieldChance);
            setArchery();
            rangedAttack = new SpecialAttack(special, level, ranged, archery,
                    arrowDamage, arrowElementalDamage, arrowElementalType);
            setOffenseRanged();
            //displayWeaponStats(ranged, fh, false);
        }
        if (!special.equals("none")) {

            setSpecialSkillCap(special);
            specialAttack = new SpecialAttack(special, level, specialSkillCap);
            specialType = special;
            setSpecial(special);
            setOffenseSpecial();
        }
        //printCharacterSheet(fh);
    }

    /**
     * Setter method for archery mastery multiplier
     * @param archeryMasteryMultiplier archery mastery multiplier
     */
    public void setArcheryMastery(double archeryMasteryMultiplier) {
        amMultiplier = archeryMasteryMultiplier;
    }

    /**
     * Getter method for archery mastery damage multiplier
     * @return archery mastery damage multiplier
     */
    public double getAMMultiplier() {
        return amMultiplier;
    }

    /**
     * Setter method for the Ambidexterity AA
     * @param dwSkillFromAA adds this amount to DW skill
     */
    public void setAmbidexterityAA(int dwSkillFromAA) {
        ambidexterity = dwSkillFromAA;
    }

    /**
     * Display character sheet information
     * @param fh the fileHandling object
     */
    public void printCharacterSheet(FileHandling fh) {
        System.out.println("Level: " + getLevel());
        System.out.println("Class: " + getCharacterClass());
        if (!primaryWeapon.equals("none")) {
            System.out.print("Primary Skill: " + primary.getWeaponType());
            fh.writeToFile("resources/ParseOutput", "Primary Skill: ");
            fh.writeToFile("resources/ParseOutput", primary.getWeaponType());
            System.out.println(", cap: " + primarySkillCap);
            fh.writeToFile("resources/ParseOutput", ", cap: ");
            fh.writeToFile("resources/ParseOutput", Integer.toString(primarySkillCap));
            fh.writeToFile("resources/ParseOutput", "\n");
        }
        if (!secondaryWeapon.equals("none")) {
            System.out.print("Secondary Skill: " + secondary.getWeaponType());
            fh.writeToFile("resources/ParseOutput", "Secondary Skill: ");
            fh.writeToFile("resources/ParseOutput", secondary.getWeaponType());
            System.out.println(", cap: " + secondarySkillCap);
            fh.writeToFile("resources/ParseOutput", ", cap: ");
            fh.writeToFile("resources/ParseOutput", Integer.toString(secondarySkillCap));
            fh.writeToFile("resources/ParseOutput", "\n");
        }
        if (!special.equals("none")) {
            System.out.print("Special Skill: " + special);
            fh.writeToFile("resources/ParseOutput", "Special Skill: ");
            fh.writeToFile("resources/ParseOutput", special);
            System.out.println(", cap: " + specialSkillCap);
            fh.writeToFile("resources/ParseOutput", ", cap: ");
            if (special.equals("Backstab"))
                fh.writeToFile("resources/ParseOutput", Integer.toString((int)(specialSkillCap * backstabSkillMod)));
            if (special.equals("Archery"))
                fh.writeToFile("resources/ParseOutput", Integer.toString((int)(specialSkillCap * archerySkillMod)));
            fh.writeToFile("resources/ParseOutput", "\n");
        }
        if (!rangedWeapon.equals("none")) {
            System.out.print("Ranged Skill: " + ranged.getWeaponType());
            fh.writeToFile("resources/ParseOutput", "Ranged Skill: ");
            fh.writeToFile("resources/ParseOutput", ranged.getWeaponType());
            System.out.println(", cap: " + archery);
            fh.writeToFile("resources/ParseOutput", ", cap: ");
            fh.writeToFile("resources/ParseOutput", Integer.toString(archery));
            fh.writeToFile("resources/ParseOutput", "\n");
        }
        System.out.println("Defense: " + getDefense());
        fh.writeToFile("resources/ParseOutput", "Defense: ");
        fh.writeToFile("resources/ParseOutput", Integer.toString(getDefense()));
        fh.writeToFile("resources/ParseOutput", "\n");
        System.out.println("DW: " + getDualWield());
        fh.writeToFile("resources/ParseOutput", "DW: ");
        fh.writeToFile("resources/ParseOutput", Integer.toString(getDualWield()));
        fh.writeToFile("resources/ParseOutput", "\n");
        System.out.println("DA: " + getDoubleAttack());
        fh.writeToFile("resources/ParseOutput", "DA: ");
        fh.writeToFile("resources/ParseOutput", Integer.toString(getDoubleAttack()));
        fh.writeToFile("resources/ParseOutput", "\n");
        System.out.println("Haste: " + getHaste() + "%");
        fh.writeToFile("resources/ParseOutput", "Haste: ");
        fh.writeToFile("resources/ParseOutput", Integer.toString(getHaste()));
        fh.writeToFile("resources/ParseOutput", "%\n");
        System.out.println("Attack: " + (getWornAttack() + getSpellAttack()));
        fh.writeToFile("resources/ParseOutput", "Attack: ");
        fh.writeToFile("resources/ParseOutput", Integer.toString(getWornAttack() + getSpellAttack()));
        fh.writeToFile("resources/ParseOutput", "\n");
    }

    /**
     * Populates the skillIDs and skillCaps with tables
     * from the db.
     */
    public void getSkillTables() {
        skillIDs = Controller.grabTableInDB("skill_difficulty", Controller.getDb());
        skillCaps = Controller.grabTableInDB("skill_caps", Controller.getDb());
        classIDs = Controller.grabTableInDB("classID", Controller.getDb());
    }

    /**
     * Reads the db string and finds the skill id
     * @param skillName the name of the skill
     * @return the id number of the skill
     */
    public int getSkillID(String skillName) {
        int indexStart, indexStart1, indexEnd;
        String tempString, tempString2;

        indexStart = skillIDs.indexOf(skillName) - 14;
        tempString = skillIDs.substring(indexStart, indexStart + 7);
        indexStart1 = tempString.indexOf("(") + 1;
        tempString2 = tempString.substring(indexStart1);
        indexEnd = tempString2.indexOf(",");
        tempString2 = tempString2.substring(0, indexEnd);

        return Integer.parseInt(tempString2);
    }

    /**
     * Takes the skill ID and looks up the cap
     * @param skillID the ID of the skill
     * @return the corresponding skill cap
     */
    public int getSkillCap(int skillID) {
        int indexStart, indexEnd, length;
        String levelString, classString, skillIDString, tempString;

        indexStart = skillCaps.indexOf(skillID + "," + classID + "," + getLevel());
        levelString = Integer.toString(getLevel());
        classString = Integer.toString(classID);
        skillIDString = Integer.toString(skillID);
        length = skillIDString.length() + 1 + classString.length() + 1 + levelString.length() + 1;
        tempString = skillCaps.substring(indexStart + length, indexStart + length + 5);
        indexEnd = tempString.indexOf(",");

        return Integer.parseInt(tempString.substring(0, indexEnd));
    }

    /**
     * This takes the class name and converts it to the
     * integer that represents the class
     * @param className the name of the class
     * @return the integer representing the class
     */
    public int getClassID(String className) {
        int indexStart, indexEnd;
        String tempString;

        indexStart = classIDs.indexOf(className);
        tempString = classIDs.substring(indexStart);
        indexEnd = tempString.indexOf(",");

        return Integer.parseInt(tempString.substring(className.length() + 1, indexEnd));
    }

    /**
     * Getter method for primary weapon
     * @return the primary weapon
     */
    public static Weapon getPrimary() {
        return primary;
    }

    /**
     * Getter method for secondary weapon
     * @return the secondary weapon
     */
    public Weapon getSecondary() {
       return secondary;
    }

    /**
     * Setter method that sets the level
     * @param level character level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Getter method that gets character level
     * @return character level
     */
    public static int getLevel() {
        return level;
    }

    /**
     * Setter method that sets character class
     * @param className the class of the character
     */
    public void setCharacterClass(String className) {
        characterClass = className;
    }

    /**
     * Getter method that gets the character class
     * @return the class of the character
     */
    public static String getCharacterClass() {
        return characterClass;
    }

    /**
     * Getter method for 1hb skill cap
     * @return 1hb skill cap
     */
    public int getOneHandedBlunt() {
        return oneHandedBlunt;
    }

    /**
     * Setter method for 1hb skill
     */
    public void setOneHandedBlunt() {
        oneHandedBlunt = getSkillCap(getSkillID("1HBlunt"));
    }

    /**
     * Getter method for 1h pierce skill cap
     * @return 1h pierce skill cap
     */
    public int getPiercing() {
        return piercing;
    }

    /**
     * Setter method for 1h pierce skill cap
     */
    public void setPiercing() {
        piercing = getSkillCap(getSkillID("1HPiercing"));
    }

    /**
     * Getter method for 1h slashing cap
     * @return 1h slashing skill cap
     */
    public int getOneHandedSlashing() {
        return oneHandedSlashing;
    }

    /**
     * Setter method for 1h slashing skill cap
     */
    public void setOneHandedSlashing() {
        oneHandedSlashing = getSkillCap(getSkillID("1HSlashing"));
    }

    /**
     * Getter method for 2h blunt skill cap
     * @return 2h blunt skill cap
     */
    public int getTwoHandedBlunt() {
        return twoHandedBlunt;
    }

    /**
     * Setter method for 2h blunt skill cap
     */
    public void setTwoHandedBlunt() {
        twoHandedBlunt = getSkillCap(getSkillID("2HBlunt"));
    }

    /**
     * Getter method for 2h slashing skill cap
     * @return 2h slashing skill cap
     */
    public int getTwoHandedSlashing() {
        return twoHandedSlashing;
    }

    /**
     * Setter method for 2h slashing skill cap
     */
    public void setTwoHandedSlashing() {
        twoHandedSlashing = getSkillCap(getSkillID("2HSlashing"));
    }

    /**
     * Getter method for abjuration skill cap
     * @return abjuration skill cap
     */
    public int getAbjuration() {
        return abjuration;
    }

    /**
     * Setter method for abjuration skill cap
     */
    public void setAbjuration() {
        abjuration = getSkillCap(getSkillID("Abjuration"));
    }

    /**
     * Getter method for alteration skill cap
     * @return alteration skill cap
     */
    public int getAlteration() {
        return alteration;
    }

    /**
     * Setter method for alteration skill cap
     */
    public void setAlteration() {
        alteration = getSkillCap(getSkillID("Alteration"));
    }

    /**
     * Getter method for archery skill cap
     * @return archery skill cap
     */
    public int getArchery() {
        return archery;
    }

    /**
     * Setter method for archery skill cap
     */
    public void setArchery() {
        archery = (int)(getSkillCap(getSkillID("Archery")) * archerySkillMod);
    }

    /**
     * Getter method for backstab skill cap
     * @return backstab skill cap
     */
    public int getBackstab() {
        return backstab;
    }

    /**
     * Setter method for backstab skill cap
     */
    public void setBackstab() {
        backstab = (int)(getSkillCap(getSkillID("Backstab")) * backstabSkillMod);
    }

    /**
     * Getter method for bash skill cap
     * @return bash skill cap
     */
    public int getBash() {
        return bash;
    }

    /**
     * Setter method for bash skill cap
     */
    public void setBash() {
        bash = getSkillCap(getSkillID("Bash"));
    }

    /**
     * Getter method for block skill cap
     * @return block skill cap
     */
    public int getBlock() {
        return block;
    }

    /**
     * Setter method for block skillcap
     */
    public void setBlock() {
        block = getSkillCap(getSkillID("Block"));
    }

    /**
     * Getter method for brass skill cap
     * @return brass skill cap
     */
    public int getBrass() {
        return brass;
    }

    /**
     * Setter method for brass skill cap
     */
    public void setBrass() {
        brass = getSkillCap(getSkillID("Brass"));
    }

    /**
     * Getter method for channeling skill cap
     * @return channeling skill cap
     */
    public int getChanneling() {
        return channeling;
    }

    /**
     * Setter method for channeling skill cap
     */
    public void setChanneling() {
        channeling = getSkillCap(getSkillID("Channeling"));
    }

    /**
     * Getter method for conjuration skill cap
     * @return conjuration skill cap
     */
    public int getConjuration() {
        return conjuration;
    }

    /**
     * Setter method for conjuration skill cap
     */
    public void setConjuration() {
        conjuration = getSkillCap(getSkillID("Conjuration"));
    }

    /**
     * Getter method for defense skillcap
     * @return defense skill cap
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Setter method for defense skill cap
     */
    public void setDefense() {
        defense = getSkillCap(getSkillID("Defense"));
    }

    /**
     * Getter method for divination skill cap
     * @return divination skill cap
     */
    public int getDivination() {
        return divination;
    }

    /**
     * Setter method for divination skill cap
     */
    public void setDivination() {
        divination = getSkillCap(getSkillID("Divination"));
    }

    /**
     * Getter method for dodge skill cap
     * @return dodge skill cap
     */
    public int getDodge() {
        return dodge;
    }

    /**
     * Setter method for dodge skill cap
     */
    public void setDodge() {
        dodge = getSkillCap(getSkillID("Dodge"));
    }

    /**
     * Getter method for double attack skill cap
     * @return double attack skill cap
     */
    public int getDoubleAttack() {
        return doubleAttack;
    }

    /**
     * Setter method for double attack skill cap
     */
    public void setDoubleAttack() {
        doubleAttack = getSkillCap(getSkillID("DoubleAttack"));
    }

    /**
     * Getter method for dragon punch skill cap
     * @return dragon punch skill cap
     */
    public int getDragonPunch() {
        return dragonPunch;
    }

    /**
     * Setter method for dragon punch skill cap
     */
    public void setDragonPunch() {
        dragonPunch = getSkillCap(getSkillID("DragonPunch"));
    }

    /**
     * Getter method for dual wield skill cap
     * @return dual wield skill cap
     */
    public int getDualWield() {
        return dualWieldSkill;
    }

    /**
     * Setter method for dual wield skill cap
     */
    public void setDualWield() {
        if (characterClass.equals("Warrior") || characterClass.equals("Monk")
            || characterClass.equals("Ranger") || characterClass.equals("Bard")
            || characterClass.equals("Beastlord") || characterClass.equals("Rogue"))
            dualWieldSkill = getSkillCap(getSkillID("DualWield")) + ambidexterity;
    }

    /**
     * Getter method for eagle strike skill cap
     * @return eagle strike skill cap
     */
    public int getEagleStrike() {
        return eagleStrike;
    }

    /**
     * Setter method for eagle strike skill cap
     */
    public void setEagleStrike() {
        eagleStrike = getSkillCap(getSkillID("EagleStrike"));
    }

    /**
     * Getter method for evocation skill cap
     * @return evocation skill cap
     */
    public int getEvocation() {
        return evocation;
    }

    /**
     * Setter method for evocation skill cap
     */
    public void setEvocation() {
        evocation = getSkillCap(getSkillID("Evocation"));
    }

    /**
     * Getter method for flying kick skill cap
     * @return flying kick skill cap
     */
    public int getFlyingKick() {
        return flyingKick;
    }

    /**
     * Setter method for flying kick skill cap
     */
    public void setFlyingKick() {
        flyingKick = getSkillCap(getSkillID("FlyingKick"));
    }

    /**
     * Getter method for hand-to-hand skill cap
     * @return hand-to-hand skill cap
     */
    public int getHandToHand() {
        return handToHand;
    }

    /**
     * Setter method for hand-to-hand skill cap
     */
    public void setHandToHand() {
        handToHand = getSkillCap(getSkillID("HandtoHand"));
    }

    /**
     * Getter method for kick skill cap
     * @return kick skill cap
     */
    public int getKick() {
        return kick;
    }

    /**
     * Setter method for kick skill cap
     */
    public void setKick() {
        kick = getSkillCap(getSkillID("Kick"));
    }

    /**
     * Getter method for meditate skill cap
     * @return meditate skill cap
     */
    public int getMeditate() {
        return meditate;
    }

    /**
     * Setter method for meditate skill cap
     */
    public void setMeditate() {
        meditate = getSkillCap(getSkillID("Meditate"));
    }

    /**
     * Getter method for primary offense
     * @return primary offense
     */
    public int getOffensePrimary() {
        return offensePrimary;
    }

    /**
     * Getter method for secondary offense
     * @return secondary offense
     */
    public int getOffenseSecondary() {
        return offenseSecondary;
    }

    /**
     * Getter method for special offense
     * @return special offense
     */
    public int getOffenseSpecial() {
        return offenseSpecial;
    }

    /**
     * Setter method for player primary hand offense
     */
    public void setOffensePrimary() {
        offensePrimary = primarySkillCap + spellAttack + wornAttack;

        if(strength >= 75)
            offensePrimary += ((2 * strength - 150) / 3);
        if (offensePrimary < 1)
            offensePrimary = 1;

        if (characterClass.equals("Ranger") && level > 54) {
            offensePrimary += level * 4 - 216;
        }
    }

    /**
     * Setter method for player secondary hand offense
     */
    public void setOffenseSecondary() {
        offenseSecondary = secondarySkillCap + spellAttack + wornAttack;

        if(strength >= 75)
            offenseSecondary += ((2 * strength - 150) / 3);
        if (offenseSecondary < 1)
            offenseSecondary = 1;

        if (characterClass.equals("Ranger") && level > 54) {
            offenseSecondary += level * 4 - 216;
        }
    }

    /**
     * Setter method for player special offense
     */
    public void setOffenseSpecial() {
        int statBonus = strength;

        offenseSpecial = specialSkillCap + spellAttack + wornAttack;
        if(statBonus >= 75)
            offenseSpecial += ((2 * statBonus - 150) / 3);
        if (offenseSpecial < 1)
            offenseSpecial = 1;

        if (characterClass.equals("Ranger") && level > 54) {
            offenseSpecial += level * 4 - 216;
        }
    }

    /**
     * Setter method for player special offense
     */
    public void setOffenseRanged() {
        int statBonus;

        if (ranged.getWeaponType().equals("Archery") || (ranged.getWeaponType().equals("Throwing"))) {
            statBonus = dexterity;
        } else {
            statBonus = strength;
        }

        offenseRanged = archery + spellAttack + wornAttack;
        if(statBonus >= 75)
            offenseRanged += ((2 * statBonus - 150) / 3);
        if (offenseRanged < 1)
            offenseRanged = 1;

        if (characterClass.equals("Ranger") && level > 54) {
            offenseRanged += level * 4 - 216;
        }
    }

    /**
     * Getter method for ranged offense
     * @return ranged offense
     */
    public int getOffenseRanged() {
        return offenseRanged;
    }

    /**
     * Getter method for parry skill cap
     * @return parry skill cap
     */
    public int getParry() {
        return parry;
    }

    /**
     * Setter method for parry skill cap
     */
    public void setParry() {
        parry = getSkillCap(getSkillID("Parry"));
    }

    /**
     * Getter method for percussion skill cap
     * @return percussion skill cap
     */
    public int getPercussion() {
        return percussion;
    }

    /**
     * Setter method for percussion skill cap
     */
    public void setPercussion() {
        percussion = getSkillCap(getSkillID("Percussion"));
    }

    /**
     * Getter method for riposte skill cap
     * @return riposte skill cap
     */
    public int getRiposte() {
        return riposte;
    }

    /**
     * Setter method for riposte skill cap
     */
    public void setRiposte() {
        riposte = getSkillCap(getSkillID("Riposte"));
    }

    /**
     * Getter method for round kick skill cap
     * @return round kick skill cap
     */
    public int getRoundKick() {
        return roundKick;
    }

    /**
     * Setter method for round kick skill cap
     */
    public void setRoundKick() {
        roundKick = getSkillCap(getSkillID("RoundKick"));
    }

    /**
     * Getter method for singing skill cap
     * @return singing skill cap
     */
    public int getSinging() {
        return singing;
    }

    /**
     * Setter method for singing skill cap
     */
    public void setSinging() {
        singing = getSkillCap(getSkillID("Singing"));
    }

    /**
     * Getter method for specialize abjuration skill cap
     * @return specialize abjuration skill cap
     */
    public int getSpecializeAbjuration() {
        return specializeAbjure;
    }

    /**
     * setter method for specialize abjuration skill cap
     */
    public void setSpecializeAbjuration() {
        specializeAbjure = getSkillCap(getSkillID("specializeAbjure"));
    }

    /**
     * Getter method for specialize alteration skill cap
     * @return specialize alteration skill cap
     */
    public int getSpecializeAlteration() {
        return specializeAlteration;
    }

    /**
     * Setter method for specialize alteration skill cap
     */
    public void setSpecializeAlteration() {
        specializeAlteration = getSkillCap(getSkillID("specializeAlteration"));
    }

    /**
     * Getter method for specialize conjuration skill cap
     * @return specialize conjuration skill cap
     */
    public int getSpecializeConjuration() {
        return specializeConjuration;
    }

    /**
     * Setter method for specialize conjuration skill cap
     */
    public void setSpecializeConjuration() {
        specializeConjuration = getSkillCap(getSkillID("specializeConjuration"));
    }

    /**
     * Getter method for specialize divination skill cap
     * @return specialize divination skill cap
     */
    public int getSpecializeDivination() {
        return specializeDivination;
    }

    /**
     * Setter method for specialize divination skill cap
     */
    public void setSpecializeDivination() {
        specializeDivination = getSkillCap(getSkillID("specializeDivination"));
    }

    /**
     * Getter method for specialize evocation skill cap
     * @return specialize evocation skill cap
     */
    public int getSpecializeEvocation() {
        return specializeEvocation;
    }

    /**
     * Setter method for specialize evocation skill cap
     */
    public void setSpecializeEvocation() {
        specializeEvocation = getSkillCap(getSkillID("specializeEvocation"));
    }

    /**
     * Getter method for stringed skill cap
     * @return stringed skill cap
     */
    public int getStringed() {
        return stringed;
    }

    /**
     * Setter method for stringed skill cap
     */
    public void setStringed() {
        stringed = getSkillCap(getSkillID("Stringed"));
    }

    /**
     * Getter method for throwing skill cap
     * @return throwing skill cap
     */
    public int getThrowing() {
        return throwing;
    }

    /**
     * Setter method for throwing skill cap
     */
    public void setThrowing() {
        throwing = getSkillCap(getSkillID("Throwing"));
    }

    /**
     * Getter method for tiger claw skill cap
     * @return tiger claw skill cap
     */
    public int getTigerClaw() {
        return tigerClaw;
    }

    /**
     * Setter method for tiger claw skill cap
     */
    public void setTigerClaw() {
        tigerClaw = getSkillCap(getSkillID("TigerClaw"));
    }

    /**
     * Getter method for wind skill cap
     * @return wind skill cap
     */
    public int getWind() {
        return wind;
    }

    /**
     * Setter method for wind skill cap
     */
    public void setWind() {
        this.wind = getSkillCap(getSkillID("Wind"));
    }

    /**
     * Getter method for class ID as an integer
     * @return class ID as an integer
     */
    public int getClassID() {
        return classID;
    }

    /**
     * Setter class that sets class number
     * @param classID class number
     */
    public void setClassID(int classID) {
        this.classID = classID;
    }

    /**
     * Getter method for spell attack
     * @return spell attack
     */
    public int getSpellAttack() {
        return spellAttack;
    }

    /**
     * Setter method for spell attack
     * @param spellAttack attack from spells
     */
    public void setSpellAttack(int spellAttack) {
        this.spellAttack = spellAttack;
    }

    /**
     * Getter method for worn attack
     * @return total worn attack
     */
    public int getWornAttack() {
        return wornAttack;
    }

    /**
     * Setter method for worn attack
     * @param wornAttack total worn attack
     */
    public void setWornAttack(int wornAttack) {
        if (wornAttack > 250)
            wornAttack = 250;
        this.wornAttack = wornAttack;
    }

    /**
     * Getter method for haste
     * @return total haste amount
     */
    public static int getHaste() {
        return haste;
    }

    /**
     * Setter method for total haste
     * @param haste total haste amount
     */
    public void setHaste(int haste) {
        this.haste = haste;
    }

    /**
     * Method to display weapon stats
     * @param weapon the weapon to inspect
     * @param fh the fileHandler object
     * @param primary true if primary handed weapon
     */
    public void displayWeaponStats(Weapon weapon, FileHandling fh, boolean primary) {
        ClassBitmask cbm = new ClassBitmask(weapon.getClasses());
        String weaponName =  weapon.getWeaponName();
        System.out.print("Name: ");
        for(int i = 0; i < weaponName.length(); i++) {
            if(weaponName.charAt(i) != '\\') {
                System.out.print(weaponName.charAt(i));
                fh.writeToFile("resources/ParseOutput", Character.toString(weaponName.charAt(i)));
            }
        }
        System.out.print("\n");
        fh.writeToFile("resources/ParseOutput", "\n");
        System.out.println("Type: " + weapon.getWeaponType());
        fh.writeToFile("resources/ParseOutput", "Type: ");
        fh.writeToFile("resources/ParseOutput", weapon.getWeaponType());
        fh.writeToFile("resources/ParseOutput", "\n");
        System.out.println("Damage: " + weapon.getDamage());
        fh.writeToFile("resources/ParseOutput", "Damage: ");
        fh.writeToFile("resources/ParseOutput", Integer.toString(weapon.getDamage()));
        fh.writeToFile("resources/ParseOutput", "\n");
        System.out.println("Delay: " + weapon.getDelay());
        fh.writeToFile("resources/ParseOutput", "Delay: ");
        fh.writeToFile("resources/ParseOutput", Integer.toString(weapon.getDelay()));
        fh.writeToFile("resources/ParseOutput", "\n");
        if (primary) {
            System.out.println("Damage bonus: " + getDamageBonus());
            fh.writeToFile("resources/ParseOutput", "Damage bonus: ");
            fh.writeToFile("resources/ParseOutput", Integer.toString(getDamageBonus()));
            fh.writeToFile("resources/ParseOutput", "\n");
            System.out.print("Classes: ");
        }
        cbm.listClasses();
        System.out.println("\nProc Rate: " + weapon.getProcRate());
        fh.writeToFile("resources/ParseOutput", "Proc Rate: ");
        fh.writeToFile("resources/ParseOutput", Integer.toString(weapon.getProcRate()));
        fh.writeToFile("resources/ParseOutput", "\n");
    }

    /**
     * Getter method for the weapon skill of the specified type
     * @param weaponType weapon skill to be set
     * @return the skill cap for the specified weapon
     */
    public int getWeaponSkill(String weaponType) {

        switch(weaponType) {
            case "1HBlunt":
                return getOneHandedBlunt();
            case "2HBlunt":
                return getTwoHandedBlunt();
            case "1HPiercing":
                return getPiercing();
            case "1HSlashing":
                return getOneHandedSlashing();
            case "2HSlashing":
                return getTwoHandedSlashing();
            case "HandtoHand":
                return getHandToHand();
            case "Archery":
                return getArchery();
        }
        return 0;
    }

    /**
     * Sets the skill of the specified special attack type
     * @param specialAttack name of special attack
     */
    private void setSpecial(String specialAttack) {
        switch(specialAttack) {
            case "Backstab":
                setBackstab();
                break;
            case "Kick":
                setKick();
                break;
            case "FlyingKick" :
                setFlyingKick();
                break;
            case "DragonPunch" :
                setDragonPunch();
                break;
            case "TigerClaw" :
                setTigerClaw();
                break;
            case "RoundKick":
                setRoundKick();
                break;
            case "EagleStrike":
                setEagleStrike();
                break;
            case "Bash":
                setBash();
                break;
        }
    }

    /**
     * Getter method for strength
     * @return strength stat
     */
    public int getStrength() {
        return strength;
    }

    /**
     * Setter method for strength
     * @param strength strength stat
     */
    public void setStrength(int strength) {
        this.strength = strength;
    }

    /**
     * Getter method for dexterity
     * @return dexterity stat
     */
    public static int getDexterity() {
        return dexterity;
    }

    /**
     * Setter method for dexterity stat
     * @param dexterity dexterity stat
     */
    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    /**
     * Getter method for agility stat
     * @return agility stat
     */
    public int getAgility() {
        return agility;
    }

    /**
     * Setter method for agility stat
     * @param agility agility stat
     */
    public void setAgility(int agility) {
        this.agility = agility;
    }

    /**
     * Getter method for wisdom stat
     * @return wisdom stat
     */
    public int getWisdom() {
        return wisdom;
    }

    /**
     * Setter method for wisdom stat
     * @param wisdom wisdom stat
     */
    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    /**
     * Getter method for intelligence stat
     * @return intelligence sat
     */
    public static int getIntelligence() {
        return intelligence;
    }

    /**
     * Setter method for intelligence stat
     * @param intelligence intelligence stat
     */
    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    /**
     * Getter method for charisma stat
     * @return charisma stat
     */
    public int getCharisma() {
        return charisma;
    }

    /**
     * Setter method for charisma stat
     * @param charisma charisma stat
     */
    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    /**
     * Getter method for stamina stat
     * @return stamina stat
     */
    public int getStamina() {
        return stamina;
    }

    /**
     * Setter method for stamina stat
     * @param stamina stamina stat
     */
    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    /**
     * Setter method to set attributes
     * @param score the amount of the attribute to set
     */
    public void setStats(int score) {
        strength = score;
        stamina = score;
        agility = score;
        charisma = score;
        dexterity = score;
        intelligence = score;
        wisdom = score;

    }

    /**
     * Getter method to get the primary weapon skill cap
     * @return primary weapon skill cap
     */
    public int getPrimarySkillCap() {
        return primarySkillCap;
    }

    /**
     * Setter method for primary skill cap
     */
    public void setPrimarySkillCap() {
        this.primarySkillCap = getSkillCap(getWeaponSkill(primary.getWeaponType()));
    }

    /**
     * Getter method for secondary skill cap
     * @return secondary skill cap
     */
    public int getSecondarySkillCap() {
        return secondarySkillCap;
    }

    /**
     * Setter method for secondary skill cap
     */
    public void setSecondarySkillCap() {
        this.secondarySkillCap = getSkillCap(getWeaponSkill(secondary.getWeaponType()));
    }

    /**
     * Getter method for special attack skill cap
     * @return special attack skill cap
     */
    public int getSpecialSkillCap() {
        return specialSkillCap;
    }

    /**
     * Setter method for special attack skill cap
     * @param special the amount of skill to set the skill cap to
     */
    public void setSpecialSkillCap(String special) {
        this.specialSkillCap = getSkillCap(getSkillID(special));
    }

    /**
     * Getter method for special attack skill
     * @return the special attack
     */
    public SpecialAttack getSpecialAttack() {
        return specialAttack;
    }

    /**
     * Getter method for offense skill
     * @return offense skill
     */
    public int getOffense() {
        return offense;
    }

    /**
     * Setter method for offense skill
     */
    public void setOffense() {
        offense = getSkillCap(getSkillID("Offense"));
    }

    /**
     * Converts a string into the cap for the corresponding skill
     * @param attackType the skill cap to return
     * @return the skill cap
     */
    public int getAttackType(String attackType) {
        switch(attackType) {
            case "primary" :
                return getOffensePrimary();
            case "secondary" :
                return getOffenseSecondary();
            case "ranged" :
                return getOffenseRanged();
            default :
                return getOffenseSpecial();
        }
    }

    /**
     * Getter method for damage bonus
     * @return primary hand weapon damage bonus
     */
    public int getDamageBonus() {
        return damageBonus;
    }

    /**
     * Setter method for damage bonus
     */
    public void setDamageBonus() {
        if (level < 28 || !characterClass.equals("Warrior"))
            damageBonus = 0;

        int delay = primary.getDelay();
        damageBonus = 1 + (level - 28) / 3;
        
        if (primary.getWeaponType().equals("2HSlashing") || primary.getWeaponType().equals("2HBlunt")
                ||primary.getWeaponType().equals("2HPiercing")) {
            if (delay <= 27)
                damageBonus += 1;

            if (level > 29)
            {
                int level_bonus = (level - 30) / 5 + 1;
                if (level > 50)
                {
                    level_bonus++;
                    int level_bonus2 = level - 50;
                    if (level > 67)
                        level_bonus2 += 5;
                    else if (level > 59)
                        level_bonus2 += 4;
                    else if (level > 58)
                        level_bonus2 += 3;
                    else if (level > 56)
                        level_bonus2 += 2;
                    else if (level > 54)
                        level_bonus2++;
                    level_bonus += level_bonus2 * delay / 40;
                }
                damageBonus += level_bonus;
            }
            if (delay >= 40)
            {
                int delay_bonus = (delay - 40) / 3 + 1;
                if (delay >= 45)
                    delay_bonus += 2;
                else if (delay >= 43)
                    delay_bonus++;
                damageBonus += delay_bonus;
            }
        }
    }

    /**
     * Method for checking berserk
     * @return true if warrior is low health
     */
    public boolean isBerserk() {
        return berserk;
    }

    /**
     * Setter method for setting berserk state
     * @param berserk warrior low health state
     */
    public void setBerserk(boolean berserk) {
        this.berserk = berserk;
    }

    /**
     * Getter method for combat fury AA
     * @return combat fury AA crit %
     */
    public int getCombatFury() {
        return combatFury;
    }

    /**
     * Setter method for combat fury crit%
     * @param combatFury crit %
     */
    public void setCombatFury(int combatFury) {
        this.combatFury = combatFury;
    }

    /**
     * Setter method to set flurry
     * @param flurry flurry from AA
     */
    public void setFlurry(int flurry) {
        this.flurry = flurry;
    }

    /**
     * Getter method to get flurry
     * @return the flurry amount
     */
    public int getFlurry() {
        return flurry;
    }

    /**
     * Getter method to get chance to hit
     * @param skill skill in use
     * @return chance to hit
     */
    public int getToHit(String skill) {
        int accuracy = 0;
        int toHit = 7 + getOffense();
        if (skill.equals("Archery")){
            toHit += (int)(getSkillCap(getSkillID(skill)) * archerySkillMod);
            if (Discipline.getActiveDisc() == 1067) {
                toHit *= 1.33;
            }
        } else if (skill.equals("Backstab")){
            toHit += (int)(getSkillCap(getSkillID(skill)) * backstabSkillMod);
        } else {
            toHit += getSkillCap(getSkillID(skill));
        }

//        if (IsClient())
//        {
//            accuracy = itembonuses.Accuracy[EQ::skills::HIGHEST_SKILL + 1] +
//                spellbonuses.Accuracy[EQ::skills::HIGHEST_SKILL + 1] +
//                aabonuses.Accuracy[EQ::skills::HIGHEST_SKILL + 1] +
//                aabonuses.Accuracy[skill] +
//                itembonuses.HitChance; //Item Mod 'Accuracy'

            //} else

        if (characterClass.equals("Warrior") && isBerserk()) {
                toHit += 2 * getLevel() / 5;
        }

//        }
//        else //mob logic
//        {
//            accuracy = CastToNPC()->GetAccuracyRating();	// database value
//            if (GetLevel() < 3)
//                accuracy += 2;		// level 1 and 2 NPCs parsed a few points higher than expected
//        }

        return toHit + accuracy;
    }

    /**
     * Getter method for getting the type of arrow
     * @return arrow type equipped
     */
    public String getArrow() {
        return arrow;
    }

    /**
     * Setter method for setting the type of arrow
     * @param arrow arrow equipped
     */
    public void setArrow(String arrow) {
        this.arrow = arrow;
    }

    /**
     * Getter method for arrow damage
     * @return arrow damage
     */
    public int getArrowDamage() {
        return arrowDamage;
    }

    /**
     * Setter method for arrow damage
     * @param arrowDamage arrow damage
     */
    public void setArrowDamage(int arrowDamage) {
        this.arrowDamage = arrowDamage;
    }

    /**
     * Getter method to retrieve the name of the primary weapon
     * @return primary weapon name
     */
    public String getPrimaryWeapon() {
        return primaryWeapon;
    }

    /**
     * Getter method to retrieve the name of the secondary weapon
     * @return secondary weapon name
     */
    public String getSecondaryWeapon() {
        return secondaryWeapon;
    }

    /**
     * Getter method to retrieve the name of the ranged weapon
     * @return ranged weapon name
     */
    public String getRangedWeapon() {
        return rangedWeapon;
    }

    /**
     * Getter method to retrieve the name of the special attack
     * @return special attack name
     */
    public String getSpecial() {
        return special;
    }

    /**
     * Getter method to get the special ranged attack
     * @return ranged attack specialAttack object
     */
    public SpecialAttack getRangedAttack() {
        return rangedAttack;
    }

    public Weapon getRanged() {
        return ranged;
    }

    /**
     * Set DW chance 1% per 3.75 skill
     */
    public void setDualWieldChance() {
       dualWieldChance = (float)((dualWieldSkill + level)/375.0);
    }

    /**
     * Getter method to get dual wield chance
     * @return dual wield chance
     */
    public float getDualWieldChance() {
        return dualWieldChance;
    }

    /**
     * Getter method for backstab skill mod
     * @return backstabSkillMod
     */
    public static double getBackstabSkillMod() {
        return backstabSkillMod;
    }
}
