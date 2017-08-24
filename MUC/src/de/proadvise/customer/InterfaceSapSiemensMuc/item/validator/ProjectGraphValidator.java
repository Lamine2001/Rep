package de.proadvise.customer.InterfaceSapSiemensMuc.item.validator;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.Project;
import com.primavera.integration.client.bo.object.ProjectCodeAssignment;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ProjectGraph;
import de.proadvise.tool.p6util.dao.bograph.ProjectCodeAssignmentBucket;
import de.proadvise.tool.p6util.validation.DictionaryValidator;
import de.proadvise.tool.p6util.validation.ProjectCodeTypeValidator;
import de.proadvise.tool.p6util.validation.ProjectCodeValidator;

public class ProjectGraphValidator implements Validator<ProjectGraph> {
    private static final Logger LOG = Logger.getLogger(ProjectGraphValidator.class);
    
    private Map<String, Object> p6Dictionary;
    private String codeTerminplanartKey = "terminplanart";
    private String codeTerminplanstatusKey = "terminplanstatus";
    private String codeTerminplanartAcKey = "terminplanart_ac";
    private String codeTerminplanstatusApKey = "terminplanstatus_ap";

    @Override
    public void validate(ProjectGraph graph) throws ValidationException {
        if (null == graph) {
            // do not validate a null object
            return;
        }
        String terminplanartLabel = (String) p6Dictionary.get(getDictionaryProjectCodeTypeKey(codeTerminplanartKey, "label"));
        String terminplanartValueLabel = (String) p6Dictionary.get(getDictionaryProjectCodeKey(codeTerminplanartAcKey, "label"));
        ProjectCodeAssignment terminplanartValue = graph.getProjectCodeAssignmentByKey(terminplanartLabel);
        validateCodeAssignment(graph, graph.getStartObject(), terminplanartLabel, terminplanartValueLabel,
                terminplanartValue);
        
        String terminplanstatusLabel = (String) p6Dictionary.get(getDictionaryProjectCodeTypeKey(codeTerminplanstatusKey, "label"));
        String terminplanstatusValueLabel = (String) p6Dictionary.get(getDictionaryProjectCodeKey(codeTerminplanstatusApKey, "label"));
        ProjectCodeAssignment terminplanstatusValue = graph.getProjectCodeAssignmentByKey(terminplanstatusLabel);
        validateCodeAssignment(graph, graph.getStartObject(), terminplanstatusLabel, terminplanstatusValueLabel,
                terminplanstatusValue);
        
    }


    protected void validateCodeAssignment(ProjectCodeAssignmentBucket graph, Project project, String codeTypeLabel,
            String expectedCodeValueLabel, ProjectCodeAssignment actualCodeAssignment) {
        if (null == actualCodeAssignment) {
            debugAndThrowValidationException(
                    String.format("The project [%s] has no value for code type [%s] assigned.", extractId(project), codeTypeLabel));
        }
        String codeValue = null;
        try {
            codeValue = actualCodeAssignment.getProjectCodeValue();
        } catch (BusinessObjectException e) {
            debugAndThrowValidationException(
                    String.format("The project code value for code type [%s] could not be read.", codeTypeLabel));
        }
        if (!expectedCodeValueLabel.equals(codeValue)) {
            debugAndThrowValidationException(
                    String.format("The project code value of project [%s] for code type [%s] is not [%s] but [%s]",
                            extractId(project), codeTypeLabel, expectedCodeValueLabel, codeValue));
        }
    }
    
    
    private String extractId(Project project) {
        if (null == project) {
            return "[null]";
        }
        try {
            return String.format("%s", project.getId());
        } catch (BusinessObjectException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("use object ids instead of ids, because: " + e.getMessage());
            }
            
            try {
                return String.format("Project [%s]", project.getObjectId());
            } catch (BusinessObjectException ex) {
                return "";
            }
        }
    }
    
    private void debugAndThrowValidationException(String message) throws ValidationException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
        
        throw new ValidationException(message);
    }
    
    protected String getDictionaryProjectCodeTypeKey(String codeTypeKey, String property) {
        return StringUtils.join(new String[] { DictionaryValidator.DICTIONARY_KEY_PREFIX, 
                ProjectCodeTypeValidator.CONFIGURATION_KEY_PROJECTCODETYPE_PREFIX,
                codeTypeKey, property},
                DictionaryValidator.CONFIG_KEY_SEPARATOR);
    }
    
    protected String getDictionaryProjectCodeKey(String codeKey, String property) {
        return StringUtils.join(new String[] { DictionaryValidator.DICTIONARY_KEY_PREFIX, 
                ProjectCodeValidator.CONFIGURATION_KEY_PROJECTCODE_PREFIX,
                codeKey, property},
                DictionaryValidator.CONFIG_KEY_SEPARATOR);
    }


    public Map<String, Object> getP6Dictionary() {
        return p6Dictionary;
    }


    public void setP6Dictionary(Map<String, Object> p6Dictionary) {
        this.p6Dictionary = p6Dictionary;
    }
}
