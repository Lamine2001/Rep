package de.proadvise.customer.InterfaceSapSiemensMuc.item.mapper;

import java.text.DateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import com.primavera.common.value.BeginDate;
import com.primavera.common.value.EndDate;
import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.Activity;

import de.proadvise.common.mapper.Mapping;
import de.proadvise.customer.InterfaceSapSiemensMuc.domain.SapItem;

/**
 * <p>
 * Das {@code SapItem2ActivityMapping} mappt die Felder des {@link SapItem} auf
 * die dazugehoerige {@code Activity}
 * </p>
 */
public class SapItem2ActivityMapping implements Mapping<SapItem, Activity> {
    private static final Logger LOG = Logger.getLogger(SapItem2ActivityMapping.class);

    @Override
    public void mapFields(final SapItem inputObject, final Activity outputObject) {
        mapStartDate(inputObject, outputObject);
        mapFinishDate(inputObject, outputObject);
    }

    protected void mapStartDate(final SapItem inputObject, final Activity outputObject) {

        if (null == inputObject.getEinschraenkungAnfangTermin()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("StartDate won't be mapped, because it's not available in input object "
                        + inputObject);
            }
            return;
        }

        try {
            Date newDate = mergeDate(outputObject.getStartDate(),
                    inputObject.getEinschraenkungAnfangTermin());
            outputObject.setActualStartDate(new BeginDate(newDate));
            
            if (LOG.isDebugEnabled()) {
                try {
                    LOG.debug(String.format("Map ActualStartDate of Activity [%s] with status [%s] from [%s] to [%s].",
                            outputObject.getId(), outputObject.getStatus(), 
                            outputObject.getStartDate(), outputObject.getActualStartDate()));
                } catch (BusinessObjectException e) {}
            }
        } catch (BusinessObjectException e) {
            LOG.error("StartDate of Activity could not be mapped to SapItem because of BusinessObjectException: "
                    + e.getMessage());
        }
    }

    protected void mapFinishDate(final SapItem inputObject, final Activity outputObject) {

        if (null == inputObject.getEinschraenkungEndeTermin()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("FinishDate won't be mapped, because it's not available in input object "
                        + inputObject);
            }
            return;
        }

        try {
            Date newDate = mergeDate(outputObject.getFinishDate(),
                    inputObject.getEinschraenkungEndeTermin());
            outputObject.setActualFinishDate(new EndDate(newDate));
            
            if (LOG.isDebugEnabled()) {
                try {
                    LOG.debug(String.format("Map ActualFinishDate of Activity [%s] with status [%s] from [%s] to [%s].",
                            outputObject.getId(), outputObject.getStatus(), 
                            outputObject.getFinishDate(), outputObject.getActualFinishDate()));
                } catch (BusinessObjectException e) {}
            }
        } catch (BusinessObjectException e) {
            LOG.error("FinishDate of Activity coould not be mapped to SapItem because of BusinessObjectException: "
                    + e.getMessage());
        }
    }

    protected Date mergeDate(Date activityDate, Date sapItemDate) {

        if (LOG.isTraceEnabled()) {
            LOG.trace(String.format("Merge activity date [%s] and sap item date [%s]",
                    DateFormat.getDateTimeInstance().format(activityDate), sapItemDate));
        }
        DateTime currentDate = new DateTime(activityDate);
        MutableDateTime newDate = new MutableDateTime(sapItemDate);
        newDate.setHourOfDay(currentDate.getHourOfDay());
        newDate.setMinuteOfHour(currentDate.getMinuteOfHour());
        
        if (LOG.isTraceEnabled()) {
            LOG.trace(String.format("Merged Date is [%s]", newDate));
        }
        return newDate.toDate();
    }

}
