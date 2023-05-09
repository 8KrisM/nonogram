package sk.tuke.gamestudio.game.nonogram.core;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Maps {

    public static List<int[]> getAvailableSizes() throws Exception{
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

    public static void save(int rows, int columns, Tile[][] tiles) throws Exception {
        String fileName = "src/main/resources/maps/"+rows+'x'+columns;
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(file.getAbsoluteFile(), true);
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                if (tiles[i-1][j-1].getState() == Tile.State.FILLED) {
                    String iStr = (i < 9) ? "0" + i : String.valueOf(i);
                    String jStr = (j < 9) ? "0" + j : String.valueOf(j);
                    String outputStr = iStr + jStr + " ";
                    writer.write(outputStr);
                }
            }
        }
        writer.write("\n");
        writer.close();
    }
}
