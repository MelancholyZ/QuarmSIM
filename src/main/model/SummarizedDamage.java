package main.model;

public class SummarizedDamage {
    public static int primarySwingCounter = 0;
    public static int primaryDoubleCounter = 0;
    public static int primaryTripleCounter = 0;
    public static int primaryQuadCounter = 0;
    public static int primaryQuintCounter = 0;
    public static int primaryMissCounter = 0;
    public static int primaryDamage = 0;
    public static int primaryCritDamage = 0;
    public static int primaryMinimumHit = 9999;
    public static int primaryMaximumHit = 0;
    public static int primaryMinimumCrit = 9999;
    public static int primaryMaximumCrit = 0;
    public static int primaryCritCounter = 0;
    public static int primaryProcCounter = 0;
    public static int primaryProcCritCounter = 0;
    public static int primaryProcResistCounter = 0;
    public static int primaryProcDD = 0;
    public static int primaryProcCritDamage = 0;
    public static int primaryMinimumDD = 9999;
    public static int primaryMaximumDD = 0;
    public static int primaryProcDot = 0;
    public static int secondarySwingCounter = 0;
    public static int secondaryDoubleCounter = 0;
    public static int secondaryMissCounter = 0;
    public static int secondaryDamage = 0;
    public static int secondaryCritDamage = 0;
    public static int secondaryMinimumHit = 9999;
    public static int secondaryMaximumHit = 0;
    public static int secondaryMinimumCrit = 9999;
    public static int secondaryMaximumCrit = 0;
    public static int secondaryCritCounter = 0;
    public static int secondaryProcCritCounter = 0;
    public static int secondaryProcCounter = 0;
    public static int secondaryProcResistCounter = 0;
    public static int secondaryProcDD = 0;
    public static int secondaryProcCritDamage = 0;
    public static int secondaryMinimumDD = 9999;
    public static int secondaryMaximumDD = 0;
    public static int secondaryProcDot = 0;
    public static int rangedSwingCounter = 0;
    public static int rangedMissCounter = 0;
    public static int rangedDamage = 0;
    public static int rangedCritDamage = 0;
    public static int rangedMinimumHit = 9999;
    public static int rangedMaximumHit = 0;
    public static int rangedMinimumCrit = 9999;
    public static int rangedMaximumCrit = 0;
    public static int rangedCritCounter = 0;
    public static int rangedProcCounter = 0;
    public static int rangedProcCritCounter = 0;
    public static int rangedProcResistCounter = 0;
    public static int rangedProcDD = 0;
    public static int rangedProcCritDamage = 0;
    public static int rangedMinimumDD = 9999;
    public static int rangedMaximumDD = 0;
    public static int rangedProcDot = 0;
    public static int specialSwingCounter = 0;
    public static int specialDoubleCounter = 0;
    public static int specialMissCounter = 0;
    public static int specialDamage = 0;
    public static int specialCritDamage = 0;
    public static int specialMinimumHit = 9999;
    public static int specialMaximumHit = 0;
    public static int specialMinimumCrit = 9999;
    public static int specialMaximumCrit = 0;
    public static int specialCritCounter = 0;
    public static int discUptime = 0;
    public static int discDamage = 0;

