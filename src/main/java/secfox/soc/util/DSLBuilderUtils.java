package secfox.soc.util;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import secfox.soc.es.search.ESSearchConstants;
import secfox.soc.es.search.request.ESQueryRequestParam;
import secfox.soc.es.search.request.ESQueryRequestWrapper;
import secfox.soc.es.search.service.bean.Idx4SlotWrapper;
import secfox.soc.es.search.service.bean.SearchConditionWrapper;
import secfox.soc.netty.server.NettyConfig;

/**
 * DSL查询语句拼接
 * @author wangzhijie
 *
 */
public class DSLBuilderUtils {
	private final static Logger logger = LoggerFactory.getLogger(DSLBuilderUtils.class);	
	//映射集合
	private static Map<String,String> parmToEs = new HashMap<String,String>();
	
	//前端param映射es field
	static {
		parmToEs.put(ESQueryRequestParam.KEY_DEVADDR, ESSearchConstants.ES_DEV);
		parmToEs.put(ESQueryRequestParam.KEY_SADDR, ESSearchConstants.ES_SIP);
		parmToEs.put(ESQueryRequestParam.KEY_SPORT, ESSearchConstants.ES_SPORT);
		parmToEs.put(ESQueryRequestParam.KEY_DADDR, ESSearchConstants.ES_DIP);
		parmToEs.put(ESQueryRequestParam.KEY_DPORT, ESSearchConstants.ES_DPORT);
		parmToEs.put(ESQueryRequestParam.KEY_ID, ESSearchConstants.ES_ID);
		parmToEs.put(ESQueryRequestParam.KEY_SYSTYPE, ESSearchConstants.ES_SYSTEM_TYPE);
		parmToEs.put(ESQueryRequestParam.KEY_PRIORITY, ESSearchConstants.ES_PRIORITY);
		parmToEs.put(ESQueryRequestParam.KEY_DEVTYPE, ESSearchConstants.ES_DEV_TYPE);
		parmToEs.put(ESQueryRequestParam.KEY_DEVCATEGORY, ESSearchConstants.ES_DEV_CATEGORY);
		
		parmToEs.put(ESQueryRequestParam.KEY_ACTION, ESSearchConstants.ES_ACT);
		parmToEs.put(ESQueryRequestParam.KEY_RESULT, ESSearchConstants.ES_RET);
		parmToEs.put(ESQueryRequestParam.KEY_SUSERNAME, ESSearchConstants.ES_USER);
		parmToEs.put(ESQueryRequestParam.KEY_STADDR, ESSearchConstants.ES_NAT_SIP);
		parmToEs.put(ESQueryRequestParam.KEY_STPORT, ESSearchConstants.ES_NAT_SPORT);
		parmToEs.put(ESQueryRequestParam.KEY_DTADDR, ESSearchConstants.ES_NAT_DIP);
		parmToEs.put(ESQueryRequestParam.KEY_DTPORT, ESSearchConstants.ES_NAT_DPORT);
		parmToEs.put(ESQueryRequestParam.KEY_KEYWORDS, ESSearchConstants.ES_MSG);
		
		//second add
		parmToEs.put(ESQueryRequestParam.KEY_DEV_VENDOR, ESSearchConstants.ES_DEV_VENDOR);
		parmToEs.put(ESQueryRequestParam.KEY_DEV_PRODUCT, ESSearchConstants.ES_DEV_PRODUCT);
		parmToEs.put(ESQueryRequestParam.KEY_SESSION, ESSearchConstants.ES_SESSION);
		parmToEs.put(ESQueryRequestParam.KEY_REQUESTURI, ESSearchConstants.ES_URI);
		parmToEs.put(ESQueryRequestParam.KEY_CUSTOMS1, ESSearchConstants.ES_CUSTOMS1);
		parmToEs.put(ESQueryRequestParam.KEY_CUSTOMS2, ESSearchConstants.ES_CUSTOMS2);
		parmToEs.put(ESQueryRequestParam.KEY_CUSTOMS3, ESSearchConstants.ES_CUSTOMS3);
		parmToEs.put(ESQueryRequestParam.KEY_CUSTOMS4, ESSearchConstants.ES_CUSTOMS4);
		parmToEs.put(ESQueryRequestParam.KEY_RESOURCE, ESSearchConstants.ES_RESOURCE);
		parmToEs.put(ESQueryRequestParam.KEY_CUSTOMD1, ESSearchConstants.ES_CUSTOMD1);
		parmToEs.put(ESQueryRequestParam.KEY_CUSTOMD2, ESSearchConstants.ES_CUSTOMD2);
		parmToEs.put(ESQueryRequestParam.KEY_CUSTOMD3, ESSearchConstants.ES_CUSTOMD3);
		parmToEs.put(ESQueryRequestParam.KEY_CUSTOMD4, ESSearchConstants.ES_CUSTOMD4);
		parmToEs.put(ESQueryRequestParam.KEY_SMAC, ESSearchConstants.ES_SMAC);
		parmToEs.put(ESQueryRequestParam.KEY_DMAC, ESSearchConstants.ES_DMAC);
		parmToEs.put(ESQueryRequestParam.KEY_RAWID, ESSearchConstants.ES_RAWID);
		
		parmToEs.put(ESQueryRequestParam.KEY_COLLECT_TYPE, ESSearchConstants.ES_COLLECTOR_TYPE);
		parmToEs.put(ESQueryRequestParam.KEY_NAME, ESSearchConstants.ES_NAME);
		parmToEs.put(ESQueryRequestParam.KEY_OBJECT, ESSearchConstants.ES_OBJECT);
	}
	
	
	/**
	 * 将param参数转查询map集合
	 * @param condMap
	 * @return
	 */
	public static Map<String,SearchConditionWrapper> convert(ESQueryRequestWrapper reqWrapper){
		Map<String,Object> params = reqWrapper.getParams();
		Map<String,SearchConditionWrapper> scMap = new HashMap<String,SearchConditionWrapper>();
		for(String key : params.keySet()){
			if(isEmpty(params.get(key))){
				scMap.put(key, null);
			}else{
				SearchConditionWrapper wrapper = new SearchConditionWrapper();
				String val = (String)params.get(key);
				wrapper.setKey(key);
				wrapper.setValue(val);
				if(val.startsWith("~")){
					wrapper.setEq(false);
					wrapper.setValue(val.substring(1));
				}else{
					wrapper.setEq(true);
				}
				scMap.put(key, wrapper);
			}
		}
		return scMap;
	}
	
