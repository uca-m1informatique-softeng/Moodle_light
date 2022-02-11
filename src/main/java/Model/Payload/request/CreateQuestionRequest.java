package Model.Payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class CreateQuestionRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String enonce_;

    @NotBlank
    @Size(min = 3, max = 20)
    private String questionType_;

    public String getQuestionType(){
        return  this.questionType_;
    }

    public  String getEnonce(){
        return this.enonce_;
    }


}
