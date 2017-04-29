import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

import java.util.List;

public class main {

    static ClarifaiClient client = new ClarifaiBuilder("M3Lycsll2820DWX6QPEZDmu9UMXmxJ1QM98NLWxy", "LGJnHrF9Lf98TIg9ddNLBKsea0xsdr9lLvso7TdL").buildSync();

    public static void main(String[] args){
        String url = "https://scontent-arn2-1.xx.fbcdn.net/v/t35.0-12/15292871_10157811465920721_368762954_o.jpg?oh=244e6ae0e240fd9d289211dc5d1fc72b&oe=58F8E5FB";

        List<ClarifaiOutput<Concept>> outputs = client.getDefaultModels().generalModel().predict().withInputs(
                ClarifaiInput.forImage(ClarifaiImage.of(url))
        ).executeSync().get();
        for (ClarifaiOutput<Concept> output: outputs) {
           for(Concept concept: output.data()) {
               System.out.println(concept.value() + " for " + concept.name());
           }
        }
    }

}
