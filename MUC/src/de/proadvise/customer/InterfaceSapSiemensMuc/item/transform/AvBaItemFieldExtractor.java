/*
 * Company:         proadvise GmbH   www.proadvise.com
 * Create date:     06.10.2014 15:53:53
 * Copyright(C):    proadvise GmbH 2014
 * 
 * License agreement:
 * 
 */
package de.proadvise.customer.InterfaceSapSiemensMuc.item.transform;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.file.transform.FieldExtractor;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.AvBaItem;

/**
 * <p>
 * Erstellt aus dem uebergebenen Item ein Objekt, dass folgendem Aufbau
 * entspricht:
 * </p>
 * <ul>
 * <li>Project ID +TAB
 * <li>Activity ID + TAB
 * <li>Activity Name + TAB
 * <li>Resource ID + TAB
 * <li>Budgeted Units
 * </ul>
 * 
 * @author proadvise GmbH
 * 
 */
public class AvBaItemFieldExtractor implements FieldExtractor<AvBaItem> {

    /**
     * @see org.springframework.batch.item.file.transform.FieldExtractor#extract(java.lang.Object)
     */
    @Override
    public Object[] extract(AvBaItem paramItem) {

        List<String> value = new ArrayList<String>();
        value.add(paramItem.getProjectId());
        value.add(paramItem.getActivityId());
        value.add(paramItem.getActivityName());
        value.add(paramItem.getResourceId());

        if (null == paramItem.getUnits()) {
            value.add("");
        } else if (0 != ((paramItem.getUnits() * 10.0) % 10)) {
            value.add(String.valueOf(paramItem.getUnits()));
        } else {
            value.add(String.valueOf(paramItem.getUnits() == null ? null : Integer
                    .valueOf((int) Math.round(paramItem.getUnits()))));
        }

        return value.toArray();
    }
}
