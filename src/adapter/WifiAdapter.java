package adapter;

public class WifiAdapter implements Adapter{
	private String mInterface;
	private String mChipset;
	private String mDriver;
	
	public WifiAdapter(String outInterface, String outChipset, String outDriver){
		this.mInterface = outInterface;
		this.mChipset = outChipset;
		this.mDriver = outDriver;
	}
	
	public String getInterface() {
		return mInterface;
	}

	public String getChipset() {
		return mChipset;
	}

	public String getDriver() {
		return mDriver;
	}
    
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AirMonNG [mInterface=");
		builder.append(mInterface);
		builder.append(", mChipset=");
		builder.append(mChipset);
		builder.append(", mDriver=");
		builder.append(mDriver);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mChipset == null) ? 0 : mChipset.hashCode());
		result = prime * result + ((mDriver == null) ? 0 : mDriver.hashCode());
		result = prime * result + ((mInterface == null) ? 0 : mInterface.hashCode());
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
		WifiAdapter other = (WifiAdapter) obj;
		if (mChipset == null) {
			if (other.mChipset != null)
				return false;
		} else if (!mChipset.equals(other.mChipset))
			return false;
		if (mDriver == null) {
			if (other.mDriver != null)
				return false;
		} else if (!mDriver.equals(other.mDriver))
			return false;
		if (mInterface == null) {
			if (other.mInterface != null)
				return false;
		} else if (!mInterface.equals(other.mInterface))
			return false;
		return true;
	}
}
