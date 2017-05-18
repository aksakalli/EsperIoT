package org.esperiot.rest.dto;


import io.swagger.annotations.ApiModelProperty;


public class StatementDTO {

    private String name;

    @ApiModelProperty(required = true)
    private String statement;

    public StatementDTO() {
    }

    public StatementDTO(String name, String statement) {
        this.name = name;
        this.statement = statement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }
}
