package screens.camp;

import engine.audio3.AudioManager;
import engine.control.InputMain;
import engine.control.interfaces.Button;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import game.actor.player.Player;
import game.actor.player.RolePlay;
import org.newdawn.slick.Color;
import screens.Controls;
import screens.ingame.GameScreen;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;

public class camp_skills extends GameScreen {
    private static final int SKILL_MAX_VALUE = 100;
    private static String[] skillsNames = {"skillHealth","skillStamina","skillRestoration","skillEquipLoad","skillAttackSpeed","skillDaggers","skillBlades","skillAxes","skillBlunts","skillSpears","skillBows","skillCrossbows","skillFireMagic","skillWaterMagic","skillEarthMagic","skillAirMagic","skillMentalMagic","skillWizardry","skillSacredMagic","skillLockPick"};
    private static String[] skills = {"Здоровье","Выносливость","Восстановление","Нагрузка","Скорость действий","Кинжалы","Мечи","Топоры","Дробящее оружие","Копья","Луки","Арбалеты","Магия огня","Магия воды","Магия земли","Магия воздуха","Псионика","Волшебство","Сакральная магия","Взлом замков"};
    private static int[] skillsVal = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private  static int[] skillsLowest = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private  static int[] skillsCost = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private int totalSkillsVal;
    private static List<Button> minuses = new ArrayList<>();
    private static List<Button> pluses = new ArrayList<>();
    private Button cancel = new Button(Render2D.getWindowWidth()/2-(float)(600/2)+147,(int)Render2D.getWindowHeight()/2-516/2+55+20*22+9,18,18,"interface/butCancel");
    private Button OK = new Button((int)Render2D.getWindowWidth()/2-600/2+147+125,(int)Render2D.getWindowHeight()/2-516/2+55+20*22+9,18,18,"interface/butOK");

    static {
        for(int i = 0; i < skills.length;i++){
            Button minus = new Button(Render2D.getWindowWidth()/2-(float)600/2+147,(int)Render2D.getWindowHeight()/2-516/2+30+i*22+9,18,18,"interface/butMinus","interface/AddQuest");
            minuses.add(minus);
            Button plus = new Button(Render2D.getWindowWidth()/2-(float)600/2+207,(int)Render2D.getWindowHeight()/2-516/2+30+i*22+9,18,18,"interface/butPlus","interface/CampAddSkill");
            pluses.add(plus);
        }
    }

    @Override
    public void preLoad(){
        setSkillVals();
    }

    private int curEXP;
    private int needEXP = 0;
    private int uppedSkills = 0;
    private void setSkillVals(){
        totalSkillsVal = 0;
        for (int i = 0; i < skills.length; i++) {
            skillsVal[i] = StartGame.game.getControled().getVariableInteger(skillsNames[i]);
            skillsLowest[i] = skillsVal[i];
            totalSkillsVal += skillsVal[i];
        }
        curEXP = StartGame.game.getControled().getVariableInteger("exp");
    }

    @Override
    public void input() {
        boolean changedPlayer = Controls.playerChoos();
        Controls.campScreenChoose();
        Controls.closeCampScreen();
        if(changedPlayer) setSkillVals();
        for(int i = 0; i <skills.length;i++){
            minuses.get(i).work();
            if(minuses.get(i).isResponsed()){
                if(skillsVal[i]>skillsLowest[i]){
                    skillsVal[i]--;
                    totalSkillsVal--;
                    needEXP-=(skillsCost[i]-SKILL_ALL_COST-SKILL_BASE_COST);
                    uppedSkills--;
                    AudioManager.playSoundInterface("interface/CampAddSkill");

                }
            }
            pluses.get(i).work();
            if(pluses.get(i).isResponsed()){
                skillsVal[i]++;
                totalSkillsVal++;
                needEXP+=skillsCost[i];
                uppedSkills++;
                AudioManager.playSoundInterface("interface/CampAddSkill");

            }
        }
        cancel.work();
        if(cancel.isReleased()){
            needEXP = 0;
            totalSkillsVal-=uppedSkills;
            uppedSkills = 0;
            System.arraycopy(skillsLowest, 0, skillsVal, 0, skillsVal.length);
            AudioManager.playSoundInterface("interface/Cancel");

        }
        OK.work();
        if(OK.isReleased()) {
            StartGame.game.getControled().setVariable("exp", curEXP - needEXP);
            curEXP -= needEXP;
            needEXP = 0;
            for (int i = 0; i < skills.length; i++) {
                skillsLowest[i] = skillsVal[i];
                StartGame.game.getControled().setVariable(skillsNames[i], skillsVal[i]);
            }
            RolePlay.setParams(StartGame.game.getControled());
            StartGame.game.getControled().setVariable("health", StartGame.game.getControled().getVariable("maxHealth"));
            StartGame.game.getControled().setVariable("stamina", StartGame.game.getControled().getVariable("maxStamina"));
            uppedSkills = 0;
            AudioManager.playSoundInterface("interface/Ok");

        }
    }

