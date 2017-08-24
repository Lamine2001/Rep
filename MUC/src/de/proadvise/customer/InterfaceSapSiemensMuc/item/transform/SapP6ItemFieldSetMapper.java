/*
 * Company:         proadvise GmbH   www.proadvise.com
 * Create date:     01.10.2014 15:11:13
 * Copyright(C):    proadvise GmbH 2014
 * 
 * License agreement:
 * 
 */
package de.proadvise.customer.InterfaceSapSiemensMuc.item.transform;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.SapItem;


/**
 * Mappt ausgelesene Zeile in Bean gemaess Spezifikation.
 * 
 * @author proadvise GmbH
 * 
 */
public class SapP6ItemFieldSetMapper implements FieldSetMapper<SapItem> {

    private String dateFormat = "ddMMyyyy";

    private String netzplannummer;
    private String netzplanvorgang;
    private String einschraenkungAnfangEinschraenkungsart;
    private String einschraenkungAnfangTermin;
    private String einschraenkungEndeEinschraenkungsart;
    private String einschraenkungEndeTermin;
    private String stellplatz;
    private String istPrognoseAnfangTermin;
    private String istPrognoseEndeTermin;

    /**
     * @see org.springframework.batch.item.file.mapping.FieldSetMapper#mapFieldSet(org.springframework.batch.item.file.transform.FieldSet)
     */
    @Override
    public SapItem mapFieldSet(final FieldSet fs) throws BindException {

        if (null == fs) {
            return null;
        }

        SapItem entity = new SapItem();

        entity.setNetzplannummer(fs.readString("netzplannummer"));
        entity.setNetzplanvorgang(fs.readString("netzplanvorgang"));
        entity.setEinschraenkungAnfangEinschraenkungsart(fs
                .readString("einschraenkungAnfangEinschraenkungsart"));
        entity.setEinschraenkungAnfangTermin(fs.readDate("einschraenkungAnfangTermin", dateFormat, null));
        entity.setEinschraenkungEndeEinschraenkungsart(fs
                .readString("einschraenkungEndeEinschraenkungsart"));
        entity.setEinschraenkungEndeTermin(fs.readDate("einschraenkungEndeTermin", dateFormat, null));
        entity.setStellplatz(fs.readString("stellplatz"));
        if (StringUtils.isBlank(entity.getStellplatz())) {
            entity.setStellplatz("xxxxxxxx");// Default laut Spezifikation
        }
        entity.setIstPrognoseAnfangTermin(fs.readDate("istPrognoseAnfangTermin", dateFormat, null));
        entity.setIstPrognoseEndeTermin(fs.readDate("istPrognoseEndeTermin", dateFormat, null));

        return entity;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getNetzplannummer() {
        return netzplannummer;
    }

    public void setNetzplannummer(String netzplannummer) {
        this.netzplannummer = netzplannummer;
    }

    public String getNetzplanvorgang() {
        return netzplanvorgang;
    }

    public void setNetzplanvorgang(String netzplanvorgang) {
        this.netzplanvorgang = netzplanvorgang;
    }

    public String getEinschraenkungAnfangEinschraenkungsart() {
        return einschraenkungAnfangEinschraenkungsart;
    }

    public void setEinschraenkungAnfangEinschraenkungsart(
            String einschraenkungAnfangEinschraenkungsart) {
        this.einschraenkungAnfangEinschraenkungsart = einschraenkungAnfangEinschraenkungsart;
    }

    public String getEinschraenkungAnfangTermin() {
        return einschraenkungAnfangTermin;
    }

    public void setEinschraenkungAnfangTermin(String einschraenkungAnfangTermin) {
        this.einschraenkungAnfangTermin = einschraenkungAnfangTermin;
    }

    public String getEinschraenkungEndeEinschraenkungsart() {
        return einschraenkungEndeEinschraenkungsart;
    }

    public void setEinschraenkungEndeEinschraenkungsart(String einschraenkungEndeEinschraenkungsart) {
        this.einschraenkungEndeEinschraenkungsart = einschraenkungEndeEinschraenkungsart;
    }

    public String getEinschraenkungEndeTermin() {
        return einschraenkungEndeTermin;
    }

    public void setEinschraenkungEndeTermin(String einschraenkungEndeTermin) {
        this.einschraenkungEndeTermin = einschraenkungEndeTermin;
    }

    public String getStellplatz() {
        return stellplatz;
    }

    public void setStellplatz(String stellplatz) {
        this.stellplatz = stellplatz;
    }

    public String getIstPrognoseAnfangTermin() {
        return istPrognoseAnfangTermin;
    }

    public void setIstPrognoseAnfangTermin(String istPrognoseAnfangTermin) {
        this.istPrognoseAnfangTermin = istPrognoseAnfangTermin;
    }

    public String getIstPrognoseEndeTermin() {
        return istPrognoseEndeTermin;
    }

    public void setIstPrognoseEndeTermin(String istPrognoseEndeTermin) {
        this.istPrognoseEndeTermin = istPrognoseEndeTermin;
    }
}
