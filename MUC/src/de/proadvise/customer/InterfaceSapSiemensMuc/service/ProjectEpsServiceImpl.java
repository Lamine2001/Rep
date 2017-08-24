package de.proadvise.customer.InterfaceSapSiemensMuc.service;

import java.util.List;

import com.primavera.common.value.ObjectId;
import com.primavera.integration.client.bo.object.Project;

import de.proadvise.tool.p6util.connection.P6SessionFactory;
import de.proadvise.tool.p6util.dao.BoDao;
import de.proadvise.tool.p6util.dao.BoResultSet;
import de.proadvise.tool.p6util.dao.DefaultBoDao;
import de.proadvise.tool.p6util.dao.DefaultP6QueryProvider;
import de.proadvise.tool.p6util.dao.P6QueryProvider;
import de.proadvise.tool.p6util.dao.namedparam.MapConditionParameters;

public class ProjectEpsServiceImpl implements ProjectEpsService {
    private BoDao<Project> projectDao = new DefaultBoDao<Project>(Project.class);
    private P6QueryProvider projectByEpsObjectIdQueryProvider = new DefaultP6QueryProvider("ObjectId", 
            "ParentEPSObjectId in (:epsObjectIds)", "ObjectId", "Id", "Name");
    
    @Override
    public BoResultSet<Project> findByEpsObjectIds(List<ObjectId> epsObjectIds) {
        MapConditionParameters params = new MapConditionParameters();
        params.addValue("epsObjectIds", epsObjectIds);
        
        return projectDao.load(projectByEpsObjectIdQueryProvider, params);
    }

    @Override
    public void setSessionFactory(P6SessionFactory sessionFactory) {
        projectDao.setSessionFactory(sessionFactory);
    }

    @Override
    public void setSessionName(String sessionName) {
        projectDao.setSessionName(sessionName);
    }
   
}
