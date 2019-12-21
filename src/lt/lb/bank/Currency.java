/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.bank;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author IX Hero
 */
public class Currency {
    
    private String currencyName;
    private String currencyCode;
    private double ratio;
    private Date currencyDate;

    public Currency(String currencyName, String currencyCode, double ratio, Date currencyDate) {
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
        this.ratio = ratio;
        this.currencyDate = currencyDate;
    }
    
    public String setStringDate (Date date) {
        String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return stringDate;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public Date getCurrencyDate() {
        return currencyDate;
    }

    public void setCurrencyDate(Date currencyDate) {
        this.currencyDate = currencyDate;
    }

    @Override
    public String toString() {
        return "Currency{" + "currencyName=" + currencyName + ", currencyCode=" + currencyCode + ", ratio=" + ratio + ", currencyDate=" + setStringDate(currencyDate) + '}';
    }

    
}
