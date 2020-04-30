package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;



public class OneDayMarketAction {

    public static class OneTickerOneDay {
        private String m_ticker;
        private String m_series;
        private double m_open;
        private double m_close;
        private double m_high;
        private double m_low;
        private double m_previous;

        public OneTickerOneDay(final String[] oneQuote) {
            setM_ticker(oneQuote[0]);
            setM_series(oneQuote[1]);
            setM_open(Double.parseDouble(oneQuote[2]));
            setM_close(Double.parseDouble(oneQuote[3]));
            setM_high(Double.parseDouble(oneQuote[4]));
            setM_low(Double.parseDouble(oneQuote[5]));
            setM_previous(Double.parseDouble(oneQuote[7]));
            

        }

        public String getM_ticker() {
            return m_ticker;
        }

        public void setM_ticker(String m_ticker) {
            this.m_ticker = m_ticker;
        }

        public String getM_series() {
            return m_series;
        }

        public void setM_series(String m_series) {
            this.m_series = m_series;
        }

        public double getM_open() {
            return m_open;
        }

        public void setM_open(double m_open) {
            this.m_open = m_open;
        }

        public double getM_close() {
            return m_close;
        }

        public void setM_close(double m_close) {
            this.m_close = m_close;
        }

        public double getM_high() {
            return m_high;
        }

        public void setM_high(double m_high) {
            this.m_high = m_high;
        }

        public double getM_low() {
            return m_low;
        }

        public void setM_low(double m_low) {
            this.m_low = m_low;
        }

        public double getM_previous() {
            return m_previous;
        }

        public void setM_previous(double m_previous) {
            this.m_previous = m_previous;
        }

        public double getPctChange() {
            if (this.getM_previous()!= 0) {
                return((this.getM_close()-this.getM_previous())/this.getM_previous());
            }
            return Double.NaN;
        }



    }

    private Map <String,OneTickerOneDay> mapOfTickets = new HashMap<>();

    private String m_fileName;

    public OneDayMarketAction(String csvFile) {
        this.m_fileName = csvFile;
        BufferedReader br = null;
        String line = null;

        String csvSplitBy = ",";

        int lineNumber = 0;

        try {
            br = new BufferedReader(new FileReader(csvFile));

            while ((line = br.readLine()) != null ) {
                lineNumber++;

                if (lineNumber > 1) {
                    String[] oneQuote = line.split(csvSplitBy);
                    OneDayMarketAction.OneTickerOneDay otod = new OneDayMarketAction.OneTickerOneDay(oneQuote);

                    if (otod.getM_series().compareTo("EQ") == 0) {
                        mapOfTickets.put(oneQuote[0], otod);
                    }

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        finally {
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        



    }

    
    public List<OneTickerOneDay> getSortedMovers() {
        List <OneTickerOneDay>  listOfMarketAction = new ArrayList<>(this.mapOfTickets.values());
        Collections.sort(listOfMarketAction, new StockMoveComparator());
        return listOfMarketAction;
    }


    public static class StockMoveComparator implements Comparator<OneTickerOneDay> {
        public int compare (OneTickerOneDay t1, OneTickerOneDay t2) {
            double pctChange1 = t1.getPctChange();
            double pctChange2 = t2.getPctChange();
            if (pctChange1>pctChange2) {
                return 1;
            } else if (pctChange1<pctChange2) {
                return -1;
            }
            return 0;
        }
    }

}