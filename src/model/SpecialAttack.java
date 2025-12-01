package model;

/**
 * This class represents the various special attacks
 */
public class SpecialAttack {
    private static int damage;
    private static int minDamage;
    private static int delay;
    private static int level;
    private static int skillCap;
    private static int arrowDamage, arrowElementalDamage, arrowElementType, bowArrowCombined;
    private static Weapon weapon;
    private static String specialName;

    /**
     * Default constructor for special attacks
     * @param attackName the name of the special attack
     * @param level player level
     * @param skillCap the skill cap for the special attack type
     */
    public SpecialAttack(String attackName, int level, int skillCap) {
        specialName = attackName;
        this.skillCap = skillCap;
        this.level = level;
        setDamageDelay();
        //displaySpecialAttackStats();
    }

    /**
     * Constructor for Special Attack concerning ranged attacks
     * @param attackName the attack name
     * @param level player level
     * @param weapon equipped bow
     * @param skillCap archery skill cap
     * @param arrowDamage base damage of the arrow equipped
     * @param arrowElemental elemental damage of the arrow equipped
     * @param arrowElementType elemental type of the arrow equipped
     */
    public SpecialAttack(String attackName, int level, Weapon weapon, int skillCap, int arrowDamage, int arrowElemental,
                            int arrowElementType) {
        specialName = attackName;
        this.weapon = weapon;
        this.skillCap = skillCap;
        this.level = level;
        this.arrowDamage = arrowDamage;
        this.arrowElementalDamage = arrowElemental;
        this.arrowElementType = arrowElementType;
        setDamageDelay();
        setArcheryDamage();
        //displaySpecialAttackStats();
    }

    /**
     * Getter method to get arrow elemental damage
     * @return arrow elemental damage
     */
    public static int getArrowElementalDamage() {
        return arrowElementalDamage;
    }

    /**
     * Getter method to get arrow elemental type
     * @return arrow elemental type
     */
    public static int getArrowElementType() {
        return arrowElementType;
    }

    /**
     * Print special attack stats
     */
    public void displaySpecialAttackStats() {
        System.out.println("specialName: " + specialName);
        System.out.println("damage: " + damage);
        System.out.println("minDamage: " + minDamage);
        System.out.println("delay: " + delay);
        System.out.println("level: " + level);
    }
    /**
     * Setter method that sets the damage of the special attack
     *                 HarmTouchReuseTime = 4320,
     */
    public void setDamageDelay() {
        damage = 1;
        delay = 100;
        minDamage = 1;
        switch(specialName) {
            case "Backstab" :
                damage = setBackstabDamage();
                minDamage = setBackstabMinHit();
                delay = 100;
                break;
            case "FlyingKick" :
                damage = 25;
                delay = 80;
                minDamage = level * 4 / 5;
                break;
            case "Bash":
                damage = 2;
                delay = 80;
                break;
            case "Intimidation":
                damage = 2;
                delay = 90;
                break;
            case "Kick":
                damage = 3;
                delay = 80;
                break;
            case "TailRake":
            case "DragonPunch":
                damage = 12;
                delay = 60;
                break;
            case "RoundKick":
                damage = 5;
                delay = 80;
                break;
            case "EagleStrike":
                damage = 7;
                delay = 60;
                break;
            case "TigerClaw":
                damage = 4;
                delay = 70;
                break;
            case "HarmTouch": //TODO HT damage can't be here because it's a spell and doesn't get all the combat multipliers
                delay = 43200;
                break;
            }

            if (specialName.equals("HarmTouch")) {
                if (skillCap >= 25)
                    damage++;
                if (skillCap >= 75)
                    damage++;
                if (skillCap >= 125)
                    damage++;
                if (skillCap >= 175)
                    damage++;
            }
    }

    /**
     * Getter method for combined physical bow plus arrow damage
     * @return bow and arrow damage combined
     */
    public int getBowArrowCombined() {
        return bowArrowCombined;
    }

    /**
     * Setter method for bowArrowCombined damage
     */
    public void setArcheryDamage() {
        bowArrowCombined = weapon.getDamage() + arrowDamage;
    }

    /**
     * Getter method for special attack damage
     * @return damage of the special attack
     */
    public static int getDamage() {
        return damage;
    }

    /**
     * Getter method to get special attack minimum damage
     * @return special attack minimum damage
     */
    public static int getMinDamage() {
        return minDamage;
    }
    /**
     * Getter method for special attack delay
     * @return special attack delay
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Setter method to set the minimum damage of backstab
     * @return minimum damage of backstab
     */
    public int setBackstabMinHit() {
        if (level >= 60)
            minDamage = level * 2;
     	else if (level > 50)
            minDamage = level * 3 / 2;
        else
     	    minDamage = level;
        return minDamage;
    }

    /**
     * Setter method that sets backstab base damage
     * @return backstab base damage
     */
    public int setBackstabDamage() {
        return (int)((((skillCap*CharacterSheet.getBackstabSkillMod()) * 0.02) + 2.0) * CharacterSheet.getPrimary().getDamage());
    }

    /**
     * Getter method that returns the name of the special attack
     * @return special attack name
     */
    public static String getSpecialName() {
        return specialName;
    }
}
