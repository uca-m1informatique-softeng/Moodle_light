package Model.Payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AddTextRequest {
    @NotBlank
    @Size(min = 3, max = 100)
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public AddTextRequest(){
        this.text = "[default Text]";
    }

    public AddTextRequest(String text){
        this.text = text;
    }

}
