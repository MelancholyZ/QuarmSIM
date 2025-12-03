package main.controller;

import main.model.*;

/**
 * Represents a round of combat
 */
public class CombatRound {
    private CharacterSheet cs;
    private String skill, attackType;
    private Weapon weapon;
    private SpecialAttack specialAttack;
    private Mob mob;
    private int baseDamage;
    private int offense;
    private int minHit = 0;
    private static boolean critHit, crip_success, slayUndead;

    /**
     * Weapon constructor for Combat Round
     * @param weapon the weapon object
     * @param attackType primary, secondary, or ranged
     * @param offense offense of skill in use
     * @return the damage dealt
     */
    public int attack(Weapon weapon, String attackType, int offense) {
        this.cs = Controller.getCharacterSheet();
        this.mob = Controller.getTargetMob();

        this.weapon = weapon;
        this.skill = weapon.getWeaponType();
        if (skill.equals("Archery")) {
            specialAttack = cs.getRangedAttack();
        }
        critHit = false;
        crip_success = false;
        slayUndead = false;
        this.offense = offense;
        this.attackType = attackType;
        return meleeRound();
    }

    /**
     * Special attack constructor for Combat Round
     * @param attack special attack object
     * @param attackType name of the special attack
     * @param offense offense for the special attack
     * @param minHit the minimum damage of a hit
     * @return melee round/damage dealt
     */
    public int attack(SpecialAttack attack, String attackType, int offense, int minHit) {
        this.cs = Controller.getCharacterSheet();
        this.mob = Controller.getTargetMob();
        critHit = false;
        crip_success = false;
        slayUndead = false;
        this.specialAttack = attack;
        this.skill = attackType;
        this.baseDamage = SpecialAttack.getDamage();
        this.offense = offense;
        this.attackType = attackType;
        this.minHit = minHit;
        return meleeRound();
    }

    /**
     * One round of combat
     * @return damage dealt
     */
    public int meleeRound() {
        int damage;
        if (Controller.random(mob.hitChance(cs, skill))) {

            if (skill.equals("Archery")) {
                baseDamage = specialAttack.getBowArrowCombined();
            } else if (attackType.equals("primary") || attackType.equals("secondary")) {
                baseDamage = weapon.getDamage();
            } else {
                baseDamage = SpecialAttack.getDamage();
            }

            if (attackType.equals("primary") || attackType.equals("secondary") || attackType.equals("ranged")) {
                if (weapon.getElementalDamage() > 0) {
                    baseDamage += calcEleWeaponResist(weapon.getElementalDamage(), weapon.getElementalType(),
                            Controller.isUseCustomResists(), Controller.getCustomResists());
                }

                if (attackType.equals("ranged") && SpecialAttack.getArrowElementalDamage() > 0) {
                    baseDamage += calcEleWeaponResist(SpecialAttack.getArrowElementalDamage(),
                            SpecialAttack.getArrowElementType(), Controller.isUseCustomResists(), Controller.getCustomResists());
                }
            }
            // AM multiplier
            if (skill.equals("Archery")) {
                baseDamage *= (int) cs.getAMMultiplier();
            }
            // ranged physical damage does half that of melee
            if (skill.equals("Archery") || skill.equals("Throwing") && baseDamage > 1) {
                baseDamage /= 2;
            }

            damage = calcMeleeDamage();

            if (attackType.equals("primary")) {
                damage += cs.getDamageBonus();
            }
            int damageDealt;
            if (attackType.equals("primary")) {
                damageDealt = CriticalDamage(damage, 0, cs.getDamageBonus());
            } else if (attackType.equals("secondary") || attackType.equals("ranged")) {
                damageDealt = CriticalDamage(damage, 0, 0);
            } else {
                damageDealt = CriticalDamage(damage, SpecialAttack.getMinDamage(), 0);
            }
            // Max backstab off by 1 possibly due to rounding
            if (skill.equals("Backstab")) {
                damageDealt++;
            }
            if (!attackType.equals("primary") && !attackType.equals("secondary") &&
                    !attackType.equals("ranged") && (damageDealt < SpecialAttack.getMinDamage())) {
                damageDealt = SpecialAttack.getMinDamage();
                if (damageDealt < minHit)
                    minHit = damageDealt;
            }
            return damageDealt;
        }
        return 0;
    }


