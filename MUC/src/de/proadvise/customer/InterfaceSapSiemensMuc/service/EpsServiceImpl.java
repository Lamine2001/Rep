package de.proadvise.customer.InterfaceSapSiemensMuc.service;

import java.util.ArrayList;
import java.util.List;

import com.primavera.integration.client.bo.object.EPS;

import de.proadvise.tool.p6util.connection.P6SessionFactory;
import de.proadvise.tool.p6util.dao.BoDao;
import de.proadvise.tool.p6util.dao.BoResultSet;
import de.proadvise.tool.p6util.dao.DefaultBoDao;
import de.proadvise.tool.p6util.dao.DefaultP6QueryProvider;
import de.proadvise.tool.p6util.dao.P6QueryProvider;
import de.proadvise.tool.p6util.dao.namedparam.MapConditionParameters;

public class EpsServiceImpl implements EpsService {
    
    private BoDao<EPS> epsDao = new DefaultBoDao<EPS>(EPS.class);
    private P6QueryProvider likeIdQueryProvider = new DefaultP6QueryProvider("ObjectId", "Id like :likeClause", 
            "ObjectId", "Id", "Name", "ParentObjectId");
    private P6QueryProvider idQueryProvider = new DefaultP6QueryProvider("ObjectId", "Id = :id", 
            "ObjectId", "Id", "Name", "ParentObjectId");

    @Override
    public EPS findById(String id) {
        MapConditionParameters params = new MapConditionParameters();
        params.addValue("id", id);
        return epsDao.loadOne(idQueryProvider, params);
    }

    @Override
    public List<EPS> findLikeId(String likeIdClause) {
        MapConditionParameters params = new MapConditionParameters();
        params.addValue("likeClause", likeIdClause);
        BoResultSet<EPS> resultSet = epsDao.load(likeIdQueryProvider, params);
        
        List<EPS> epsObjects = new ArrayList<EPS>();
        while (resultSet.hasNext()) {
            epsObjects.add(resultSet.next());
        }
        
        return epsObjects;
    }

    @Override
    public void setSessionFactory(P6SessionFactory sessionFactory) {
        epsDao.setSessionFactory(sessionFactory);
    }

    @Override
    public void setSessionName(String sessionName) {
        epsDao.setSessionName(sessionName);
    }

}
