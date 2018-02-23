package secfox.soc.es.search;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.stats.IndexStats;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import secfox.soc.es.search.request.ESQueryRequest;
import secfox.soc.es.search.request.ESQueryRequestParam;
import secfox.soc.es.search.response.ESQueryHitCountResponse;
import secfox.soc.es.search.response.ESQueryResponse;
import secfox.soc.es.search.service.bean.Idx4SlotWrapper;
import secfox.soc.es.search.service.bean.QueryHitCountWrapper;
import secfox.soc.es.search.service.bean.Slot;
import secfox.soc.es.search.service.exception.UnkownSearchException;
import secfox.soc.netty.server.NettyConfig;
import secfox.soc.util.CalendarUtils;
import secfox.soc.util.DSLBuilderUtils;
import secfox.soc.util.IndexGenerater;

/**
 * ES客户端
 * 
 * @author wangzhijie
 * 
 */
public class ESClient {

	private final static Logger logger = LoggerFactory
			.getLogger(ESClient.class);

	private static volatile ESClient instance = new ESClient();

	private TransportClient client;

	private ESClient() {
		try {
			Settings settings = Settings
					.builder()
					.put("cluster.name",
							NettyConfig.getInstance().getConfigValue(
									NettyConfig.KEY_ES_CLUSTERNAME)).build();
			client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new InetSocketTransportAddress(
							InetAddress.getByName(NettyConfig.getInstance()
									.getConfigValue(NettyConfig.KEY_ES_HOST)),
							Integer.parseInt(NettyConfig.getInstance()
									.getConfigValue(NettyConfig.KEY_ES_PORT))));
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	public static ESClient getInstance() {
		return instance;
	}
	
	public TransportClient getClient(){
		return client;
	}
	
	public Set<String> getSkyeyeLasIndexes() {
		Set<String> skyeyeLasIndex = new HashSet<String>();
	    ActionFuture<IndicesStatsResponse> isr = ESClient.getInstance().getClient().admin().indices().stats(new IndicesStatsRequest().all());
	    Set<String> indexes = isr.actionGet().getIndices().keySet();
	    StringBuilder indexLog = new StringBuilder();
	    for(String index : indexes){
	    	if(index.startsWith("skyeye-las_event-")){
	    		skyeyeLasIndex.add(index);
	    		indexLog.append(index+",");
	    	}
	    }
	    
	    logger.info("[exist skyeye indexes : "+indexLog.toString()+"]");
	    
	    return skyeyeLasIndex;
	}
	
	
	public ESQueryHitCountResponse doQueryHitCount(QueryHitCountWrapper qhcWrapper,AbstractQueryBuilder queryBuilder)throws UnkownSearchException{
		String type = NettyConfig.getInstance().getConfigValue(
				NettyConfig.KEY_ES_TYPE);
		
		SearchRequestBuilder srb = client.prepareSearch(qhcWrapper.getHitIndex()).setTypes(type);
		
		// query
		srb.setQuery(queryBuilder);
		
		srb.addSort(ESSearchConstants.ES_RECEPT_TIME, SortOrder.ASC);
		
		srb.setSize(0);
		srb.setExplain(false);
		
		logger.info("[do queryHitCount execute...]");
		logger.info(srb.toString());
		
		long c1 = System.currentTimeMillis();
		// 执行
		SearchResponse response = srb.get();
		long c2 = System.currentTimeMillis();

		SearchHits hits = response.getHits();
		long total = hits.getTotalHits();
		
		TreeSet<String> treeSet = new TreeSet<String>(qhcWrapper.getIdx4Time().keySet());  
		
//		TreeSet<String> treeSet = (TreeSet<String>)qhcWrapper.getIdx4Time().keySet();
		String nextHitIndex = treeSet.tailSet(qhcWrapper.getHitIndex()).iterator().next();
		String lastHitIndex = treeSet.last();
		int isMore = 1;
		if(nextHitIndex.equals(lastHitIndex)) isMore = 0;
		
		ESQueryHitCountResponse queryHitCountResp = new ESQueryHitCountResponse(ESQueryHitCountResponse.Type.OK.code,
				ESQueryHitCountResponse.Type.OK.msg,total,nextHitIndex,isMore);
		
		logger.info("[query finished, index='{}',totalHits={},queryTime={}ms,queryRange={},nextHitIndex={},isMore={}]",
				qhcWrapper.getHitIndex(),
				total,(c2 - c1),
				CalendarUtils.logRangeTime(qhcWrapper.getQueryTimePair().getStartTime(),qhcWrapper.getQueryTimePair().getEndTime()),
				nextHitIndex,
				isMore);
		
		return queryHitCountResp;
	}
	
	
	public ESQueryResponse doQuery(Idx4SlotWrapper wrapper,AbstractQueryBuilder queryBuilder) throws UnkownSearchException{
		//execute 
		StringBuilder result = new StringBuilder();
		String type = NettyConfig.getInstance().getConfigValue(
				NettyConfig.KEY_ES_TYPE);

		int pageSize = wrapper.getReqWrapper().getRequest().getPageSize();
		
//		int pageSize = ESQueryRequestParam.FETCH_PAGESIZE;
		
		SearchRequestBuilder srb = client.prepareSearch(wrapper.getCurrentIndex()).setTypes(type);

		// filter
		// srb.setPostFilter(queryBuilder);
		
		// query
		srb.setQuery(queryBuilder);
		
		//默认升序
		if(wrapper.isAsc()) srb.addSort(ESSearchConstants.ES_RECEPT_TIME, SortOrder.ASC);
		else
			srb.addSort(ESSearchConstants.ES_RECEPT_TIME, SortOrder.DESC);

		// 分页
		srb.setFrom(wrapper.getSlotFrom()).setSize(pageSize);
		srb.setExplain(false);
		
		logger.info("[do query execute...]");
		logger.info(srb.toString());
		
		long c1 = System.currentTimeMillis();
		// 执行
		SearchResponse response = srb.get();
		long c2 = System.currentTimeMillis();

		SearchHits hits = response.getHits();
		long total = hits.getTotalHits();
		
		logger.info("[query finished, index='"+wrapper.getCurrentIndex()+"',slotKey='" +wrapper.getCurrentSlot().getSlotKey() +"',total hits="+total+",queryTime="+(c2 - c1)+"ms,query range="+CalendarUtils.logRangeTime(wrapper.getCurrentSlot())+"]");

		SearchHit[] searchHits = hits.hits();
		for (int i = 0; i < searchHits.length; i++) {
//			logger.info(searchHits[i].getSourceAsString());
			result.append(searchHits[i].getSourceAsString());
			if (i != searchHits.length - 1)
				result.append("\n");
		}
		
		ESQueryResponse respnose = new ESQueryResponse();
		
		if(total > pageSize){
			respnose.setHitIndex(wrapper.getCurrentIndex());
			respnose.setHitSlotKey(wrapper.getCurrentSlot().getSlotKey());
			respnose.setSlotFrom(wrapper.getSlotFrom()+pageSize+1);
		}else{
			if(wrapper.hasExtraSlot4CurrIdx()){
				respnose.setHitIndex(wrapper.getCurrentIndex());
				Slot nextSlot = wrapper.getNextSlot();
				if(nextSlot != null){
					respnose.setHitSlotKey(nextSlot.getSlotKey());
					wrapper.updateCurrentSlot(nextSlot);
				}else{
					//error
					throw new UnkownSearchException("unkown error");
				}
			}else{
				String nextIndex = wrapper.getNextIndex(wrapper.getCurrentIndex());
				if(nextIndex != null){
					Slot nextSlot = wrapper.getNextSlot();
					respnose.setHitSlotKey(nextSlot.getSlotKey());
					wrapper.updateCurrentSlot(nextSlot);
					respnose.setHitIndex(nextIndex);
					wrapper.updateCurrentIndex(nextIndex);
				}else{
					//set isMore 0
					respnose.setIsMore(0);
				}
			}
		}

		respnose.setCode(ESQueryResponse.Type.OK.code);
		respnose.setMsg(ESQueryResponse.Type.OK.msg);
		respnose.setCount(searchHits.length);
		respnose.setData(result.toString());

		return respnose;
	}
}
