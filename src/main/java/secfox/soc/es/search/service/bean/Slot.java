package secfox.soc.es.search.service.bean;


public class Slot extends TimePair{

	private String slotKey;
	
	public Slot(){}
	
	public Slot(long startTime,long endTime){
		super(startTime,endTime);
		StringBuilder builder = new StringBuilder("slot_");
		builder.append(startTime);
		this.slotKey = builder.toString();
	}

	public String getSlotKey() {
		return slotKey;
	}

	/*public void setSlotKey(String slotKey) {
		this.slotKey = slotKey;
	}*/
	
	private void setSlotKey(String slotKey){
		this.slotKey = slotKey;
	}
	
	public static Slot slotKeyWrapper(String slotKey){
		Slot slot = new Slot(0L,0L);
		slot.setSlotKey(slotKey);
		return slot;
	}
	
	@Override
	public void setStartTime(long startTime) {
		super.setStartTime(startTime);
		if(slotKey == null){
			StringBuilder builder = new StringBuilder("slot_");
			builder.append(startTime);
			this.slotKey = builder.toString();
		}
	}
	
}
