package main.model;

import main.controller.Controller;

import java.util.ArrayList;

/**
 * This class represents an in game weapon
 */
public class Weapon {
    private int damage;
    private int delay;
    private int classes;
    private int procRate, procSpellID;
    private int elementalDamage, elementalType;
    private static int casterLevel;
    private String weaponName;
    private String weaponType;
    private String procName;
    private String items;
    public ArrayList<String> classList;
    private Spell proc;
    private int[] effect_value;
    private double procChance = 0.0;
    private String slot;
    private static boolean critical = false;

    /**
     * Default constructor for the Weapon class
     * @param weaponName the name of the weapon
     * @param charLevel the level of the character
     * @param slot the slot of the weapon
     * @param dwc dual wield chance used in proc offhand calcs
     */
    public Weapon(String weaponName, int charLevel, String slot, float dwc) {
        setWeaponName(weaponName);
        setItemTable();
        setDBString();
        casterLevel = charLevel;
        this.slot = slot;
        damage = getIntElement(25);
        classes = getIntElement(21);
        delay = getIntElement(27);
        procSpellID = getIntElement(82);
        if (procSpellID != -1 && procSpellID != 2434) {
            proc = new Spell(procSpellID);
            procName = proc.getSpellName();
            effect_value = new int[12];
            calculateProcChance(dwc);
        }
        procRate = getIntElement(61);
        weaponType = weapTypetoString(getIntElement(50));
        elementalDamage = getIntElement(32);
        elementalType = getIntElement(31);
        ClassBitmask cb = new ClassBitmask(classes);
        classList = cb.getClasses();
        setWeaponCleanName();
    }

    /**
     * Getter method to access proc chance
     * @return proc chance of the weapon
     */
    public double getProcChance() {
        return procChance;
    }

    /**
     * Gets the integer value at the index of Weapon table
     * @param index index of element
     * @return int value at the index
     */
    public int getIntElement(int index) {
        String tempString, tempString2, tempString3;
        int endIndex;
        tempString = items.substring(Controller.ordinalIndexOf(items, ",", index));
        tempString2 = tempString.substring(1,100);
        endIndex = tempString2.indexOf(",");
        tempString3 = tempString2.substring(0, endIndex);
        return Integer.parseInt(tempString3);
    }

    /**
     * Getter method for the name of the proc
     * @return proc name
     */
    public String getProcName() {
        return procName;
    }

    /**
     * Getter method for elemental weapon damage
     * @return elemental weapon damage
     */
    public int getElementalDamage() {
        return elementalDamage;
    }

    /**
     * Getter method for elemental weapon damage type
     * @return elemental weapon damage type
     */
    public int getElementalType() {
        return elementalType;
    }

    /**
     * setter method for shortening the db table string
     * towards the desired weapon
     */
    public void setDBString() {
        int index;
        index = items.indexOf(weaponName + "'");
        items = items.substring(weaponName.length() + index);
    }

    /**
     * Setter method for getting the item db table
     */
    public void setItemTable() {
        items = Controller.grabTableInDB("items` (", Controller.getDb());
    }

    /**
     * Getter method for weapon damage
     * @return weapon damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Getter method for weapon delay
     * @return the weapon delay
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Getter method for the classes that can use
     * the weapon
     * @return the classes that can use the weapon
     */
    public int getClasses() {
        return classes;
    }

    /**
     * Getter method for the weapon type
     * @return the type of weapon
     */
    public String getWeaponType() {
        return weaponType;
    }

    /**
     * Getter method for weapon name
     * @return the weapon name
     */
    public String getWeaponName() {
        return weaponName;
    }

    /**
     * Setter method for weapon name
     * @param weaponName the name of the weapon
     */
    public void setWeaponName(String weaponName) {

        this.weaponName = weaponName;
    }

    /**
     * Removes \ from weapon names
     */
    public void setWeaponCleanName() {
        for(int i = 0; i < weaponName.length(); i++) {
            if (weaponName.charAt(i) == '\\') {
                weaponName = weaponName.substring(0,i) + weaponName.substring(i+1,weaponName.length());
            }
        }
    }

