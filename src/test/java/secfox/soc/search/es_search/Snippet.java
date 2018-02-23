/*package secfox.soc.search.es_search;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import scala.Option;
import scala.collection.immutable.List;
import kafka.admin.AdminClient;
import kafka.admin.AdminClient.ConsumerGroupSummary;
import kafka.admin.AdminClient.ConsumerSummary;

public class Snippet {
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		  Properties props = new Properties();
		  props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "192.168.75.129:9092");
		  AdminClient adminClient = AdminClient.create(props);
		  ConsumerGroupSummary  consumerGroupSummary =  adminClient.describeConsumerGroup("kafkatest");
		  if(consumerGroupSummary.state().equals("Empty")){
			  System.out.println("is empty");
		  }
		  Option<List<ConsumerSummary>> consumerSummaryOption =  consumerGroupSummary.consumers();
	
		  List<ConsumerSummary> ConsumerSummarys = consumerSummaryOption.get();//获取组中的消费者
		  KafkaConsumer consumer = getNewConsumer();
		  for(int i=0;i<ConsumerSummarys.size();i++){ //循环组中的每一个消费者
			  
			  ConsumerSummary consumerSummary = ConsumerSummarys.apply(i);
			  String consumerId  = consumerSummary.consumerId();//获取消费者的id
			  scala.collection.immutable.Map<TopicPartition, Object> maps = 
					  adminClient.listGroupOffsets("kafkatest");//或者这个组消费的所有topic，partition和当前消费到的offset
			  List<TopicPartition> topicPartitions= consumerSummary.assignment();//获取这个消费者下面的所有topic和partion
			  for(int j =0;j< topicPartitions.size();j++){ //循环获取每一个topic和partion
				  TopicPartition topicPartition = topicPartitions.apply(j);
				  String CURRENToFFSET = maps.get(topicPartition).get().toString();
				  long endOffset =getLogEndOffset(topicPartition);
				  System.out.println("topic的名字为："+topicPartition.topic()+"====分区为："+topicPartition.partition()+"===目前消费offset为："+CURRENToFFSET+"===,此分区最后offset为："+endOffset);
			  }
		  }
	}
	
	 public static KafkaConsumer getNewConsumer(){
	     Properties props = new Properties();
	     props.put("bootstrap.servers", "192.168.75.129:9092");
	     props.put("group.id", "kafkatest");
	     props.put("enable.auto.commit", "true");
	     props.put("auto.offset.reset", "earliest");
	     props.put("auto.commit.interval.ms", "1000");
	     props.put("auto.commit.interval.ms", "1000");
	     props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
	     return consumer;
  }
  
	 public static long getLogEndOffset(TopicPartition topicPartition){
		  KafkaConsumer<String, String> consumer= getNewConsumer();
		  consumer.assign(Arrays.asList(topicPartition));
		  consumer.seekToEnd(Arrays.asList(topicPartition));
		  long endOffset = consumer.position(topicPartition);
		  return endOffset;
	  }
}

*/