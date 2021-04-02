package game.actor.player;

import engine.Actor;
import engine.Mathp;
import engine.Start;
import game.StartGame;
import game.actor.actorsHyper.ItemDataBase;
import game.actor.enviroment.Item;
import game.actor.game.Projectile;
import game.actor.magic.MagicProjectile;
import screens.dialogs.DialogScreen;

import static game.actor.player.Player.*;
import static game.actor.player.AutoControl.BATTLEMODE_BLOCK;
import static game.actor.player.PlayerMovesSet.MOVE_TARGET_NON;
import static game.actor.player.PlayerMovesSet.setTargetAttack;
import static game.actor.actorsHyper.ItemDataBase.TYPE_BOWS;

class PlayerAttacks {

    static String[] armors = {"armorHead","armorTorso","armorArms","armorLegs"};

    private static boolean isGuarding(Player player,Player lastHit){
        if(player.getVariable("battleMode").equals(BATTLEMODE_BLOCK)){
            double angle = Math.atan2(lastHit.getTileLoc()[1] - player.getTileLoc()[1], lastHit.getTileLoc()[0] - player.getTileLoc()[0]);
            double angleWatch = 0;
            switch ((int) player.getVariable("partY")) {
                case 0:
                    angleWatch = Math.PI / 2;
                    break;
                case 2:
                    angleWatch = 0;
                    break;
                case 1:
                    angleWatch = Math.PI;
                    break;
                case 3:
                    angleWatch = -Math.PI / 2;
                    break;
            }
            return Mathp.angleBetwinAngles(angle, angleWatch) < Math.PI / 2;
        }
        return false;
    }
    private static double damageThruShield(Player player, Projectile projectile, double damage){
        Item shield = (Item) player.getVariable("shieldCur");
        if (shield == null) {
            shield = new Item("", 0, 0, 0, 0, null, 0);
            shield.setVariable("armorPierce", 2);
            shield.setVariable("armorSlash", 2);
            shield.setVariable("armorStrike", 2);
            shield.setVariable("stability", 0.3);
            shield.setVariable("locX",player.getVariable("locX"));
            shield.setVariable("locY",player.getVariable("locY"));
            PlayerVoice.setVoiceIngame(shield, "weapons/shieldArms", true);

        } else {
            shield.setVariable("locX",player.getVariable("locX"));
            shield.setVariable("locY",player.getVariable("locY"));
            if(shield.getVariable("armorClass").equals("0")) {
                PlayerVoice.setVoiceIngame(shield, "weapons/shieldLight", true);
            }
            if(shield.getVariable("armorClass").equals("1")) {
                PlayerVoice.setVoiceIngame(shield, "weapons/shieldHeavy", true);
            }
        }
        String damageType = projectile.getVariable("damageType")+"";
        double shieldDamage = (int)shield.getVariable(damageType);
        double shieldStability = (double)shield.getVariable("stability");
        damage -= shieldDamage;
        if(damage <0) {damage = 0;}
        double stamina = (double)player.getVariable("stamina");
        double staminaDamage = damage * (shieldStability)*1.5;
        damage -= staminaDamage;
        stamina -= staminaDamage;
        if(stamina <0) {
            damage+=-(stamina)*3;
            stamina = 0;
            AutoControl.changeBattleMode(player, AutoControl.BATTLEMODE_WAIT);
        }
        player.setVariable("stamina",stamina);
        player.setVariable("timeBeforeRecovery",(150));
        return damage;
    }

    private static double damageThruArmor(Player player,double damage,String damageType,String partHit){
        Item headArmor = (Item)player.getVariable(partHit);
        if(headArmor != null){
            damage -= headArmor.getVariableInteger(damageType);
        } else {
            damage -= player.getVariableInteger(damageType);

        }
        if(damage < 0) damage = 0;
        return damage;
    }

