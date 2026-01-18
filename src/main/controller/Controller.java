package main.controller;

import main.model.*;

import java.util.Random;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Controller class
 */
public class Controller {
    private static String db;
    private static String Expansion;
    private static CharacterSheet cs;
    private static Mob targetMob;
    private static String[] mobList;
    private static boolean useCustomResists, useCustomAC, bard, beastlord, ancientAvatar, berserk;
    private static int customResists, customAC;
    private static String characterClass;
    private static String playerName;
    private static String primary = "none";
    private static String secondary = "none";
    private static String ranged = "none";
    private static String specialAttack = "none";
    private int spellAttack;
    private int wornAttack;
    private int ambidexterity;
    private int combatFury;
    private int flurry;
    private static int spellCastingFury, punishOrKnightExtraAttack = 0;
    private int arrowDamage, arrowElementalDamage, arrowElementalType, haste;
    private double archeryMultipliers, archeryMod, backstabMod;
    private static double piercingMod;
    private static int primaryHastedDelay, secondaryHastedDelay, specialHastedDelay, rangedHastedDelay;
    private static int primaryDamage = 0, secondaryDamage = 0, rangedDamage = 0, specialDamage = 0;
    private static int primaryProcDamage = 0, secondaryProcDamage = 0, rangedProcDamage = 0;
    private static int primaryDotDamage = 0, secondaryDotDamage = 0, rangedDotDamage = 0;
    private static int primaryDotTics = 0, secondaryDotTics = 0, rangedDotTics = 0, discipline = 0;
    private static long startPrimaryDotTimer = -1, startSecondaryDotTimer = -1, startRangedDotTimer = -1;
    private static long discStartTimer = 0, timeToParse;
    private static long spellCastingOffsetPrimary = 0, spellCastingOffsetSecondary = 0;
    private static long spellCastingOffsetSpecial = 0, spellCastingOffsetRanged = 0;
    private static long reactionTimePlusPing = 0;
    private static boolean fullCombatTextToFile, reportToConsole, windOfTash, malo, manaRatioEqualToBossHP;
    private static boolean writeToFile, summarizedOutput;
    private static String fileToWriteTo;
    private static SummarizedDamage sd;


    /**
     * Default constructor for Controller
     * Desired parse styles
     *      Long term average - current configuration
     *      Short term burst - can add levels to flip between long and short
     *      Class selection settings - preconfigured attack levels for all melee classes
     *      Melee parsing 1 current configuration
     *      Caster parsing - 2
     *      Defensive parsing - 3
     *      Threat parsing - 4
     * @param fh file handler object
     */
    public Controller(FileHandling fh) {
        int stats = 255, level = 60;
        setExpansion("PlanesEQ");
        setDb(fh.readFromFile("resources/quarm_shortdb.sql"));
        setTimeToParse(36000000); // 300000 milliseconds is 5 minutes, 36000000 milliseconds is 10 hours

        setReactionTimePlusPing(150); // In milliseconds and you (probably) aren't Faker

        setBard(true); // McVaxius w/AAs (44 attack) + epic proc (30 attack)
        setBeastlord(true); // Savagery
        setAncientAvatar(true); // +25 attack over avatar

        setClass("Monk");
        setPlayerName("Soandso");

        setDiscipline(1059); // Fellstrike = Bestialrage = Innerflame = Duelist = 1059, Trueshot = 1067, Holyforge = 1065
        setWornAttack(250);
        setSpellAttack();

        setWriteToFile(false);
        setFullCombatText(false);
        setReportToConsole(false);


        setSummarizedOutput(true);
        if(summarizedOutput) {
            sd = new SummarizedDamage();
        }

        setWindOfTash(true);
        setMalo(true);
        setManaRatioEqualToBossHP(true);


        // setCustomAC(i);
        // setCustomResists(i);

        //primary, secondary, ranged, specialAttack
        //setWeaponsAndSpecial("Khalshazar", "Acrylia Handled Broadsword", "none", "Backstab");
        //setWeaponsAndSpecial("Mrylokar\\'s Dagger of Vengeance", "Acrylia Handled Broadsword", "none", "Backstab");
        //setWeaponsAndSpecial("Yttrium War Hammer", "none", "none", "FlyingKick");
        //setWeaponsAndSpecial("Staff of Transcendence", "none", "none", "none");
        //setWeaponsAndSpecial("Caen\\'s Bo Staff of Fury", "none", "none", "none");
        //setWeaponsAndSpecial("Gharn\\'s Rock of Smashing", "Fist of Nature", "none", "none");
        //setWeaponsAndSpecial("Bo Staff of Transcendence", "none", "none", "none");
        //setWeaponsAndSpecial("Staff of the Silent Star", "none", "none", "none");
        // Monk: Gharn\\'s Rock of Smashing, Fist of Nature, Primal Velium Fist Wraps, Sceptre of Destruction, FlyingKick
        // Ranger: Bow of the Destroyer, Primal Velium Reinforced Bow, Bow of Storms, Khalshazar
        // Rogue: Mrylokar\\'s Dagger of Vengeance, Massive Heartwood Thorn, Backstab, Acrylia Handled Broadsword
        //setWeaponsAndSpecial("Mrylokar\\'s Dagger of Vengeance", "Tor Vignus", "none", "Backstab");
        //setWeaponsAndSpecial("Mrylokar\\'s Dagger of Vengeance", "Massive Heartwood Thorn", "none", "Backstab");
        //setWeaponsAndSpecial("Salindrite Dagger", "Ragebringer", "none", "Backstab");
        //setWeaponsAndSpecial("Mrylokar\\'s Dagger of Vengeance", "Ragebringer", "none", "Backstab");
        //setWeaponsAndSpecial("Palladius\\' Axe of Slaughter", "none", "none", "Kick");
        //setWeaponsAndSpecial("Bloodied Berserker\\'s Blade", "none", "none", "Bash");Shuriken of the Tranquil
        //setWeaponsAndSpecial("Goldenrod", "none", "none", "none");
        //setWeaponsAndSpecial("The Sword of Ssraeshza", "none", "none", "none");
        //setWeaponsAndSpecial("Mrylokar\\'s Dagger of Vengeance", "none", "none", "Backstab");
        //setWeaponsAndSpecial("Salindrite Dagger", "Baton of Flame", "none", "Kick");
        //setWeaponsAndSpecial("none", "none", "Primal Velium Reinforced Bow", "none");
        //setWeaponsAndSpecial("Emaciated Maul of the Overseer", "none", "none", "Kick");
        setWeaponsAndSpecial("Fangs of Vyzh`dra", "Glowing Mithril Ulak", "none", "FlyingKick");

        //arrowDamage, arrowElementalDamage, arrowElementalType
        //Blessed Champion Arrows
        //setArrows(11, 4, 1);
        //Shardwing
        //setArrows(12, 0, 0);
        //None
        setArrows(0, 0, 0);

        //ambidexterity (32), combatFury (15, 40, 75, PoP crit: 100, 125, 150)
        //archery mastery (1 2 or 3), spell casting fury (1 2 or 3)
        //flurry 15% at r3, berserk
        //Combat Fury 1 2 3: 15, 40, 75. FotA1 2 3: 100, 125, 150
        setBerserkAndAA(32, 75, 0, 0, 0, false);
        //setBerserkAndAA(32, 150, 3, 3, 15, true);

        // Punishing Blade and Speed of the Knight are equivalent r 1 2 3 : 2% 4% 8% extra attack
        setPunishingBlade(0);

        setSkillMods(1.00, 1.12); // archeryMod, backstabMod
        setPiercingMod(1.00); // Tor Vignus 7% mod, set to 1 to remove mod
        setWornSpellHastePreBard(100); // If bard is true, adds 25%
        setMobListToParse();
        setUseCustomResists(false);
        setUseCustomAC(false);

        if (writeToFile) {
            // Build file name based on inputs
            String filename = "resources/eqlog_" + playerName + "_" + characterClass;
            if (!primary.equals("none")) {
                filename += "_" + getCleanName(primary);
            }
            if (!secondary.equals("none")) {
                filename += "_" + getCleanName(secondary);
            }
            if (!ranged.equals("none")) {
                filename += "_" + getCleanName(ranged);
            }
            filename += "_Vs_" + getCleanName(mobList[0]) + ".txt";
            setFileToWriteTo(filename);
            fh.clearFile(filename);
        }

        setCharacterSheet(new CharacterSheet(level, characterClass, primary, secondary, ranged, specialAttack, spellAttack, wornAttack,
                haste, stats, ambidexterity, archeryMultipliers, combatFury, archeryMod, backstabMod,
                arrowDamage, arrowElementalDamage, arrowElementalType, berserk, flurry, fh));

        if (!cs.getPrimaryWeapon().equals("none")) {
            primaryHastedDelay = (int)(hastedDelay(cs.getPrimary())*1000);
        }
        if (!cs.getSecondaryWeapon().equals("none")) {
            secondaryHastedDelay = (int)(hastedDelay(cs.getSecondary())*1000);
        }
        if (!cs.getRangedWeapon().equals("none")) {
            rangedHastedDelay = (int)(hastedDelay(cs.getRanged())*1000);
        }
        if (!cs.getSpecial().equals("none")) {
            setSpecialDelay(cs.getSpecialAttack());
        }

        CombatRound crMH = new CombatRound();
        CombatRound crOH = new CombatRound();
        CombatRound crSp = new CombatRound();
        CombatRound crRng = new CombatRound();
        //calefaction 1740
        //Spell sunVortex = new Spell(3325); //sun vortex

        runMobList(fh, crMH, crOH, crSp, crRng); //file handling and rounds to parse
    }

