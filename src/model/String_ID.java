package model;

/**
 * Model for discipline flavor text
 */
public class String_ID {

    /**
     * Flavor text for when a discipline is used
     * @param discID the name of the discipline
     * @return the flavor text of the disc
     */
    public static String discStartFlavorText(int discID) {

        switch (discID) {
            case (1007) : //SLAY UNDEAD MALE USER
                return "'s holy blade cleanses his target!";
            case (1008) : //SLAY UNDEAD FEMALE USER
                return "'s holy blade cleanses her target!;";
            case (1048) : //AGGRESSIVE
                return "You assume an aggressive fighting style.";
            case (1049) : //PRECISION
                return  "You assume a highly precise fighting style.";
            case (1050) : //DEFENSIVE
                return "You assume a defensive fighting style.";
            case (1051) : //EVASIVE
                return "You assume an evasive fighting style.";
            case (1052) : //SILENTFIST
                return "Your fist clenches in silent but deadly fury.";
            case (1053) : //ASHENHAND
                return "Your fist clenches with fatal fervor.";
            case (1054) : //SILENTFIST_IKSAR
                return "Your tail twitches in silent but deadly fury.";
            case (1055) : //FURIOUS
                return "Your face becomes twisted with fury.";
            case (1056) : //STONESTANCE
                return "Your feet become one with the earth.";
            case (1057) : //THUNDERKICK
                return "Your feet glow with mystical power.";
            case (1058) : //FORTITUDE
                return "You become untouchable.";
            case (1059) : //FELLSTRIKE
                return "Your muscles bulge with force of will.";
            case (1060) : //NIMBLE
                return "Your reflexes become sharpened by concentrated efforts.";
            case (1061) : //CHARGE
                return "You feel unstoppable!";
            case (1062) : //MIGHTYSTRIKE
                return "You feel like a killing machine!";
            case (1063) : //HUNDREDFIST
                return "You assume an intimidating demeanor.";
            case (1064) : //KINESTHETICS
                return "Your arms feel alive with mystical energy.";
            case (1065) : //HOLYFORGE
                return "Your weapon is bathed in a holy light.";
            case (1066) : //SANCTIFICATION
                return "A sanctifying aura surrounds you.";
            case (1067) : //TRUESHOT
                return "Your bow crackles with natural energy.";
            case (1068) : //WPNSLD_MALE
                return "Your deftly twirls his weapon(s).";
            case (1069) : //WPNSLD_FEMALE
                return "You deftly twirls her weapon(s).";
            case (1070) : //WPNSLD_MONSTER
                return "Your deftly twirls your weapon(s).";
            case (1071) : //UNHOLYAURA
                return "An unholy aura envelops you.";
            case (1072) : //LEECHCURSE
                return "Your weapon pulsates with an eerie blue light.";
            case (1073) : //DEFTDANCE
                return "Your prances about nimbly.";
            case (1074) : //PURETONE
                return "Your voice becomes perfectly melodius.";
            case (1075) : //RESISTANT
                return "You have become more resistant.";
            case (1076) : //FEARLESS
                return "You become fearless.";
            default:
                return "";
        }
    }

    /**
     * Flavor text for when a discipline ends
     * @param discID the name of the discipline
     * @return the flavor text of the disc
     */
    public static String discEndFlavorText(int discID) {

        switch (discID) {
            case (1048) : //AGGRESSIVE
            case (1049) : //PRECISION
            case (1050) : //DEFENSIVE
            case (1051) : //EVASIVE
                return "You return to your normal fighting style.";
            case (1052) : //SILENTFIST
            case (1054) : //SILENTFIST_IKSAR
                return "The silent fury fades away.";
            case (1053) : //ASHENHAND
                return "Your fists lose their fervor.";
            case (1055) : //FURIOUS
                return "The rage leaves you.";
            case (1056) : //STONESTANCE
                return "You are no longer one with the earth.";
            case (1057) : //THUNDERKICK
                return "The glow fades from your feet.";
            case (1058) : //FORTITUDE
                return "Your battle instinct leaves you.";
            case (1059) : //FELLSTRIKE
                return "Your weapons lose their accuracy.";
            case (1060) : //NIMBLE
            case (1073) : //DEFTDANCE
                return "You movements return to normal.";
            case (1061) : //CHARGE
                return "Your perfect focus fades";
            case (1062) : //MIGHTYSTRIKE
                return "Your killer instinct fades.";
            case (1063) : //HUNDREDFIST
                return "Your hands slow down.";
            case (1064) : //KINESTHETICS
                return "The mystic energy fades.";
            case (1065) : //HOLYFORGE
                return "The holy light fades from your weapon.";
            case (1066) : //SANCTIFICATION
                return "Your sanctification fades.";
            case (1067) : //TRUESHOT
                return "The natural energy fades from your bow.";
            case (1068) : //WPNSLD_MALE
            case (1069) : //WPNSLD_FEMALE
            case (1070) : //WPNSLD_MONSTER
                return "Your weapons slow down.";
            case (1071) : //UNHOLYAURA
                return "The unholy aura fades.";
            case (1072) : //LEECHCURSE
                return "The dark energy fades from your skin.";
            case (1074) : //PURETONE
                return "Your voice loses its perfection.";
            case (1075) : //RESISTANT
                return "Your resistance fades.";
            case (1076) : //FEARLESS
                return "The specter of fear returns to your mind.";
            default:
                return "";
        }
    }
}
