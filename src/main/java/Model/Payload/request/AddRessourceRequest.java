package Model.Payload.request;

import Model.Documents.EQuestion;

import javax.validation.constraints.NotBlank;

public class AddRessourceRequest {
    @NotBlank
    private String name;

    @NotBlank
    private Long moduleid;

    public String getName() {
        return name;
    }

    public Long getModuleid() {
        return moduleid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setModuleid(Long moduleid) {
        this.moduleid = moduleid;
    }
}
