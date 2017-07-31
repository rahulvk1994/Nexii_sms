package com.omniwyse.sms.utils;

import java.sql.Date;

public class AssignmentDTO {

	private String lessonname;
	private String assignmentname;
	private Date dateofassigned;
	private Date duedate;
	private String subjectname;
	private long id;
	private long assignedid;
	
	public long getAssignedid() {
		return assignedid;
	}
	public void setAssignedid(long assignedid) {
		this.assignedid = assignedid;
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
	public Date getDateofassigned() {
		return dateofassigned;
	}
	public void setDateofassigned(Date dateofassigned) {
		this.dateofassigned = dateofassigned;
	}
	public Date getDuedate() {
		return duedate;
	}
	public void setDuedate(Date duedate) {
		this.duedate = duedate;
	}
	
	
}
