/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.bank;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IX Hero
 */
public class Download extends SetConnection {
    
    /**
     * Downloading files. 
     * @param link
     * @param out 
     */
    public void newDownload(String link, File out) {
        try (BufferedInputStream in = new BufferedInputStream(setConnection(link).getInputStream());
                FileOutputStream fos = new FileOutputStream(out);
                BufferedOutputStream bout = new BufferedOutputStream(fos, 1024)) {
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = in.read(buffer, 0, 1024)) >= 0) {
                bout.write(buffer, 0, read);
            }
            System.out.println("\nFile downloaded!");
        } catch (IOException ex) {
            Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