    /**
     * Runs the mob list
     * @param fh file handler object
     * @param crMH main hand combat round
     * @param crOH offhand combat round
     * @param crRng ranged attack combat round
     * @param crSp special attack combat round
     */
    public void runMobList(FileHandling fh, CombatRound crMH, CombatRound crOH, CombatRound crSp, CombatRound crRng) {

        for (String s : mobList) {
            setTargetMob(new Mob(s));
            combatRound(fh, crMH, crOH, crSp, crRng, timeToParse, s);
        }
    }

    /**
     * A list of mob names as they appear in the database in increasing amounts of AC
     * a_racnar 200 AC
     * Ikatiar_the_Venom 300 AC
     * Lord_Koi'Doken 350 AC
     * Tunare 400 AC
     * Dain_Frostreaver_IV 550 AC
     * Lady_Nevederia 650 AC
     * Aten_Ha_ra 850 AC
     * The_Avatar_of_War 900 AC
     * Vulak 1000 AC
     */
    public void setMobListToParse() {
        mobList = new String[] {"Lord_Inquisitor_Seru"};
        //mobList = new String[] {"The_Avatar_of_War"};
        //mobList = new String[] {"Gozzrem"};
        //mobList = new String[] {"Ikatiar_the_Venom"};
        //mobList = new String[] {"Mistress_of_Scorn"};
        //mobList = new String[] {"a_racnar", "a_racnar", "a_racnar", "a_racnar", "a_racnar",
        //      "a_racnar", "a_racnar", "a_racnar", "a_racnar", "a_racnar"};
        //mobList = new String[] {"a_racnar", "Ikatiar_the_Venom", "Lord_Koi", "Tunare", "Dain_Frostreaver_IV",
        //      "Lady_Nevederia", "Aten_Ha_Ra", "The_Avatar_of_War", "Vulak"};
    }

    /**
     * Method that simulates mainhand, offhand, ranged, and special attack
     * @param fh file handler object
     * @param crMH main hand combat round
     * @param crOH offhand combat round
     * @param crRng ranged attack combat round
     * @param crSp special attack combat round
     * @param millisecondsToParse millisecond duration of combat
     * @param mob target mob name
     */
    private static void combatRound(FileHandling fh, CombatRound crMH, CombatRound crOH, CombatRound crSp,
                                    CombatRound crRng, long millisecondsToParse, String mob) {

        DateFormat simple = new SimpleDateFormat("E MMM dd HH:mm:ss y");

        // Berserk reporting
        if (isBerserk()) {
            String berserk = "[" + simple.format(System.currentTimeMillis()) + "] " + playerName
                                        + " goes into a berserker frenzy!\r\n";
            if (reportToConsole) {
                System.out.println(berserk);
            } else if (writeToFile) {
                fh.writeToFile(fileToWriteTo, berserk);
            }
        }

        for (long i = 0; i < millisecondsToParse; i++) {
            Date result = new Date(i + System.currentTimeMillis());

            // Disciplines
            if (Discipline.disciplineUseLevel(discipline) != 0) {
                if (Discipline.getReuseTimer() == 0) {
                    Discipline.castDiscipline(discipline, Discipline.disciplineUseLevel(discipline));
                    String discStart = "[" + simple.format(result) + "] " + String_ID.discStartFlavorText(discipline) + "\r\n";
                    if (reportToConsole) {
                        System.out.println(discStart);
                    } else if (writeToFile) {
                        fh.writeToFile(fileToWriteTo, discStart);
                    }
                    discStartTimer = i;
                }

                // Server tick for mana regen (pretending it's always synchronized
                if (i % 6000 == 0) {
                    cs.manaRegenTick();
                }

                if (discStartTimer + Discipline.getAbilityTimer() == i) {
                    String discEnd = "[" + simple.format(result) + "] " + String_ID.discEndFlavorText(discipline) + "\r\n";
                    if (reportToConsole) {
                        System.out.println(discEnd);
                    } else if (writeToFile) {
                        fh.writeToFile(fileToWriteTo, discEnd);
                    }
                    Discipline.setActiveDisc(0);
                }

                if (discStartTimer + Discipline.getReuseTimer() == i) {
                    Discipline.resetReuseTimer();
                }
            }

            // Primary combat round
            if (!cs.getPrimaryWeapon().equals("none")) {
                // Primary dot output
                if (primaryDotTics > 0 && ((i + startPrimaryDotTimer) % 6000) == 0) {
                    primaryDot(fh, mob, cs.getPrimary().getProcName(), simple.format(result));
                }
                // Primary procs
                if ((i % (primaryHastedDelay + spellCastingOffsetPrimary)) == 0) {
                    if (random((float) cs.getPrimary().getProcChance())) {
                        primaryProcDamage = cs.getPrimary().doProc(targetMob);
                        if (primaryProcDamage >= -1) {
                            primaryProc(fh, cs.getPrimary(), mob, simple.format(result));
                        }
                        if (primaryProcDamage < 0) {
                            startPrimaryDotTimer = i;
                        }
                    }
                    // Primary swing
                    primarySwing(fh, crMH, mob, simple.format(result), 1);
                    // Double attack
                    if (random((float) doubleChance("primary"))) {
                        primarySwing(fh, crMH, mob, simple.format(result), 2);
                        // Triple attack
                        if (random((float) tripleChance("primary"))) {
                            primarySwing(fh, crMH, mob, simple.format(result), 3);
                            // Flurry
                            if (cs.getFlurry() > 0) {
                                if (random((float) flurryChance("primary"))) {
                                    primarySwing(fh, crMH, mob, simple.format(result), 4);
                                    // 2nd Flurry at 10% chance of the first
                                    if (random((float) flurryChance("primary") / 10)) {
                                        primarySwing(fh, crMH, mob, simple.format(result), 5);
                                    }
                                }
                            }
                        }
                    }
                    // Punishing blade or speed of the knight swing if monk or paladin with the aa
                    if (cs.getPrimary().getWeaponType().equals("2HBlunt") ||
                            cs.getPrimary().getWeaponType().equals("2HSlash") ||
                            cs.getPrimary().getWeaponType().equals("2HPiercing")) {
                        if (random(punishOrKnightExtraAttack)) {
                            primarySwing(fh, crMH, mob, simple.format(result), 4);
                        }
                    }
                    //reportRound("primary", primary);
                }
            }
            // Secondary combat round
            if (!cs.getSecondaryWeapon().equals("none")) {
                // DW check
                if (random(cs.getDualWieldChance())) {
                    // Secondary dot output
                    if (secondaryDotTics > 0 && ((i + startSecondaryDotTimer) % 6000) == 0) {
                        secondaryDot(fh, mob, cs.getSecondary().getProcName(), simple.format(result));
                        // Secondary procs
                    }
                    if ((i % (secondaryHastedDelay + spellCastingOffsetPrimary)) == 0) {
                        if (random((float) cs.getSecondary().getProcChance())) {
                            secondaryProcDamage = cs.getSecondary().doProc(targetMob);
                            if (secondaryProcDamage >= -1) {
                                secondaryProc(fh, cs.getSecondary(), mob, simple.format(result));
                            }
                            if (secondaryProcDamage < 0) {
                                startSecondaryDotTimer = i;
                            }
                        }
                        // Secondary swing
                        secondarySwing(fh, crOH, mob, simple.format(result), 1);

                        // Secondary double attack
                        if (random((float) doubleChance("secondary"))) {
                            secondarySwing(fh, crOH, mob, simple.format(result), 2);
                        }
                    }
                    //reportRound("secondary", secondary, i, j);
                }
            }
            // Special attack
            if (!cs.getSpecial().equals("none")) {
                if (i % (specialHastedDelay + spellCastingOffsetPrimary) == 0) {
                    specialAttack(fh, crSp, mob, simple.format(result), 1);
                    // Double attack
                    if (random((float) doubleChance(SpecialAttack.getSpecialName()))) {
                        specialAttack(fh, crSp, mob, simple.format(result), 2);
                    }
                    //reportRound(cs.getSpecial(), special, i, j);
                }
            }
            // Ranged attack
            if (!cs.getRangedWeapon().equals("none")) {
                if (i % rangedHastedDelay == 0) {
                    // Ranged dot output
                    if (rangedDotTics > 0 && ((i + startRangedDotTimer) % 6000) == 0) {
                        rangedDot(fh, mob, cs.getRanged().getProcName(), simple.format(result));
                    }
                    // Ranged procs
                    if ((i % (rangedHastedDelay + spellCastingOffsetPrimary)) == 0) {
                        if (random((float) cs.getRanged().getProcChance())) {
                            rangedProcDamage = cs.getRanged().doProc(targetMob);
                            if (rangedProcDamage >= -1) {
                                rangedProc(fh, cs.getRanged(), mob, simple.format(result));
                            }
                            if (rangedProcDamage < 0) {
                                startRangedDotTimer = i;
                            }
                        }
                        rangedAttack(fh, crRng, mob, simple.format(result));
                        //reportRound("ranged", ranged, i, j);
                    }
                }
            }
            if (i == millisecondsToParse - 1) {
                String slay = "[" + simple.format(result) + "] " + getCleanName(mob) + " has been slain by " + playerName + "!";
                if (writeToFile) {
                    fh.writeToFile(fileToWriteTo, slay);
                }
                sd.printSummary(cs.getPrimary(), cs.getSecondary(), cs.getRanged(), getCleanName(mob), cs.getSpecial(), i + 1);
            }
        }
    }