    /**
     * Calculates melee damage
     * @return melee damage
     */
    public int calcMeleeDamage() {
        DamageMultiplier dmp;
        if (attackType.equals("primary") || attackType.equals("secondary")) {
            dmp = new DamageMultiplier(cs, weapon, offense);
        } else {
            dmp = new DamageMultiplier(cs, offense);
        }

        int offense = cs.getAttackType(attackType);
        int roll = 0;
        // Mitigation roll
        if (Controller.isUseCustomAC()) {
            roll = mobRandomD20(offense, Controller.getCustomAC());
        } else {
            roll = mobRandomD20(offense, mob.getMitigation());
        }

        int baseWithDisc = baseDamage;
        int minHitWithDisc = minHit;
        // Fellstrike (WAR) = Bestialrage (BST) = Innerflame (MNK) = Duelist (ROG) = 1059
        // min hit becomes 4 x weapon damage + 1 x damage bonus
        // normal hits are doubled
        if (Discipline.getActiveDisc() == 1059) {
            minHitWithDisc = baseWithDisc * 4;
            baseWithDisc *= 2;
        } else if (Discipline.getActiveDisc() == 1053) { // Ashenhand = 1053
            baseWithDisc *= 5;
        } else if (Discipline.getActiveDisc() == 1050) { // Defensive = 1050
            baseWithDisc *= 0.55;
        } else if (Discipline.getActiveDisc() == 1052) { // Silentfist = 1052
            baseWithDisc *= 1.65;
        } else if (Discipline.getActiveDisc() == 1057) { // Thunderkick = 1057
            baseWithDisc *= 1.75;
        } else if (Discipline.getActiveDisc() == 1048) { // Aggressive = 1048
            baseWithDisc *= 1.33;
        } else if (Discipline.getActiveDisc() == 1067 && attackType.equals("ranged")) { // Trueshot = 1067
            baseWithDisc *= 2.02;
        }

        // TODO: Tank parse and incoming damage during disc
        // damageMod: Defensive (0.5), Stonestance & Protective Spirit (0.1) Aggressive (2.0)
        // baseDamage += baseDamage * damageMod;

        int damage;
        if (Discipline.getActiveDisc() != 0) {
            damage = (roll * baseWithDisc + 5) / 10;
            if (damage < minHitWithDisc)
                damage = minHitWithDisc;
        } else {
            damage = (roll * baseDamage + 5) / 10;
            if (damage < minHit)
                damage = minHit;
        }

        if (damage < 1)
            damage = 1;

        return dmp.multiplier(damage);
    }

    /**
     * Calculates the elemental portion of weapon damage
     * @param weaponDamage the elemental damage
     * @param resistType the elemental resist type
     * @param useCustom use custom resists instead of mob's actual resists
     * @param customResist the custom resist amount to use
     * @return the amount of elemental damage that wasn't resisted
     */
    public int calcEleWeaponResist(int weaponDamage, int resistType, boolean useCustom, int customResist)
    {
        int resistValue = customResist;

        if (!useCustom) {
            switch (resistType) {
                case 1: // Magic
                    if (Controller.isWindOfTash() && Controller.isMalo()) {
                        resistValue = mob.getMR() - 40 - 45;
                    } else if (Controller.isWindOfTash()) {
                        resistValue = mob.getMR() - 40;
                    } else if (Controller.isMalo()) {
                        resistValue = mob.getMR() - 45;
                    } else {
                        resistValue = mob.getMR();
                    }
                    break;
                case 2: // Fire
                    if (Controller.isMalo()) {
                        resistValue = mob.getFR() - 45;
                    } else {
                        resistValue = mob.getFR();
                    }
                    break;
                case 3: // Cold
                    if (Controller.isMalo()) {
                        resistValue = mob.getCR() - 45;
                    } else {
                        resistValue = mob.getCR();
                    }
                        break;
                case 4: // Poison
                    if (Controller.isMalo()) {
                        resistValue = mob.getPR() - 45;
                    } else {
                        resistValue = mob.getPR();
                    }
                    break;
                case 5: // Disease
                    resistValue = mob.getDR();
                    break;
            }
        }

        if (resistValue < 5)
            resistValue = 5;

        if (resistValue > 200)
            return 0;

        int roll = Controller.roll0(200) + 1 - resistValue;
        if (roll < 1) {
            return 0;
        }
        if (roll <= 99) {
            return weaponDamage * roll / 100;
        }
        else {
            return weaponDamage;
        }
    }

    /**
     * Rolls 20 for damage interval
     * @param offense player offense calculation
     * @param mitigation mob's defense calculation
     * @return random roll between 1 and 20
     */
    public int mobRandomD20(int offense, int mitigation) {
        int attackRoll = Controller.roll0(offense + 5);
        int defenseRoll = Controller.roll0(mitigation + 5);

        int average = (offense + mitigation + 10) / 2;
        int index = Math.max(0, (attackRoll - defenseRoll) + (average / 2));

        index = (index * 20) / average;
        index = Math.max(0, index);
        index = Math.min(19, index);

        return index + 1;
    }

