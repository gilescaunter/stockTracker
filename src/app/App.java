package app;

import java.util.List;

public class App {
    public static void main(String[] args) {
        try {
            String urlString = "https://www1.nseindia.com/archives/fo/bhav/fo290420.zip";
            String zipFilePath = "/Users/giles/Java/boo.zip";
            String destDirectory = "/Users/giles/java/booUnzipped";

            List <String> unzippedFilesList = UnzipUtility.downloadAndUnzip(urlString, zipFilePath, destDirectory);

            if (unzippedFilesList != null ) {
                String csvFile = unzippedFilesList.get(0);
                OneDayMarketAction odma = new OneDayMarketAction(csvFile);
                List <OneDayMarketAction.OneTickerOneDay> listOfMovers = odma.getSortedMovers();

                for(OneDayMarketAction.OneTickerOneDay otod:listOfMovers) {
                    System.out.println("Tickers=" + otod.getM_series() + " , Moved By: " + otod.getPctChange()*100 + "%");
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}