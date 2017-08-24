/*
 * Company:         proadvise GmbH   www.proadvise.com
 * Create date:     02.10.2014 10:30:15
 * Copyright(C):    proadvise GmbH 2014
 * 
 * License agreement:
 * 
 */
package de.proadvise.customer.InterfaceSapSiemensMuc.item;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.Assert;

import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.enm.ResourceType;
import com.primavera.integration.client.bo.object.Activity;
import com.primavera.integration.client.bo.object.ResourceAssignment;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ActivityGraph;
import de.proadvise.customer.InterfaceSapSiemensMuc.domain.SapItem;
import de.proadvise.tool.p6util.dao.bograph.BoGraph;
import de.proadvise.tool.p6util.dao.bograph.UdfValueBucket;
import de.proadvise.tool.p6util.validation.DictionaryValidator;
import de.proadvise.tool.p6util.validation.UdfTypeValidator;

/**
 * Prozessor zum Mappen von ActivityGraph-Elementen (ist neben dem Hauptobjekt
 * Activity ein UDFValueBucket) auf SAP-P6 Beans.
 * 
 * @author proadvise GmbH
 * 
 */
public class ActivityGraph2SapBeanMapperProcessor implements
        ItemProcessor<BoGraph<Activity>, SapItem> {

    private static final Logger LOG = Logger.getLogger(ActivityGraph2SapBeanMapperProcessor.class);

    private Map<String, Object> p6Dictionary;

    private String stellplatzDefaultValue = "xxxxxxxx";
    
    private String anfangEinschraenkungsartDefaultValue = "1";
    private String endeEinschraenkungsartDefaultValue = "1";

    private String udfNetzplannummerKey = "netzplannummer";
    private String udfNetzplanvorgangKey = "netzplanvorgang";

    @Override
    public SapItem process(BoGraph<Activity> boGraph) throws Exception {

        SapItem item = new SapItem();

        Activity activity = boGraph.getStartObject();
        item.setEinschraenkungAnfangTermin(activity.getStartDate());
        item.setEinschraenkungEndeTermin(activity.getFinishDate());

        UdfValueBucket udfBucket = (UdfValueBucket) boGraph;
        item.setNetzplannummer(udfBucket.getUdfValueByKey(
                (String) p6Dictionary.get(getDictionaryUdfTypeKey(udfNetzplannummerKey, "label")))
                .getText());
        item.setNetzplanvorgang(udfBucket.getUdfValueByKey(
                (String) p6Dictionary.get(getDictionaryUdfTypeKey(udfNetzplanvorgangKey, "label")))
                .getText());
        
        item.setEinschraenkungAnfangEinschraenkungsart(anfangEinschraenkungsartDefaultValue);
        item.setEinschraenkungEndeEinschraenkungsart(endeEinschraenkungsartDefaultValue);
        
        String resourceId = null;
        ResourceAssignment firstNonLaborResourceAssignment = getFirstNonLaborResourceAssignment(boGraph);
        if(null != firstNonLaborResourceAssignment){
            resourceId = firstNonLaborResourceAssignment.getResourceId(); 
        }else{
            resourceId = activity.getPrimaryResourceId();
        }
        
        if(null == resourceId){
            resourceId = this.stellplatzDefaultValue;
        }
        
        String stellplatz = transfromResourceIdToStellplatz(resourceId);
        
        item.setStellplatz(stellplatz);
        item.setIstPrognoseAnfangTermin(null);
        item.setIstPrognoseEndeTermin(null);

        LOG.debug("Processor creates item: " + item.toString());

        return item;
    }
    
    /**
     * Schneidet die ResourceId auf die maximale laenge des Stellplatzes zu
     * 
     * @param resourceId
     * @return the stellplatz as {@link String}
     */
    protected String transfromResourceIdToStellplatz(String resourceId) {
        int length = resourceId.length();
        
        if(length <=8){ // if shorter then 8, just take it
            return resourceId;
        }else if(length > 8 && length < 12){ // if its longer as 8 but shorter then 12, just take the last 8
            return resourceId.substring(length-8, length);
        }else{ // so its longer then 12 chars, cut first 5 chars and take the next 8 chars
            return resourceId.substring(4, 12);
        }
    }

    /**
     * Gibt das erste {@link ResourceAssignment} zurueck welches in dem {@link ActivityGraph} vorhanden ist.
     * Wenn keine Non-Labor vorhanden sind wird {@link null} zurueck gegeben. 
     * 
     * @param boGraph
     * @return the first @link{@link ResourceAssignment} with {@link ResourceType.NonLabor} 
     * @throws BusinessObjectException
     */
    protected ResourceAssignment getFirstNonLaborResourceAssignment(BoGraph<Activity> boGraph)
            throws BusinessObjectException {
        Assert.isTrue(boGraph instanceof ActivityGraph, "The BoGraph MUST be a de.proadvise.customer.InterfaceSapSiemensMuc.domain.ActivityGraph");
        ActivityGraph aGraph = (ActivityGraph) boGraph;
        List<ResourceAssignment> resourceAssignments = (List<ResourceAssignment>) aGraph.getResourceAssignments();
        for (ResourceAssignment resourceAssignment : resourceAssignments) {
            if(resourceAssignment.getResourceType().equals(ResourceType.NONLABOR)){
                return resourceAssignment;
            }
        }
        return null;
    }

    protected String getDictionaryUdfTypeKey(String udfValueKey, String property) {
        return StringUtils.join(new String[] { DictionaryValidator.DICTIONARY_KEY_PREFIX,
                UdfTypeValidator.CONFIGURATION_KEY_UDFTYPE_PREFIX, udfValueKey, property },
                DictionaryValidator.CONFIG_KEY_SEPARATOR);
    }

    public Map<String, Object> getP6Dictionary() {
        return p6Dictionary;
    }

    public void setP6Dictionary(Map<String, Object> p6Dictionary) {
        this.p6Dictionary = p6Dictionary;
    }

    public String getStellplatzDefaultValue() {
        return stellplatzDefaultValue;
    }

    public void setStellplatzDefaultValue(String stellplatzDefaultValue) {
        this.stellplatzDefaultValue = stellplatzDefaultValue;
    }

    public String getAnfangEinschraenkungsartDefaultValue() {
        return anfangEinschraenkungsartDefaultValue;
    }

    public void setAnfangEinschraenkungsartDefaultValue(String anfangEinschraenkungsartDefaultValue) {
        this.anfangEinschraenkungsartDefaultValue = anfangEinschraenkungsartDefaultValue;
    }

    public String getEndeEinschraenkungsartDefaultValue() {
        return endeEinschraenkungsartDefaultValue;
    }

    public void setEndeEinschraenkungsartDefaultValue(String endeEinschraenkungsartDefaultValue) {
        this.endeEinschraenkungsartDefaultValue = endeEinschraenkungsartDefaultValue;
    }

}
