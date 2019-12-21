/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.bank;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IX Hero
 */
public class SetConnection {
    
    /**
     * Connecting to URL.
     * @param webUrl
     * @return 
     */
    HttpURLConnection setConnection(String webUrl) {
        HttpURLConnection urlcon = null;
        try {
            URL url = new URL(webUrl);
            urlcon = (HttpURLConnection) url.openConnection();
            urlcon.addRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
            System.setProperty("http.agent", "Chrome");
        } catch (IOException ex) {
            Logger.getLogger(SetConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return urlcon;
    }
    
}