    /**
     * Calculates and outputs a primary swing either to file and or console
     * @param fh file handler object
     * @param crMH mainhand weapon object
     * @param mob the target mob
     * @param timestamp start time plus simulated parse time
     */
    private static void primarySwing(FileHandling fh, CombatRound crMH, String mob, String timestamp, int attackNum) {
        switch (attackNum) {
            case 1:
                sd.primarySwingCounter++;
                break;
            case 2:
                sd.primaryDoubleCounter++;
                break;
            case 3:
                sd.primaryTripleCounter++;
                break;
            case 4:
                sd.primaryQuadCounter++;
                break;
            case 5:
                sd.primaryQuintCounter++;
                break;
        }

        primaryDamage = crMH.attack(cs.getPrimary(), "primary", cs.getOffensePrimary());
        String pd = "";
        if (fullCombatTextToFile) {
            if (primaryDamage != 0) {
                if (CombatRound.isCritHit()) {
                    sd.primaryCritCounter++;
                    sd.primaryCritDamage += primaryDamage;
                    if (primaryDamage > sd.primaryMaximumCrit)
                        sd.primaryMaximumCrit = primaryDamage;
                    if (primaryDamage < sd.primaryMinimumCrit)
                        sd.primaryMinimumCrit = primaryDamage;

                    if (CombatRound.isCrippling()) {
                        pd += "[" + timestamp + "] " + playerName + " lands a Crippling Blow! (" + primaryDamage + ")\r\n";
                    } else {
                        pd += "[" + timestamp + "] " + playerName + " scores a critical hit! (" + primaryDamage + ")\r\n";
                    }
                } else if (CombatRound.isSlayUndead()) {
                    sd.primaryCritCounter++;
                    sd.primaryCritDamage += primaryDamage;
                    if (primaryDamage > sd.primaryMaximumCrit)
                        sd.primaryMaximumCrit = primaryDamage;
                    if (primaryDamage < sd.primaryMinimumCrit)
                        sd.primaryMinimumCrit = primaryDamage;

                    pd += "[" + timestamp + "] " + playerName + String_ID.discStartFlavorText(1007) + " (" + primaryDamage + ")\r\n";
                } else {
                    sd.primaryDamage += primaryDamage;
                    if (primaryDamage > sd.primaryMaximumHit)
                        sd.primaryMaximumHit = primaryDamage;
                    if (primaryDamage < sd.primaryMinimumHit)
                        sd.primaryMinimumHit = primaryDamage;
                }
                pd += "[" + timestamp + "] " + "You " + attackType(cs.getPrimary()) + " " + getCleanName(mob) + " for "
                        + primaryDamage + " points of damage.\r\n";
            } else {
                sd.primaryMissCounter++;
                pd += "[" + timestamp + "] " + "You try to " + attackType(cs.getPrimary()) + " "
                        + getCleanName(mob) + ", but miss!\r\n";
            }
        } else {
            if (primaryDamage != 0) {
                if (CombatRound.isCritHit()) {
                    sd.primaryCritCounter++;
                    sd.primaryCritDamage += primaryDamage;
                    if (primaryDamage > sd.primaryMaximumCrit)
                        sd.primaryMaximumCrit = primaryDamage;
                    if (primaryDamage < sd.primaryMinimumCrit)
                        sd.primaryMinimumCrit = primaryDamage;

                    if (CombatRound.isCrippling()) {
                        pd = "[" + timestamp + "] primary " + primaryDamage + " crippling\r\n";
                    } else {
                        pd = "[" + timestamp + "] primary " + primaryDamage + " critical\r\n";
                    }
                } else {
                    sd.primaryDamage += primaryDamage;
                    if (primaryDamage > sd.primaryMaximumHit)
                        sd.primaryMaximumHit = primaryDamage;
                    if (primaryDamage < sd.primaryMinimumHit)
                        sd.primaryMinimumHit = primaryDamage;
                    pd = "[" + timestamp + "] primary " + primaryDamage + " \r\n";
                }
            } else {
                sd.primaryMissCounter++;
                pd = "[" + timestamp + "] primary miss\r\n";
            }
        }
        if (reportToConsole) {
            System.out.print(pd);
        } else if (writeToFile) {
            fh.writeToFile(fileToWriteTo, pd);
        }
    }

