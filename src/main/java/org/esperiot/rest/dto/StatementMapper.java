package org.esperiot.rest.dto;

import com.espertech.esper.client.EPStatement;

public class StatementMapper {

    public static StatementDTO fromStatement(EPStatement statement) {
        StatementDTO dto = new StatementDTO();
//        dto.setId(((EPStatementImpl) statement).getStatementId());
        dto.setName(statement.getName());
        dto.setStatement(statement.getText());
        return dto;
    }


}