    /**
     * Calculates melee critical damage
     * @param damage pre crit melee damage
     * @param minBase minimum damage
     * @param damageBonus weapon damage bonus if primary weapon
     * @return normal damage or normal damage multiplied by critical modifier
     */
    public int CriticalDamage(int damage, int minBase, int damageBonus) {

        if (damage < 1)
            return 0;
        if (damageBonus > damage) // damage should include the bonus already, but calcs need the non-bonus portion
            damageBonus = damage;

        float critChance = 0.0f;
        boolean isBerserk = Controller.isBerserk();
        boolean undeadTarget = false;

        //1: Try Slay Undead
        if (mob.getBodyType() == 3 || mob.getBodyType() == 8 || mob.getBodyType() == 12) {
            undeadTarget = true;

            int SlayRateBonus = 140; //placeholder for SU bonus

            if (SlayRateBonus > 0)
            {
                float slayChance = (float)(SlayRateBonus) / 10000.0f;
                if (Controller.random(slayChance)) {
                    slayUndead = true;
                    int slayDmgBonus = 1; //TODO find the actual slay bonus
                    damage = ((damage - damageBonus + 6) * slayDmgBonus) / 100 + damageBonus;

                    int minSlay = (minBase + 5) * slayDmgBonus / 100 + damageBonus;
                    if (damage < minSlay)
                        damage = minSlay;
                    return damage;
                }
            }
        }

        //2: Try Melee Critical
        // Combat Fury and Fury of the Ages AAs
        int critChanceMult = cs.getCombatFury();

        float overCap = 0.0f;

        if (cs.getDexterity() > 255)
           overCap = (float)(cs.getDexterity() - 255) / 400.0f;

        if (cs.getCharacterClass().equals("Warrior") && cs.getLevel() >= 12) {
            critChance += 0.5f + (float)(Math.min(cs.getDexterity(), 255)) / 90.0f + overCap;
        }
        else if (skill.equals("Archery") && cs.getCharacterClass().equals("Ranger") && cs.getLevel() > 16) {
            critChance += 1.35f + (float)(Math.min(cs.getDexterity(), 255)) / 34.0f + overCap * 2;
        }
		if (!cs.getCharacterClass().equals("Warrior") && critChanceMult > 0) {
            critChance += 0.275f + (float)(Math.min(cs.getDexterity(), 255)) / 150.0f + overCap;
        }
        if (Discipline.getActiveDisc() == 1062) { // Mighty Strike discipline = 1062
            critChance *= 10000;
        }

        if (critChanceMult > 0)
            critChance += critChance * (float)(critChanceMult) / 100.0f;

        int activeDisc = Discipline.getActiveDisc();

        if (activeDisc == 1050) // defensive
            critChance = 0.0f;
        else if (activeDisc == 1062) // mighty strike
            critChance = 100.0f;
        else if (activeDisc == 1065 && undeadTarget && critChance < (critChance * 2.0)) // holyforge
            critChance *= 2.0f;


        int deadlyChance = 0;
        int deadlyMod = 0;

        if (skill.equals("Throwing") && cs.getCharacterClass().equals("Rogue") && cs.getSpecialSkillCap() >= 65) {
            critChance += 25;
            deadlyChance = 80;
            deadlyMod = 2;
        }

        if (critChance > 0)
        {
            critChance /= 100.0f;

            if (Controller.random(critChance)) {
                critHit = true;
                int critMod = 17;
                int cripplingBlowChance = 0;

                // Holyforge Discipline 20% chance to cripple
                if (activeDisc == 1065) {
                    cripplingBlowChance = 20;
                }
                int minDamage = 0;

                if (isBerserk || Controller.random(cripplingBlowChance)) {
                    critMod = 29;
                    crip_success = true;
                }

                if (minBase > 0)
                    minDamage = (minBase * critMod + 5) / 10 + 8 + damageBonus;

                damage = ((damage - damageBonus) * critMod + 5) / 10 + 8 + damageBonus;
                if (crip_success) {
                    damage += 2;
                    minDamage += 2;
                }
                if (minBase > 0 && minDamage > damage)
                    damage = minDamage;

                boolean deadlySuccess = false;

                // sanity check; 1 damage crits = an error somewhere
                if (damage > 1000000 || damage < 0)
                    damage = 1;
            }
        }
        return damage;
    }

    /**
     * Getter method to check for crits
     * @return true if critical damage was dealt
     */
    public static boolean isCritHit() {
        return critHit;
    }

    /**
     * Getter method to check for crippling blows
     * @return true if crippling damage was dealt
     */
    public static boolean isCrippling() {
        return crip_success;
    }


    /**
     * Getter method to check for a slay undead proc
     * @return true if slay undead procced
     */
    public static boolean isSlayUndead() {
        return slayUndead;
    }
}
