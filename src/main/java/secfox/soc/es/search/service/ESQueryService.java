package secfox.soc.es.search.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import secfox.soc.es.search.ESClient;
import secfox.soc.es.search.request.ESQueryRequestParam;
import secfox.soc.es.search.request.ESQueryRequestWrapper;
import secfox.soc.es.search.response.ESQueryHitCountResponse;
import secfox.soc.es.search.response.ESQueryResponse;
import secfox.soc.es.search.service.bean.Idx4SlotWrapper;
import secfox.soc.es.search.service.bean.QueryHitCountWrapper;
import secfox.soc.es.search.service.bean.SearchConditionWrapper;
import secfox.soc.es.search.service.bean.Slot;
import secfox.soc.es.search.service.bean.TimePair;
import secfox.soc.es.search.service.exception.UnkownSearchException;
import secfox.soc.util.CalendarUtils;
import secfox.soc.util.DSLBuilderUtils;
import secfox.soc.util.DynamicSliceUtils;
import secfox.soc.util.IndexGenerater;

/**
 * 普通查询服务
 * @author wangzhijie
 *
 */
public class ESQueryService {
	private final static Logger logger = LoggerFactory
			.getLogger(ESQueryService.class);

	private static volatile ESQueryService instance = new ESQueryService();
	
	private ESQueryService() {
	}
	
	public static ESQueryService getInstance(){
		return instance;
	}
	
	private boolean isAsc(String order){
		if(order == null) return true;
		if(order.equals(ESQueryRequestParam.TimeOrder.ASC))
			return true;
		else
			return false;
	}
	
	/**
	 * 执行一次fetch slot查询
	 * @param wrapper
	 * @return
	 */
	private ESQueryResponse fetchSlot(Idx4SlotWrapper wrapper,Map<String, SearchConditionWrapper> scMap) throws UnkownSearchException{
		String currentIndex = wrapper.getCurrentIndex();
		if(currentIndex == null){
			currentIndex = wrapper.getHeadIndex();
			//refresh
			wrapper.updateCurrentIndex(currentIndex);
		}
		
		Slot slot = wrapper.getCurrentSlot();
		if(slot == null){
			List<Slot> slots = wrapper.getSlots(currentIndex);
			slot = slots.get(0);
			//refresh
			wrapper.updateCurrentSlot(slot);
		}
		// query buider 
		AbstractQueryBuilder queryBuilder = DSLBuilderUtils.buildQuery(wrapper.getCurrentSlot().getStartTime(),wrapper.getCurrentSlot().getEndTime(),scMap);
		
		return ESClient.getInstance().doQuery(wrapper, queryBuilder);
	}
	
	/**
	 * 将newData并入oldData尾部，将合并后整体结果返回
	 * @param oldData
	 * @param newData
	 * @return
	 */
	private String merge(String oldData, String newData) {
		if(StringUtils.isEmpty(oldData) && StringUtils.isEmpty(newData))
			return "";
		if(StringUtils.isEmpty(oldData) && !StringUtils.isEmpty(newData))
			return newData;
		if(!StringUtils.isEmpty(oldData) && StringUtils.isEmpty(newData))
			return oldData;
		StringBuilder dataAppend = new StringBuilder(oldData);
		dataAppend.append("\n");
		dataAppend.append(newData);
		return dataAppend.toString();
	}
	
	private ESQueryResponse doMoreFetchSlot(ESQueryResponse finalRep,Idx4SlotWrapper wrapper,Map<String, SearchConditionWrapper> scMap) throws UnkownSearchException{
		int queryCount=0;
		logger.info("[do more fetch start...]");
		long c1 = System.currentTimeMillis();
		ESQueryResponse rep = null;
		do{
			rep = fetchSlot(wrapper,scMap);
			queryCount++;
			//累加
			finalRep.setCount(finalRep.getCount() + rep.getCount());
			finalRep.setHitIndex(rep.getHitIndex());
			finalRep.setHitSlotKey(rep.getHitSlotKey());
			finalRep.setSlotFrom(rep.getSlotFrom());
			finalRep.setData(merge(finalRep.getData(),rep.getData()));
			finalRep.setIsMore(rep.getIsMore());
		}while(finalRep.getIsMore() != 0 && finalRep.getCount() < ESQueryRequestParam.FETCH_PAGESIZE);

		long c2 = System.currentTimeMillis();
		logger.info("[do more fetch end...,total query count:"+(queryCount+1)+",query count:"+queryCount+",cost time:"+(c2-c1)+"ms,search time range: "+
				CalendarUtils.logRangeTime(wrapper.getReqWrapper().getRequest().getStartTime(),wrapper.getReqWrapper().getRequest().getEndTime())+"]");
		return finalRep;
	}
	
