package de.proadvise.customer.InterfaceSapSiemensMuc.dao.bograph;

import com.primavera.integration.client.bo.object.UDFValue;

import de.proadvise.customer.InterfaceSapSiemensMuc.domain.ActivityGraph;
import de.proadvise.tool.batch.item.p6api.reader.BoBucketFiller;
import de.proadvise.tool.p6util.dao.BoResultSet;
import de.proadvise.tool.p6util.dao.bograph.BoGraph;

public class UdfValueFiller implements BoBucketFiller<UDFValue> {

    @Override
    public void fillBucket(BoGraph<?> boGraph, BoResultSet<UDFValue> resultSet) {
        ActivityGraph graph = (ActivityGraph) boGraph;
        
        while (resultSet.hasNextFor(graph.getStartObject())){
            graph.addUdfValue(resultSet.nextFor(graph.getStartObject()));
        }
    }
}
