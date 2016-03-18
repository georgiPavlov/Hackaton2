package accessPoint;

public class AccessPoint {
	private String bssid;
	private String chanel;
	private String enc;
	private String essid;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AccessPoint [bssid=");
		builder.append(bssid);
		builder.append(", chanel=");
		builder.append(chanel);
		builder.append(", enc=");
		builder.append(enc);
		builder.append(", essid=");
		builder.append(essid);
		builder.append("]");
		return builder.toString();
	}

	public String getBssid() {
		return bssid;
	}

	public String getChanel() {
		return chanel;
	}

	public String getEnc() {
		return enc;
	}

	public String getEssid() {
		return essid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bssid == null) ? 0 : bssid.hashCode());
		result = prime * result + ((chanel == null) ? 0 : chanel.hashCode());
		result = prime * result + ((enc == null) ? 0 : enc.hashCode());
		result = prime * result + ((essid == null) ? 0 : essid.hashCode());
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
		AccessPoint other = (AccessPoint) obj;
		if (bssid == null) {
			if (other.bssid != null)
				return false;
		} else if (!bssid.equals(other.bssid))
			return false;
		if (chanel == null) {
			if (other.chanel != null)
				return false;
		} else if (!chanel.equals(other.chanel))
			return false;
		if (enc == null) {
			if (other.enc != null)
				return false;
		} else if (!enc.equals(other.enc))
			return false;
		if (essid == null) {
			if (other.essid != null)
				return false;
		} else if (!essid.equals(other.essid))
			return false;
		return true;
	}

	public AccessPoint(String bssid, String chanel, String enc, String essid) {
		super();
		this.bssid = bssid;
		this.chanel = chanel;
		this.enc = enc;
		this.essid = essid;
	}

}
