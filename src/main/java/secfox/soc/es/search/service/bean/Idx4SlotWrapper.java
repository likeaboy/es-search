package secfox.soc.es.search.service.bean;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import secfox.soc.es.search.request.ESQueryRequestWrapper;

public class Idx4SlotWrapper {

	private Map<String,List<Slot>> map;
	private boolean isAsc = true;
	//hitIndex,hitSlotKey,slotFrom从前端传入，后端不会改变其值
	//命中的index
	private String hitIndex;
	//命中的slot key
	private String hitSlotKey;
	//es切分slot查询起始位置
	private int slotFrom;
	
	private ESQueryRequestWrapper reqWrapper;
	
	//目前查询所要用到的index，会随时更新
	private String currentIndex;
	//目前查询所用到的slot对象，会随时更新
	private Slot currentSlot;
	
	public Idx4SlotWrapper(Map<String,List<Slot>> map,boolean isAsc,String hitIndex,String hitSlotKey,int slotFrom){
		this.map = map;
		this.isAsc = isAsc;
		this.hitIndex = hitIndex;
		this.hitSlotKey = hitSlotKey;
		this.slotFrom = slotFrom;
		//init currentIndex = hitIndex
		setCurrentIndex(hitIndex);
		//如果hitSlotKey存在，则设置为current slot
		if(StringUtils.isNotEmpty(hitSlotKey)){
			currentSlot = getSlotBySlotKey(map.get(hitIndex));
		}
	}
	
	public ESQueryRequestWrapper getReqWrapper() {
		return reqWrapper;
	}
	
	public void setReqWrapper(ESQueryRequestWrapper reqWrapper) {
		this.reqWrapper = reqWrapper;
	}

	public void setCurrentIndex(String currentIndex) {
		this.currentIndex = currentIndex;
	}
	
	public String getCurrentIndex() {
		return currentIndex;
	}

	public Slot getCurrentSlot() {
		return currentSlot;
	}

	public void setCurrentSlot(Slot currentSlot) {
		this.currentSlot = currentSlot;
	}

	/**
	 * 
	 * @param slots
	 * @return
	 */
	private Slot getSlotBySlotKey(List<Slot> slots){
		for(Slot slot : slots){
			if(hitSlotKey.equals(slot.getSlotKey())){
				return slot;
			}
		}
		return null;
	}

	public Map<String, List<Slot>> getMap() {
		return map;
	}

	public boolean isAsc() {
		return isAsc;
	}
	
	public String getIndexes(){
		StringBuilder indexes = new StringBuilder();
		for(String key : map.keySet()){
			indexes.append(key);
			indexes.append(",");
		}
		return indexes.toString();
	}
	
	public Set<String> getIndexSet(){
		return map.keySet();
	}
	
	public boolean isOneDay(){
		return map.keySet().size() == 1 ? true : false;
	}

	public int getSlotFrom() {
		return slotFrom;
	}
	
	/**
	 * 获取 head index
	 * @return
	 */
	public String getHeadIndex(){
		return map.keySet().iterator().next();
	}

	/**
	 * 获取hit index
	 * @return
	 */
	public String getHitIndex() {
		if(hitIndex == null)
			return getHeadIndex();
		return hitIndex;
	}
	
	/**
	 * 获取当前index的下一个index
	 * @param index
	 * @return
	 */
	public String getNextIndex(String index){
		Iterator<String> it = getIndexSet().iterator();
		while(it.hasNext()){
			String e = it.next();
			if(e.equals(index)){
				if(it.hasNext()) return it.next();
				break;
			}
		}
		
		return null;
	}
	/**
	 * 获取当前index是否还有可用slot
	 * @return
	 */
	public boolean hasExtraSlot4CurrIdx(){
		String currIndex = getCurrentIndex();
		Slot slot = getCurrentSlot();
		List<Slot> slots = map.get(currIndex);
		int position = slots.indexOf(slot);
		if(position != -1 && position < slots.size()-1)
			return true;
		return false;
	}
	
	private Slot getNextSlotInCurrIdx(){
		List<Slot> slots = map.get(getCurrentIndex());
		int pos = slots.indexOf(getCurrentSlot());
		if(pos != -1 && pos < slots.size()){
			return pos+1 > (slots.size()-1) ? null : slots.get(pos+1);
		}
		return null;
	}
	
	/**
	 * index链是否还存在next index
	 * @return
	 */
	private boolean isNextIndexExist(){
		Iterator<String> idxIt = getIndexSet().iterator();
		String index = null;
		
		while(idxIt.hasNext()){
			index = idxIt.next();
			if(index.equals(currentIndex)) {
				if(idxIt.hasNext()) return true;
				return false;
			}
		}
		return false;
	}
	
	private String getNextIndex(){
		Iterator<String> idxIt = getIndexSet().iterator();
		String index = null;
		
		while(idxIt.hasNext()){
			index = idxIt.next();
			if(index.equals(currentIndex)) {
				if(idxIt.hasNext()) return idxIt.next();
			}
		}
		
		return null;
	}
	
	/**
	 * 查找下一个slot
	 * @return
	 */
	public Slot getNextSlot(){
		//当前index是否还有slot
		if(hasExtraSlot4CurrIdx()){
			return getNextSlotInCurrIdx();
		}
		
		if(isNextIndexExist()){
			String nextIndex = getNextIndex();
			if(nextIndex != null){
				return map.get(nextIndex).get(0);
			}
		}
		return null;
	}

	public String getHitSlotKey() {
		return hitSlotKey;
	}
	
	public void updateCurrentIndex(String index){
		setCurrentIndex(index);
	}
	public void updateCurrentSlot(Slot slot){
		setCurrentSlot(slot);
	}
	/**
	 * 获取指定index的slots集合
	 * @param index
	 * @return
	 */
	public List<Slot> getSlots(String index){
		return map.get(index);
	}
}