	/**
	 * 组装DSL查询语句
	 * @param scMap
	 * @return
	 */
	public static AbstractQueryBuilder buildQuery(long startTime,long endTime,Map<String,SearchConditionWrapper> scMap){
		//global bool query briage
		BoolQueryBuilder briage = QueryBuilders.boolQuery();
		
		briage.must(rangeMatch(startTime,endTime));
		
		for(String key : scMap.keySet()){
			switch(key){
			case ESQueryRequestParam.KEY_DEVADDR : 
			case ESQueryRequestParam.KEY_SADDR : 
			case ESQueryRequestParam.KEY_SPORT :
			case ESQueryRequestParam.KEY_DADDR :
			case ESQueryRequestParam.KEY_DPORT :
			case ESQueryRequestParam.KEY_ID : 
			case ESQueryRequestParam.KEY_SUSERNAME : 
			case ESQueryRequestParam.KEY_STADDR : 
			case ESQueryRequestParam.KEY_STPORT : 
			case ESQueryRequestParam.KEY_DTADDR : 
			case ESQueryRequestParam.KEY_DTPORT : 
			case ESQueryRequestParam.KEY_SYSTYPE : 
			//second add
			case ESQueryRequestParam.KEY_DEV_VENDOR :
			case ESQueryRequestParam.KEY_DEV_PRODUCT :
			case ESQueryRequestParam.KEY_SESSION :
			case ESQueryRequestParam.KEY_REQUESTURI :
			case ESQueryRequestParam.KEY_CUSTOMS1 :
			case ESQueryRequestParam.KEY_CUSTOMS2 :
			case ESQueryRequestParam.KEY_CUSTOMS3 :
			case ESQueryRequestParam.KEY_CUSTOMS4 :
			case ESQueryRequestParam.KEY_RESOURCE :
			case ESQueryRequestParam.KEY_CUSTOMD1 :
			case ESQueryRequestParam.KEY_CUSTOMD2 :
			case ESQueryRequestParam.KEY_CUSTOMD3 :
			case ESQueryRequestParam.KEY_CUSTOMD4 :
			case ESQueryRequestParam.KEY_SMAC :
			case ESQueryRequestParam.KEY_DMAC :
			case ESQueryRequestParam.KEY_RAWID :
			case ESQueryRequestParam.KEY_COLLECT_TYPE :
			case ESQueryRequestParam.KEY_NAME :
			case ESQueryRequestParam.KEY_OBJECT :
				//单匹配
				if(scMap.get(key) == null) continue;
				briage.must(exactMatch(scMap.get(key)));
				break;
			case ESQueryRequestParam.KEY_PRIORITY : 
			case ESQueryRequestParam.KEY_DEVTYPE : 
			case ESQueryRequestParam.KEY_ACTION : 
			case ESQueryRequestParam.KEY_RESULT : 
			case ESQueryRequestParam.KEY_KEYWORDS:
			case ESQueryRequestParam.KEY_DEVCATEGORY:
				//多选匹配
				if(scMap.get(key) == null) continue;
				briage.must(mutilMatch(scMap.get(key)));
				break;
			}
		}
		return briage;
	}
	
