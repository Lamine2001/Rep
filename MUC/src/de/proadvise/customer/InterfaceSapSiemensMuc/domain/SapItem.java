/*
 * Company:         proadvise GmbH   www.proadvise.com
 * Create date:     01.10.2014 15:30:15
 * Copyright(C):    proadvise GmbH 2014
 * 
 * License agreement:
 * 
 */
package de.proadvise.customer.InterfaceSapSiemensMuc.domain;

import java.util.Date;

/**
 * Repraesentiert eine Zeile einer ausgelesenen Datei
 * 
 * @author proadvise GmbH
 * 
 */
public class SapItem {

    private String netzplannummer;
    private String netzplanvorgang;
    private String einschraenkungAnfangEinschraenkungsart;
    private Date einschraenkungAnfangTermin;
    private String einschraenkungEndeEinschraenkungsart;
    private Date einschraenkungEndeTermin;
    private String stellplatz;
    private Date istPrognoseAnfangTermin;
    private Date istPrognoseEndeTermin;

    public String toString() {
        return "Item [Netzplannummer = " + netzplannummer + ", Netzplanvorgang = "
                + netzplanvorgang + ", Einschraenkung Anfang Einschraenkungsart = "
                + einschraenkungAnfangEinschraenkungsart + ", Einschraenkung Anfang Termin = "
                + einschraenkungAnfangTermin + ", Einschraenkung Ende Einschraenkungsart = "
                + einschraenkungEndeEinschraenkungsart + ", Einschraenkung Ende Termin = "
                + einschraenkungEndeTermin + ", Stellplatz = " + stellplatz
                + ", Ist/Prognose-Anfang Termin = " + istPrognoseAnfangTermin
                + ", Ist/Prognose-Ende Termin = " + istPrognoseEndeTermin + "]";
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

    public Date getEinschraenkungAnfangTermin() {
        return einschraenkungAnfangTermin;
    }

    public void setEinschraenkungAnfangTermin(Date einschraenkungAnfangTermin) {
        this.einschraenkungAnfangTermin = einschraenkungAnfangTermin;
    }

    public String getEinschraenkungEndeEinschraenkungsart() {
        return einschraenkungEndeEinschraenkungsart;
    }

    public void setEinschraenkungEndeEinschraenkungsart(String einschraenkungEndeEinschraenkungsart) {
        this.einschraenkungEndeEinschraenkungsart = einschraenkungEndeEinschraenkungsart;
    }

    public Date getEinschraenkungEndeTermin() {
        return einschraenkungEndeTermin;
    }

    public void setEinschraenkungEndeTermin(Date einschraenkungEndeTermin) {
        this.einschraenkungEndeTermin = einschraenkungEndeTermin;
    }

    public String getStellplatz() {
        return stellplatz;
    }

    public void setStellplatz(String stellplatz) {
        this.stellplatz = stellplatz;
    }

    public Date getIstPrognoseAnfangTermin() {
        return istPrognoseAnfangTermin;
    }

    public void setIstPrognoseAnfangTermin(Date istPrognoseAnfangTermin) {
        this.istPrognoseAnfangTermin = istPrognoseAnfangTermin;
    }

    public Date getIstPrognoseEndeTermin() {
        return istPrognoseEndeTermin;
    }

    public void setIstPrognoseEndeTermin(Date istPrognoseEndeTermin) {
        this.istPrognoseEndeTermin = istPrognoseEndeTermin;
    }
}
