package secfox.soc.es.search.service.bean;

public class SearchConditionWrapper {

	private String key;
	private String value;
	private String esFieldKey;
	private boolean isEq;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isEq() {
		return isEq;
	}
	public void setEq(boolean isEq) {
		this.isEq = isEq;
	}
	public String getEsFieldKey() {
		return esFieldKey;
	}
	public void setEsFieldKey(String esFieldKey) {
		this.esFieldKey = esFieldKey;
	}
	
}
