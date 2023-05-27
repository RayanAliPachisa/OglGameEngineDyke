package Dyke.util;


import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FileWindowHandler {
    public static File[] openFileExplorerWindow(){

        final JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {

                String back = f.getName().subSequence(f.getName().length() -5 , f.getName().length() - 1).toString();

                System.out.println("back" + back + "\n");
                if( back == ".png" || back == "jpeg" || back == ".jpg"){
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return null;
            }
        });
        fc.setMultiSelectionEnabled(true);

        fc.showOpenDialog(fc);
        return fc.getSelectedFiles();
    }
}