    /**
     * Outputs primary proc damage if it is greater than 0 and a DD
     * @param fh file handler object
     * @param primary mainhand weapon object
     * @param mob the target mob
     * @param timestamp start time plus simulated parse time
     */
    private static void primaryProc(FileHandling fh, Weapon primary, String mob, String timestamp) {
        String pd = "";
        sd.primaryProcCounter++;
        if (fullCombatTextToFile) {
            if (primaryProcDamage > 0 || primaryProcDamage == -1) {
                if (primaryProcDamage > 0) {
                    if (primaryProcDamage > sd.primaryMaximumDD)
                        sd.primaryMaximumDD = primaryProcDamage;
                    if (primaryProcDamage < sd.primaryMinimumDD)
                        sd.primaryMinimumDD = primaryProcDamage;
                    if (Weapon.isCritical()) {
                        sd.primaryProcCritCounter++;
                        sd.primaryProcCritDamage += primaryProcDamage;

                        pd += "[" + timestamp + "] " + playerName + " delivers a critical blast! ("
                                + primaryProcDamage + ")\r\n";
                    } else {
                        sd.primaryProcDD += primaryProcDamage;
                    }
                    pd += "[" + timestamp + "] " + getCleanName(mob) + " was hit by non-melee for "
                            + primaryProcDamage + " points of damage.\r\n";
                }
                pd += "[" + timestamp + "] " + getCleanName(mob) + primary.getProc().getSpellTextOnHit() + "\r\n";
                if (primaryProcDamage == -1) {
                    primaryProcDamage--;
                }
            } else {
                sd.primaryProcResistCounter++;
                pd = "[" + timestamp + "] " + "Your target resisted the " + cs.getPrimary().getProcName() + " spell.\r\n";
            }
        } else {
            if (primaryProcDamage > 0 || primaryProcDamage == -1) {
                pd = "[" + timestamp + "]" + primary.getProc().getSpellTextOnHit() + "\r\n";
                if (primaryProcDamage > 0) {
                    if (primaryProcDamage > sd.primaryMaximumDD)
                        sd.primaryMaximumDD = primaryProcDamage;
                    if (primaryProcDamage < sd.primaryMinimumDD)
                        sd.primaryMinimumDD = primaryProcDamage;
                    if (Weapon.isCritical()) {
                        sd.primaryProcCritCounter++;
                        sd.primaryProcCritDamage += primaryProcDamage;
                    } else {
                        sd.primaryProcDD += primaryProcDamage;
                    }
                    pd += "[" + timestamp + "] non-melee " + primaryProcDamage + "\r\n";
                }
                if (primaryProcDamage == -1) {
                    primaryProcDamage--;
                }
            } else {
                sd.primaryProcResistCounter++;
                pd = "[" + timestamp + "] resist\r\n";
            }
        }
        if (reportToConsole) {
            System.out.print(pd);
        } else if (writeToFile) {
            fh.writeToFile(fileToWriteTo, pd);
        }
    }

    /**
     * Outputs dot damage for primary proc
     * @param fh file handler object
     * @param mob the target mob
     * @param procName the name of the proc
     * @param timestamp start time plus simulated parse time
     */
    private static void primaryDot(FileHandling fh, String mob, String procName, String timestamp) {
        primaryDotTics--;
        String pdt;
        sd.primaryProcDot += primaryDotDamage;
        if (fullCombatTextToFile) {
            pdt = "[" + timestamp + "] " + getCleanName(mob) + " has taken "
                    + primaryDotDamage + " from your " + procName + ".\r\n";
            if (primaryDotTics == 0) {
                pdt += "[" + timestamp + "] Your " + procName + " spell has worn off.\r\n";
            }
        } else {
            pdt = "[" + timestamp + "] " + procName + " " + primaryDotDamage + "\r\n";
            if (primaryDotTics == 0) {
                pdt += "[" + timestamp + "] " + procName + " off\r\n";
            }
        }
        if (reportToConsole) {
            System.out.print(pdt);
        } else if (writeToFile) {
            fh.writeToFile(fileToWriteTo, pdt);
        }
    }

    /**
     * Calculates and outputs a secondary swing either to file and console or just to console
     * @param fh file handler object
     * @param crOH offhand weapon object
     * @param mob the target mob
     */
    private static void secondarySwing(FileHandling fh, CombatRound crOH, String mob, String timestamp, int attackNum) {
        switch (attackNum) {
            case 1:
                sd.secondarySwingCounter++;
                break;
            case 2:
                sd.secondaryDoubleCounter++;
                break;
        }
        secondaryDamage = crOH.attack(cs.getSecondary(), "secondary", cs.getOffenseSecondary());
        String scd = "";
        if (fullCombatTextToFile) {
            if (secondaryDamage != 0) {
                if (CombatRound.isCritHit()) {
                    sd.secondaryCritCounter++;
                    sd.secondaryCritDamage += secondaryDamage;
                    if (secondaryDamage > sd.secondaryMaximumCrit)
                        sd.secondaryMaximumCrit = secondaryDamage;
                    if (secondaryDamage < sd.secondaryMinimumCrit)
                        sd.secondaryMinimumCrit = secondaryDamage;

                    if (CombatRound.isCrippling()) {
                        scd = "[" + timestamp + "] " + playerName + " lands a Crippling Blow! (" + secondaryDamage + ")\r\n";
                    } else {
                        scd = "[" + timestamp + "] " + playerName + " scores a critical hit! (" + secondaryDamage + ")\r\n";
                    }
                } else {
                    sd.secondaryDamage += secondaryDamage;
                    if (secondaryDamage > sd.secondaryMaximumHit)
                        sd.secondaryMaximumHit = secondaryDamage;
                    if (secondaryDamage < sd.secondaryMinimumHit)
                        sd.secondaryMinimumHit = secondaryDamage;
                }
                scd += "[" + timestamp + "] " + "You " + attackType(cs.getSecondary()) + " " + getCleanName(mob) + " for "
                        + secondaryDamage + " points of damage.\r\n";
            } else {
                sd.secondaryMissCounter++;
                scd = "[" + timestamp + "] " + "You try to " + attackType(cs.getSecondary()) + " "
                        + getCleanName(mob) + ", but miss!\r\n";
            }
        } else {
            if (secondaryDamage != 0) {
                if (CombatRound.isCritHit()) {
                    sd.secondaryCritCounter++;
                    sd.secondaryCritDamage += secondaryDamage;
                    if (secondaryDamage > sd.secondaryMaximumCrit)
                        sd.secondaryMaximumCrit = secondaryDamage;
                    if (secondaryDamage < sd.secondaryMinimumCrit)
                        sd.secondaryMinimumCrit = secondaryDamage;

                    if (CombatRound.isCrippling()) {
                        scd = "[" + timestamp + "] secondary " + secondaryDamage + " crippling\r\n";
                    } else {
                        scd = "[" + timestamp + "] secondary " + secondaryDamage + " critical\r\n";
                    }
                } else {
                    sd.secondaryDamage += secondaryDamage;
                    if (secondaryDamage > sd.secondaryMaximumHit)
                        sd.secondaryMaximumHit = secondaryDamage;
                    if (secondaryDamage < sd.secondaryMinimumHit)
                        sd.secondaryMinimumHit = secondaryDamage;
                }
                scd += "[" + timestamp + "] secondary " + secondaryDamage + " \r\n";
            } else {
                sd.secondaryMissCounter++;
                scd = "[" + timestamp + "] secondary miss\r\n";
            }
        }
        if (reportToConsole) {
            System.out.print(scd);
        } else if (writeToFile) {
            fh.writeToFile(fileToWriteTo, scd);
        }
    }