    /**
     * Getter method for proc rate
     * @return the proc rate of the weapon
     */
    public int getProcRate() {
        return procRate;
    }

    /**
     * Getter method that returns the spell tied to the weapon
     * @return the spell tied to the weapon (proc)
     */
    public Spell getProc() {
        return proc;
    }

    /**
     * Converts the skill type number to a string
     * @param itemType the skill used for the weapon
     * @return the string of the skill used for the weapon
     */
    public String weapTypetoString (int itemType) {
        switch(itemType) {
            case 0:
                return "1HSlashing";
            case 1:
                return "2HSlashing";
            case 2:
                return "1HPiercing";
            case 3:
                return "1HBlunt";
            case 4:
                return "2HBlunt";
            case 5:
                return "Archery";
            case 45:
                return "HandtoHand";
            default:
                return "2HPiercing";
        }
    }

    /**
     * Handles weapon procs
     * @return damage dealt by the proc
     */
    public int doProc(Mob targetMob) {
        int damage = 0;
        int indexForMaxDamage = 0;
        float resist = Resists.checkResistSpell(proc.getResistType(),
                proc.getSpellID(), casterLevel, targetMob, false, 0, false);
        // Check for resist
        if (resist > 0) {
            // Calculate spell effect values
            for(int i = 0; i < 12; i++) {
                if (proc.getBase()[i].intValue() != 0) {
                    if (proc.getFormula()[i] == 100) {
                        effect_value[i] = -1 * proc.getBase()[i].intValue();
                    } else {
                        effect_value[i] = -1 * proc.calcSpellEffectValue(proc.getFormula()[i], proc.getBase()[i].intValue(),
                                proc.getMax()[i], casterLevel, proc.getSpellID(), 0);
                    }
                }
            }
            // Apply effective values
            for (int i = 0; i < effect_value.length; i++) {
                // DD or initial damage caused by a dot
                if (effect_value[i] != 0 && proc.getEffectid()[i] == 79 ||
                        (proc.getEffectid()[i] == 0 && proc.getBuffDuration() == 0)) {
                    damage = (int) ((effect_value[i] * (resist/100)));
                    indexForMaxDamage = i;
                // Dot tick damage
                } else if (effect_value[i] != 0 && proc.getEffectid()[i] == 0 && proc.getBuffDuration() > 0) {
                    damage = -1;
                    if (slot.equals("primary")) {
                        Controller.setPrimaryDotDamage(effect_value[i],
                                proc.buffDuration(casterLevel, proc.getBuffDurationFormula(), proc.getBuffDuration()));
                    } else if (slot.equals("secondary")) {
                        Controller.setSecondaryDotDamage(effect_value[i],
                                proc.buffDuration(casterLevel, proc.getBuffDurationFormula(), proc.getBuffDuration()));
                    } else {
                        Controller.setRangedDotDamage(effect_value[i],
                                proc.buffDuration(casterLevel, proc.getBuffDurationFormula(), proc.getBuffDuration()));
                    }
                }
            }
        }
        return getActSpellDamage(proc.getSpellID(), damage, proc.getMax()[indexForMaxDamage]);
    }

    //

