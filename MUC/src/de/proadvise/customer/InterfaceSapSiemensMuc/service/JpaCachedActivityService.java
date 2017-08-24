package de.proadvise.customer.InterfaceSapSiemensMuc.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.CachedActivity;

public class JpaCachedActivityService implements CachedActivityService {
    private static final Logger LOG = Logger.getLogger(JpaCachedActivityService.class);

    @PersistenceUnit(unitName = "InterfaceSapSiemensMuc")
    private EntityManagerFactory entityManagerFactory;
    
    @Override
    public CachedActivity findByNetzplan(String netzplanNummer, String netzplanVorgang) {
        EntityManager entityManager = createEntityManager();
        
        Query query = entityManager.createNamedQuery("CachedActivity.findByNetzplan");
        query.setParameter("netzplanNummer", netzplanNummer);
        query.setParameter("netzplanVorgang", netzplanVorgang);
        
        try {
            @SuppressWarnings("unchecked")
            List<CachedActivity> results = (List<CachedActivity>) query.getResultList();
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("[%s] cached activities found for netzplanNummer [%s] and netzplanVorgang [%s]",
                        null != results ? results.size() : "null", netzplanNummer, netzplanVorgang));
            }
            
            if (null == results || results.isEmpty()) {
                return null;
            }
            if (results.size() > 1) {
                LOG.warn(String.format("More than one cached activity found for netzplanNummer [%s] and netzplanVorgang [%s]",
                        netzplanNummer, netzplanVorgang));
            }
            
            return results.get(0); // use first found activity as described in specification
        } finally {
            entityManager.clear();
            entityManager.close();
            
        }
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    protected EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

}
