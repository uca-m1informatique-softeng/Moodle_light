package Model.Payload.request;

import Model.Documents.EQuestion;

import javax.validation.constraints.NotBlank;

public class AddRessourceRequest {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public AddRessourceRequest(){
        this.name = "defaultuser";
    }

    public AddRessourceRequest(String name){
        this.name = name;
    }
}
