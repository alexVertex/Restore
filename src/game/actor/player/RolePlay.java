package game.actor.player;

public class RolePlay {
    public static final int MAGIC_SKILL_BONUS = 10;
    public static final int MAGIC_INT_BONUS = 15;
    public static final double HEALTH_POWER_XP = 0.25;
    public static final double HEALTH_MULT_XP = 13;
    public static final int HEALTH_ADD_ATTR = 25;
    public static final double HEALTH_MULT_ATTR = 0.04;
    public static final double HEALTH_MULT_SKILL = 0.1;
    public static final double STAMINA_POWER_XP = 0.25;
    public static final double STAMINA_MULT_XP = 13;
    public static final int STAMINA_ADD_ATTR = 25;
    public static final double STAMINA_MULT_ATTR = 0.04;
    public static final double STAMINA_MULT_SKILL = 0.1;
    public static final int EQUIP_BASE = 40;
    public static final int EQUIP_MULT_ATTR = 2;
    public static final int EQUIP_MULT_SKILL = 4;
    public static final double SPEED_BASE = 4.0;
    public static final double SPEED_MULT_ATTR = 0.2;
    public static final double SPEED_MULT_SKILL = 1.0;

    static final int RECOVERY_TIME_AFTER_RUN = 50;

    public static void setParams(Player player) {
        int totalXP = player.getVariableInteger("totalXP");
        int strength = player.getVariableInteger("attributeStrength");
        int skill = player.getVariableInteger("skillHealth");
        double totalHealth = (Math.pow(totalXP , HEALTH_POWER_XP) * HEALTH_MULT_XP) * (1 + (strength - HEALTH_ADD_ATTR) * HEALTH_MULT_ATTR);
        totalHealth *= 1 + skill * 0.1;
        totalHealth = Math.floor(totalHealth);
        player.setVariable("maxHealth", totalHealth);

        int agility = player.getVariableInteger("attributeAgility");
        skill = player.getVariableInteger("skillStamina");
        double totalStamina = (Math.pow(totalXP , STAMINA_POWER_XP) * STAMINA_MULT_XP) * (1 + (agility - STAMINA_ADD_ATTR) * STAMINA_MULT_ATTR);
        totalStamina *= 1 + skill * STAMINA_MULT_SKILL;
        totalStamina = Math.floor(totalStamina);
        player.setVariable("maxStamina", totalStamina);

        skill = player.getVariableInteger("skillEquipLoad");
        double totalEquipLoad = EQUIP_BASE + strength * EQUIP_MULT_ATTR;
        totalEquipLoad += skill * EQUIP_MULT_SKILL;
        player.setVariable("maxEquipLoad", totalEquipLoad);

        skill = player.getVariableInteger("skillAttackSpeed");
        double totalAttackSpeed = (agility * SPEED_MULT_ATTR + SPEED_BASE + skill * SPEED_MULT_SKILL) / 100.0;
        player.setVariable("attackSpeed", totalAttackSpeed);

        double powerAttack = ((strength - 25) * 0.75 + 100) / 100;
        player.setVariable("attackStrength", powerAttack);

        double agilityAttack = ((agility - 25) * 0.75 + 100) / 100;

        player.setVariable("attackAgility", agilityAttack);
        int intellect = player.getVariableInteger("attributeIntellect");
        int baseMagic = intellect-MAGIC_INT_BONUS;
        if(baseMagic<0) baseMagic = 0;
        int magic = baseMagic + player.getVariableInteger("skillFireMagic")*MAGIC_SKILL_BONUS;
        player.setVariable("magicFire", magic);
        magic = baseMagic + player.getVariableInteger("skillWaterMagic")*MAGIC_SKILL_BONUS;
        player.setVariable("magicWater", magic);
        magic = baseMagic + player.getVariableInteger("skillEarthMagic")*MAGIC_SKILL_BONUS;
        player.setVariable("magicEarth", magic);
        magic = baseMagic + player.getVariableInteger("skillAirMagic")*MAGIC_SKILL_BONUS;
        player.setVariable("magicAir", magic);
        magic = baseMagic + player.getVariableInteger("skillMentalMagic")*MAGIC_SKILL_BONUS;
        player.setVariable("magicMind", magic);
        magic = baseMagic + player.getVariableInteger("skillWizardry")*MAGIC_SKILL_BONUS;
        player.setVariable("magicWizardry", magic);
        magic = baseMagic + player.getVariableInteger("skillSacredMagic")*MAGIC_SKILL_BONUS;
        player.setVariable("magicSacred", magic);
    }

    static void recovery(Player player) {
        int recoveryTimeWait = (int) player.getVariable("timeBeforeRecovery");
        if (recoveryTimeWait > 0) {
            recoveryTimeWait--;
            player.setVariable("timeBeforeRecovery", recoveryTimeWait);
        } else {
            double curStamina = (double) player.getVariable("stamina");
            double maxStamina = (double) player.getVariable("maxStamina");

            double recoveryRate = 0.09;
            double recoveryWaitRate = 1;
            double overLoad = player.overload();
            if (overLoad > 1.2) {
                recoveryWaitRate = 0.25;
            } else if (overLoad > 0.7) {
                recoveryWaitRate = 0.5;
            } else if (overLoad > 0.3) {
                recoveryWaitRate = 0.75;
            }
            double plusStamina = maxStamina * recoveryRate * recoveryWaitRate;
            curStamina += plusStamina;
            if (curStamina > maxStamina) {
                curStamina = maxStamina;
            }
            player.setVariable("stamina", curStamina);
            int restorationSkill = player.getVariableInteger("skillRestoration");
            int waitBeforeRecodery = (RECOVERY_TIME_AFTER_RUN);
            int saveRecoveryTime = waitBeforeRecodery * restorationSkill / 20;
            waitBeforeRecodery -= saveRecoveryTime;
            player.setVariable("timeBeforeRecovery", waitBeforeRecodery);
        }
    }

    private static final int XP_DIFF_BORDER = 250;
    private static final int XP_DIFF_SPLIT = 10;
    private static final int XP_GAIN = 2;
    static void gainXP(Player player, int deadXP){
        PlayerVoice.setVoiceInterface(player,PlayerVoice.VOICE_KILLED);

        int lastHitXP = (int)player.getVariable("exp");
        int lastHitXPTotal = (int)player.getVariable("totalXP");

        int addXP = deadXP;


        lastHitXP+=addXP;
        player.setVariable("exp",lastHitXP);
        player.setVariable("totalXP",player.getVariableInteger("totalXP")+addXP);
        RolePlay.setParams(player);
    }
}
