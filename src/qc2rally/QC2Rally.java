package qc2rally;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mercury.qualitycenter.otaclient.ClassFactory;
import com.mercury.qualitycenter.otaclient.IAttachment;
import com.mercury.qualitycenter.otaclient.IAttachmentFactory;
import com.mercury.qualitycenter.otaclient.IBug;
import com.mercury.qualitycenter.otaclient.IBugFactory;
import com.mercury.qualitycenter.otaclient.IList;
import com.mercury.qualitycenter.otaclient.ITDConnection;
import com.mercury.qualitycenter.otaclient.ITDFilter2;
import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.CreateRequest;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.response.CreateResponse;
import com.rallydev.rest.response.QueryResponse;
import com.rallydev.rest.util.QueryFilter;
import com.rallydev.rest.util.Ref;

import com4j.Com4jObject;
import com4j.Holder;

/**
 * 
 */

/**
 * @author KaushalKumar
 *
 */
public class QC2Rally {

	private ITDConnection connection = ClassFactory.createTDConnection();
	private ITDFilter2 filter;

	private RallyRestApi restApi = null;
	private String rallyReleaseRef = null;

	private Map<String, String> severityMap;
	private Map<String, String> priorityMap;
	private Map<String, String> userMap;
	private Map<String, String> userRefMap;
	private Map<String, String> defectStateMap;
	private Map<String, String> resolutionMap;
	private Map<String, String> scheduleStateMap;
	private Map<String, String> phaseMap;

	static int counter = 0;
	
	Holder<String> holderPath = new Holder<String>("C:/qcBugs");

