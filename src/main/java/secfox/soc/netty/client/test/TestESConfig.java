package secfox.soc.netty.client.test;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.cluster.node.info.NodeInfo;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.alibaba.fastjson.JSON;

import secfox.soc.es.search.ESClient;

public class TestESConfig {
	public static String clusterName = "elasticsearch";// 集群名称
    public static String serverIP = "127.0.0.1";// 服务器IP
    public static void main(String[] args) throws UnknownHostException {
        System.out.println(getMapping("skyeye-las_event-2017.12.05", "sim"));
    }

    public static String getMapping(String indexname, String typename) throws UnknownHostException {
//        Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName).build();
        String mapping="";
        TransportClient client = ESClient.getInstance().getClient();
//            TransportClient client = TransportClient.builder().settings(settings).build()
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(serverIP), 9300));

		ImmutableOpenMap<String, MappingMetaData> mappings = client.admin().cluster().prepareState().execute()
		        .actionGet().getState().getMetaData().getIndices().get(indexname).getMappings();
		mapping = mappings.get(typename).source().toString();

		try {
			Map<String,Object> maps = mappings.get(typename).sourceAsMap();
			for(String key : maps.keySet())
				System.out.println(key);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		NodesInfoResponse resp = ESClient.getInstance().getClient().admin().cluster().prepareNodesInfo().execute().actionGet();
        
        List<NodeInfo> nodes = resp.getNodes();
        
        for(NodeInfo nodeInfo : nodes){
            DiscoveryNode node = nodeInfo.getNode();
            System.out.println("version:" + node.getVersion());
            System.out.println("address:" + node.getHostAddress());
            System.out.println("nodeId:" + node.getId());
            System.out.println("nodeInfo:" + JSON.toJSONString(node));
        }
		

		client.close();

        return mapping;
    }
}

