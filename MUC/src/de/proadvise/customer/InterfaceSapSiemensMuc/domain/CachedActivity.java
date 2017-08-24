package de.proadvise.customer.InterfaceSapSiemensMuc.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: CacheActivity
 *
 */
@Entity
@Table(name = "CachedActivity")
@NamedQueries({ 
    @NamedQuery(name = "CachedActivity.findByNetzplan", query = "SELECT a FROM CachedActivity a where 1=1" +
            "and a.netzplanNummer = :netzplanNummer and a.netzplanVorgang = :netzplanVorgang") 
})
public class CachedActivity implements Serializable {

    private static final long serialVersionUID = 8801041769484096739L;
    @Id
    private Integer objectId;
    private String id;
    private Integer projectObjectId;
    private String projectId;
    private Integer wbsObjectId;
    private String status;
    private Date startDate;
    private Date actualStartDate;
    private Date finishDate;
    private Date actualFinishDate;
    private String netzplanNummer;
    private String netzplanVorgang;

    public CachedActivity() {
        super();
    }   
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }   
    public Integer getObjectId() {
        return this.objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }   
    public Integer getProjectObjectId() {
        return this.projectObjectId;
    }

    public void setProjectObjectId(Integer projectObjectId) {
        this.projectObjectId = projectObjectId;
    }   
    public String getProjectId() {
        return this.projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }   
    public Integer getWbsObjectId() {
        return wbsObjectId;
    }
    public void setWbsObjectId(Integer wbsObjectId) {
        this.wbsObjectId = wbsObjectId;
    }
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String Status) {
        this.status = Status;
    }   
    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }   
    public Date getActualStartDate() {
        return this.actualStartDate;
    }

    public void setActualStartDate(Date actualStartDate) {
        this.actualStartDate = actualStartDate;
    }   
    public Date getFinishDate() {
        return this.finishDate;
    }

    public void setFinishDate(Date FinishDate) {
        this.finishDate = FinishDate;
    }   
    public Date getActualFinishDate() {
        return this.actualFinishDate;
    }

    public void setActualFinishDate(Date ActualFinishDate) {
        this.actualFinishDate = ActualFinishDate;
    }   
    public String getNetzplanNummer() {
        return this.netzplanNummer;
    }

    public void setNetzplanNummer(String netzplanNummer) {
        this.netzplanNummer = netzplanNummer;
    }   
    public String getNetzplanVorgang() {
        return this.netzplanVorgang;
    }

    public void setNetzplanVorgang(String netzplanVorgang) {
        this.netzplanVorgang = netzplanVorgang;
    }
   
}
