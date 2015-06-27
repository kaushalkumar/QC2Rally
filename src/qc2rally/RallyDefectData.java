/**
 * 
 */
package qc2rally;

import java.util.List;

/**
 * @author KaushalKumar
 *
 */
public class RallyDefectData {
	
	private String name;
	private String description;
	private String foundIn;
	private String severity;
	private String priority;
	private String state;
	private String scheduledState;
	private String resolution;
	private String notes;
	private String release;
	private String submittedBy;
	private String owner;
	private String environment;
	private List<Attachment> attachments;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the foundIn
	 */
	public String getFoundIn() {
		return foundIn;
	}
	/**
	 * @param foundIn the foundIn to set
	 */
	public void setFoundIn(String foundIn) {
		this.foundIn = foundIn;
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
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the scheduledState
	 */
	public String getScheduledState() {
		return scheduledState;
	}
	/**
	 * @param scheduledState the scheduledState to set
	 */
	public void setScheduledState(String scheduledState) {
		this.scheduledState = scheduledState;
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
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}
	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
	/**
	 * @return the release
	 */
	public String getRelease() {
		return release;
	}
	/**
	 * @param release the release to set
	 */
	public void setRelease(String release) {
		this.release = release;
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
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	/**
	 * @return the environment
	 */
	public String getEnvironment() {
		return environment;
	}
	/**
	 * @param environment the environment to set
	 */
	public void setEnvironment(String environment) {
		this.environment = environment;
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