    private static final int SKILL_BASE_COST = 100;
    private static final int SKILL_ALL_COST = 50;
    private void recalculateCosts() {
        for (int i = 0; i < skills.length; i++) {
            skillsCost[i] = (skillsVal[i]+1)*SKILL_BASE_COST+totalSkillsVal*SKILL_ALL_COST;
            if(skillsCost[i] > curEXP-needEXP || skillsVal[i] == SKILL_MAX_VALUE){
                pluses.get(i).deactivate();
            } else {
                pluses.get(i).activate();
            }
            if(skillsVal[i] > skillsLowest[i]){
                minuses.get(i).activate();
            } else {
                minuses.get(i).deactivate();
            }
        }
    }

    @Override
    public void logic() {
        recalculateCosts();
        setParams(StartGame.game.getControled());
    }

    private void setParams(Player player) {
        int totalXP = player.getVariableInteger("totalXP");
        int strength = player.getVariableInteger("attributeStrength");
        int skill = skillsVal[0];
        double totalHealth = (Math.pow(totalXP, RolePlay.HEALTH_POWER_XP) * RolePlay.HEALTH_MULT_XP) * (1 + (strength - RolePlay.HEALTH_ADD_ATTR) * RolePlay.HEALTH_MULT_ATTR);
        totalHealth *= 1 + skill * RolePlay.HEALTH_MULT_SKILL;
        totalHealth = Math.floor(totalHealth);

        int agility = player.getVariableInteger("attributeAgility");
        skill = skillsVal[1];
        double totalStamina = (Math.pow(totalXP, RolePlay.STAMINA_POWER_XP) * RolePlay.STAMINA_MULT_XP) * (1 + (agility - RolePlay.STAMINA_ADD_ATTR) * RolePlay.STAMINA_MULT_ATTR);
        totalStamina *= 1 + skill *  RolePlay.STAMINA_MULT_SKILL;
        totalStamina = Math.floor(totalStamina);

        skill = skillsVal[3];
        double totalEquipLoad = RolePlay.EQUIP_BASE + strength * RolePlay.EQUIP_MULT_ATTR;
        totalEquipLoad += skill * RolePlay.EQUIP_MULT_SKILL;
        skill = skillsVal[4];
        double totalAttackSpeed = (agility * RolePlay.SPEED_MULT_ATTR + RolePlay.SPEED_BASE  + skill * RolePlay.SPEED_MULT_SKILL) / 100.0;

        int intellect = player.getVariableInteger("attributeIntellect");
        int baseMagic = intellect-RolePlay.MAGIC_INT_BONUS;
        if(baseMagic<0) baseMagic = 0;
        int magic = baseMagic + skillsVal[12]*RolePlay.MAGIC_SKILL_BONUS;
        attributesVals[6] = magic+"";
        magic = baseMagic + skillsVal[13]*RolePlay.MAGIC_SKILL_BONUS;
        attributesVals[7] = magic+"";
        magic = baseMagic + skillsVal[14]*RolePlay.MAGIC_SKILL_BONUS;
        attributesVals[8] = magic+"";
        magic = baseMagic + skillsVal[15]*RolePlay.MAGIC_SKILL_BONUS;
        attributesVals[9] = magic+"";
        magic = baseMagic + skillsVal[16]*RolePlay.MAGIC_SKILL_BONUS;
        attributesVals[10] = magic+"";
        magic = baseMagic + skillsVal[17]*RolePlay.MAGIC_SKILL_BONUS;
        attributesVals[11] = magic+"";
        magic = baseMagic + skillsVal[18]*RolePlay.MAGIC_SKILL_BONUS;
        attributesVals[12] = magic+"";

        attributesVals[0] = strength+"";
        attributesVals[1] = agility+"";
        attributesVals[2] = intellect+"";
        attributesVals[3] = totalXP+"";

        paramsVals[0] = (int)totalHealth+"";
        paramsVals[1] = (int)totalStamina+"";
        paramsVals[2] = (int)totalEquipLoad+"";
        paramsVals[3] = (int)(totalAttackSpeed*100)+"";
    }