    private static void takeDamage(Player player, Projectile projectile){
        Player lastHit = (Player)projectile.getVariable("whohit");
        boolean guard = isGuarding(player,lastHit);
        int tatgetOn = (int)projectile.getVariable("targetOn");
        double damage = (double)projectile.getVariable("damage");
        boolean backStab = false;
        boolean missed = false;
        if(!guard) {
            if (player instanceof NPC && player.getVariable("alarmed").equals(0)) {//Backstab
                damage = damageThruArmor(player,damage,projectile.getVariable("damageType")+"","armorHead");
                damage *= 2;
                backStab = true;
            }
            if (tatgetOn == 0) {//Targeting Head
                if (Mathp.random() > 0.9 || backStab) {
                    damage = damageThruArmor(player,damage,projectile.getVariable("damageType")+"","armorHead");
                    damage *= 2;
                } else if (Mathp.random() < 0.2) {
                    missed = true;
                    damage *= 0;
                }
            } else if (tatgetOn == 1) {//Targeting Body
                damage = damageThruArmor(player,damage,projectile.getVariable("damageType")+"","armorTorso");
                damage *= 1.0;
            } else if (tatgetOn == 2) {//Targeting Arms
                damage = damageThruArmor(player,damage,projectile.getVariable("damageType")+"","armorArms");
                damage *= 0.7;
                player.setVariable("damageArms", (double) player.getVariable("damageArms") + damage);
            } else if (tatgetOn == 3) {//Targeting Legs
                damage = damageThruArmor(player,damage,projectile.getVariable("damageType")+"","armorLegs");
                damage *= 0.7;
                player.setVariable("damageLegs", (double) player.getVariable("damageLegs") + damage);
            } else {//No specific target hit
                if (Mathp.random() > 0.95) {//Head
                    damage = damageThruArmor(player,damage,projectile.getVariable("damageType")+"","armorHead");
                    damage *= 2.0;
                    tatgetOn = 0;
                } else {
                    double random = Mathp.random();
                    if (random > 0.75) {//Arms
                        damage = damageThruArmor(player,damage,projectile.getVariable("damageType")+"","armorArms");
                        player.setVariable("damageArms", (double) player.getVariable("damageArms") + damage);
                        tatgetOn = 2;
                    } else if (random < 0.25) {//Legs
                        damage = damageThruArmor(player,damage,projectile.getVariable("damageType")+"","armorLegs");
                        player.setVariable("damageLegs", (double) player.getVariable("damageLegs") + damage);
                        tatgetOn = 3;
                    } else {//Body
                        damage = damageThruArmor(player,damage,projectile.getVariable("damageType")+"","armorTorso");
                        tatgetOn = 1;
                    }
                }
            }
        }
        if(missed){
            PlayerVoice.setVoiceIngame(lastHit,"weapons/miss",true);

        } else {
            if (guard) {
                damage = damageThruShield(player, projectile, damage);
            } else {
                String[] damageClassesSkills = {"armorPierce","armorSlash","armorStrike"};
                int weapon = 0;
                for(int i = 0; i < damageClassesSkills.length;i++){
                    if(damageClassesSkills[i].equals(projectile.getVariable("damageType"))){
                        weapon = i+1;
                    }
                }
                int skin = 1;//только один тип кожи у своих
                if(player instanceof NPC){
                    skin = player.getVariableInteger("skinType")+1;
                } else {
                    Actor armor = (Actor) player.getVariable(armors[tatgetOn]);
                    if (armor != null) {
                        skin = armor.getVariableInteger("sound")+1;
                    }
                }
                PlayerVoice.setVoiceIngame(lastHit,"weapons/"+weapon+""+skin,true);
            }
            double health = (double) player.getVariable("health");
            health -= damage;
            if (health <= 0) {
                player.death(lastHit);
                health = 0;
            }
            player.setVariable("health", health);
        }
        if(player instanceof NPC){
            ((NPC)player).getAttaked(lastHit);
        } else {

        }
        if(player.getVariable("projectile") == null)
        PlayerVoice.setVoiceIngame(player,0,false);
    }

