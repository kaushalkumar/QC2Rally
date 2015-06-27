/**
 * 
 */
package qc2rally;

import java.util.List;

/**
 * @author KaushalKumar
 *
 */
public class QCBugData {
	
	private String id;
	private List<String> comments;
	private String dateEntered;
	private String caseID;
	private String summary;
	private String status;
	private String severity;
	private String priority;
	private String phaseDetected;
	private String assignedTo;
	private String submittedBy;
	private List<String> descriptionSteps;
	private String resolution;
	private List<Attachment> attachments;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the comments
	 */
	public List<String> getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(List<String> comments) {
		this.comments = comments;
	}
	/**
	 * @return the dateEntered
	 */
	public String getDateEntered() {
		return dateEntered;
	}
	/**
	 * @param dateEntered the dateEntered to set
	 */
	public void setDateEntered(String dateEntered) {
		this.dateEntered = dateEntered;
	}
	/**
	 * @return the caseID
	 */
	public String getCaseID() {
		return caseID;
	}
	/**
	 * @param caseID the caseID to set
	 */
	public void setCaseID(String caseID) {
		this.caseID = caseID;
	}
	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}
	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the severity
	 */
	public String getSeverity() {
		return severity;
	}
	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	/**
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}
	/**
	 * @return the phaseDetected
	 */
	public String getPhaseDetected() {
		return phaseDetected;
	}
	/**
	 * @param phaseDetected the phaseDetected to set
	 */
	public void setPhaseDetected(String phaseDetected) {
		this.phaseDetected = phaseDetected;
	}
	/**
	 * @return the assignedTo
	 */
	public String getAssignedTo() {
		return assignedTo;
	}
	/**
	 * @param assignedTo the assignedTo to set
	 */
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	/**
	 * @return the submittedBy
	 */
	public String getSubmittedBy() {
		return submittedBy;
	}
	/**
	 * @param submittedBy the submittedBy to set
	 */
	public void setSubmittedBy(String submittedBy) {
		this.submittedBy = submittedBy;
	}
	/**
	 * @return the descriptionSteps
	 */
	public List<String> getDescriptionSteps() {
		return descriptionSteps;
	}
	/**
	 * @param descriptionSteps the descriptionSteps to set
	 */
	public void setDescriptionSteps(List<String> descriptionSteps) {
		this.descriptionSteps = descriptionSteps;
	}
	/**
	 * @return the resolution
	 */
	public String getResolution() {
		return resolution;
	}
	/**
	 * @param resolution the resolution to set
	 */
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	/**
	 * @return the attachments
	 */
	public List<Attachment> getAttachments() {
		return attachments;
	}
	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	
}
