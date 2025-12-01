package model;

/**
 *
 */
public class Discipline {
    private static long reuse_timer = 0;
    private static long ability_timer = 0;
    //private static int spellid;
    private static int activeDisc = 0;

    /**
     *
     * @param disc_id the ID of the discipline
     * @param level_to_use the minimum level that a discipline is usable
     */
    public static void castDiscipline(int disc_id, int level_to_use) {
            activeDisc = disc_id;

            int current_level = CharacterSheet.getLevel();

            // reuse_timer and ability_timer are in milliseconds.
            String string = "";

            switch(disc_id) {
                case (1048): //disc_aggressive
                    reuse_timer = 1620000;
                    ability_timer = 180000;
                    //spellid = 4498;
                    string = String_ID.discStartFlavorText(1048);
                    break;
                case (1049): //disc_precision
                    reuse_timer = 1800000;
                    ability_timer = 180000;
                    //spellid = 4501;
                    string = String_ID.discStartFlavorText(1049);
                    break;
                case (1050): //disc_defensive
                    reuse_timer = 900000;
                    ability_timer = 180000;
                    //spellid = 4499;
                    string = String_ID.discStartFlavorText(1050);
                    break;
                case (1051): //disc_evasive
                    reuse_timer = 900000;
                    ability_timer = 180000;
                    //spellid = 4503;
                    string = String_ID.discStartFlavorText(1051);
                    break;
                case (1053): //disc_ashenhand
                    reuse_timer = 4320000;
                    //spellid = 4508;
                    string = String_ID.discStartFlavorText(1053);
                    break;
                case (1055): //disc_furious furious (WAR), whirlwind (MNK), counterattack (ROG)
                    reuse_timer = 3600000;
                    ability_timer = 9000;
//                    if (CharacterSheet.getCharacterClass().equals("Warrior"))
//                        spellid = 4674;
//                    else if (CharacterSheet.getCharacterClass().equals("Monk"))
//                        spellid = 4509;
//                    else if (CharacterSheet.getCharacterClass().equals("Rogue"))
//                        spellid = 4673;
                    string = String_ID.discStartFlavorText(1055);
                    break;
                case (1056): //disc_stonestance stonestance (MNK), protectivespirit (BST)
                    reuse_timer = 720000;
                    ability_timer = 12000;
//                    if (CharacterSheet.getCharacterClass().equals("Monk"))
//                        spellid = 4510;
//                    else if (CharacterSheet.getCharacterClass().equals("Beastlord"))
//                        spellid = 4671;
                    string = String_ID.discStartFlavorText(1056);
                    break;
                case (1057): //disc_thunderkick
                    reuse_timer = 540000;
                    //spellid = 4511;
                    string = String_ID.discStartFlavorText(1057);
                    break;
                case (1058): //disc_fortitude fortitude (WAR), voiddance (MNK)
                    reuse_timer = 3600000;
                    ability_timer = 8000;
//                    if (CharacterSheet.getCharacterClass().equals("Warrior"))
//                        spellid = 4670;
//                    else if (CharacterSheet.getCharacterClass().equals("Monk"))
//                        spellid = 4502;
                    string = String_ID.discStartFlavorText(1058);
                    break;
                case (1059): //disc_fellstrike fellstrike (WAR), bestialrage (BST), innerflame (MNK), duelist (ROG)
                    reuse_timer = 1800000;
                    ability_timer = 12000;
//                    if (CharacterSheet.getCharacterClass().equals("Warrior"))
//                        spellid = 4675;
//                    else if (CharacterSheet.getCharacterClass().equals("Monk"))
//                        spellid = 4512;
//                    else if (CharacterSheet.getCharacterClass().equals("Rogue"))
//                        spellid = 4676;
//                    else if (CharacterSheet.getCharacterClass().equals("Beastlord"))
//                        spellid = 4678;
                    string = String_ID.discStartFlavorText(1059);
                    break;
                case (1063): //disc_hundredfist hundredfist (MNK), blindingspeed (ROG)
                    reuse_timer = 1800000;
                    ability_timer = 15000;
//                    if (CharacterSheet.getCharacterClass().equals("Monk"))
//                        spellid = 4513;
//                    else if (CharacterSheet.getCharacterClass().equals("Rogue"))
//                        spellid = 4677;
                    string = String_ID.discStartFlavorText(1063);
                    break;
                case (1061): //disc_charge charge (WAR), deadeye (ROG)
                    reuse_timer = 1800000;
                    ability_timer = 14000;
//                    if (CharacterSheet.getCharacterClass().equals("Warrior"))
//                        spellid = 4672;
//                    else if (CharacterSheet.getCharacterClass().equals("Rogue"))
//                        spellid = 4505;
                    string = String_ID.discStartFlavorText(1061);
                    break;
                case (1062): //disc_mightystrike
                    reuse_timer = 3600000;
                    ability_timer = 10000;
                    //spellid = 4514;
                    string = String_ID.discStartFlavorText(1062);
                    break;
                case (1060): //disc_nimble
                    reuse_timer = 1800000;
                    ability_timer = 12000;
                    //spellid = 4515;
                    string = String_ID.discStartFlavorText(1060);
                    break;
                case (1052): //disc_silentfist
                case (1054): //disc_silentfist_iksar
                    reuse_timer = 594000;
                    //spellid = 4507;
                    if (disc_id == 1052)
                        string = String_ID.discStartFlavorText(1052);
                    else
                        string = String_ID.discStartFlavorText(1054);
                    break;
                case (1064): //disc_kinesthetics
                    reuse_timer = 1800000;
                    ability_timer = 18000;
                    //spellid = 4517;
                    string = String_ID.discStartFlavorText(1064);
                    break;
                case (1065): //disc_holyforge
                    reuse_timer = 4320000;
                    ability_timer = 300000;
                    //spellid = 4500;
                    string = String_ID.discStartFlavorText(1065);
                    break;
                case (1066): //disc_sanctification
                    reuse_timer = 4320000;
                    ability_timer = 15000;
                    //spellid = 4518;
                    string = String_ID.discStartFlavorText(1066);
                    break;
                case (1067): //disc_trueshot
                    reuse_timer = 4320000;
                    ability_timer = 120000;
                    //spellid = 4506;
                    string = String_ID.discStartFlavorText(1067);
                    break;
                case (1068): //disc_weaponshield_male
                case (1069): //disc_weaponshield_female
                case (1070): //disc_weaponshield_monster
                    reuse_timer = 4320000;
                    ability_timer = 20000;
                    //spellid = 4519;
                    if (disc_id == 1068)
                        string = String_ID.discStartFlavorText(1068);
                    else if (disc_id == 1069)
                        string = String_ID.discStartFlavorText(1069);
                    else
                        string = String_ID.discStartFlavorText(1070);
                    break;
                case (1071): //disc_unholyaura
                    reuse_timer = 4320000;
                    //spellid = 4520;
                    string = String_ID.discStartFlavorText(1071);
                    break;
                case (1072): //disc_leechcurse
                    reuse_timer = 4320000;
                    ability_timer = 20000;
                    //spellid = 4504;
                    string = String_ID.discStartFlavorText(1072);
                    break;
                case (1073): //disc_deftdance
                    reuse_timer = 4320000;
                    ability_timer = 15000;
                    //spellid = 4516;
                    string = String_ID.discStartFlavorText(1073);
                    break;
                case (1074): //disc_puretone
                    reuse_timer = 4320000;
                    ability_timer = 240000;
                    //spellid = 4586;
                    string = String_ID.discStartFlavorText(1074);
                    break;
                case (1075): //disc_resistant
                    reuse_timer = 3600000;
                    ability_timer = 300000;
                    //spellid = 4585;
                    string = String_ID.discStartFlavorText(1075);
                    break;
                case (1076): //disc_fearless
                    reuse_timer = 3600000;
                    ability_timer = 11000;
                    //spellid = 4587;
                    string = String_ID.discStartFlavorText(1076);
                    break;
            }

            if(!string.isEmpty()) {
                if (reuse_timer < 1620000 && current_level > 60)
                    current_level = 60;
                int reuse_timer_mod = 0 - ((current_level - level_to_use) * 54);
                reuse_timer += reuse_timer_mod;
                if (reuse_timer > 4320000)
                    reuse_timer = 4320000; // 72:00 maximum reuse time
                if (reuse_timer < 234000)
                    reuse_timer = 234000; // 3:54 minimum reuse time
            }
        }

