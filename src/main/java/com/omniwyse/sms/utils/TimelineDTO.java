package com.omniwyse.sms.utils;

import java.sql.Date;

public class TimelineDTO {

	private long id;
	private String assignmentname;
	private String worksheetname;
	private String lessonname;
	private String subjectname;
	private String tags;
	private Date lessonstartdate;
	private String status;
	private Date assignmentduedate;
	private Date worksheetduedate;
	
	public Date getAssignmentduedate() {
		return assignmentduedate;
	}

	public void setAssignmentduedate(Date assignmentduedate) {
		this.assignmentduedate = assignmentduedate;
	}

	public Date getWorksheetduedate() {
		return worksheetduedate;
	}

	public void setWorksheetduedate(Date worksheetduedate) {
		this.worksheetduedate = worksheetduedate;
	}

	public Date getLessonstartdate() {
		return lessonstartdate;
	}

	public void setLessonstartdate(Date lessonstartdate) {
		this.lessonstartdate = lessonstartdate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getLessonname() {
		return lessonname;
	}

	public void setLessonname(String lessonname) {
		this.lessonname = lessonname;
	}

	public String getSubjectname() {
		return subjectname;
	}

	public void setSubjectname(String subjectname) {
		this.subjectname = subjectname;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAssignmentname() {
		return assignmentname;
	}

	public void setAssignmentname(String assignmentname) {
		this.assignmentname = assignmentname;
	}

	public String getWorksheetname() {
		return worksheetname;
	}

	public void setWorksheetname(String worksheetname) {
		this.worksheetname = worksheetname;
	}


}
