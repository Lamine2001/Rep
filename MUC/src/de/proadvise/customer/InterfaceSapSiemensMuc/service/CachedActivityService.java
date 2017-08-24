package de.proadvise.customer.InterfaceSapSiemensMuc.service;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.CachedActivity;

public interface CachedActivityService {
    public CachedActivity findByNetzplan(String netzplanNummer, String netzplanVorgang);
}
