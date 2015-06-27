/**
 * 
 */
package qc2rally;

/**
 * @author KaushalKumar
 *
 */
public class Attachment {
	private byte[] attachmentData;
	private String attachmentName;

	/**
	 * @return the attachmentData
	 */
	public byte[] getAttachmentData() {
		return attachmentData;
	}

	/**
	 * @param attachmentData
	 *            the attachmentData to set
	 */
	public void setAttachmentData(byte[] attachmentData) {
		this.attachmentData = attachmentData;
	}

	/**
	 * @return the attachmentName
	 */
	public String getAttachmentName() {
		return attachmentName;
	}

	/**
	 * @param attachmentName
	 *            the attachmentName to set
	 */
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
}
