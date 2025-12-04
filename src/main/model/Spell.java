package main.model;

import main.controller.Controller;

/**
 * Spell class for weapon procs and cast spells
 */
public class Spell {
    private int spellID, manaCost, resistType, skill, resistDiff, aoeDuration;
    private int buffDurationFormula, buffDuration;
    private double recastTime, recoveryTime, castTime;
    private String allSpells, spell, name, spellTextOnHit, spellFades;
    private Number[] base;
    private int partialCapable;
    private int[] formula, max, effectid;

    /**
     * Default constructor for spells
     * @param spellID the ID of the spell
     */
    public Spell(int spellID) {
        base = new Number[12];
        max = new int[12];
        formula = new int[12];
        effectid = new int[12];
        this.spellID = spellID;
        setSpellDB();
        setSpellEntry();
        name = getStringElement(1);
        spellTextOnHit = getStringElement(7);
        spellFades = getStringElement(8);
        cleanStrings();
        castTime = getIntElement(13);
        recoveryTime = getIntElement(14);
        recastTime = getIntElement(15);
        buffDurationFormula = getIntElement(16);
        buffDuration = getIntElement(17);
        aoeDuration = getIntElement(18);
        manaCost = getIntElement(19);
        resistType = getIntElement(73);
        skill = getIntElement(88);
        resistDiff = getIntElement(134);
        partialCapable = getIntElement(138);
        for (int i = 0; i < 12; i++) {
            if (i != 2) {
                base[i] = getIntElement(20 + i);
            } else {
                base[i] = getFloatElement(20 + i);

            }
            max[i] = getIntElement(32 + i);
            formula[i] = getIntElement(58 + i);
            effectid[i] = getIntElement(74 + i);
        }
        //displaySpellStats();
    }

    /**
     * Display all the spell private member variables
     */
    public void displaySpellStats() {
        System.out.println("Spell ID: " + spellID);
        System.out.println("name: " + name);
        System.out.println("castTime: " + castTime);
        System.out.println("recoveryTime: " + recoveryTime);
        System.out.println("recastTime: " + recastTime);
        System.out.println("buffDurationFormula: " + buffDurationFormula);
        System.out.println("buffDuration: " + buffDuration);
        System.out.println("AoEDuration: " + aoeDuration);
        System.out.println("manaCost: " + manaCost);
        System.out.println("resistType: " + resistType);
        System.out.println("skill: " + skill);
        System.out.println("resistDiff: " + resistDiff);
        System.out.println("partialCapable: " + partialCapable);
        for (int i = 0; i < 12; i++) {
            System.out.println("base[" + (i+1) + "]: " + base[i]);
        }
        for (int i = 0; i < 12; i++) {
            System.out.println("max[" + (i+1) + "]: " + max[i]);
        }
        for (int i = 0; i < 12; i++) {
            System.out.println("formula[" + (i+1) + "]: " + formula[i]);
        }
        for (int i = 0; i < 12; i++) {
            System.out.println("effectid[" + (i+1) + "]: " + effectid[i]);
        }
    }

    /**
     * Setter method for getting the item db table
     */
    public void setSpellDB() {
        allSpells = Controller.grabTableInDB("spells_en` (", Controller.getDb());
    }

    /**
     * Setter method for shortening the table to the specified spell
     */
    public void setSpellEntry() {
        String temp = allSpells.substring(allSpells.indexOf("(" + spellID + ",'"));
        spell = temp.substring(0, temp.indexOf(")"));
    }

    /**
     * Gets the integer value at the index of spell table
     * @param index index of element
     * @return int value at the index
     */
    public int getIntElement(int index) {
        String tempString, tempString2;
        tempString = spell.substring(Controller.ordinalIndexOf(spell, ",", index) + 1);
        tempString2 = tempString.substring(0, tempString.indexOf(","));
        return Integer.parseInt(tempString2);
    }

    /**
     * Gets the integer value at the index of spell table
     * @param index index of element
     * @return int value at the index
     */
    public float getFloatElement(int index) {
        String tempString, tempString2;
        tempString = spell.substring(Controller.ordinalIndexOf(spell, ",", index) + 1);
        tempString2 = tempString.substring(0, tempString.indexOf(","));
        return Float.parseFloat(tempString2);
    }

    /**
     * Gets the String value at the index of spell table
     * @param index index of element
     * @return String value at the index
     */
    public String getStringElement(int index) {
        String tempString, tempString2;
        int endIndex;
        tempString = spell.substring(Controller.ordinalIndexOf(spell, ",", index));
        tempString2 = tempString.substring(1,60);
        endIndex = tempString2.indexOf(",");
        return tempString2.substring(0, endIndex);
    }

