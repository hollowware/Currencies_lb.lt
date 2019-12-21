/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.bank;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IX Hero
 */
public class Run {

    static ReadCSV rfw = new ReadCSV();

    public static void main(String[] args) {

        System.out.println("Welcome to \"Lietuvos Bankas\" currency list, please choose option: "
                + "\n1 - for generate currencies by date."
                + "\n2 - for choosing separate currency.");
        Scanner scan = new Scanner(System.in);
        int choice = scan.nextInt();
        switch (choice) {
            case 1:
                currenciesByDate();
                break;
            case 2:
                separateCurrency();
                break;
            default:
                System.out.println("Wrong choice.");
        }

    }

    /**
     * Set previous day's date of announced currencies. On weekends currencies
     * aren't announced, so, if previous day is Sunday - Friday returned. if
     * previous day is Saturday - Friday returned.
     *
     * @return
     * @throws ParseException
     */
    static String lastAnnouncedDate() throws ParseException {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDateYesterday = dateFormat.format(yesterday);
        String day = checkWeekDay(strDateYesterday);
        if (day.equals("SATURDAY")) {
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -2);
            Date newDate = calendar.getTime();
            strDateYesterday = dateFormat.format(newDate);
        } else if (day.equals("SUNDAY")) {
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -3);
            Date newDate = calendar.getTime();
            strDateYesterday = dateFormat.format(newDate);
        }
        return strDateYesterday;
    }

    /**
     * Menu for main currency page. Downloads all currency csv of the chosen
     * date.
     */
    static void currenciesByDate() {
        boolean repeat = true;
        do {
            try {
                Scanner scan = new Scanner(System.in);
                System.out.println("Date: ");
                String strDate = scan.nextLine();
                String weekDay = checkWeekDay(strDate);
                String dbDate = "2014-10-01";
                Date formatedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dbDate);
                Date formatedInputDate = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
                if (weekDay.equals("SATURDAY") || weekDay.equals("SUNDAY")) {
                    System.out.println("Currencies are being announced only on work days, please choose other date.");
                } else if (formatedInputDate.before(formatedDate)) {
                    System.out.println("Currency data base not bellow 2014-10-01");
                } else {
                    repeat = false;
                    String generatedUrl = generateFrontUrl(strDate);
                    System.out.println("\nPlease enter the path with new csv file name you want file to be rewrited: ");
                    System.out.println("Example(C:\\Users\\IX Hero\\Desktop\\sample.csv)");
                    String path = scan.nextLine();
                    File out = new File(path);
                    Download download = new Download();
                    download.newDownload(generatedUrl, out);
                    System.out.println("\nRequested list:\n");
                    for (Currency currency : rfw.readCurrenciesFromWeb(generatedUrl)) {
                        System.out.print(currency + "\n");
                    }
                }
            } catch (IOException | ParseException ex) {
                Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (repeat);
    }

    /**
     * Menu for separate chosen currency. Downloads chosen date range csv and
     * show's ratio difference between beginning and end of the range.
     */
    static void separateCurrency() {
        try (Scanner scan = new Scanner(System.in)) {
            String fixedDate = lastAnnouncedDate();
            String generatedUrl = generateFrontUrl(fixedDate);
            System.out.println("\nChoose currency: ");
            for (Currency currency : rfw.readCurrenciesFromWeb(generatedUrl)) {
                System.out.print(currency.getCurrencyCode() + " ");
            }
            System.out.println();
            String currCode = scan.nextLine().toUpperCase();
            boolean repeat = true;
            do {
                System.out.println("\nChoose date range from: ");
                String dateFrom = scan.nextLine();
                String dbDate = "2014-10-01";
                Date formatedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dbDate);
                Date formatedInputDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateFrom);
                if (formatedInputDate.before(formatedDate)) {
                    System.out.println("\nCurrency data base not bellow 2014-10-01");
                } else {
                    repeat = false;
                    System.out.println("\nChoose date range to: ");
                    String dateTo = scan.nextLine();
                    System.out.println("\nPlease enter the path with new file name you want file to be rewrited: ");
                    System.out.println("Example(C:\\Users\\IX Hero\\Desktop\\sample.csv)");
                    String path = scan.nextLine();
                    File out = new File(path);
                    String separateCurrUrl = separateCurrUrl(currCode, dateFrom, dateTo);
                    Download download = new Download();
                    download.newDownload(separateCurrUrl, out);
                    List<Currency> list = rfw.readCurrenciesFromWeb(separateCurrUrl);
                    System.out.println("\nCurrency ratio in the beginning of period - " + list.get(list.size() - 1).getRatio()
                            + "\nCurrency ratio in the end of period - " + list.get(0).getRatio());
                    System.out.print("\nDifference: ");
                    System.out.format("%.4f%n", (list.get(0).getRatio() - list.get(list.size() - 1).getRatio()));
                }
            } while (repeat);
        } catch (IOException | ParseException ex) {
            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Checking user input date if it's not a weekend, as currencies are not
     * announced on weekends.
     *
     * @param input
     * @return
     * @throws ParseException
     */
    static String checkWeekDay(String input) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(input);
        LocalDateTime ldt = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        DayOfWeek dow = ldt.getDayOfWeek();
        return dow.name();
    }

    /**
     * Generating main page URL for all currencies.
     *
     * @param date
     * @return
     */
    static String generateFrontUrl(String date) {
        return "https://www.lb.lt/lt/currency/daylyexport/?csv=1&class=Eu&type=day&date_day=" + date;
    }

    /**
     * Generating URL for chosen currency.
     *
     * @param currCode
     * @param dateFrom
     * @param dateTo
     * @return
     */
    static String separateCurrUrl(String currCode, String dateFrom, String dateTo) {
        return "https://www.lb.lt/lt/currency/exportlist/?csv=1&currency=" + currCode + "&ff=1&class=Eu&type=day&date_from_day=" + dateFrom + "&date_to_day=" + dateTo;
    }

}