	/**
	 * 时间范围匹配
	 * @param _startTime
	 * @param _endTime
	 * @return
	 */
	public static AbstractQueryBuilder rangeMatch(long startTime,long endTime){
		//时间条件
		RangeQueryBuilder receptTimeCond = QueryBuilders.rangeQuery(ESSearchConstants.ES_RECEPT_TIME).lte(endTime).gte(startTime);
		if(NettyConfig.getInstance().isQueryDebug())
			logger.info("[[time],["+receptTimeCond.toString()+"]]");
		return receptTimeCond;
	}
	
	/**
	 * 完全匹配
	 * @param wrapper
	 * @return
	 */
	public static AbstractQueryBuilder exactMatch(SearchConditionWrapper wrapper){
		BoolQueryBuilder bool = QueryBuilders.boolQuery();
		if(wrapper != null){
			MatchPhraseQueryBuilder eqCond = QueryBuilders
	                .matchPhraseQuery(parmToEs.get(wrapper.getKey()),wrapper.getValue());
//			ExistsQueryBuilder existCond = QueryBuilders.existsQuery(parmToEs.get(wrapper.getKey()));
			if(wrapper.isEq()){
				/*bool.must(eqCond).must(existCond);*/
				bool.must(eqCond);
			}else{
				/*bool.mustNot(eqCond).must(existCond);*/
				bool.mustNot(eqCond);
			}
			if(NettyConfig.getInstance().isQueryDebug())
				logger.info("[[" + parmToEs.get(wrapper.getKey()) + "],["+bool.toString()+"]]");
		}
		return bool;
	}
	
	/**
	 * 多选匹配
	 * @param wrapper
	 * @return
	 */
	public static AbstractQueryBuilder mutilMatch(SearchConditionWrapper wrapper){
		BoolQueryBuilder must = QueryBuilders.boolQuery();
		BoolQueryBuilder should = QueryBuilders.boolQuery();
		if(wrapper != null){
			String[] items = wrapper.getValue().split(",");
//			ExistsQueryBuilder existCond = QueryBuilders.existsQuery(parmToEs.get(wrapper.getKey()));
			for(String e : items){
				should.should(QueryBuilders.matchQuery(parmToEs.get(wrapper.getKey()),e));
			}
			/*must.must(existCond).must(should);*/
			must.must(should);
			if(NettyConfig.getInstance().isQueryDebug())
				logger.info("[[" + parmToEs.get(wrapper.getKey()) + "],["+must.toString()+"]]");
		}
		return must;
	}
	
	private static boolean isEmpty(Object o){
		if(o instanceof String){
			String s = (String)o;
			if(s == null || "".equals(s.trim()))
				return true;
			return false;
		}else{
			if(o == null)
				return true;
			return false;
		}
	}
}