    /**
     * Removes single quotes off of DB strings
     */
    public void cleanStrings() {
        name = name.substring(1,name.length()-1);
        spellTextOnHit = spellTextOnHit.substring(1,spellTextOnHit.length()-1);
        spellTextOnHit = spellTextOnHit.replaceAll("\\\\", "");
        spellFades = spellFades.substring(1,spellFades.length()-1);
    }

    /**
     * Getter method for spell name
     * @return the name of the spell
     */
    public String getSpellName() {
       return name;
    }

    /**
     * Getter method for spell flavor text on hit
     * @return spell flavor text on spell landing
     */
    public String getSpellTextOnHit() {
        return spellTextOnHit;
    }

    /**
     * Getter method for spell flavor text on fade
     * @return spell flavor text on spell ending
     */
    public String getSpellFades() {
        return spellFades;
    }

    /**
     * Getter method that gets the spell ID
     * @return the spell ID
     */
    public int getSpellID() {
        return spellID;
    }

    /**
     * Getter method for spell mana cost
     * @return spell mana cost
     */
    public int getManaCost() {
        return manaCost;
    }

    /**
     * Getter method for spell duration
     * @return spell duration
     */
    public int getBuffDuration() {
        return buffDuration;
    }

    /**
     * Getter method for spell resist type
     * @return spell resist type
     */
    public int getResistType() {
        return resistType;
    }

    /**
     * Getter method for spell skill type
     * @return spell skill type
     */
    public int getSkill() {
        return skill;
    }

    /**
     * Getter method for spell resist modifier
     * @return spell resist modifier
     */
    public int getResistDiff() {
        return resistDiff;
    }

    /**
     * Getter method that returns the effect id array
     * @return the effect id array
     */
    public int[] getEffectid() {
        return effectid;
    }

    /**
     * Getter method that returns the base effect array
     * @return the base effect array
     */
    public Number[] getBase() {
        return base;
    }

    /**
     * Getter method that returns the formula array
     * @return the formula array
     */
    public int[] getFormula() {
        return formula;
    }

    /**
     * Getter method that returns the max array
     * @return the max array
     */
    public int[] getMax() {
        return max;
    }


    /**
     * Getter method to access AoE duration
     * @return aoeDuration value
     */
    public int getAoEDuration() {
        return aoeDuration;
    }

    /**
     * Checks the db value noPartials to see if the spell is capable of partial resists
     * @return true if the spell is capable of partial resists
     */
    public boolean partialCapable() {
        return partialCapable != 1;
    }

    /**
     * Getter method to return the buffdurationformula attribute of the spell in the database
     * @return buffdurationformula value of the database
     */
    public int getBuffDurationFormula() {
        return buffDurationFormula;
    }

    /**
     * Generic formula calculations
     * @param level caster level
     * @param formula database value of which formula to use
     * @param duration database value of buff duration
     * @return buff duration
     */
    public int buffDuration(int level, int formula, int duration)
    {
        int i; // temp variable

        if (formula >= 200)
        {
            return formula;
        }

        switch(formula)
        {
            case 0:	// not a buff
                return 0;

            case 1:
                i = level / 2;
                return i < duration ? (i < 1 ? 1 : i) : duration;

            case 2:
                i = level <= 1 ? 6 : level / 2 + 5;
                return i < duration ? (i < 1 ? 1 : i) : duration;

            case 3:
                i = level * 30;
                return i < duration ? (i < 1 ? 1 : i) : duration;

            case 4:
                i = 50;
                return (duration != 0) ? (i < duration ? i : duration) : i;

            case 5:
                i = 2;
                return (duration != 0)  ? (i < duration ? i : duration) : i;

            case 6:
                i = level / 2 + 2;
                return (duration != 0)  ? (i < duration ? i : duration) : i;

            case 7:
                i = level;
                return (duration != 0)  ? ((i < duration) ? i : duration) : i;

            case 8:
                i = level + 10;
                return i < duration ? (i < 1 ? 1 : i) : duration;

            case 9:
                i = level * 2 + 10;
                return i < duration ? (i < 1 ? 1 : i) : duration;

            case 10:
                i = level * 3 + 10;
                return i < duration ? (i < 1 ? 1 : i) : duration;

            case 11:
                i = level * 30 + 90;
                return i < duration ? (i < 1 ? 1 : i) : duration;

            case 12:	// not used by any spells
                i = level / 4;
                i = (i != 0) ? i : 1;
                return (duration != 0) ? i < duration ? i : duration : i;

            case 50:	// permanent buff
                return Integer.MAX_VALUE;

            default:
                return 0;
        }
    }