    /**
     * Returns the minimum level to use a discipline, 0 means they cannot use that discipline
     * @param disc_id the id of the discipline
     * @return the minimum level that a discipline is usable, 0 if never
     */
    public static int disciplineUseLevel(int disc_id) {
        switch (disc_id) {
            case (1048): //disc_aggressive
                if (CharacterSheet.getCharacterClass().equals("Warrior"))
                    return 60;
                else
                    return 0;
            case (1049): //disc_precision
                if (CharacterSheet.getCharacterClass().equals("Warrior"))
                    return 57;
                else
                    return 0;
            case (1050): //disc_defensive
                if (CharacterSheet.getCharacterClass().equals("Warrior"))
                    return 55;
                else
                    return 0;
            case (1051): //disc_evasive
                if (CharacterSheet.getCharacterClass().equals("Warrior"))
                    return 52;
                else
                    return 0;
            case (1053): //disc_ashenhand
                if (CharacterSheet.getCharacterClass().equals("Monk"))
                    return 60;
                else
                    return 0;
            case (1055): //disc_furious
                if (CharacterSheet.getCharacterClass().equals("Warrior"))
                    return 56;
                else if (CharacterSheet.getCharacterClass().equals("Monk")
                        || CharacterSheet.getCharacterClass().equals("Rogue"))
                    return 53;
                else
                    return 0;
            case (1056): //disc_stonestance
                if (CharacterSheet.getCharacterClass().equals("Monk"))
                    return 51;
                else if (CharacterSheet.getCharacterClass().equals("Beastlord"))
                    return 55;
                else
                    return 0;
            case (1057): //disc_thunderkick
                if (CharacterSheet.getCharacterClass().equals("Monk"))
                    return 52;
                else
                    return 0;
            case (1058): //disc_fortitude
                if (CharacterSheet.getCharacterClass().equals("Warrior"))
                    return 59;
                else if (CharacterSheet.getCharacterClass().equals("Monk"))
                    return 54;
                else
                    return 0;
            case (1059): //disc_fellstrike
                if (CharacterSheet.getCharacterClass().equals("Warrior"))
                    return 58;
                else if (CharacterSheet.getCharacterClass().equals("Monk"))
                    return 56;
                else if (CharacterSheet.getCharacterClass().equals("Beastlord"))
                    return 60;
                else if (CharacterSheet.getCharacterClass().equals("Rogue"))
                    return 59;
                else
                    return 0;
            case (1063): //disc_hundredfist
                if (CharacterSheet.getCharacterClass().equals("Monk"))
                    return 57;
                else if (CharacterSheet.getCharacterClass().equals("Rogue"))
                    return 58;
                else
                    return 0;
            case (1061): //disc_charge
                if (CharacterSheet.getCharacterClass().equals("Warrior"))
                    return 53;
                else if (CharacterSheet.getCharacterClass().equals("Rogue"))
                    return 54;
                else
                    return 0;
            case (1062): //disc_mightystrike
                if (CharacterSheet.getCharacterClass().equals("Warrior"))
                    return 54;
                else
                    return 0;
            case (1060): //disc_nimble
                if (CharacterSheet.getCharacterClass().equals("Rogue"))
                    return 55;
                else
                    return 0;
            case (1052): //disc_silentfist
            case (1054): //disc_silentfist_iksar
                if (CharacterSheet.getCharacterClass().equals("Monk"))
                    return 59;
                else
                    return 0;
            case (1064): //disc_kinesthetics
                if (CharacterSheet.getCharacterClass().equals("Rogue"))
                    return 57;
                else
                    return 0;
            case (1065): //disc_holyforge
                if (CharacterSheet.getCharacterClass().equals("Paladin"))
                    return 55;
                else
                    return 0;
            case (1066): //disc_sanctification
                if (CharacterSheet.getCharacterClass().equals("Paladin"))
                    return 60;
                else
                    return 0;
            case (1067): //disc_trueshot
                if (CharacterSheet.getCharacterClass().equals("Ranger"))
                    return 55;
                else
                    return 0;
            case (1068): //disc_weaponshield_male
            case (1069): //disc_weaponshield_female
            case (1070): //disc_weaponshield_monster
                if (CharacterSheet.getCharacterClass().equals("Ranger"))
                    return 60;
                else
                    return 0;
            case (1071): //disc_unholyaura
                if (CharacterSheet.getCharacterClass().equals("ShadowKnight"))
                    return 55;
                else
                    return 0;
            case (1072): //disc_leechcurse
                if (CharacterSheet.getCharacterClass().equals("ShadowKnight"))
                    return 60;
                else
                    return 0;
            case (1073): //disc_deftdance
                if (CharacterSheet.getCharacterClass().equals("Bard"))
                    return 55;
                else
                    return 0;
            case (1074): //disc_puretone
                if (CharacterSheet.getCharacterClass().equals("Bard"))
                    return 60;
                else
                    return 0;
            case (1075): //disc_resistant
                if (CharacterSheet.getCharacterClass().equals("Warrior")
                        || CharacterSheet.getCharacterClass().equals("Monk")
                        || CharacterSheet.getCharacterClass().equals("Rogue"))
                    return 30;
                else if (CharacterSheet.getCharacterClass().equals("Paladin")
                        || CharacterSheet.getCharacterClass().equals("Ranger")
                        || CharacterSheet.getCharacterClass().equals("ShadowKnight")
                        || CharacterSheet.getCharacterClass().equals("Bard")
                        || CharacterSheet.getCharacterClass().equals("Beastlord"))
                    return 51;
                else
                    return 0;
            case (1076): //disc_fearless
                if (CharacterSheet.getCharacterClass().equals("Warrior")
                        || CharacterSheet.getCharacterClass().equals("Monk")
                        || CharacterSheet.getCharacterClass().equals("Rogue"))
                    return 40;
                else if (CharacterSheet.getCharacterClass().equals("Paladin")
                        || CharacterSheet.getCharacterClass().equals("Ranger")
                        || CharacterSheet.getCharacterClass().equals("ShadowKnight")
                        || CharacterSheet.getCharacterClass().equals("Bard")
                        || CharacterSheet.getCharacterClass().equals("Beastlord"))
                    return 54;
                else
                    return 0;
            default:
                return 0;
        }
    }

    /**
     * Getter method to get active disc
     * @return the active disc, 0 if no disc is active
     */
    public static int getActiveDisc() {
        return activeDisc;
    }

    /**
     * Getter method to get reuse timer
     * @return reuse timer
     */
    public static long getReuseTimer() {
        return reuse_timer;
    }

    /**
     * Getter method to get ability timer
     * @return ability timer
     */
    public static long getAbilityTimer() {
        return ability_timer;
    }

    /**
     * Setter method to set the active disc
     * @param discID the id of the active disc
     */
    public static void setActiveDisc(int discID) {
        activeDisc = discID;
    }

    /**
     * Setter method to reset the reuse timer
     */
    public static void resetReuseTimer() {
        reuse_timer = 0;
    }
}
