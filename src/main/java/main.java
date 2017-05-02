import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class main {

    static ClarifaiClient client = new ClarifaiBuilder("M3Lycsll2820DWX6QPEZDmu9UMXmxJ1QM98NLWxy", "LGJnHrF9Lf98TIg9ddNLBKsea0xsdr9lLvso7TdL").buildSync();

    public static String csvSplitter = ";";
    public static int rowCount = 0;
    public static int badRowCount = 0;
    public static Set<String> uniqueUrls = new TreeSet<String>();
    static List<String> rowsToInsert = new ArrayList<String>();
    static long start = System.currentTimeMillis();
    static String outputVersion = "1";


    public static void main(String[] args) {
        fileReader();
    }

    private static void fileReader() {
        BufferedReader br = null;
        FileReader fr = null;

        try {

            String path = "data/to_parse.txt";
            fr = new FileReader(path);
            br = new BufferedReader(fr);

            String sCurrentLine;

            br = new BufferedReader(new FileReader(path));
            while ((sCurrentLine = br.readLine()) != null) {
                rowsToInsert.add(getCsvLine(sCurrentLine));
            }
            System.out.println("good rows: " + rowCount);
            System.out.println("bad rows: " + badRowCount);
            System.out.println("unique urls: " + uniqueUrls.size());
            writeToFile();


        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
    }

    private static void writeToFile() {

        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter("data/outputs/output" + outputVersion + ".txt");
            bw = new BufferedWriter(fw);
            bw.write("media_id;concept_name;concept_value;date(GMT);image_url;short_url;\n");
            for (String row : rowsToInsert) {
                bw.write(row);
            }

            System.out.println("Done");
            System.out.println("Time taken: " + (System.currentTimeMillis() - start) + "ms");
        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
    }

    private static String getCsvLine(String inputLine) {
        inputLine = inputLine.replace("\"", "");
        String[] csvArray = inputLine.split(",");
        try {
            if (csvArray.length > 5) {

//                int startIdx = inputLine.lastIndexOf("https://scontent-sea1-1.cdninstagram.com");
                int startIdx = inputLine.lastIndexOf("https://scontent.cdninstagram.com");
                int endIdx = inputLine.lastIndexOf(".jpg");
                String url = inputLine.substring(startIdx, endIdx + 4);
                if (url.startsWith("http")) {
                    uniqueUrls.add(url);
                    try {
                        Long mediaId = Long.parseLong(csvArray[0]);
                        rowCount++;
                        String csvToReturn = "";
//                        for (Concept concept : getClarifaiArray(url)) {
//                            if (concept != null) {
//                                csvToReturn = csvToReturn.concat(csvArray[0].concat(csvSplitter)
//                                        .concat(concept.name() == null ? "" : concept.name()).concat(csvSplitter)
//                                        .concat(String.valueOf(concept.value())).concat(csvSplitter)
//                                        .concat(csvArray[3]).concat(csvSplitter)
//                                        .concat(url).concat(csvSplitter)
//                                        .concat(csvArray[1]).concat(csvSplitter)
//                                        .concat("\n"));
//                            }
//                        }
                        return csvToReturn;

                    } catch (Exception e) {
                        String jpgName = url.substring(url.lastIndexOf("/") + 1);
                        rowCount++;
                        String csvToReturn = "";
//                        for (Concept concept : getClarifaiArray(url)) {
//                            if (concept != null) {
//                                csvToReturn = csvToReturn.concat(jpgName.concat(csvSplitter)
//                                        .concat(concept.name() == null ? "" : concept.name()).concat(csvSplitter)
//                                        .concat(String.valueOf(concept.value())).concat(csvSplitter)
//                                        .concat(csvSplitter)
//                                        .concat(url).concat(csvSplitter)
//                                        .concat("").concat(csvSplitter)
//                                        .concat("\n"));
//                            }
//
//                        }
                        return csvToReturn;

                    }

                } else {

                    badRowCount++;
                }

            } else {
                badRowCount++;
            }
        } catch (Exception e) {
            badRowCount++;
        }
        return "";
    }

    private static List<Concept> getClarifaiArray(String url) {
        List<ClarifaiOutput<Concept>> outputs = client.getDefaultModels().generalModel().predict().withInputs(
                ClarifaiInput.forImage(ClarifaiImage.of(url))
        ).executeSync().get();
        List<Concept> outputsToReturn = new ArrayList<Concept>();
        for (ClarifaiOutput<Concept> output : outputs) {
            outputsToReturn.addAll(output.data());
        }
        return outputsToReturn;
    }
// /
//    String url = "https://scontent.cdninstagram.com/t51.2885-15/sh0.08/e35/18011866_134613993744923_3197176919317544960_n.jpg";
//
//    List<ClarifaiOutput<Concept>> outputs = client.getDefaultModels().generalModel().predict().withInputs(
//            ClarifaiInput.forImage(ClarifaiImage.of(url))
//    ).executeSync().get();
//        for (ClarifaiOutput<Concept> output : outputs) {
//        for (Concept concept : output.data()) {
//            System.out.println("media_id," + concept.name() + "," + concept.value()
//                    +",date,comments_count,likes_count,profile_url,instagram_url,image_url");
//        }
//    }

}