    /**
     * Handle crits and apply AA and disc bonuses/modifiers to spell damage cast by clients. dmg should be negative
     * @param spell_id the spell ID
     * @param dmg the amount of damage from the spell
     * @param maxHit the maximum hit of the spell
     * @return damage dealt plus crits
     */
    public int getActSpellDamage(int spell_id, int dmg, int maxHit) {
        critical = false;
        // TODO add harm touch AA, currently nots ure what spell IDs
        //  if (spell_id == SPELL_IMP_HARM_TOUCH)	// Improved Harm Touch AA skill
        //     dmg -= GetAA(aaUnholyTouch) * 450;	// Unholy Touch AA
        //
        //  if ((spell_id == SPELL_HARM_TOUCH || spell_id == SPELL_HARM_TOUCH2 || spell_id == SPELL_IMP_HARM_TOUCH) && HasInstantDisc(spell_id))		// Unholy Aura disc; 50% dmg is guaranteed
        //      dmg = dmg * 150 / 100;

        String item_name;
        int focusDmg = 0;

        // TODO implement spell focus
        //  focusDmg = dmg * GetFocusEffect(focusImprovedDamage, spell_id, item_name) / 100;
        //  if (focusDmg)


        // TODO more SK stuff: SK AA Soul Abrasion; only SKs get something with focusSpellDamageMult
        // the AA bonus only applies to spells with spellgroup 99, so don't need spell ID check here
        //  if (CharacterSheet.getCharacterClass().equals("ShadowKnight")) {
        //      dmg += dmg * GetFocusEffect(focusSpellDamageMult, spell_id, item_name) / 100;
        //  }

                //Quarm implementation but we don't really have spell or item bonuses that give crit
        //int critChanceAA = itembonuses.CriticalSpellChance + spellbonuses.CriticalSpellChance + aabonuses.CriticalSpellChance;
        //Spell casting fury 2 4 and 7 percent for ranks 1 2 and 3
        int critChanceAA = Controller.getSpellCastingFury();


        switch(critChanceAA) {
            case(1) :
                critChanceAA = 2;
                break;
            case(2) :
                critChanceAA = 4;
                break;
            case(3) :
                critChanceAA = 7;
                break;
            default :
                critChanceAA = 0;
        }

        if (Controller.random(critChanceAA)) {
            critical = true;
        }

        // Improved Harm Touch is a guaranteed crit if you have at least one level of SCF.
        //  if (spell_id == SPELL_IMP_HARM_TOUCH && (GetAA(aaSpellCastingFury) > 0) && (GetAA(aaUnholyTouch) > 0))
        //      critical = true;

        if (critical)
        {
            int mult = 100;
            if (critChanceAA == 1)	// lower ranks do not do double damage
                mult = 33;
            else if (critChanceAA == 2)
                mult = 66;

            dmg += dmg * mult / 100 + focusDmg;		// focused damage is not multiplied by crit

        }
        else if (CharacterSheet.getCharacterClass().equals("Wizard"))
            dmg = tryWizardInnateCrit(dmg, focusDmg, maxHit);
        else
            dmg += focusDmg;

        return dmg;
    }

    /**
     * Used for innate wizard crits
     * @param damage spell damage pre crit
     * @param focusDmg damage from focus effects
     * @param maxHit max hit of the spell
     * @return damage post wizard crit if crit is successful or non crit damage
     */
    public int tryWizardInnateCrit(int damage, int focusDmg, int maxHit)
    {
        //RULE_INT ( Spells, WizCritLevel, 12, "level wizards first get spell crits")
        if (CharacterSheet.getCharacterClass().equals("Wizard") && CharacterSheet.getLevel() >= 12)
        {
            double wizCritChance = (((Math.min(CharacterSheet.getIntelligence(), 255)
                                + Math.min(CharacterSheet.getDexterity(), 255)) / 2.0) + 32.0) / 10000.0;
            boolean critSuccess = Controller.random((float)wizCritChance);

            if (critSuccess) {
                critical = true;
                int mult = Controller.random(1, 50);

                damage += damage * mult / 100 + focusDmg;

                if (maxHit > 0 && damage < maxHit)
                    damage = maxHit;
            } else {
                damage += focusDmg;
            }
        }
        return damage;
    }

    /**
     * Setter method to calculate proc chance of the weapon
     */
    public void calculateProcChance(float dwc) {
        double dex = CharacterSheet.getDexterity();
        if (dex > 255.0)
            dex = 255.0;

        procChance = ((0.0004166667 + (0.000011437908496732 * dex)) * (float)(delay/(1 + CharacterSheet.getHaste()/100.0)));
        if (slot.equals("secondary")) {
            procChance *= 0.5 / (dwc);
        }
    }

    /**
     * Getter method to check if a proc was a spell crit
     * @return true if spell crit was successful
     */
    public static boolean isCritical() {
        return critical;
    }
}
