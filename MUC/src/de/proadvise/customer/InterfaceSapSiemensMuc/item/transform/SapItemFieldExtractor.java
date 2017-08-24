/*
 * Company:         proadvise GmbH   www.proadvise.com
 * Create date:     06.10.2014 15:53:53
 * Copyright(C):    proadvise GmbH 2014
 * 
 * License agreement:
 * 
 */
package de.proadvise.customer.InterfaceSapSiemensMuc.item.transform;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.file.transform.FieldExtractor;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.SapItem;

/**
 * <p>
 * Erstellt aus dem uebergebenen Item ein Objekt, dass folgendem Aufbau
 * entspricht:
 * </p>
 * <ul>
 * <li>Netzplannummer +TAB (P6 UDF Netzplannummer)
 * <li>Netzplanvorgang + TAB (P6 UDF Netzplanvorgang)
 * <li>1 + TAB (Default Wert)
 * <li>Datum Format TTMMJJJJ + TAB (P6 Start)
 * <li>1 + TAB (Default Wert)
 * <li>Datum Format TTMMJJJJ + TAB (P6 Finish)
 * <li>+1 TAB
 * <li>Stellplatz + TAB (P6 Resource ID)
 * <li>+2 TABS (keine Werte fuer: Ist/Prognose-Anfang Termin, Ist/Prognose-Ende
 * Termin)
 * </ul>
 * 
 * @author proadvise GmbH
 * 
 */
public class SapItemFieldExtractor implements FieldExtractor<SapItem> {

    private String dateFormat = "ddMMyyyy";

    /**
     * @see org.springframework.batch.item.file.transform.FieldExtractor#extract(java.lang.Object)
     */
    @Override
    public Object[] extract(SapItem paramItem) {

        DateFormat df = new SimpleDateFormat(dateFormat);

        List<String> value = new ArrayList<String>();
        value.add(paramItem.getNetzplannummer());
        value.add(paramItem.getNetzplanvorgang());
        value.add(paramItem.getEinschraenkungAnfangEinschraenkungsart());
        value.add(df.format(paramItem.getEinschraenkungAnfangTermin()));
        value.add(paramItem.getEinschraenkungEndeEinschraenkungsart());
        value.add(df.format(paramItem.getEinschraenkungEndeTermin()));
        value.add(paramItem.getStellplatz());

        if (null == paramItem.getIstPrognoseAnfangTermin()) {
            value.add(null);
        } else {
            value.add(df.format(paramItem.getIstPrognoseAnfangTermin()));
        }

        if (null == paramItem.getIstPrognoseEndeTermin()) {
            value.add(null);
        } else {
            value.add(df.format(paramItem.getIstPrognoseEndeTermin()));
        }

        return value.toArray();
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

}