	public ESQueryResponse query(ESQueryRequestWrapper esReqWrapper)
			throws UnkownSearchException {
		//slice
		Idx4SlotWrapper wrapper = DynamicSliceUtils.slice(
				esReqWrapper.getRequest().getStartTime(), 
				esReqWrapper.getRequest().getEndTime(), 
				isAsc(esReqWrapper.getRequest().getOrder()),
				esReqWrapper.getRequest().getHitIndex(),
				esReqWrapper.getRequest().getHitSlotKey(),
				esReqWrapper.getRequest().getSlotFrom());
		
		//绑request
		wrapper.setReqWrapper(esReqWrapper);
		
		if(wrapper == null || wrapper.getMap().keySet().size() == 0)
			return new ESQueryResponse(ESQueryResponse.Type.UNKONWERR.code,
					ESQueryResponse.Type.UNKONWERR.msg, 0, "");
		
		logger.info("[indexes : " + wrapper.getIndexes() + "]");
		
		// parse
		Map<String, SearchConditionWrapper> scMap = DSLBuilderUtils
				.convert(wrapper.getReqWrapper());
		
		//具体判断是否需要连续调用多次query
		
		ESQueryResponse esQueryRep = fetchSlot(wrapper,scMap);
		
		//no more data wili be fetched
		if(esQueryRep.getIsMore() == 0)
			return esQueryRep;
		//max count is 999+1000
		if(esQueryRep.getCount() >= ESQueryRequestParam.FETCH_PAGESIZE)
			return esQueryRep;
		//do fetch only once
		if(esReqWrapper.getRequest().isFetchOnlyOnce())
			return esQueryRep;
		
		//具体判断是否需要连续调用多次query
		esQueryRep = doMoreFetchSlot(esQueryRep, wrapper,scMap);
		
		return esQueryRep;
	}
	
	public ESQueryHitCountResponse queryHitCount(ESQueryRequestWrapper esReqWrapper) throws Exception{
		Map<String,TimePair> idx4Time = DynamicSliceUtils.slice(esReqWrapper.getRequest().getStartTime(),
				esReqWrapper.getRequest().getEndTime());
		
		TreeSet<String> tree = new TreeSet<String>(idx4Time.keySet());  
		
//		TreeSet<String> tree = (TreeSet<String>)idx4Time.keySet();
		
		// parse
		Map<String, SearchConditionWrapper> scMap = DSLBuilderUtils
				.convert(esReqWrapper);
		
		String hitIndex = null;
		
		if(esReqWrapper.getRequest().getHitIndex() == null){
			hitIndex = tree.first();
		}else{
			hitIndex = esReqWrapper.getRequest().getHitIndex();
		}
		
		TimePair t = idx4Time.get(hitIndex);
		if(t == null)
			throw new Exception("TimePair object is null");
		
		// query buider 
		AbstractQueryBuilder queryBuilder = DSLBuilderUtils.buildQuery(t.getStartTime(),t.getEndTime(),scMap);
		
		//优化，返回0则继续查
		QueryHitCountWrapper qhcWrapper = new QueryHitCountWrapper(t,hitIndex,idx4Time);
		ESQueryHitCountResponse hitCountResp = ESClient.getInstance().doQueryHitCount(qhcWrapper, queryBuilder);
		
		while(hitCountResp.getCount() == 0 && hitCountResp.getIsMore() == 1){
			//index cursor next
			hitIndex = tree.tailSet(hitIndex).iterator().next();
			t = idx4Time.get(hitIndex);
			qhcWrapper = new QueryHitCountWrapper(t,hitIndex,idx4Time);
			hitCountResp = ESClient.getInstance().doQueryHitCount(qhcWrapper, queryBuilder);
		}
		
		return hitCountResp;
		
	}
}