    private String[] attributesNames = {"Сила","Ловкость","Разум","Опыт","","Познания в видах магии"," магия огня"," магия воды"," магия земли"," магия воздуха"," псионика"," волшебство"," чары"};
    private  String[] attributesVals = {"attributeStrength","attributeAgility","attributeIntellect","totalXP","","","magicFire","magicWater","magicEarth","magicAir","magicMind","magicWizardry","magicSacred",};
    private  String[] paramsNames = {"Здоровье","Запас сил","Нагрузка","Скорость атак"};
    private String[] paramsVals = {"maxHealth","maxStamina","maxEquipLoad","attackSpeed"};
    @Override
    public void render() {
        super.render();
        float wide = 600;
        float high = 516;
        camp_Base.render(1);
        Text.drawString("Навыки",Render2D.getWindowWidth()/2-wide/2+10,Render2D.getWindowHeight()/2-high/2+30-22,Text.CAMBRIA_14, Color.white);
        Render2D.angleDraw("skillBorder","interface/border", Render2D.getWindowWidth()/2-wide/2+12+130,Render2D.getWindowHeight()/2-high/2+50+-1*22,280,4,0);
        for(int i = 0; i <skills.length;i++){
            Render2D.angleDraw("skillBorder"+i,"interface/border", Render2D.getWindowWidth()/2-wide/2+12+130,Render2D.getWindowHeight()/2-high/2+50+i*22,280,4,0);
            Text.drawString(skills[i],Render2D.getWindowWidth()/2-wide/2+10,Render2D.getWindowHeight()/2-high/2+30+i*22,Text.CAMBRIA_14, Color.white);
            Text.drawStringCenter(skillsVal[i]+"",Render2D.getWindowWidth()/2-wide/2+177,Render2D.getWindowHeight()/2-high/2+30+i*22,Text.CAMBRIA_14, Color.white);
            minuses.get(i).draw();
            pluses.get(i).draw();
            Text.drawString(skillsCost[i]+"",Render2D.getWindowWidth()/2-wide/2+225,Render2D.getWindowHeight()/2-high/2+30+i*22,Text.CAMBRIA_14, Color.white);
        }
        Text.drawString("Опыт персонажа: ",Render2D.getWindowWidth()/2-wide/2+10,Render2D.getWindowHeight()/2-high/2+35+20*22,Text.CAMBRIA_14, Color.white);
        Text.drawStringCenter(curEXP+"",Render2D.getWindowWidth()/2-wide/2+210,Render2D.getWindowHeight()/2-high/2+35+20*22,Text.CAMBRIA_14, Color.white);
        Text.drawString("Требуемый опыт: ",Render2D.getWindowWidth()/2-wide/2+10,Render2D.getWindowHeight()/2-high/2+55+20*22,Text.CAMBRIA_14, Color.white);
        Text.drawStringCenter(needEXP+"",Render2D.getWindowWidth()/2-wide/2+210,Render2D.getWindowHeight()/2-high/2+55+20*22,Text.CAMBRIA_14, Color.white);
        cancel.draw();
        OK.draw();
        for(int i = 0; i <attributesNames.length;i++){
            Text.drawString(attributesNames[i],Render2D.getWindowWidth()/2-wide/2+10+280,Render2D.getWindowHeight()/2-high/2+30+i*22,Text.CAMBRIA_14, Color.white);
            Text.drawStringRigth(attributesVals[i],Render2D.getWindowWidth()/2-wide/2+147+290,Render2D.getWindowHeight()/2-high/2+30+i*22,Text.CAMBRIA_14, Color.white);
        }
        for(int i = 0; i <paramsNames.length;i++){
            Text.drawString(paramsNames[i],Render2D.getWindowWidth()/2-wide/2+10+280+160,Render2D.getWindowHeight()/2-high/2+30+(i)*22,Text.CAMBRIA_14, Color.white);
            Text.drawStringRigth(paramsVals[i],Render2D.getWindowWidth()/2-wide/2+147+280+160,Render2D.getWindowHeight()/2-high/2+30+i*22,Text.CAMBRIA_14, Color.white);
        }
        Render2D.simpleDraw("cursor","cursors/normal",InputMain.getCursorX()+16,InputMain.getCursorY()+16,32,32);
    }
}
