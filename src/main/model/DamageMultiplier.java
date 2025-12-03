package main.model;

import main.controller.Controller;

/**
 * Damage multiplier for melee damage
 */
public class DamageMultiplier {
    private CharacterSheet cs;
    private int level, offense;
    private String charClass, skillInUse;

    /**
     * Default constructor for damage multiplier for primary and secondary
     * @param characterSheet character sheet
     * @param weapon weapon being used
     * @param offense offense
     */
    public DamageMultiplier(CharacterSheet characterSheet, Weapon weapon, int offense) {
        cs = characterSheet;
        level = cs.getLevel();
        this.offense = offense;
        charClass = cs.getCharacterClass();
        skillInUse = weapon.getWeaponType();
    }
    /**
     * Constructor for damage multiplier for special attacks and ranged attacks
     * @param characterSheet character sheet
     * @param offense offense
     */
    public DamageMultiplier(CharacterSheet characterSheet, int offense) {
        cs = characterSheet;
        level = cs.getLevel();
        this.offense = offense;
        charClass = cs.getCharacterClass();
        skillInUse = SpecialAttack.getSpecialName();
    }

    /**
     * Class that rolls for damage multiplier
     * @param damage damage to be multiplied
     * @return damage multiplied but sometimes only by 100%
     */
    public int multiplier(int damage) {
        int rollChance = 51;
        int maxExtra = 210;
        int minusFactor = 105;

	    if (charClass.equals("Monk") && level >= 65) {
            rollChance = 83;
            maxExtra = 300;
            minusFactor = 50;
        } else if (cs.getLevel() >= 65 || (charClass.equals("Monk") && level >= 63)) {
            rollChance = 81;
            maxExtra = 295;
            minusFactor = 55;
        } else if (level >= 63 || (charClass.equals("Monk") && level >= 60)) {
            rollChance = 79;
            maxExtra = 290;
            minusFactor = 60;
        } else if (level >= 60 || (charClass.equals("Monk") && level >= 56)) {
            rollChance = 77;
            maxExtra = 285;
            minusFactor = 65;
        } else if (level >= 56) {
            rollChance = 72;
            maxExtra = 265;
            minusFactor = 70;
        } else if (level >= 51 || charClass.equals("Monk")) {
            rollChance = 65;
            maxExtra = 245;
            minusFactor = 80;
        }

        int baseBonus = (offense - minusFactor) / 2;
	    if (baseBonus < 10)
            baseBonus = 10;

	    if (Controller.random(rollChance)) {
            int roll;

            roll = Controller.roll0(baseBonus) + 100;
            if (roll > maxExtra)
                roll = maxExtra;

            damage = damage * roll / 100;

            if ((level >= 55) && (damage > 1) && !skillInUse.equals("Archery") && cs.getCharacterClass().equals("Warrior")) {
                damage++;
            }
        }
        return damage;
    }
}
