package sk.epholl.dissim.sem3.input;

import org.junit.Assert;
import sk.epholl.dissim.generator.ExponentialRandom;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by Tomáš on 09.05.2017.
 */
public class InputParser {

    public static final DateTimeFormatter INPUT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d. M. uuuu H:mm:ss");

    public static final void main(String[] args)
            throws IOException {
        File inputFile = new File("sources/Vstupy.csv");
        Assert.assertTrue(inputFile.exists());

        BufferedWriter entryTimesWriter = new BufferedWriter(new FileWriter("sources/entries.txt"));
        BufferedWriter repairAmountWriter = new BufferedWriter(new FileWriter("sources/repair_counts.txt"));
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));

        LocalDateTime oldTime = null;
        LocalDateTime newTime = null;

        final int[] repairsHistogram = new int[10];
        int totalRepairs = 0;

        String line = reader.readLine(); // skip first line
        while ((line = reader.readLine()) != null) {
            String[] split = line.split(Pattern.quote(";"));

            // Date difference
            newTime = LocalDateTime.parse(split[0], INPUT_DATE_TIME_FORMATTER);
            if (oldTime == null || oldTime.getDayOfMonth() != newTime.getDayOfMonth()) {
                oldTime = LocalDateTime.of(newTime.getYear(), newTime.getMonth(), newTime.getDayOfMonth(), 7, 0, 0);
            }
            long secondsBetween = oldTime.until(newTime, ChronoUnit.SECONDS);
            entryTimesWriter.write(secondsBetween + "");
            entryTimesWriter.newLine();

            oldTime = newTime;

            final int repairs = Integer.parseInt(split[1]);
            repairsHistogram[repairs]++;
            totalRepairs++;
        }
        entryTimesWriter.close();

        for (int i = 0; i < repairsHistogram.length; i++) {
            double probability = ((double)repairsHistogram[i]) / totalRepairs;
            repairAmountWriter.write(i + " repairs: P = " + probability);
            repairAmountWriter.newLine();
        }

        repairAmountWriter.close();
    }
}
