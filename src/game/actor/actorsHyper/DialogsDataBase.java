package game.actor.actorsHyper;

import game.actor.story.Dialog;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DialogsDataBase {
    public static List<Dialog> dialogs = new ArrayList<>();
    static {
        File folder = new File("res/dat/dialogs");
        File[] folderEntries = folder.listFiles();
        for (int i = 0; i < folderEntries.length; i++) {
            String name = folderEntries[i].getName().substring(0, folderEntries[i].getName().length() - 4);
            openDialog(name);
        }
    }
    private static void openDialog(String text) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get("res/dat/dialogs/"+text+".dia"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert lines != null;
        String[] mainData = lines.get(0).split(";");
        String id = (mainData[0]);
        String name = (mainData[1]);
        String speeker = (mainData[2]);

        String reasons = "";
        for(int i = 0; i < 8;i++){
            mainData = lines.get(1+i).split(":");
            reasons += mainData[0]+":"+mainData[1]+";";
        }

        String nexts = "";

        for(int i = 0; i < 8;i++){
            mainData = lines.get(9+i).split(":");
            nexts += mainData[0]+":"+mainData[1]+":"+mainData[2]+";";
        }

        List<String> replics = new ArrayList<>();
        List<String> names = new ArrayList<>();
        for(int i = 17; i < lines.size();i++){
            mainData = lines.get(i).split(":");
            replics.add(mainData[1]);
            names.add(mainData[0]);
        }
        Dialog dialog = new Dialog();
        dialog.setVariable("replics",replics);
        dialog.setVariable("speakers",names);
        dialog.setVariable("nextSteps",nexts);
        dialog.setVariable("reasons",reasons);
        dialog.setVariable("speaker",speeker);
        dialog.setVariable("name",name);
        dialog.setVariable("ID",text);
        dialogs.add(dialog);
    }
}
