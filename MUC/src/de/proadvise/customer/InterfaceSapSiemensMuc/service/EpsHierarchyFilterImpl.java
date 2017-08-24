package de.proadvise.customer.InterfaceSapSiemensMuc.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.primavera.common.value.ObjectId;
import com.primavera.integration.client.bo.object.EPS;

import de.proadvise.tool.p6util.connection.P6SessionFactory;
import de.proadvise.tool.p6util.dao.BoDao;
import de.proadvise.tool.p6util.dao.BoResultSet;
import de.proadvise.tool.p6util.dao.DefaultBoDao;
import de.proadvise.tool.p6util.dao.DefaultP6QueryProvider;
import de.proadvise.tool.p6util.dao.P6QueryProvider;
import de.proadvise.tool.p6util.hierarchy.BoHierarchyBuilder;
import de.proadvise.tool.p6util.hierarchy.HierarchyBuilder;
import de.proadvise.tool.p6util.hierarchy.HierarchyIndex;
import de.proadvise.tool.p6util.hierarchy.HierarchyNode;

public class EpsHierarchyFilterImpl implements EpsHierarchyFilter {
    
    private BoDao<EPS> epsDao = new DefaultBoDao<EPS>(EPS.class);
    private P6QueryProvider allEpsQueryProvider = new DefaultP6QueryProvider("ObjectId", "", 
            "ObjectId", "ParentObjectId", "Id", "Name");
    
    private HierarchyIndex<EPS> hierarchyIndex;
    private HierarchyBuilder<EPS> hierarchyBuilder = new BoHierarchyBuilder<EPS>(EPS.class);
    
    @Override
    public List<ObjectId> getDescendantNodeKeysFor(List<ObjectId> keys) {
        lazyLoadAllEpsObjects();
        
        Collection<HierarchyNode<EPS>> epsNodes = getNodesFromHierarchyFor(keys);
        
        List<ObjectId> descendantNodeKeys = searchAllDescendantKeys(epsNodes);
        
        return descendantNodeKeys;
    }

    protected void lazyLoadAllEpsObjects() {
        if (null == hierarchyIndex) {
            BoResultSet<EPS> allEpsObjects = epsDao.load(allEpsQueryProvider);
            hierarchyIndex = hierarchyBuilder.add(allEpsObjects).build();
        }
    }

    protected Collection<HierarchyNode<EPS>> getNodesFromHierarchyFor(List<ObjectId> keys) {
        return hierarchyIndex.getNodes(keys);
    }

    protected List<ObjectId> searchAllDescendantKeys(Collection<HierarchyNode<EPS>> epsNodes) {
        List<ObjectId> descendantNodeKeys = new ArrayList<ObjectId>();
        for (HierarchyNode<EPS> node : epsNodes) {
            traverseDeepFirst(node, descendantNodeKeys);
        }
        return descendantNodeKeys;
    }
    
    protected void traverseDeepFirst(HierarchyNode<EPS> node, final List<ObjectId> deepFirstTraversal) {
        deepFirstTraversal.add((ObjectId) node.getKey());
        
        for (HierarchyNode<EPS> childNode : node.getChildren()) {
            traverseDeepFirst(childNode, deepFirstTraversal);
        }
    }

    @Override
    public void setSessionFactory(P6SessionFactory sessionFactory) {
        epsDao.setSessionFactory(sessionFactory);
    }

    @Override
    public void setSessionName(String sessionName) {
        epsDao.setSessionName(sessionName);
    }

    protected HierarchyIndex<EPS> getHierarchyIndex() {
        return hierarchyIndex;
    }

    protected void setHierarchyIndex(HierarchyIndex<EPS> hierarchyIndex) {
        this.hierarchyIndex = hierarchyIndex;
    }
}
