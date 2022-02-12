package Model.Payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AddTextRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String Text;

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }
}