    /**
     * Outputs secondary proc damage if it is greater than 0 and a DD
     * @param fh file handler object
     * @param secondary mainhand weapon object
     * @param mob the target mob
     * @param timestamp start time plus simulated parse time
     */
    private static void secondaryProc(FileHandling fh, Weapon secondary, String mob, String timestamp) {
        String scd = "";
        sd.secondaryProcCounter++;
        if (fullCombatTextToFile) {
            if (secondaryProcDamage > 0 || secondaryProcDamage == -1) {
                if (secondaryProcDamage > 0 ) {
                    if (secondaryProcDamage > sd.secondaryMaximumDD)
                        sd.secondaryMaximumDD = secondaryProcDamage;
                    if (secondaryProcDamage < sd.secondaryMinimumDD)
                        sd.secondaryMinimumDD = secondaryProcDamage;
                    if (Weapon.isCritical()) {
                        sd.secondaryProcCritCounter++;
                        sd.secondaryProcCritDamage += secondaryProcDamage;
                        scd += "[" + timestamp + "] " + playerName + " delivers a critical blast! ("
                                + secondaryProcDamage + ")\r\n";
                    } else {
                        sd.secondaryProcDD += secondaryProcDamage;
                    }
                    scd += "[" + timestamp + "] " + getCleanName(mob) + " was hit by non-melee for "
                            + secondaryProcDamage + " points of damage.\r\n";
                }
                scd += "[" + timestamp + "] " + getCleanName(mob) + secondary.getProc().getSpellTextOnHit() + "\r\n";
                if (secondaryProcDamage == -1) {
                    secondaryProcDamage--;
                }
            } else {
                sd.secondaryProcResistCounter++;
                scd = "[" + timestamp + "] " + "Your target resisted the " + cs.getSecondary().getProcName() + " spell.\r\n";
            }
        } else {
            if (secondaryProcDamage > 0 || secondaryProcDamage == -1) {
                if (secondaryProcDamage > sd.secondaryMaximumDD)
                    sd.secondaryMaximumDD = secondaryProcDamage;
                if (secondaryProcDamage < sd.secondaryMinimumDD)
                    sd.secondaryMinimumDD = secondaryProcDamage;

                if (Weapon.isCritical()) {
                    sd.secondaryProcCritCounter++;
                    sd.secondaryProcCritDamage += secondaryProcDamage;
                    scd = "[" + timestamp + "]" + secondary.getProc().getSpellTextOnHit() + "\r\n";
                } else {
                    if (secondaryProcDamage > 0) {
                        sd.secondaryProcDD += secondaryProcDamage;
                        scd += "[" + timestamp + "] non-melee " + secondaryProcDamage + "\r\n";
                    }
                    if (secondaryProcDamage == -1) {
                        secondaryProcDamage--;
                    }
                }
            } else {
                sd.secondaryProcResistCounter++;
                scd = "[" + timestamp + "] resist\r\n";
            }
        }
        if (reportToConsole) {
            System.out.print(scd);
        } else if (writeToFile) {
            fh.writeToFile(fileToWriteTo, scd);
        }
    }

    /**
     * Outputs dot damage for secondary proc
     * @param fh file handler object
     * @param mob the target mob
     * @param procName the name of the proc
     * @param timestamp start time plus simulated parse time
     */
    private static void secondaryDot(FileHandling fh, String mob, String procName, String timestamp) {
        secondaryDotTics--;
        String sdt = "";
        sd.secondaryProcDot += secondaryDotDamage;
        if (fullCombatTextToFile) {
            sdt = "[" + timestamp + "] " + getCleanName(mob) + " has taken "
                    + secondaryDotDamage + " from your " + procName + ".\r\n";
            if (secondaryDotTics == 0) {
                sdt += "[" + timestamp + "] Your " + procName + " spell has worn off.\r\n";
            }
        } else {
            sdt = "[" + timestamp + "] " + procName + " " + secondaryDotDamage + "\r\n";
            if (secondaryDotTics == 0) {
                sdt += "[" + timestamp + "] " + procName + " off\r\n";
            }
        }
        if (reportToConsole) {
            System.out.print(sdt);
        } else if (writeToFile) {
            fh.writeToFile(fileToWriteTo, sdt);
        }
    }

    /**
     * Calculates and outputs a special attack either to file and console or just to console
     * @param fh file handler object
     * @param crSp special attack object
     * @param mob the target mob
     */
    private static void specialAttack(FileHandling fh, CombatRound crSp, String mob, String timestamp, int attackNum) {
        switch (attackNum) {
            case 1:
                sd.specialSwingCounter++;
                break;
            case 2:
                sd.specialDoubleCounter++;
                break;
        }

        specialDamage = crSp.attack(cs.getSpecialAttack(), cs.getSpecial(), cs.getOffenseSpecial(), SpecialAttack.getMinDamage());
        String spa = "";
        if (fullCombatTextToFile) {
            if (specialDamage != 0) {
                if (CombatRound.isCritHit()) {
                    sd.specialCritCounter++;
                    sd.specialCritDamage += specialDamage;
                    if (specialDamage > sd.specialMaximumCrit)
                        sd.specialMaximumCrit = specialDamage;
                    if (specialDamage < sd.specialMinimumCrit)
                        sd.specialMinimumCrit = specialDamage;

                    if (CombatRound.isCrippling()) {
                        spa = "[" + timestamp + "] " + playerName + " lands a Crippling Blow! (" + specialDamage + ")\r\n";
                    } else {
                        spa = "[" + timestamp + "] " + playerName + " scores a critical hit! (" + specialDamage + ")\r\n";
                    }
                } else {
                    sd.specialDamage += specialDamage;
                    if (specialDamage > sd.specialMaximumHit)
                        sd.specialMaximumHit = specialDamage;
                    if (specialDamage < sd.specialMinimumHit)
                        sd.specialMinimumHit = specialDamage;
                }
                spa += "[" + timestamp + "] " + "You ";
                if (cs.getSpecial().equals("FlyingKick"))
                    spa += "kick";
                else
                    spa += cs.getSpecial().toLowerCase();
                spa += " " + getCleanName(mob) + " for " + specialDamage + " points of damage.\r\n";
            } else {
                sd.specialMissCounter++;
                spa = "[" + timestamp + "] " + "You try to ";
                if (cs.getSpecial().equals("FlyingKick"))
                    spa += "kick";
                else
                    spa += cs.getSpecial().toLowerCase();
                spa += " " + getCleanName(mob) + ", but miss!\r\n";
            }
        } else {
            if (specialDamage != 0) {
                if (CombatRound.isCritHit()) {
                    sd.specialCritCounter++;
                    sd.specialCritDamage += specialDamage;
                    if (specialDamage > sd.specialMaximumCrit)
                        sd.specialMaximumCrit = specialDamage;
                    if (specialDamage < sd.specialMinimumCrit)
                        sd.specialMinimumCrit = specialDamage;

                    if (CombatRound.isCrippling()) {
                        spa = "[" + timestamp + "] " + cs.getSpecial().toLowerCase() + " " + specialDamage + " crippling\r\n";
                    } else {
                        spa = "[" + timestamp + "] " + cs.getSpecial().toLowerCase() + " " + specialDamage + " critical\r\n";
                    }
                } else {
                    sd.specialDamage += specialDamage;
                    if (specialDamage > sd.specialMaximumHit)
                        sd.specialMaximumHit = specialDamage;
                    if (specialDamage < sd.specialMinimumHit)
                        sd.specialMinimumHit = specialDamage;
                    spa = "[" + timestamp + "] " + cs.getSpecial().toLowerCase() + " " + specialDamage + " \r\n";
                }
            } else {
                sd.specialMissCounter++;
                spa = "[" + timestamp + "] " + cs.getSpecial().toLowerCase() + " miss\r\n";
            }
        }
        if (reportToConsole) {
            System.out.print(spa);
        } else if (writeToFile) {
            fh.writeToFile(fileToWriteTo, spa);
        }
    }