	/**
	 * @param args
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static void main(String[] args) throws URISyntaxException, IOException {
		QC2Rally qc2Rally = new QC2Rally();

		// connect to QC
		qc2Rally.connectQC();

		// connect to rally
		qc2Rally.connectRally();

		// init data
		qc2Rally.initMappingData();
		qc2Rally.initRallyData();

		// getAllBugs
		IList bugList = qc2Rally.getBugList();
		
		System.out.println("Total number of defects:" + bugList.count());
		
		// for each bug
		for (Com4jObject com4jObject : bugList) {
			QCBugData qcBugData = qc2Rally.getQCBugData(com4jObject);
			if (qcBugData != null) {
				// map each qcBugData to rallyDefectData
				RallyDefectData rallyDefectData = new RallyDefectData();
				qc2Rally.mapQCBug2RallyDefect(qcBugData, rallyDefectData);

				// create Rally defect
				qc2Rally.createDefectInRally(rallyDefectData);

			}
		}

	}

	private void connectRally() throws URISyntaxException {
		restApi = new RallyRestApi(new URI("https://rally1.rallydev.com"), "<<<API_KEY>>>");
		System.out.println(restApi);
	}

	private void connectQC() {
		connection.initConnectionEx("<<<QC_CONN_URL>>>");
		connection.connectProjectEx("<<<DOMAIN>>>", "<<<PROJECT>>>", "<<<USERNAME>>>", "<<<PASSWORD>>>");
		
		IBugFactory bugFactory = (IBugFactory) connection.bugFactory().queryInterface(IBugFactory.class);
		filter = bugFactory.filter().queryInterface(ITDFilter2.class);


		System.out.println(connection);

	}

	private IList getBugList() {
		filter.clear();
		String queryString = "=CUSTOMERNAME";
		filter.filter("BG_USER_33", queryString);
		IList bugList = filter.newList();
		return bugList;
	}

	private QCBugData getQCBugData(Com4jObject com4jObject) throws IOException {
		QCBugData qcBugData = null;

		IBug bug = com4jObject.queryInterface(IBug.class);
		if (bug != null) {
			qcBugData = new QCBugData();

			System.out.println(bug.field("BG_BUG_ID").toString());

			qcBugData.setId(bug.field("BG_BUG_ID").toString());

			if (bug.field("BG_DEV_COMMENTS") != null) {
				String htmlComments = bug.field("BG_DEV_COMMENTS").toString();
				Document doc = Jsoup.parse(htmlComments);
				String allComment = doc.select("body").first().text();
				String[] splitComment = allComment.split("________________________________________");
				List<String> comments = new ArrayList<String>();
				for (String eachComment : splitComment) {
					comments.add(eachComment);
				}
				qcBugData.setComments(comments);
			}

			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			String dateEntered = sdf.format((Date) (bug.field("BG_DETECTION_DATE")));
			qcBugData.setDateEntered(dateEntered);

			if (bug.field("BG_USER_29") != null) {
				qcBugData.setCaseID(bug.field("BG_USER_29").toString());
			}

			qcBugData.setSummary(bug.field("BG_SUMMARY").toString());
			qcBugData.setStatus(bug.field("BG_STATUS").toString());
			qcBugData.setSeverity(bug.field("BG_SEVERITY").toString());
			qcBugData.setPriority(bug.field("BG_PRIORITY").toString());

			if (bug.field("BG_USER_35") != null) {
				qcBugData.setPhaseDetected(bug.field("BG_USER_35").toString());
			}

			qcBugData.setAssignedTo(bug.field("BG_RESPONSIBLE").toString());
			qcBugData.setSubmittedBy(bug.field("BG_DETECTED_BY").toString());

			String htmlDesc = bug.field("BG_DESCRIPTION").toString();
			Document docDesc = Jsoup.parse(htmlDesc);
			List<TextNode> descTextNodes = docDesc.select("body").first().textNodes();
			List<String> desc = new ArrayList<String>();
			for (TextNode textNode : descTextNodes) {
				desc.add(textNode.text());
			}
			qcBugData.setDescriptionSteps(desc);

			if (bug.field("BG_USER_13") != null) {
				qcBugData.setResolution(bug.field("BG_USER_13").toString());
			}

			List<Attachment> attachments = getAttachment(bug, qcBugData);
			qcBugData.setAttachments(attachments);
		}

		return qcBugData;
	}

	private List<Attachment> getAttachment(IBug bug, QCBugData qcBugData) throws IOException {
		List<Attachment> attachmentList = null;
		boolean hasAttachment = bug.hasAttachment();
		if (hasAttachment) {
			attachmentList = new ArrayList<Attachment>();

			IAttachmentFactory attachfac = bug.attachments().queryInterface(IAttachmentFactory.class);
			IList attachments = attachfac.newList("");

			for (Com4jObject com4jObject : attachments) {
				Attachment attachment = new Attachment();
				IAttachment attach = com4jObject.queryInterface(IAttachment.class);
				if (attach.type() == 1) {
					//file
					attachment.setAttachmentName(attach.name(1));
					attach.data();
					attach.load(true, holderPath);
					String fileName = attach.fileName();
					
					File attachFile = new File(fileName);
					byte[] bFile = new byte[(int) attachFile.length()];
					FileInputStream fileInputStream = new FileInputStream(attachFile);
				    fileInputStream.read(bFile);
				    fileInputStream.close();
					attachment.setAttachmentData(bFile);
				} else {
					//ref internet
					System.out.println("ERROR for ATTACHMENT ************************");
				}
				attachmentList.add(attachment);
			}
		}
		return attachmentList;
	}

	private void mapQCBug2RallyDefect(QCBugData qcBugData, RallyDefectData rallyDefectData) throws IOException {
		rallyDefectData.setNotes(getNotesFromqcBugData(qcBugData));
		rallyDefectData.setFoundIn(qcBugData.getCaseID());
		rallyDefectData.setName(qcBugData.getSummary());
		rallyDefectData.setState(defectStateMap.get(qcBugData.getStatus()));
		rallyDefectData.setScheduledState(scheduleStateMap.get(qcBugData.getStatus()));
		rallyDefectData.setSeverity(severityMap.get(qcBugData.getSeverity()));
		rallyDefectData.setPriority(priorityMap.get(qcBugData.getPriority()));
		rallyDefectData.setEnvironment(phaseMap.get(qcBugData.getPhaseDetected()));
		rallyDefectData.setOwner(getUserRef(qcBugData.getAssignedTo()));
		rallyDefectData.setSubmittedBy(getUserRef(qcBugData.getSubmittedBy()));
		rallyDefectData.setDescription(getDescriptionFromqcBugData(qcBugData));
		rallyDefectData.setResolution(resolutionMap.get(qcBugData.getResolution()));
		rallyDefectData.setRelease(rallyReleaseRef);
		rallyDefectData.setAttachments(qcBugData.getAttachments());
	}

	private String getNotesFromqcBugData(QCBugData qcBugData) {
		StringBuffer notesData = new StringBuffer();
		notesData.append("QC ID: " + qcBugData.getId() + "<br/>");
		notesData.append("Date Entered: " + qcBugData.getDateEntered() + "<br/>");
		List<String> comments = qcBugData.getComments();
		if(comments != null) {
			for (String comment : comments) {
				notesData.append(comment + "<br/>");
			}
		}

		return notesData.toString();
	}

	private String getDescriptionFromqcBugData(QCBugData qcBugData) {
		StringBuffer descData = new StringBuffer();
		List<String> descList = qcBugData.getDescriptionSteps();
		for (String descLine : descList) {
			descData.append(descLine + "<br/>");
		}

		return descData.toString();
	}

	private void initMappingData() {
		severityMap = new HashMap<String, String>();
		severityMap.put("1-Critical", "Crash/Data Loss");
		severityMap.put("2-Severe", "Major Problem");
		severityMap.put("3-Average", "Minor Problem");
		severityMap.put("4-Minor", "Cosmetic");

		priorityMap = new HashMap<String, String>();
		priorityMap.put("1-Resolve Immediately", "Resolve Immediately");
		priorityMap.put("2-Give High Attention", "High Attention");
		priorityMap.put("3-Normal Queue", "Normal");
		priorityMap.put("", "Normal");
		priorityMap.put("4-Low Priority", "Low");

		userMap = new HashMap<String, String>();
		userMap.put("<<<USER1_IN_MANTIS>>>", "<<<USER1_IN_RALLY>>>");
		userMap.put("<<<USER2_IN_MANTIS>>>", "<<<USER2_IN_RALLY>>>");
		userMap.put("<<<USER3_IN_MANTIS>>>", "<<<USER3_IN_RALLY>>>");
		userMap.put("<<<USER4_IN_MANTIS>>>", "<<<USER4_IN_RALLY>>>");

		userRefMap = new HashMap<String, String>();
		
		defectStateMap = new HashMap<String, String>();
		defectStateMap.put("", "Submitted");
		defectStateMap.put("New", "Submitted");
		defectStateMap.put("Open", "Open");
		defectStateMap.put("Awaiting Information", "Open");
		defectStateMap.put("Postponed", "Open");
		defectStateMap.put("Returned", "Open");
		defectStateMap.put("Reopen", "Open");
		defectStateMap.put("Fixed", "Fixed");
		defectStateMap.put("Ready to Test", "Fixed");
		defectStateMap.put("Cancelled", "Closed");
		defectStateMap.put("Closed", "Closed");

		resolutionMap = new HashMap<String, String>();
		resolutionMap.put("Configuration", "Configuration Change");
		resolutionMap.put("Code Change", "Code Change");
		resolutionMap.put("", "Code Change");
		resolutionMap.put("Documentation", "Not a Defect");
		resolutionMap.put("Environment", "Configuration Change");

		scheduleStateMap = new HashMap<String, String>();
		scheduleStateMap.put("", "Defined");
		scheduleStateMap.put("New", "Defined");
		scheduleStateMap.put("Open", "In-Progress");
		scheduleStateMap.put("Awaiting Information", "In-Progress");
		scheduleStateMap.put("Postponed", "In-Progress");
		scheduleStateMap.put("Returned", "In-Progress");
		scheduleStateMap.put("Reopen", "In-Progress");
		scheduleStateMap.put("Fixed", "In-Progress");
		scheduleStateMap.put("Ready to Test", "In-Progress");
		scheduleStateMap.put("Cancelled", "Accepted");
		scheduleStateMap.put("Closed", "Accepted");

		phaseMap = new HashMap<String, String>();
		phaseMap.put("Dev Testing", "Development");
		phaseMap.put("Development", "Development");
		phaseMap.put("QA Testing", "Development");
		phaseMap.put("Regression Testing", "Development");
		phaseMap.put("SIT", "Test");

	}

	private String getUserRef(String userInput) throws IOException {
		if (userRefMap.get(userInput) == null) {
			// Read User
			QueryRequest userRequest = new QueryRequest("User");
			userRequest.setQueryFilter(new QueryFilter("DisplayName", "=", userMap.get(userInput)));
			QueryResponse userQueryResponse = restApi.query(userRequest);
			JsonArray userQueryResults = userQueryResponse.getResults();
			JsonElement userQueryElement = userQueryResults.get(0);
			JsonObject userQueryObject = userQueryElement.getAsJsonObject();
			String userRef = userQueryObject.get("_ref").getAsString();

			userRefMap.put(userInput, userRef);
		}

		return userRefMap.get(userInput);
	}

	private void initRallyData() throws IOException {
		// release ref
		QueryRequest releaseRequest = new QueryRequest("Release");
		releaseRequest.setQueryFilter(new QueryFilter("Name", "=", "<<<RELEASE_NAME_IN_RALLY>>>"));
		QueryResponse releaseQueryResponse = restApi.query(releaseRequest);
		JsonArray releaseQueryResults = releaseQueryResponse.getResults();
		JsonElement releaseQueryElement = releaseQueryResults.get(0);
		JsonObject releaseQueryObject = releaseQueryElement.getAsJsonObject();
		rallyReleaseRef = releaseQueryObject.get("_ref").getAsString();
	}

	private void createDefectInRally(RallyDefectData rallyDefectData) throws IOException {

		JsonObject newDefect = new JsonObject();

		newDefect.addProperty("Name", rallyDefectData.getName());
		newDefect.addProperty("Description", rallyDefectData.getDescription());
		newDefect.addProperty("Severity", rallyDefectData.getSeverity());
		newDefect.addProperty("Priority", rallyDefectData.getPriority());
		newDefect.addProperty("Environment", rallyDefectData.getEnvironment());
		newDefect.addProperty("FoundInBuild", rallyDefectData.getFoundIn());
		newDefect.addProperty("State", rallyDefectData.getState());
		newDefect.addProperty("ScheduleState", rallyDefectData.getScheduledState());
		newDefect.addProperty("Resolution", rallyDefectData.getResolution());
		newDefect.addProperty("Notes", rallyDefectData.getNotes());
		newDefect.addProperty("Release", rallyDefectData.getRelease());
		newDefect.addProperty("SubmittedBy", rallyDefectData.getSubmittedBy());
		newDefect.addProperty("Owner", rallyDefectData.getOwner());

		CreateRequest createRequest = new CreateRequest("defect", newDefect);

		CreateResponse createResponse = restApi.create(createRequest);

		if (createResponse.wasSuccessful()) {
			System.out.println(String.format("Created %s", createResponse.getObject().get("_ref").getAsString()));
			// Read defect
			String ref = Ref.getRelativeRef(createResponse.getObject().get("_ref").getAsString());
			System.out.println(String.format("\nReading Defect %s...", ref));

			String imageBase64String;
			int attachmentSize;
			String mimeType;
			List<Attachment> attachmentDataList = rallyDefectData.getAttachments();
			if (attachmentDataList != null && attachmentDataList.size() > 0) {
				for (Attachment attachment : attachmentDataList) {
					try {
						imageBase64String = Base64.encodeBase64String(attachment.getAttachmentData());
						attachmentSize = attachment.getAttachmentData().length;

						MagicMatch match = Magic.getMagicMatch(attachment.getAttachmentData());
						mimeType = match.getMimeType();

						// First create AttachmentContent
						JsonObject myAttachmentContent = new JsonObject();
						myAttachmentContent.addProperty("Content", imageBase64String);
						CreateRequest attachmentContentCreateRequest = new CreateRequest("AttachmentContent", myAttachmentContent);
						CreateResponse attachmentContentResponse = restApi.create(attachmentContentCreateRequest);
						String myAttachmentContentRef = attachmentContentResponse.getObject().get("_ref").getAsString();
						System.out.println("Attachment Content created: " + myAttachmentContentRef);

						// Now create the Attachment itself
						JsonObject myAttachment = new JsonObject();
						myAttachment.addProperty("Artifact", ref);
						myAttachment.addProperty("Content", myAttachmentContentRef);
						myAttachment.addProperty("Name", attachment.getAttachmentName());
						myAttachment.addProperty("Description", attachment.getAttachmentName());
						myAttachment.addProperty("ContentType", mimeType);
						myAttachment.addProperty("Size", attachmentSize);

						CreateRequest attachmentCreateRequest = new CreateRequest("Attachment", myAttachment);
						CreateResponse attachmentResponse = restApi.create(attachmentCreateRequest);
						String myAttachmentRef = attachmentResponse.getObject().get("_ref").getAsString();
						System.out.println("Attachment  created: " + myAttachmentRef);

						if (attachmentResponse.wasSuccessful()) {
							System.out.println("Successfully created Attachment");
						} else {
							String[] attachmentContentErrors;
							attachmentContentErrors = attachmentResponse.getErrors();
							System.out.println("Error occurred creating Attachment: ");
							for (int j = 0; j < attachmentContentErrors.length; j++) {
								System.out.println(attachmentContentErrors[j]);
							}
						}
					} catch (Exception e) {
						System.out.println("Exception occurred while attempting to create Content and/or Attachment: ");
						e.printStackTrace();
					}
				}
			}
		} else {
			String[] createErrors;
			createErrors = createResponse.getErrors();
			System.out.println("Error occurred creating a defect: ");
			for (int j = 0; j < createErrors.length; j++) {
				System.out.println(createErrors[j]);
			}
		}
	}
}
