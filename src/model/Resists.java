package model;

import controller.Controller;

public class Resists {

    /**
     * Spell resists:
     * returns an effectiveness index from 0 to 100. for most spells, 100 means
     * it landed, and anything else means it was resisted; however there are some
     * spells that can be partially effective, and this value can be used there.
     * @param resist_type the type of resist of the spell
     * @param spell_id the id of the spell
     * @param caster_level the level of the caster
     * @param target the target mob
     * @param use_resist_override always false
     * @param resist_override always 0
     * @param tick_save always false
     * @return returns 0 if full resist, or any float between 0 and 100 for damage percent
     */
    public static float checkResistSpell(int resist_type, int spell_id, int caster_level, Mob target, boolean use_resist_override, int resist_override, boolean tick_save) {

        //Cant find any mobs with magic immunity but save if needed
//        if(GetSpecialAbility(SpecialAbility::MagicImmunity)) {
//            return(0);
//        }
        Spell spell = new Spell(spell_id);
        //Get resist modifier and adjust it based on focus 2 resist about eq to 1% resist chance
        int resist_modifier = (use_resist_override) ? resist_override : spell.getResistDiff();

        //Get the resist chance for the target
        if (spell.getResistType() == 0) {
            return 100;
        }

        int target_resist;

        switch (resist_type) {
            case 1: // Magic
                if (Controller.isWindOfTash() && Controller.isMalo()) {
                    target_resist = target.getMR() - 40 - 45;
                } else if (Controller.isWindOfTash()) {
                    target_resist = target.getMR() - 40;
                } else if (Controller.isMalo()) {
                    target_resist = target.getMR() - 45;
                } else {
                    target_resist = target.getMR();
                }
                break;
            case 2: // Fire
                if (Controller.isMalo()) {
                    target_resist = target.getFR() - 45;
                } else {
                    target_resist = target.getFR();
                }
                break;
            case 3: // Cold
                if (Controller.isMalo()) {
                    target_resist = target.getCR() - 45;
                } else {
                    target_resist = target.getCR();
                }
                break;
            case 4: // Poison
                if (Controller.isMalo()) {
                    target_resist = target.getPR() - 45;
                } else {
                    target_resist = target.getPR();
                }
                break;
            case 5: // Disease
                target_resist = target.getDR();
                break;
            default:
                target_resist = 0;
                break;
        }

        if (target_resist < 5) {
            target_resist = 5;
        }

        //Set up our base resist chance.
        if (tick_save) {
            caster_level += 4;
        }
        int target_level = target.getLevel();
        int resist_chance = 0;
        int level_mod = 0;

        //Adjust our resist chance based on level modifiers
        int leveldiff = target_level - caster_level;
        int temp_level_diff = leveldiff;

        //RuleI(Casting, ResistFalloff = 67
        if (target_level >= 67) {
            int a = (67 - 1) - caster_level;
            if (a > 0) {
                temp_level_diff = a;
            } else {
                temp_level_diff = 0;
            }
        }

        if (temp_level_diff < -9) {
            temp_level_diff = -9;
        }

        level_mod = temp_level_diff * temp_level_diff / 2;
        if (temp_level_diff < 0) {
            level_mod = -level_mod;
        }

        // set resist modifiers for targets well above caster's level
        // this is a crude approximation of Sony's convoluted function
        // it's far from precise but way better than nothing
        if (caster_level < 50) {
            // after a certain level above the caster, NPCs gain a significant resist bonus
            // how many levels above the caster and how large the bonus is both increase with caster level
            // It's not a flat 1000 resist mod as Sony's pseudocode stated.  Many parses were done to prove this
            int bump_level = caster_level + 4 + caster_level / 6;
            if (target_level >= bump_level) {
                level_mod += 70 + caster_level * 6;
            }
        } else {
            if (caster_level < 64) {
                if (leveldiff >= 13)
                    level_mod = caster_level * 5;
            } else {
                // note: if you use this for expacs beyond PoP, you may need to expand this
                if (leveldiff >= 16)
                    level_mod = caster_level * 5;
            }
        }

        //Even more level stuff this time dealing with damage spells
        // RuleI (Spells, ResistFalloff) = 67
        if (isDirectDamageSpell(spell_id)) {
            if (target_level >= 67) {
                temp_level_diff = (67 - 1) - caster_level;
                if (temp_level_diff < 0) {
                    temp_level_diff = 0;
                }
            } else {
                temp_level_diff = target_level - caster_level;
            }

            if (temp_level_diff > 0 && target_level >= 17) {
                level_mod += (2 * temp_level_diff);
            }
        }

        boolean no_partial = false;

        //Add our level, resist and -spell resist modifier to our roll chance
        resist_chance += target_resist;
        resist_chance += level_mod;

        resist_chance += resist_modifier;

        if (tick_save && resist_chance < 5) {
            resist_chance = 5;
        }

        //Special rules for rain spells
        if (spell.getAoEDuration() > 0) {

            //RuleI(Spells, RainWizardResistChance) = 0
            int resist_chance_percentage = 20;
            if (CharacterSheet.getCharacterClass().equals("Wizard")) {
                resist_chance_percentage = 0;
            }

            // 20% innate resist for most classes
            if (Controller.random(resist_chance_percentage)) {
                return 0;
            }
        }

        //Finally our roll
        int roll = Controller.roll0(200);
        if (roll > resist_chance) {
            return 100;
        } else {
            if (!spell.partialCapable() || resist_chance == 0) {
                return 0;
            } else {

                int partial_modifier = ((150 * (resist_chance - roll)) / resist_chance);

                if (target_level > caster_level && target_level >= 17 && (caster_level <= 50)) {
                    partial_modifier += 5;
                }

                if (target_level >= 30 && (caster_level <= 50)) {
                    partial_modifier += (target_level - 25);
                }

                if (target_level < 15) {
                    partial_modifier -= 5;
                }

                if (partial_modifier <= 0) {
                    return 100;
                } else if (partial_modifier >= 100) {
                    return 0;
                }

                return (100.0f - partial_modifier);
            }
        }
    }

    /**
     * Checks to see if a spell is a dd or not
     * @param spellid the id of the spell
     * @return true if the spell effect is considered a direct damage spell
     */
    static boolean isDirectDamageSpell(int spellid) {
        Spell spell = new Spell(spellid);

        if (spell.getBuffDuration() > 0)
            return false;

        for (int o = 0; o < spell.getEffectid().length; o++) {
            int tid = spell.getEffectid()[o];
            if ((tid == 79 || tid == 0) && (int)spell.getBase()[o] < 0)
                return true;
        }
        return false;
    }
}