    /**
     * Calculates and outputs a ranged combat round either to file and console or just to console
     * @param fh file handler object
     * @param crRng ranged weapon object
     * @param mob the target mob
     */
    private static void rangedAttack(FileHandling fh, CombatRound crRng, String mob, String timestamp) {
        String rangedType = cs.getRanged().getWeaponType();
        sd.rangedSwingCounter++;
        rangedDamage = crRng.attack(cs.getRanged(), rangedType, cs.getOffenseRanged());
        String ra;
        if (fullCombatTextToFile) {
            if (rangedDamage != 0) {
                if (CombatRound.isCritHit()) {
                    sd.rangedCritCounter++;
                    sd.rangedCritDamage += rangedDamage;
                    if (rangedDamage > sd.rangedMaximumCrit)
                        sd.rangedMaximumCrit = rangedDamage;
                    if (rangedDamage < sd.rangedMinimumCrit)
                        sd.rangedMinimumCrit = rangedDamage;

                    if (CombatRound.isCrippling()) {
                        ra = "[" + timestamp + "] " + playerName + " lands a Crippling Blow! (" + rangedDamage + ")\r\n";
                    } else {
                        ra = "[" + timestamp + "] " + playerName + " scores a critical hit! (" + rangedDamage + ")\r\n";
                    }
                } else {
                    sd.rangedDamage += rangedDamage;
                    if (rangedDamage > sd.rangedMaximumHit)
                        sd.rangedMaximumHit = rangedDamage;
                    if (rangedDamage < sd.rangedMinimumHit)
                        sd.rangedMinimumHit = rangedDamage;
                    ra = "[" + timestamp + "] " + "You " + attackType(cs.getRanged()) + " " + getCleanName(mob) + " for " + rangedDamage + " points of damage.\r\n";
                }
            } else {
                sd.rangedMissCounter++;
                ra = "[" + timestamp + "] " + "You try to " + attackType(cs.getRanged()) + " " + getCleanName(mob) + ", but miss!\r\n";
            }
        } else {
            if (rangedDamage != 0) {
                if (CombatRound.isCritHit()) {
                    sd.rangedCritCounter++;
                    sd.rangedCritDamage += rangedDamage;
                    if (rangedDamage > sd.rangedMaximumCrit)
                        sd.rangedMaximumCrit = rangedDamage;
                    if (rangedDamage < sd.rangedMinimumCrit)
                        sd.rangedMinimumCrit = rangedDamage;

                    if (CombatRound.isCrippling()) {
                        ra = "[" + timestamp + "] ranged " + rangedDamage + " crippling\r\n";
                    } else {
                        ra = "[" + timestamp + "] ranged " + rangedDamage + " critical\r\n";
                    }
                } else {
                    sd.rangedDamage += rangedDamage;
                    if (rangedDamage > sd.rangedMaximumHit)
                        sd.rangedMaximumHit = rangedDamage;
                    if (rangedDamage < sd.rangedMinimumHit)
                        sd.rangedMinimumHit = rangedDamage;
                    ra = "[" + timestamp + "] ranged " + rangedDamage + " \r\n";
                }
            } else {
                sd.rangedMissCounter++;
                ra = "[" + timestamp + "] ranged miss\r\n";
            }
        }
        if (reportToConsole) {
            System.out.print(ra);
        } else if (writeToFile) {
            fh.writeToFile(fileToWriteTo, ra);
        }
    }
    /**
     * Outputs ranged proc damage if it is greater than 0 and a DD
     * @param fh file handler object
     * @param ranged mainhand weapon object
     * @param mob the target mob
     * @param timestamp start time plus simulated parse time
     */
    private static void rangedProc(FileHandling fh, Weapon ranged, String mob, String timestamp) {
        String rd = "";
        sd.rangedProcCounter++;
        if (fullCombatTextToFile) {
            if (rangedProcDamage > 0 || rangedDamage == -1) {
                if (rangedProcDamage > 0) {
                    if (rangedProcDamage > sd.rangedMaximumDD)
                        sd.rangedMaximumDD = rangedProcDamage;
                    if (rangedProcDamage < sd.rangedMinimumDD)
                        sd.rangedMinimumDD = rangedProcDamage;
                    if (Weapon.isCritical()) {
                        sd.rangedProcCritCounter++;
                        sd.rangedProcCritDamage += rangedProcDamage;
                        rd += "[" + timestamp + "] " + playerName + " delivers a critical blast! ("
                                + rangedProcDamage + ")\r\n";
                    } else {
                        sd.rangedProcDD += rangedProcDamage;
                    }
                    rd += "[" + timestamp + "] " + getCleanName(mob) + " was hit by non-melee for "
                        + rangedProcDamage + " points of damage.\r\n";
                }
                rd += "[" + timestamp + "] " + getCleanName(mob) + ranged.getProc().getSpellTextOnHit() + "\r\n";
                if (rangedProcDamage == -1) {
                    rangedProcDamage--;
                }
            } else {
                sd.rangedProcResistCounter++;
                rd = "[" + timestamp + "] " + "Your target resisted the " + cs.getRanged().getProcName() + " spell.\r\n";
            }
        } else {
            if (rangedProcDamage > 0 || rangedProcDamage == -1) {
                if (rangedProcDamage > sd.rangedMaximumDD)
                    sd.rangedMaximumDD = rangedProcDamage;
                if (rangedProcDamage < sd.rangedMinimumDD)
                    sd.rangedMinimumDD = rangedProcDamage;
                if (Weapon.isCritical()) {
                    sd.rangedProcCritCounter++;
                    sd.rangedProcCritDamage += rangedProcDamage;
                } else {
                    sd.rangedProcDD += rangedProcDamage;
                }
                rd = "[" + timestamp + "]" + ranged.getProc().getSpellTextOnHit() + "\r\n";
                if (rangedProcDamage > 0) {
                    rd += "[" + timestamp + "] non-melee " + rangedProcDamage + "\r\n";
                }
                if (rangedProcDamage == -1) {
                    rangedProcDamage--;
                }
            } else {
                sd.rangedProcResistCounter++;
                rd = "[" + timestamp + "] resist\r\n";
            }
        }
        if (reportToConsole) {
            System.out.print(rd);
        } else if (writeToFile) {
            fh.writeToFile(fileToWriteTo, rd);
        }
    }

    /**
     * Outputs dot damage for ranged proc
     * @param fh file handler object
     * @param mob the target mob
     * @param procName the name of the proc
     * @param timestamp start time plus simulated parse time
     */
    private static void rangedDot(FileHandling fh, String mob, String procName, String timestamp) {
        rangedDotTics--;
        String rdt = "";
        sd.rangedProcDot += rangedDotDamage;
        if (fullCombatTextToFile) {
            rdt = "[" + timestamp + "] " + getCleanName(mob) + " has taken "
                    + rangedDotDamage + " from your " + procName + ".\r\n";
            if (rangedDotTics == 0) {
                rdt += "[" + timestamp + "] Your " + procName + " spell has worn off.\r\n";
            }
        } else {
            rdt = "[" + timestamp + "] " + procName + " " + rangedDotDamage + "\r\n";
            if (rangedDotTics == 0) {
                rdt += "[" + timestamp + "] " + procName + " off\r\n";
            }
        }
        if (reportToConsole) {
            System.out.print(rdt);
        } else if (writeToFile) {
            fh.writeToFile(fileToWriteTo, rdt);
        }
    }

    /**
     * Setter method for selecting which discipline to use
     * @param discipline the discipline to use
     */
    public static void setDiscipline(int discipline) {
        Controller.discipline = discipline;
    }

    /**
     * Setter method to set player name
     * @param playerName player's name
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Setter method to enable write to file
     * @param writeEachSwing set to true to enable writing to file
     */
    public static void setFullCombatText(boolean writeEachSwing) {
        Controller.fullCombatTextToFile = writeEachSwing;
    }

    /**
     * Setter method to enable print to console
     * @param reportToConsole set to true to enable reporting to console
     */
    public static void setReportToConsole(boolean reportToConsole) {
        Controller.reportToConsole = reportToConsole;
    }

    /**
     * Setter method to enable fully parsed output
     * @param b true if summarized output is desired
     */
    private void setSummarizedOutput(boolean b) {
        summarizedOutput = b;
    }

    /**
     * Rolls between 0 and 99 (100 total) to try and
     * roll under the argument number
     * @param required the number to roll below
     * @return true if rolls below the argument
     */
    public static boolean random(int required) {
        Random randGen = new Random();
        randGen.setSeed(randGen.nextInt(Integer.MAX_VALUE));

        return randGen.nextInt(100) < required;
    }

    /**
     * Generates a random number between min and max
     * @param min the minimum number to roll between
     * @param max the maximum number to roll between
     * @return the random result between min and max
     */
    public static int random(int min, int max) {
        Random randGen = new Random();
        randGen.setSeed(randGen.nextInt(Integer.MAX_VALUE));

        return randGen.nextInt((max - min) + 1) + min;
    }

    /**
     * Random generator that is true if it rolls below the argument
     * @param required the argument number to roll under
     * @return true if the random float is below the target number
     */
    public static boolean random(float required) {
        Random randGen = new Random();
        randGen.setSeed(randGen.nextInt(Integer.MAX_VALUE));
        return randGen.nextFloat() < required;
    }

