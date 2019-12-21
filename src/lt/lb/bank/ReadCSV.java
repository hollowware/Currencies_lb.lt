/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.bank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IX Hero
 */
public class ReadCSV extends SetConnection {
    
    /**
     * Reads CSV directly from URL.
     * Reading lines, splitting into values and putting into array.
     * Creating a list of Currency objects.
     * @param webUrl
     * @return
     * @throws MalformedURLException
     * @throws IOException 
     */
    public List<Currency> readCurrenciesFromWeb(String webUrl) throws MalformedURLException, IOException {
        List<Currency> currencies = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(setConnection(webUrl).getInputStream()));
        reader.readLine(); // skip header
        String line = reader.readLine();
        while (line != null) {
            String[] valueArr = line.split(";");
            String[] valueArrCleaned = new String[valueArr.length];
            for (int i = 0; i < valueArr.length; i++) {
                String modified = valueArr[i].replaceAll("\"", "").replaceAll(",", ".");
                valueArrCleaned[i] = modified;
            }
            Currency currency = createCurrency(valueArrCleaned);
            currencies.add(currency);
            line = reader.readLine();
        }
        return currencies;
    }
    
    /**
     * Creating Currency objects.
     * @param values
     * @return 
     */
    public Currency createCurrency(String[] values) {
        String currencyName = values[0];
        String currencyCode = values[1];
        double ratio = Double.parseDouble(values[2]);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(values[3]);
        } catch (ParseException ex) {
            Logger.getLogger(ReadCSV.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Currency(currencyName, currencyCode, ratio, date);
    }

}
