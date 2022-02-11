package Model.Payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateTextQuestionRequest extends CreateQuestionRequest{

    @NotBlank
    @Size(min = 3, max = 20)
    private String reponse_;

    public String getReponse(){
        return this.reponse_;
    }


}