    /**
     * Random generator that rolls between 0 and the argument
     * @param max the number to roll up to
     * @return random value between 0 and the argument
     */
    public static int roll0(int max) {
        Random randGen = new Random();
        randGen.setSeed(randGen.nextInt(Integer.MAX_VALUE));
        return randGen.nextInt(max + 1);
    }

    /**
     * Getter method that gets the target mob
     * @return the target mob
     */
    public static Mob getTargetMob() {
        return targetMob;
    }

    /**
     * Setter method that sets the target mob
     * @param targetMob the desired target mob
     */
    public static void setTargetMob(Mob targetMob) {
        Controller.targetMob = targetMob;
    }

    /**
     * Getter method to get the database
     * @return the database
     */
    public static String getDb() {
        return db;
    }

    /**
     * Setter method to set the database string
     * @param db the database
     */
    public void setDb(String db) {
        Controller.db = db;
    }

    /**
     * This method grabs tables from the db and
     * returns them as a string
     * @param tableName the name of the table to grab
     * @param DBToScan the database to scan
     * @return the table loaded into a string
     */
    public static String grabTableInDB(String tableName, String DBToScan) {
        int tableIndexStart, tableIndexEnd;
        String tempDB;
        tableIndexStart = DBToScan.indexOf(tableName) - 14;
        tempDB = DBToScan.substring(tableIndexStart);
        tableIndexEnd = tempDB.indexOf("CREATE", 20);
        return tempDB.substring(0, tableIndexEnd);
    }

    /**
     * Getter method that gets the current expansion
     * @return the current expansion
     */
    public static String getExpansion() {
        return Expansion;
    }

    /**
     * Setter method that sets the current expansion
     * @param expansion the current expansion
     */
    public void setExpansion(String expansion) {
        Expansion = expansion;
    }

    /**
     * Setter method that sets the character sheet
     * @param cs the character sheet
     */
    public void setCharacterSheet(CharacterSheet cs) {
        Controller.cs = cs;
    }

    /**
     * Getter method that gets the character sheet
     * @return the character sheet
     */
    public static CharacterSheet getCharacterSheet(){
        return cs;
    }

