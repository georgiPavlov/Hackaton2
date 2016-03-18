package comunication;

import java.util.List;

import accessPoint.AccessPoint;
import adapter.Adapter;

public class StatePakage {
      public enum State { RUNING,FINISHED,BLOKED,INIT,END,START,MONITOR,ACCESS_POINT,CRACK};
      private String pin;
      private String psk;
      private State state;
      private Adapter monitor;
      private List<AccessPoint> aps;
      public Adapter getMonitor() {
		return monitor;
	}
	public List<AccessPoint> getAps() {
		return aps;
	}
	
	public StatePakage(State state ,String pin, String psk , Adapter monitor, List<AccessPoint> aps) {
		this.pin = pin;
		this.psk = psk;
		this.state = state;
		this.monitor = monitor;
		this.aps = aps;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aps == null) ? 0 : aps.hashCode());
		result = prime * result + ((monitor == null) ? 0 : monitor.hashCode());
		result = prime * result + ((pin == null) ? 0 : pin.hashCode());
		result = prime * result + ((psk == null) ? 0 : psk.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StatePakage other = (StatePakage) obj;
		if (aps == null) {
			if (other.aps != null)
				return false;
		} else if (!aps.equals(other.aps))
			return false;
		if (monitor == null) {
			if (other.monitor != null)
				return false;
		} else if (!monitor.equals(other.monitor))
			return false;
		if (pin == null) {
			if (other.pin != null)
				return false;
		} else if (!pin.equals(other.pin))
			return false;
		if (psk == null) {
			if (other.psk != null)
				return false;
		} else if (!psk.equals(other.psk))
			return false;
		if (state != other.state)
			return false;
		return true;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StatePakage [pin=");
		builder.append(pin);
		builder.append(", psk=");
		builder.append(psk);
		builder.append(", state=");
		builder.append(state);
		builder.append(", monitor=");
		builder.append(monitor);
		builder.append(", aps=");
		builder.append(aps);
		builder.append("]");
		return builder.toString();
	}
	public String getPin() {
		return pin;
	}
	public String getPsk() {
		return psk;
	}
	public State getState() {
		return state;
	}
	
}