    static void setTargetEnemy(Player player, Player targetEnemy){
        Item playerWeapon = (Item) player.getVariable("weaponCur");
        if(playerWeapon == null){
            playerWeapon = new Item(null,0,0,0,0,null,0);
            playerWeapon.setVariable("distant",player.getVariable("distant"));
            playerWeapon.setVariable("attackSpeed",BARE_FIST_ATTACKSPEED);
            Projectile melee = new Projectile("melee1",targetEnemy.getTileLoc()[0],targetEnemy.getTileLoc()[1],32,16,4,(double)player.getVariableTrunked("damage"),player.getVariable("damageType")+"");
            playerWeapon.setVariable("projectile",melee);
            playerWeapon.setVariable("weaponClass","skillHandToHand");
            playerWeapon.setVariable("damage",(double)player.getVariableTrunked("damage"));
        }
        double realDamage = (double)(playerWeapon.getVariable("damage"));//damage mod by weaponSkill
        String weaponSkill = playerWeapon.getVariable("weaponClass")+"";
        int attribute = 0;
        if(weaponSkill.equals("skillBlades") || weaponSkill.equals("skillAxes") || weaponSkill.equals("skillBlunts") || weaponSkill.equals("skillHandToHand")){
            attribute = StartGame.game.getControled().getVariableTrunked("attackStrength");
        } else {
            attribute = StartGame.game.getControled().getVariableTrunked("attackAgility");
        }
        double weaponSkillVal = StartGame.game.getControled().getVariableInteger(weaponSkill);
        realDamage *= attribute;
        realDamage *= 1+weaponSkillVal*Player.DAMAGE_WEAPON_SKILL_MULT;

        if(playerWeapon.getVariable("projectile") == null){
            int projectileID = playerWeapon.getVariableInteger("projectileID");
            Projectile projectile = ItemDataBase.projectiles[projectileID];
            projectile.setVariable("damageType",playerWeapon.getVariable("damageType")+"");
            playerWeapon.setVariable("projectile",projectile);
        }
        ((Actor)playerWeapon.getVariable("projectile")).setVariable("damage",realDamage);
        double weaponRange = (double)playerWeapon.getVariable("distant");
        boolean clear = PathFinding.getCollisionPoint(player.getTileLoc()[0],player.getTileLoc()[1],targetEnemy.getTileLoc()[0],targetEnemy.getTileLoc()[1],false) == null;
        if(Mathp.inRange(player.getTileLoc()[0],player.getTileLoc()[1],targetEnemy.getTileLoc()[0],targetEnemy.getTileLoc()[1],weaponRange) && clear){
            player.setVariable("path",null);
            generateProjectile(player,targetEnemy,playerWeapon);
            player.setVariable("partX",1);
            double[] tar = targetEnemy.getTileLoc();
            double angle = Math.atan2(tar[1] - player.getTileLoc()[1], tar[0] - player.getTileLoc()[0]);
            if (Math.abs(angle) <= Math.PI * 0.25) {
                player.setVariable("partY", 2);
            } else if (Math.abs(angle) >= Math.PI * 0.75) {
                player.setVariable("partY", 1);
            } else if (angle <= Math.PI * 0.75 && angle >= Math.PI * 0.25) {
                player.setVariable("partY", 0);
            } else {
                player.setVariable("partY", 3);
            }
        } else {
            setTargetAttack(player,targetEnemy,player.getVariableInteger("targetOn"));
        }
    }

    static void setTargetFriend(Player player, Player targetEnemy){
        if(Mathp.inRange(player.getTileLoc()[0],player.getTileLoc()[1],targetEnemy.getTileLoc()[0],targetEnemy.getTileLoc()[1],32)){
            player.setVariable("path",null);
            player.setVariable("partX",1);
            double[] tar = targetEnemy.getTileLoc();
            double angle = Math.atan2(tar[1] - player.getTileLoc()[1], tar[0] - player.getTileLoc()[0]);
            if (Math.abs(angle) <= Math.PI * 0.25) {
                player.setVariable("partY", 2);
            } else if (Math.abs(angle) >= Math.PI * 0.75) {
                player.setVariable("partY", 1);
            } else if (angle <= Math.PI * 0.75 && angle >= Math.PI * 0.25) {
                player.setVariable("partY", 0);
            } else {
                player.setVariable("partY", 3);
            }
            player.setVariable("moveTargetType", MOVE_TARGET_NON);
            Start.setScreen(new DialogScreen((Player) player.getVariable("moveTargetAttack")));
            player.setVariable("moveTarget", null);

        } else {
            setTargetAttack(player,targetEnemy,player.getVariableInteger("targetOn"));
        }
    }

