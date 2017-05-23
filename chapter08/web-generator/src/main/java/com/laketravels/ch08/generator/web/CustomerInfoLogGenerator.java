package com.laketravels.ch08.generator.web;

import java.io.*;

/**
 * Created by pankajmisra on 3/10/17.
 */
public class CustomerInfoLogGenerator {

    private static final int OFFSET = 100;

    private static final String USAGE_MESSAGE = "Usage: java -jar laketravels-client-generator <output-directory> <number-of-Records> <type:(default)location|contact>";

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println(USAGE_MESSAGE);
            return;
        }
        int numRecords = Integer.parseInt(args[1]);
        DataType type = DataType.LOCATION;
        if (args.length == 3) {
            String typeArg = args[2].trim();
            if (typeArg.equals("location") || (typeArg.equals("contact"))) {
                if (typeArg.equals("contact")) {
                    type = DataType.CONTACT;
                }
            } else {
                System.out.println(USAGE_MESSAGE);
                return;
            }
        }

        DataGenerator generator = null;
        switch (type) {
            case CONTACT:
                generator = new ContactGenerator();
                break;
            case LOCATION:
                generator = new LocationGenerator();
                break;

        }




        FileOutputStream out = new FileOutputStream(new File(args[0]));
        PrintWriter writer= new PrintWriter(out);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        for(int ctr=1;ctr<=numRecords;ctr++) {
            String log  = generator.generateData(ctr,OFFSET);
            bufferedWriter.write(log);
            bufferedWriter.newLine();
            if (ctr % 500 == 0 || ctr == numRecords) {
                bufferedWriter.flush();
            }
        }
        bufferedWriter.close();
        writer.close();
        out.close();

    }
}