    /**
     * Method used for parsing the DB for specific attributes
     * @param str the string to be parsed
     * @param substr the substring to look for
     * @param n the iteration of the substring to look for
     * @return the substring beyond the desired point
     */
    public static int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
    }

    /**
     * This method counts the number of commas in a string
     * @param str the string to count the commas
     * @return the number of commas
     */
    public static int numCommas(String str) {
        int totalCommas = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ',') {
                totalCommas++;
            }
        }
        return totalCommas;
    }

    /**
     * Setter method that enables bard 25% v3 haste and attack songs plus epic proc
     * @param bard true if bard is pumping the group
     */
    public static void setBard(boolean bard) {
        Controller.bard = bard;
    }

    /**
     * Setter method if a beastlord is giving you the juice
     * @param beastlord true if you have savagery
     */
    public static void setBeastlord(boolean beastlord) {
        Controller.beastlord = beastlord;
    }

    /**
     * Setter method to enable ancient avtar over primal proc/normal avatar
     * @param ancientAvatar true if ancient avtar is being cast on you
     */
    public static void setAncientAvatar(boolean ancientAvatar) {
        Controller.ancientAvatar = ancientAvatar;
    }

    /**
     * Getter method for the custom AC
     * @return custom AC
     */
    public static int getCustomAC() {
        return customAC;
    }

    public static boolean isUseCustomAC() {
        return useCustomAC;
    }

    /**
     * Sets custom mob AC
     * @param customAC the amount of AC to use
     */
    private void setCustomAC(int customAC) {
        Controller.customAC = customAC;
    }

    /**
     * Toggles on using custom AC values instead of actual mob AC
     * @param useCustomAC true if custom AC is desired
     */
    public static void setUseCustomAC(boolean useCustomAC) {
        Controller.useCustomAC = useCustomAC;
    }
    /**
     * Getter method to get the custom resist amount
     * @return custom resist applied to all of a mob's resists
     */
    public static int getCustomResists() {
        return customResists;
    }

    /**
     * Setter method to set the level of resists
     * @param customResists mob resist all amount
     */
    public void setCustomResists(int customResists) {
        Controller.customResists = customResists;
    }

    /**
     * Determines if to use actual resists or custom
     * @return true if custom resists is desired
     */
    public static boolean isUseCustomResists() {
        return useCustomResists;
    }

    /**
     * Setter method to set to true to use custom resists
     * @param useCustomResists true if custom resists is enabled
     */
    public void setUseCustomResists(boolean useCustomResists) {
        Controller.useCustomResists = useCustomResists;
    }

    /**
        Spell attack stacking
        VoG - 20
        Bih`Li - 15
        Feral avatar - 125 or Avatar - 100
        Dance of the blade - 30
        McVaxius with 2.8 horn - 28 epic only - 18 no horn - 10, supposedly 44 with max aa
        Ring10 - 10
        Savagery - 100

        Doesn't stack with everything:
        CotP - 40
        SoN - 25

        Self only:
        Warder's Protection - 70
        Natures' Precision - 15
        Bard Jonathan's - 50
        Monk Epic - 40
     */
    public void setSpellAttack() {
        spellAttack = 210;

        if(bard) {
            spellAttack += 74;
        }
        if(beastlord) {
            spellAttack += 100;
        }
        if(ancientAvatar){
            spellAttack += 25;
        }

        switch(characterClass) {
            case "Ranger" :
                spellAttack += 20;
                break;
            case "Monk" :
                spellAttack += 15;
                break;
            case "Bard" :
                spellAttack += 50;
                break;
        }
    }

    /**
     * Setter method for worn attack
     * @param attack total worn attack
     */
    private void setWornAttack(int attack) {
        wornAttack = attack;
    }

    /**
     * Setter method to set weapons and special attack, none means unequipped or no special attack
     * @param primary the primary weapon to be used
     * @param secondary the secondary weapon to be used
     * @param ranged the ranged weapon to be used
     * @param specialAttack the special attack to be used
     */
    public static void setWeaponsAndSpecial(String primary, String secondary, String ranged, String specialAttack) {
        Controller.primary = primary;
        Controller.secondary = secondary;
        Controller.ranged = ranged;
        Controller.specialAttack = specialAttack;
    }

    /**
     * Setter method to set arrow damage
     * @param arrowDamage base damage of the arrow
     * @param arrowElementalDamage elemental damage of the arrow
     * @param arrowElementalType Spell resist types 12345 mr fr cr pr dr
     */
    public void setArrows(int arrowDamage, int arrowElementalDamage, int arrowElementalType) {
        this.arrowDamage = arrowDamage;
        this.arrowElementalDamage = arrowElementalDamage;
        this.arrowElementalType = arrowElementalType;
    }

    /**
     * @param ambidexterity adds 32 DW skill when AA is active (set to 1 for no bonus)
     * @param combatFury Combat Fury 1 2 3: 15, 40, 75. FotA1 2 3: 100, 125, 150
     * @param archeryMastery Ranger archery mastery AA 1 2 3
     * @param spellCastingFury caster spell crit AA r1 2 3
     * @param flurry Warrior Flurry AA r1 2 3: 7%, 11%, 15%
     * @param berserk Warrior low health berserk true/false
     */
    public void setBerserkAndAA(int ambidexterity, int combatFury, int archeryMastery, int spellCastingFury,
                                                    int flurry, boolean berserk) {
        this.ambidexterity = ambidexterity;
        this.combatFury = combatFury;
        this.flurry = flurry;
        this.spellCastingFury = spellCastingFury;
        Controller.berserk = berserk;
        if (archeryMastery == 3) {
            archeryMultipliers = 4.0; // 2x for AM3 and 2x for a mob that isn't moving
        } else if (archeryMastery == 2) {
            archeryMultipliers = 3.2; // 1.6x for AM3 and 2x for a mob that isn't moving
        } else if (archeryMastery == 1) {
            archeryMultipliers = 2.6; // 1.3x for AM3 and 2x for a mob that isn't moving
        } else {
            archeryMultipliers = 2.0; //2x for a mob that isn't moving
        }
    }

    /**
     * Setter method to set skill mods for archery and backstab
     * @param archeryMod skill modifier for archery
     * @param backstabMod skill modifier for backstab
     */
    public void setSkillMods(double archeryMod, double backstabMod) {
        this.archeryMod = archeryMod;
        this.backstabMod = backstabMod;
    }

    /**
     * Setter method to set skill mods for piercing
     * @param mod skill modifier for piercing
     */
    public void setPiercingMod(double mod) {
        this.piercingMod = mod;
    }

    /**
     * Getter method for piercing mod
     * @return piercing mod
     */
    public static double getPiercingMod() {
        return piercingMod;
    }

    /**
     * Setter method to set haste, bard adds 25% v3
     */
    public void setWornSpellHastePreBard(int h) {
        haste = h;
        if(bard) {
            haste += 25;
        }
    }

    /**
     * Setter method that sets character class
     * @param characterClass the class of the player character
     */
    public static void setClass(String characterClass) {
        Controller.characterClass = characterClass;
    }

    /**
     * Method to calculate hasted weapon delay
     * @param weapon the weapon object used
     * @return hasted delay
     */
    public double hastedDelay(Weapon weapon) {

        double hastedDelay = weapon.getDelay()/((100+(double)cs.getHaste()))*10;
        if(weapon.getWeaponType().equals("Archery")) {
            double bow_delay_reduction = 0.15 * hastedDelay + 0.01;
            if (hastedDelay - bow_delay_reduction > 1) {
                hastedDelay -= bow_delay_reduction;
            }
        }
        return hastedDelay;
    }

    /**
     * Setter method to set hasted weapon delay
     * @param specialAttack special attack object
     */
    public void setSpecialDelay(SpecialAttack specialAttack) {
        specialHastedDelay = (int)((specialAttack.getDelay()/(100+(double)cs.getHaste()))*10000);
    }

    /**
     * Get chance to double attack
     * @param attackType attack type (primary, secondary, backstab)
     * @return double attack chance
     */
    public static double doubleChance(String attackType) {
        if(cs.getDoubleAttack() > 0 && (attackType.equals("primary") || attackType.equals("secondary") ||
                (attackType.equals("Backstab") && cs.getLevel() > 54))) {
            return (cs.getDoubleAttack() + cs.getLevel()) / 500.0;
        }
        return 0.0;
    }

    /**
     * Get chance to triple attack
     * @param attackType attack type (primary)
     * @return chance to triple attack
     */
    public static double tripleChance(String attackType) {
        if (attackType.equals("primary") && cs.getLevel() >= 60 &&
                (CharacterSheet.getCharacterClass().equals("Warrior") || CharacterSheet.getCharacterClass().equals("Monk"))) {
            return (135.0/1000.0);
        }
        return 0.0;
    }

    /**
     * Get chance to flurry
     * @param attackType attack type (primary)
     * @return chance to flurry
     */
    public static double flurryChance(String attackType) {
        if (attackType.equals("primary") && cs.getFlurry() > 0) {
            return (cs.getFlurry()/100.0);
        }
        return 0.0;
    }

    /**
     * Percent chance for extra attacks for monks and paladins based on aa rank
     * @param aaRank the rank of AA for either punishing blade or speed of the knight aa
     * @return the percent chance for an extra attack
     */
    public void setPunishingBlade(int aaRank) {
        if (characterClass.equals("Monk") || characterClass.equals("Paladin")) {
            switch (aaRank) {
                case 1 :
                    punishOrKnightExtraAttack = 2;
                    break;
                case 2 :
                    punishOrKnightExtraAttack = 4;
                    break;
                case 3 :
                    punishOrKnightExtraAttack = 8;
                    break;
                default :
                    punishOrKnightExtraAttack = 0;
                    break;
            }
        }
    }

    /**
     * Converts the weapon type to a verb
     * 1hs/2hs slashing
     * 1hb/2hb crush
     * 1hp/2hp pierce
     * h2h punch
     * @param weapon weapon in use
     * @return the verb of the attack type
     */
    public static String attackType(Weapon weapon) {
        switch(weapon.getWeaponType()) {
            case("1HSlashing") :
            case("2HSlashing") :
                return "slash";
            case("1HBlunt") :
            case("2HBlunt") :
                return "crush";
            case("1HPiercing") :
            case("2HPiercing") :
                return "pierce";
            case("Archery") :
                return "shoot";
            case("Throwing") :
                return "hit";
            default :
                return "punch";
        }
    }

    /**
     * replaces all extra characters from mob's names
     * @param mobName the database entry for mob name
     * @return a cleaned string version of the mob name
     */
    public static String getCleanName(String mobName) {
        return mobName.replaceAll("[_\\\\]", " ");
    }

    /**
     * Sets the tick damage for primary procs
     * @param damage primary weapon proc tic damage
     * @param tics total duration of the dot
     */
    public static void setPrimaryDotDamage(int damage, int tics) {
        primaryDotDamage = damage;
        primaryDotTics = tics;
    }

    /**
     * Sets the tick damage for secondary procs
     * @param damage secondary weapon proc tic damage
     * @param tics total duration of the dot
     */
    public static void setSecondaryDotDamage(int damage, int tics) {
        secondaryDotDamage = damage;
        secondaryDotTics = tics;
    }

    /**
     * Sets the tick damage for ranged procs
     * @param damage ranged weapon proc tic damage
     * @param tics total duration of the dot
     */
    public static void setRangedDotDamage(int damage, int tics) {
        rangedDotDamage = damage;
        rangedDotTics = tics;
    }

    /**
     * Checks if berserk is enabled
     * @return true if berserk is enabled
     */
    public static boolean isBerserk() {
        return berserk;
    }

    /**
     * Getter method to see if wind of tash is enabled
     * @return true if wind of tash is enabled
     */
    public static boolean isWindOfTash() {
        return windOfTash;
    }

    /**
     * Setter method to enable wind of tash
     * Subtracts 40 mr
     * @param windOfTash true if wind of tash is enabled
     */
    public static void setWindOfTash(boolean windOfTash) {
        Controller.windOfTash = windOfTash;
    }

    /**
     * Getter method to see if malo is enabled
     * @return true if malo is enabled
     */
    public static boolean isMalo() {
        return malo;
    }

    /**
     * Setter method to enable malo
     * Subtracts 45 to mr/cr/fr/dr
     * @param malo true if malo is enabled
     */
    public static void setMalo(boolean malo) {
        Controller.malo = malo;
    }

    /**
     * Getter method for spellCastingFuryAA
     * Rank 1: 2% chance to crit
     * Rank 2: 4% chance to crit
     * Rank 3: 7% chance to crit
     * @return the AA level of spellCastingFury
     */
    public static int getSpellCastingFury() {
        return spellCastingFury;
    }

    /**
     * Setter method to set the path of the output file
     * @param fileToWriteTo sets the output file
     */
    public void setFileToWriteTo(String fileToWriteTo) {
        this.fileToWriteTo = fileToWriteTo;
    }

    /**
     * Setter method that enables file writing
     * @param b true if write to file
     */
    private void setWriteToFile(boolean b) {
        writeToFile = b;
    }

    /**
     * Setter method to set the fight length
     * @param v the time to parse
     */
    private void setTimeToParse(long v) {
        timeToParse = v;
    }

    /**
     * Setter that sets delay that is used in conjunction with casting
     * @param reactionTimePlusPing reaction time plus ping in milliseconds
     */
    public static void setReactionTimePlusPing(long reactionTimePlusPing) {
        Controller.reactionTimePlusPing = reactionTimePlusPing;
    }

    /**
     *
     * @return true if the sim is attempting to maintain mana ratio equal to
     */
    public static boolean isManaRatioEqualToBossHP() {
        return manaRatioEqualToBossHP;
    }

    public static void setManaRatioEqualToBossHP(boolean manaRatioEqualToBossHP) {
        Controller.manaRatioEqualToBossHP = manaRatioEqualToBossHP;
    }
}
