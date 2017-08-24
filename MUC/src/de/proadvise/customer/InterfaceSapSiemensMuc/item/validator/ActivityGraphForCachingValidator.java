package de.proadvise.customer.InterfaceSapSiemensMuc.item.validator;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.Activity;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ActivityGraph;

public class ActivityGraphForCachingValidator implements Validator<ActivityGraph> {

    private String netzplannummerLabel = "UDF_ MFMCH_ sap-netzplannummer";
    private String netzplanvorgangLabel = "UDF_ MFMCH_ sap-vorgangsnummer";

    @Override
    public void validate(ActivityGraph activityGraph) throws ValidationException {
        validateRequiredFieldOfActivity(activityGraph.getStartObject());
        validateExistingUdf(activityGraph, getNetzplannummerLabel());
        validateExistingUdf(activityGraph, getNetzplanvorgangLabel());
    }
    
    protected void validateRequiredFieldOfActivity(Activity activity) {
        try {
            if (null == activity.getObjectId()) {
                throw new ValidationException("ObjectId of activity must not be null. So, filter it.");
            }
            if (null == activity.getProjectObjectId()) {
                throw new ValidationException("ProjectObjectId of activity must not be null. So, filter it.");
            }
        } catch (BusinessObjectException e) {
            throw new ValidationException("Retrieving ObjectId or ProjectObjectId of activity should not throw Exception. So, filter it.");
        }
    }

    protected void validateExistingUdf(ActivityGraph activityGraph, String udfTitle) {
        try {
            if (null == activityGraph.getUdfValueByKey(udfTitle) || "".equals(activityGraph.getUdfValueByKey(udfTitle).getText())) {
                throw new ValidationException(String.format("Activity misses UDF %s. So, filter it.", udfTitle));
            }
        } catch (BusinessObjectException e) {
            throw new ValidationException(String.format("Retrieving udf for title %s throws BusinessObjectException. So, filter it", udfTitle));
        }
    }

    public String getNetzplannummerLabel() {
        return netzplannummerLabel;
    }

    public void setNetzplannummerLabel(String netzplannummerLabel) {
        this.netzplannummerLabel = netzplannummerLabel;
    }

    public String getNetzplanvorgangLabel() {
        return netzplanvorgangLabel;
    }

    public void setNetzplanvorgangLabel(String netzplanvorgangLabel) {
        this.netzplanvorgangLabel = netzplanvorgangLabel;
    }

}
