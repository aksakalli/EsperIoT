package org.esperiot.rest;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import org.esperiot.rest.dto.StatementDTO;
import org.esperiot.rest.dto.StatementMapper;
import org.esperiot.service.StatementListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class StatementController {

    private final Logger log = LoggerFactory.getLogger(StatementController.class);

    @Inject
    private EPServiceProvider epService;

    @Inject
    private StatementListener statementListener;

    @RequestMapping(value = "/statements", method = RequestMethod.GET)
    public List<StatementDTO> getAll() {
        EPAdministrator epAdmin = epService.getEPAdministrator();
        String[] names = epAdmin.getStatementNames();
        return Arrays.stream(names)
            .map(i -> StatementMapper.fromStatement(epAdmin.getStatement(i)))
            .collect(Collectors.toList());

    }

    @RequestMapping(value = "/statements/{name}", method = RequestMethod.GET)
    public ResponseEntity<StatementDTO> get(@PathVariable String name) {
        EPStatement stmt = epService.getEPAdministrator().getStatement(name);
//        epService.getEPAdministrator().getConfiguration().getEventType("OrderEvent").getUnderlyingType();

        if (stmt == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(StatementMapper.fromStatement(stmt), HttpStatus.OK);
        }

    }

    @RequestMapping(value = "/statements",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatementDTO> create(@Valid @RequestBody StatementDTO statementDTO) throws URISyntaxException {
        EPStatement statement = epService.getEPAdministrator().createEPL(statementDTO.getStatement(), statementDTO.getName());
        statement.addListener(statementListener);
        StatementDTO dto = StatementMapper.fromStatement(statement);
        return ResponseEntity.created(new URI("/api/statements/" + dto.getName()))
            .body(dto);
    }

    @RequestMapping(value = "/statements/{name}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable String name) {

        EPStatement stmt = epService.getEPAdministrator().getStatement(name);

        if (stmt == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        stmt.destroy();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
