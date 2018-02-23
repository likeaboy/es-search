/*package secfox.soc.search.es_search;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.admin.AdminClient;
import kafka.admin.AdminClient.ConsumerGroupSummary;
import kafka.admin.AdminClient.ConsumerSummary;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.TopicAndPartition;
import kafka.coordinator.GroupOverview;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.consumer.SimpleConsumer;

public class KafkaAdminTest {
	public static void main(String[] args) {
		String topic = "report";
		String groupId = "es-cons-report";
		List<String> topics = Collections.singletonList(topic);  
        TopicMetadataRequest topicMetadataRequest = new TopicMetadataRequest(topics);  
  
        KafkaTopicOffset kafkaTopicOffset = new KafkaTopicOffset(topic);  
//        String[] seeds = getIpsFromBrokerList(brokerlist);  
//        Map<String,Integer> portMap = getPortFromBrokerList(brokerlist);  
        
        SimpleConsumer consumer = new SimpleConsumer("192.168.75.129",9092,10,65535,groupId);
        kafka.javaapi.TopicMetadataResponse resp = consumer.send(topicMetadataRequest);  
        List<TopicMetadata> metaData = resp.topicsMetadata();
        for (TopicMetadata item : metaData) {  
            for (PartitionMetadata part : item.partitionsMetadata()) {  
                kafkaTopicOffset.getLeaderList().put(part.partitionId(),part.leader().host());  
                kafkaTopicOffset.getOffsetList().put(part.partitionId(),0L);  
            }  
        }  
        
        Iterator iterator = kafkaTopicOffset.getOffsetList().entrySet().iterator();  
        
        while(iterator.hasNext()){  
            Map.Entry<Integer,Long> entry = (Map.Entry<Integer, Long>) iterator.next();  
            int partitonId = entry.getKey();  

            if(!kafkaTopicOffset.getLeaderList().get(partitonId).equals("192.168.75.129")){  
                continue;  
            }  

            TopicAndPartition topicAndPartition = new TopicAndPartition(topic,  
                    partitonId);  
            Map<TopicAndPartition,PartitionOffsetRequestInfo> requestInfo =  
                    new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();  

            requestInfo.put(topicAndPartition,  
                    new PartitionOffsetRequestInfo(kafka.api.OffsetRequest.LatestTime(),1)  
            );  
            kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(  
                    requestInfo, kafka.api.OffsetRequest.CurrentVersion(),  
                    groupId);  
            OffsetResponse response = consumer.getOffsetsBefore(request);  
            long[] offsets = response.offsets(topic,partitonId);  
            if(offsets.length > 0){  
                kafkaTopicOffset.getOffsetList().put(partitonId,offsets[0]);  
            }  
        }  
	}
}
*/