    private static void generateProjectile(Player player, Player targetEnemy, Item weapon){
        if(((Actor)player.getVariable("moveTargetAttack")).getVariable("dead").equals(1)){
            PlayerMovesSet.setTarget(player, player.getTileLoc()[0],player.getTileLoc()[1]);
            player.setVariable("moveTargetType",MOVE_TARGET_NON);
            return;
        }
        if(player.getVariable("projectile")==null) {
            Object fires = weapon.getVariable("fires");
            if(fires != null && fires.equals(0)){
                PlayerMovesSet.setTarget(player, player.getTileLoc()[0],player.getTileLoc()[1]);
                player.setVariable("moveTargetType",MOVE_TARGET_NON);
                return;
            }
            double attackProgressAdd = (double) player.getVariable("attackSpeed") + (double) weapon.getVariable("attackSpeed");
            attackProgressAdd*=0.05;
            double armsDamage = player.damageArmsPercent();//damage mode by armsDamage
            if(armsDamage > 0.9){
                attackProgressAdd *= 0.5;
            } else if (armsDamage > 0.65){
                attackProgressAdd *= 0.7;
            }else if (armsDamage > 0.4){
                attackProgressAdd *= 0.9;
            }
            Projectile melee = (Projectile) weapon.getVariable("projectile");
            melee.setVariable("attacked",0);
            melee.setVariable("targetOn",player.getVariable("targetOn"));
            melee.setVariable("locX", targetEnemy.getTileLoc()[0]);
            melee.setVariable("locY", targetEnemy.getTileLoc()[1]);
            melee.setVariable("whohit", player);

            player.setVariable("projectile", melee);
            player.setVariable("attackProgressAdd", attackProgressAdd);
            player.setVariable("attackProgress", 0.001);
        }
    }

    static void attackProgress(Player player){
        if(((Actor)player.getVariable("moveTargetAttack")).getVariableInteger("dead") == 1){
            player.setVariable("attackProgress",0.0);
            player.setVariable("projectile", null);
            return;
        }
        double attackProgress = (double) player.getVariable("attackProgress");
        double attackProgressAdd = (double) player.getVariable("attackProgressAdd");
        attackProgress += attackProgressAdd;
        Item weapon = (Item)player.getVariable("weaponCur");
        boolean farWeapon = false;
        if(weapon != null){
            int filter = weapon.getVariableInteger("type");
            if(filter == TYPE_BOWS){
                farWeapon = true;
            }
        }
        if(farWeapon){
            if (attackProgress >= 1) {
                attackProgress = 0.0;
                Projectile melee = (Projectile) player.getVariable("projectile");
                MagicProjectile mp = new MagicProjectile(melee.getVariableString("texture").split("/")[1],(int)player.getTileLoc()[0],(int)player.getTileLoc()[1],
                        melee.getVariableTrunked("size"),((Player) player.getVariable("moveTargetAttack")),player,melee.getVariableTrunked("damage"),"magicFire","bolt",null,null,0,0);
                StartGame.game.projectiles.add(mp);
                weapon.setVariable("fires",weapon.getVariableInteger("fires")-1);
                player.setVariable("projectile", null);
                if(weapon.getVariableString("weaponClass").equals("skillBows")) {
                    PlayerVoice.setVoiceIngame(player, "weapons/bowShot", true);
                } else {
                    PlayerVoice.setVoiceIngame(player, "weapons/crossbowShot", true);

                }

            }
        } else {
            if (attackProgress >= 1) {
                attackProgress = 0.0;
                player.setVariable("projectile", null);
            } else {
                Projectile melee = (Projectile) player.getVariable("projectile");
                boolean attackFrame = melee.setFrame(attackProgress);
                if (attackFrame) {
                    PlayerAttacks.takeDamage(((Player) player.getVariable("moveTargetAttack")), melee);
                    PlayerVoice.setVoiceIngame(player,2,true);

                }
            }
        }
        player.setVariable("attackProgress",attackProgress);
    }
}
