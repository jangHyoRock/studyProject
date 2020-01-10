package dhi.optimizer.model.json;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * AlarmAndEvent main json model of screen.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlarmAndEvent {
	
	private String datetime;
	
	private String type;

	private String item;
	
	private String status;
	
	private String description;
	
	@JsonProperty("alarm_event")
	private ArrayList<AlarmAndEvent> alarmAndEventList;
	
	public AlarmAndEvent() {}
	
	public AlarmAndEvent(String datetime, String type, String item, String status, String description)
	{
		this.datetime = datetime;
		this.type = type;
		this.item = item;
		this.status = status;
		this.description = description;
	}
	
	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public ArrayList<AlarmAndEvent> getAlarmAndEventList() {
		return alarmAndEventList;
	}

	public void setAlarmAndEventList(ArrayList<AlarmAndEvent> alarmAndEventList) {
		this.alarmAndEventList = alarmAndEventList;
	}
}

