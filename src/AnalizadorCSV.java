import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorCSV {

    public static void main(String[] args) {
        try {
            String url = "https://people.sc.fsu.edu/~jburkardt/data/csv/csv.html";
            Document doc = Jsoup.connect(url).get();

            Elements links = doc.select("a[href$=.csv]");
            List<Thread> threads = new ArrayList<>();

            for (Element link : links) {
                String csvUrl = link.attr("abs:href");
                String csvNOmbre = link.text();
                Thread thread = new Thread(new CSVThread(csvNOmbre, csvUrl));
                threads.add(thread);
                thread.start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class CSVThread implements Runnable {
        private String csvNombre;
        private String csvUrl;

        public CSVThread(String csvNombre, String csvUrl) {
            this.csvNombre = csvNombre;
            this.csvUrl = csvUrl;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(csvUrl);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                int lineCount = 0;
                String line;

                while ((line = reader.readLine()) != null) {
                    lineCount++;
                }

                System.out.println("File: " + csvNombre + ", Lines: " + lineCount);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
