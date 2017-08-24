package de.proadvise.customer.InterfaceSapSiemensMuc.item.validator;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.Activity;
import com.primavera.integration.client.bo.object.ActivityCodeAssignment;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ActivityGraph;
import de.proadvise.tool.p6util.validation.ActivityCodeTypeValidator;
import de.proadvise.tool.p6util.validation.ActivityCodeValidator;
import de.proadvise.tool.p6util.validation.DictionaryValidator;
import de.proadvise.tool.p6util.validation.UdfTypeValidator;

public class SapActivityGraphValidator implements Validator<ActivityGraph> {
    
    private static final Logger LOG = Logger.getLogger(SapActivityGraphValidator.class);
    
    private Map<String, Object> p6Dictionary;
    private String udfNetzplannummerKey = "netzplannummer";
    private String udfNetzplanvorgangKey = "netzplanvorgang";
    private String codeSapRueckmeldungKey = "sap_rueckmeldung";
    private String codeSapRueckmeldungYKey = "sap_rueckmeldung_y";
    
    @Override
    public void validate(ActivityGraph graph) throws ValidationException {
        String netzplannummerLabel = (String) p6Dictionary.get(getDictionaryUdfTypeKey(udfNetzplannummerKey, "label"));
        if (null == graph.getUdfValueByKey(netzplannummerLabel)) {
            debugAndThrowValidationException(
                    String.format("The activity [%s] has no value for udf [%s] assigned.", extractId(graph), netzplannummerLabel));
        }
        String netzplanvorgangLabel = (String) p6Dictionary.get(getDictionaryUdfTypeKey(udfNetzplanvorgangKey, "label"));
        if (null == graph.getUdfValueByKey(netzplanvorgangLabel)) {
            debugAndThrowValidationException(
                    String.format("The activity [%s] has no value for udf [%s] assigned.", extractId(graph), netzplanvorgangLabel));
        }
        
        String sapRueckmeldungTypeLabel = (String) p6Dictionary.get(getDictionaryActivityCodeTypeKey(codeSapRueckmeldungKey, "label"));
        String sapRueckmeldungValueLabel = (String) p6Dictionary.get(getDictionaryActivityCodeKey(codeSapRueckmeldungYKey, "label"));
        
        ActivityCodeAssignment sapRueckmeldungValue = graph.getActivityCodeAssignmentByKey(sapRueckmeldungTypeLabel);
        if (null == sapRueckmeldungValue) {
            debugAndThrowValidationException(
                    String.format("The activity [%s] has no value for activity code type [%s] assigned.", extractId(graph), sapRueckmeldungTypeLabel));
        }
        
        String codeValue = null;
        try {
            codeValue = sapRueckmeldungValue.getActivityCodeValue();
        } catch (BusinessObjectException e) {
            debugAndThrowValidationException(
                    String.format("The activity code value for code type [%s] could not be read.", sapRueckmeldungTypeLabel));
        }
        if (!sapRueckmeldungValueLabel.equals(codeValue)) {
            debugAndThrowValidationException(
                    String.format("The activity code value of activity [%s] for code type [%s] is not [%s] but [%s]", 
                            extractId(graph), sapRueckmeldungTypeLabel, sapRueckmeldungValueLabel, codeValue));
        }
    }

    private String extractId(ActivityGraph graph) {
        Activity activity = graph.getStartObject();
        
        if (null == activity) {
            return "[null]";
        }
        try {
            return String.format("%s : %s", activity.getProjectId(), activity.getId());
        } catch (BusinessObjectException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("use object ids instead of ids, because: " + e.getMessage());
            }
            
            try {
                return String.format("Project [%s] : Activity [%s]", activity.getProjectObjectId(), activity.getObjectId());
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

    protected String getDictionaryUdfTypeKey(String udfValueKey, String property) {
        return StringUtils.join(new String[] { DictionaryValidator.DICTIONARY_KEY_PREFIX,
                UdfTypeValidator.CONFIGURATION_KEY_UDFTYPE_PREFIX, udfValueKey, property },
                DictionaryValidator.CONFIG_KEY_SEPARATOR);
    }
    
    protected String getDictionaryActivityCodeTypeKey(String codeTypeKey, String property) {
        return StringUtils.join(new String[] { DictionaryValidator.DICTIONARY_KEY_PREFIX, 
                ActivityCodeTypeValidator.CONFIGURATION_KEY_ACTIVITYCODETYPE_PREFIX,
                codeTypeKey, property},
                DictionaryValidator.CONFIG_KEY_SEPARATOR);
    }
    
    protected String getDictionaryActivityCodeKey(String codeKey, String property) {
        return StringUtils.join(new String[] { DictionaryValidator.DICTIONARY_KEY_PREFIX, 
                ActivityCodeValidator.CONFIGURATION_KEY_ACTIVITYCODETYPE_PREFIX,
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