    /**
     * Prints combat summary to console
     * @param time in milliseconds
     */
    public void printSummary(Weapon primary, Weapon secondary, Weapon ranged, String mob, String special, long time) {
        System.out.println("Combat time: " + time/1000 + " seconds");
        System.out.printf("Total dps versus %s: %2.2f\n", mob,  ((double)(primaryDamage + primaryCritDamage + primaryProcDD
                + primaryProcCritDamage + primaryProcDot + secondaryDamage + secondaryProcDD + secondaryProcCritDamage+ secondaryProcDot
                + rangedDamage + rangedProcDD + rangedProcCritDamage + rangedProcDot + specialDamage) / (time/1000)));
        if (discUptime > 0) {
            System.out.println("Disc uptime: " + discUptime/1000 + " seconds");
            System.out.println("Damage dealt while under disc: " + discDamage);
        }
        if (primarySwingCounter > 0) {
            System.out.printf("%s total dps: %2.2f\n", primary.getWeaponName(), ((double)(primaryDamage + primaryProcDD
                    + primaryProcCritDamage + primaryProcDot + primaryCritDamage)/(time/1000)));
            System.out.println(primary.getWeaponName() + " total damage: " + primaryDamage);
            System.out.printf("%s physical dps: %2.2f\n", primary.getWeaponName(), ((double)(primaryDamage + primaryCritDamage)/(time/1000)));
            System.out.println(primary.getWeaponName() + " single swings: " + primarySwingCounter);
            if (primaryDoubleCounter > 0)
                System.out.println(primary.getWeaponName() + " double attacks: " + primaryDoubleCounter);
            if (primaryTripleCounter > 0)
                System.out.println(primary.getWeaponName() + " triple attacks: " + primaryTripleCounter);
            if (primaryQuadCounter > 0)
                System.out.println(primary.getWeaponName() + " quad attacks: " + primaryQuadCounter);
            if (primaryQuintCounter > 0)
                System.out.println(primary.getWeaponName() + " quint attacks: " + primaryQuintCounter);
            System.out.println(primary.getWeaponName() + " total swings: " + (primarySwingCounter
                                + primaryDoubleCounter + primaryTripleCounter + primaryQuadCounter + primaryQuintCounter));
            System.out.println(primary.getWeaponName() + " total misses: " + primaryMissCounter);
            System.out.println(primary.getWeaponName() + " min hit: " + primaryMinimumHit);
            System.out.println(primary.getWeaponName() + " max hit: " + primaryMaximumHit);
            if (primaryCritCounter> 0) {
                System.out.printf("%s crit percent: %2.4f%%\n", primary.getWeaponName(),  ((double)primaryCritCounter
                        / ((primarySwingCounter + primaryDoubleCounter + primaryTripleCounter
                        + primaryQuadCounter + primaryQuintCounter) - primaryMissCounter))*100);
                System.out.println(primary.getWeaponName() + " crit hits: " + primaryCritCounter);
                System.out.println(primary.getWeaponName() + " crit damage: " + primaryCritDamage);
                System.out.println(primary.getWeaponName() + " min crit: " + primaryMinimumCrit);
                System.out.println(primary.getWeaponName() + " max crit: " + primaryMaximumCrit);
            }
            if (primaryProcCounter > 0 && ((primaryProcDD > 0) || (primaryProcDot > 0))) {
                System.out.printf("%s proc dps: %2.2f\n", getCleanName(primary.getProcName()),
                        ((double) (primaryProcDD + primaryProcCritDamage + primaryProcDot) / (time / 1000)));
                System.out.println(getCleanName(primary.getProcName()) + " procs: " + primaryProcCounter);
                System.out.println(getCleanName(primary.getProcName()) + " resists: " + primaryProcResistCounter);
            }
            if (primaryProcDD > 0) {
                System.out.println(getCleanName(primary.getProcName()) + " proc DD: " + primaryProcDD);
                System.out.println(getCleanName(primary.getProcName()) + " proc min DD: " + primaryMinimumDD);
                System.out.println(getCleanName(primary.getProcName()) + " proc max DD: " + primaryMaximumDD);
            }
            if (primaryProcCritCounter > 0) {
                System.out.println(getCleanName(primary.getProcName()) + " crit procs: " + primaryProcCritCounter);
                System.out.println(getCleanName(primary.getProcName()) + " crit damage: " + primaryProcCritDamage);
            }
            if (primaryProcDot > 0)
                System.out.println(getCleanName(primary.getProcName()) + " proc dot damage: " + primaryProcDot);
        }
        if (secondarySwingCounter > 0) {
            System.out.printf("%s total dps: %2.2f\n", secondary.getWeaponName(), ((double)(secondaryDamage
                    + secondaryCritDamage + secondaryProcDD + secondaryProcCritDamage + secondaryCritDamage
                    + secondaryProcDot)/(time/1000)));
            System.out.printf("%s physical dps: %2.2f\n", secondary.getWeaponName(), ((double)(secondaryDamage
                    + secondaryCritDamage)/(time/1000)));
            System.out.println(secondary.getWeaponName() + " single swings: " + secondarySwingCounter);
            if (secondaryDoubleCounter > 0)
                System.out.println(secondary.getWeaponName() + " double attacks: " + secondaryDoubleCounter);
            System.out.println(secondary.getWeaponName() + " total swings: " + (secondarySwingCounter + secondaryDoubleCounter));
            System.out.println(secondary.getWeaponName() + " total misses: " + secondaryMissCounter);
            System.out.println(secondary.getWeaponName() + " total damage: " + secondaryDamage);
            System.out.println(secondary.getWeaponName() + " min hit: " + secondaryMinimumHit);
            System.out.println(secondary.getWeaponName() + " max hit: " + secondaryMaximumHit);
            if (secondaryCritCounter > 0) {
                System.out.printf("%s crit percent: %2.4f%%\n", secondary.getWeaponName(),
                         ((double)secondaryCritCounter / ((secondarySwingCounter + secondaryDoubleCounter)
                                 - secondaryMissCounter))*100);
                System.out.println(secondary.getWeaponName() + " crit hits: " + secondaryCritCounter);
                System.out.println(secondary.getWeaponName() + " crit damage: " + secondaryCritDamage);
                System.out.println(secondary.getWeaponName() + " min crit: " + secondaryMinimumCrit);
                System.out.println(secondary.getWeaponName() + " max crit: " + secondaryMaximumCrit);
            }
            if (secondaryProcCounter > 0 && ((secondaryProcDD > 0) || (secondaryProcDot > 0))) {
                System.out.printf("%s proc dps: %2.2f\n", getCleanName(secondary.getProcName()), ((double) (secondaryProcDD
                        + secondaryProcCritDamage + secondaryProcDot) / (time / 1000)));
                System.out.println(getCleanName(secondary.getProcName()) + " procs: " + secondaryProcCounter);
                System.out.println(getCleanName(secondary.getProcName()) + " resists: " + secondaryProcResistCounter);
            }
            if (secondaryProcDD > 0) {
                System.out.println(getCleanName(secondary.getProcName()) + " proc DD: " + secondaryProcDD);
                System.out.println(getCleanName(secondary.getProcName()) + " proc min DD: " + secondaryMinimumDD);
                System.out.println(getCleanName(secondary.getProcName()) + " proc max DD: " + secondaryMaximumDD);
            }
            if (secondaryProcCritCounter > 0) {
                System.out.println(getCleanName(secondary.getProcName()) + " crit procs: " + secondaryProcCritCounter);
                System.out.println(getCleanName(secondary.getProcName()) + " crit damage: " + secondaryProcCritDamage);
            }
            if (secondaryProcDot > 0)
                System.out.println(getCleanName(secondary.getProcName()) + " proc dot damage: " + secondaryProcDot);
        }
        if (rangedSwingCounter > 0) {
            System.out.printf("%s total dps: %2.2f\n", ranged.getWeaponName(), ((double)(rangedDamage + rangedProcDD
                    + rangedProcCritDamage + rangedProcDot)/(time/1000)));
            System.out.printf("%s physical dps: %2.2f\n", ranged.getWeaponName(), ((double)rangedDamage/(time/1000)));
            System.out.println("Number of shots: " + rangedSwingCounter);
            System.out.println("Missed shots: " + rangedMissCounter);
            System.out.println(ranged.getWeaponName() + " total damage: " + rangedDamage);
            System.out.println(ranged.getWeaponName() + " min hit: " + rangedMinimumHit);
            System.out.println(ranged.getWeaponName() + " max hit: " + rangedMaximumHit);
            if (rangedCritCounter > 0) {
                System.out.printf(ranged.getWeaponName() + " crit percent: %2.4f%%\n",
                        ((double)(rangedCritCounter - rangedMissCounter) / (rangedSwingCounter))*100);
                System.out.println(ranged.getWeaponName() + " crit hits: " + rangedCritCounter);
                System.out.println(ranged.getWeaponName() + " crit damage: " + rangedCritDamage);
                System.out.println(ranged.getWeaponName() + " min crit: " + rangedMinimumCrit);
                System.out.println(ranged.getWeaponName() + " max crit: " + rangedMaximumCrit);
            }
            if (rangedProcCounter > 0 && ((rangedProcDD > 0) || (rangedProcDot > 0))) {
                System.out.printf("%s proc dps: %2.2f\n", getCleanName(ranged.getProcName()), ((double) (rangedProcDD
                        + rangedProcCritDamage + rangedProcDot) / (time / 1000)));
                System.out.println(getCleanName(ranged.getProcName()) + " procs: " + rangedProcCounter);
                System.out.println(getCleanName(ranged.getProcName()) + " resists: " + rangedProcResistCounter);
            }
            if (rangedProcDD > 0) {
                System.out.println(getCleanName(ranged.getProcName()) + " proc DD: " + rangedProcDD);
                System.out.println(getCleanName(ranged.getProcName()) + " proc min DD: " + rangedMinimumDD);
                System.out.println(getCleanName(ranged.getProcName())+ " proc max DD: " + rangedMaximumDD);
            }
            if (rangedProcCritCounter > 0) {
                System.out.println(getCleanName(ranged.getProcName()) + " crit procs: " + rangedProcCritCounter);
                System.out.println(getCleanName(ranged.getProcName()) + " crit damage: " + rangedProcCritDamage);
            }
            if (rangedProcDot > 0)
                System.out.println(getCleanName(ranged.getProcName()) + " proc dot damage: " + rangedProcDot);
        }
        if (specialSwingCounter > 0) {
            System.out.printf("%s total dps: %2.2f\n", special, ((double)specialDamage/(time/1000)));
            System.out.println(special + " total swings: " + specialSwingCounter);
            if (specialDoubleCounter > 0) {
                System.out.println(special + " double attacks: " + specialDoubleCounter);
            }
            System.out.println(special + " total misses: " + specialMissCounter);
            System.out.println(special + " total damage: " + specialDamage);
            System.out.println(special + " min hit: " + specialMinimumHit);
            System.out.println(special + " max hit: " + specialMaximumHit);
            if (specialCritCounter > 0) {
                System.out.printf(special + " crit percent: %2.4f%%\n",
                        ((double)(specialCritCounter) / (specialSwingCounter + specialDoubleCounter - specialMissCounter))*100);
                System.out.println(special + " crit damage: " + specialCritDamage);
                System.out.println(special + " min crit: " + specialMinimumCrit);
                System.out.println(special + " max crit: " + specialMaximumCrit);
            }
        }
    }

    /**
     * replaces all extra characters from proc names
     * @param procName the database entry for proc name
     * @return a cleaned string version of the proc name
     */
    public static String getCleanName(String procName) {
        return procName.replaceAll("[\\\\]", "");
    }
}