    /**
     * 0 = base
     * 1 - 99 = base + level * formulaID
     * 100 = base
     * 101 = base + level / 2
     * 102 = base + level
     * 103 = base + level * 2
     * 104 = base + level * 3
     * 105 = base + level * 4
     * 106 ? base + level * 5
     * 107 ? min + level / 2
     * 108 = min + level / 3
     * 109 = min + level / 4
     * 110 = min + level / 5
     * 119 ? min + level / 8
     * 121 ? min + level / 4
     * 122 = splurt
     * 123 ?
     * 203 = stacking issues ? max
     * 205 = stacking issues ? 105
     * 0x77 = min + level / 8
     * @param formula database value that determines which formula to use
     * @param base database base value
     * @param max database max value
     * @param caster_level the level of the caster
     * @param spell_id the spell id
     * @param ticsremaining remaining tics on the spell
     * @return spell effect value
     */
    public int calcSpellEffectValue(int formula, int base, int max, int caster_level, int spell_id, int ticsremaining) {

        int result = 0, updownsign, ubase = base;
        if(ubase < 0)
            ubase = 0 - ubase;
        /*
        this up-down thing might look messed up but if you look at the
        spells it actually looks like some have a positive base and max where
        the max is actually less than the base, hence they grow downward

        This seems to mainly catch spells where both base and max are negative.
        Strangely, damage spells have a negative base and positive max, but
        snare has both of them negative, yet their range should work the same:
        (meaning they both start at a negative value and the value gets lower)
        */
        if (max < base && max != 0) {
            // values are calculated down
            updownsign = -1;
        }
        else {
            // values are calculated up
            updownsign = 1;
        }

        switch(formula)
        {
            // 60 and 70 are used only in magician air pet stuns
            // This was making the stuns last only 30 milliseconds
            // commenting this out so it uses the default case, which works better.  No idea what it should be
            //case 60:
            //case 70:
            //	result = ubase / 100; break;
            case 0:
            case 100:	// confirmed 2/6/04
                result = ubase; break;
            case 101:	// confirmed 2/6/04
                result = updownsign * (ubase + (caster_level / 2)); break;
            case 102:	// confirmed 2/6/04
                result = updownsign * (ubase + caster_level); break;
            case 103:	// confirmed 2/6/04
                result = updownsign * (ubase + (caster_level * 2)); break;
            case 104:	// confirmed 2/6/04
                result = updownsign * (ubase + (caster_level * 3)); break;
            case 105:	// confirmed 2/6/04
                result = updownsign * (ubase + (caster_level * 4)); break;

            case 107: {
                // Client duration extension focus effects are disabled for spells that use this formula
                if (ticsremaining > 0)
                {
                    int ticdif = buffDuration(caster_level, buffDurationFormula, buffDuration) - (ticsremaining - 1);
                    if (ticdif < 0)
                        ticdif = 0;

                    result = updownsign * (ubase - ticdif);
                }
                else {
                    result = updownsign * ubase;
                }
                break;
            }
            case 108: {
                // Client duration extension focus effects are disabled for spells that use this formula
                if (ticsremaining > 0) {
                    int ticdif = buffDuration(caster_level, buffDurationFormula, buffDuration) - (ticsremaining - 1);
                    if (ticdif < 0)
                        ticdif = 0;

                    result = updownsign * (ubase - (2 * ticdif));
                } else {
                    result = updownsign * ubase;
                }
                break;
            }
            case 109:	// confirmed 2/6/04
                result = updownsign * (ubase + (caster_level / 4)); break;
            case 110:
                result = updownsign * (ubase + (caster_level / 6)); break;
            case 111:
                result = updownsign * (ubase + 6 * (caster_level - 16));
                break;
            case 112:
                result = updownsign * (ubase + 8 * (caster_level - 24));
                break;
            case 113:
                result = updownsign * (ubase + 10 * (caster_level - 34));
                break;
            case 114:
                result = updownsign * (ubase + 15 * (caster_level - 44));
                break;

            case 115:	// this is only in symbol of transal
                result = ubase;
                if (caster_level > 15)
                    result += 7 * (caster_level - 15);
                break;
            case 116:	// this is only in symbol of ryltan
                result = ubase;
                if (caster_level > 24)
                    result += 10 * (caster_level - 24);
                break;
            case 117:	// this is only in symbol of pinzarn
                result = ubase;
                if (caster_level > 34)
                    result += 13 * (caster_level - 34);
                break;
            case 118:	// used in naltron and a few others
                result = ubase;
                if (caster_level > 44)
                    result += 20 * (caster_level - 44);
                break;

            case 119:	// confirmed 2/6/04
                result = ubase + (caster_level / 8); break;
            case 120: {
                // Client duration extension focus effects are disabled for spells that use this formula
                if (ticsremaining > 0) {
                    int ticdif = buffDuration(caster_level, buffDurationFormula, buffDuration) - (ticsremaining - 1);
                    if (ticdif < 0)
                        ticdif = 0;

                    result = updownsign * (ubase - (5 * ticdif));
                } else {
                    result = updownsign * ubase;
                }
                break;
            }
            case 121:	// corrected 2/6/04
                result = ubase + (caster_level / 3); break;
            case 122: {
                // Client duration extension focus effects are disabled for spells that use this formula
                if (ticsremaining > 0) {
                    int ticdif = buffDuration(caster_level, buffDurationFormula, buffDuration) - (ticsremaining - 1);
                    if (ticdif < 0)
                        ticdif = 0;

                    result = updownsign * (ubase - (12 * ticdif));
                } else {
                    result = updownsign * ubase;
                }
                break;
            }
            case 123:	// added 2/6/04
                result = Controller.random(ubase, Math.abs(max));
                break;

            case 124:	// check sign
                result = ubase;
                if (caster_level > 50)
                    result += updownsign * (caster_level - 50);
                break;

            case 125:	// check sign
                result = ubase;
                if (caster_level > 50)
                    result += updownsign * 2 * (caster_level - 50);
                break;

            case 126:	// check sign
                result = ubase;
                if (caster_level > 50)
                    result += updownsign * 3 * (caster_level - 50);
                break;

            case 127:	// check sign
                result = ubase;
                if (caster_level > 50)
                    result += updownsign * 4 * (caster_level - 50);
                break;

            case 128:	// MentalCorruptionRecourse
                result = ubase;
                if (caster_level > 50)
                    result += updownsign * 5 * (caster_level - 50);
                break;

            case 129:	// Used in LoY era spell Frozen Harpoon
                result = ubase;
                if (caster_level > 50)
                    result += updownsign * 10 * (caster_level - 50);
                break;

            case 130:	// check sign
                result = ubase;
                if (caster_level > 50)
                    result += updownsign * 15 * (caster_level - 50);
                break;

            case 131:	// check sign
                result = ubase;
                if (caster_level > 50)
                    result += updownsign * 20 * (caster_level - 50);
                break;

            case 144: // Denon's Desperate Dirge scaling changed to 10/level (February 21, 2001)
                result = ubase;
                if (caster_level > 43)
                    result += updownsign * 10 * (caster_level - 43);
                break;

            case 150: //resistant discipline (custom formula)
                result = caster_level > 50 ? 10 : caster_level > 45 ? 5 + caster_level - 45 : caster_level > 40 ? 5 : caster_level > 34 ? 4 : 3;
                break;

            //these are used in stacking effects... formula unknown
            case 201:
            case 202:
            case 203:
            case 204:
            case 205:
                result = max;
                break;
            default: {
                if (formula < 100) {
                    if (spell_id == 88)		// Unholy Aura disc HT.  This has formula = 14, but it's supposed to multiply all HTs by 1.5, including
                        formula = 10;						// Unholy Touch AA's added damage.  SPELL_HARM_TOUCH has formula 10.  Doing the mult elsewhere

                    result = ubase + (caster_level * formula);

                    // Sony hardcoded a HT damage bonus
                    if (spell_id == 88) {
                        if (caster_level > 40) {		// HT damage starts increasing by 30 per level at level 41

                            int htBonus = 20 * caster_level - 40;
                            if (htBonus > 400)		// scale goes back to +10 per level at level 60
                                htBonus = 400;
                            result += htBonus;
                        }
                    }
                }
            }
        }

        //int oresult = result;

        // now check result against the allowed maximum
        if (max != 0) {
            if (updownsign == 1) {
                if (result > max)
                    result = max;
            } else {
                if (result < max)
                    result = max;
            }
        }

        // if base is less than zero, then the result need to be negative too
        if (base < 0 && result > 0)
            result *= -1;

        return result;
    }

}
