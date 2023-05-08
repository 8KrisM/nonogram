package sk.tuke.gamestudio.game.nonogram.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Maps {

    public List<int[]> getAvailableSizes() throws Exception{
        List<int[]> sizes = new ArrayList<>();
        String folderName = "maps";
        File folder = new File(Maps.class.getClassLoader().getResource(folderName).getFile());

        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                String fileName = file.getName();
                Pattern pattern = Pattern.compile("(\\d+)x(\\d+)");
                Matcher matcher = pattern.matcher(fileName);
                if (matcher.find()) {
                    int size1 = Integer.parseInt(matcher.group(1));
                    int size2 = Integer.parseInt(matcher.group(2));
                    sizes.add(new int[]{size1, size2});
                }
            }
        }
        else throw new Exception("Error loading map files");
        return sizes;
    }
}
