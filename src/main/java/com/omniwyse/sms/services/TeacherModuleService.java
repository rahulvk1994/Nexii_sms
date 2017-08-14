package com.omniwyse.sms.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dieselpoint.norm.Database;
import com.dieselpoint.norm.Transaction;
import com.omniwyse.sms.db.DatabaseRetrieval;
import com.omniwyse.sms.models.Assignments;
import com.omniwyse.sms.models.ClassRoom;
import com.omniwyse.sms.models.ClassroomWorksheets;
import com.omniwyse.sms.models.Lessons;
import com.omniwyse.sms.models.SubjectTeacherClass;
import com.omniwyse.sms.models.Subjects;
import com.omniwyse.sms.models.Teachers;
import com.omniwyse.sms.models.Worksheets;
import com.omniwyse.sms.utils.AssignmentDTO;
import com.omniwyse.sms.models.TestSyllabus;
import com.omniwyse.sms.utils.ClassRoomDetails;
import com.omniwyse.sms.utils.ClassSectionTransferObject;
import com.omniwyse.sms.utils.TeacherModuleDTO;
import com.omniwyse.sms.utils.TeacherScheduleDTO;
import com.omniwyse.sms.utils.TestTransferObject;
import com.omniwyse.sms.utils.TimelineDTO;
import com.omniwyse.sms.utils.WorkSheetsDTO;

@Service
public class TeacherModuleService {

	@Autowired
	private DatabaseRetrieval retrive;
	@Autowired
	private StudentsService studentService;

	@Autowired
	private WorksheetService workSheetService;

	private Database db;

	public List<TeacherModuleDTO> listAllSubjectsAlongWithClassRooms(long tenantId,
			ClassSectionTransferObject moduleDTO) {

		db = retrive.getDatabase(tenantId);

		List<TeacherModuleDTO> list = db.sql(
				"select classrooms.id, subjects.subjectname, classrooms.gradeid, classrooms.sectionname from subjects "
						+ "JOIN class_subject_teacher ON class_subject_teacher.subjectid = subjects.id JOIN classrooms"
						+ " ON classrooms.id = class_subject_teacher.classid where class_subject_teacher.teacherid = ?",
				moduleDTO.getId()).results(TeacherModuleDTO.class);

		return list;
	}

	public List<ClassSectionTransferObject> getClassRoomOfTeacherAssignedCRT(long tenantId,
			ClassSectionTransferObject moduleDTO) {
		db = retrive.getDatabase(tenantId);

		List<ClassSectionTransferObject> list = db
				.sql("select id, gradeid, sectionname from classrooms where classteacherid = ? ", moduleDTO.getId())
				.results(ClassSectionTransferObject.class);

		return list;
	}

	public Teachers showTeacherProfile(long tenantId, ClassSectionTransferObject moduleDTO) {

		db = retrive.getDatabase(tenantId);
		Teachers teacher = db.where("id = ?", moduleDTO.getId()).results(Teachers.class).get(0);
		return teacher;

	}

	@SuppressWarnings("deprecation")
	public List<TeacherScheduleDTO> getSchedule(long tenantId, ClassSectionTransferObject dataObject, String date) {

		db = retrive.getDatabase(tenantId);

		List<TeacherScheduleDTO> list = new ArrayList<>();
		List<SubjectTeacherClass> classub = db
				.sql("select classid, subjectid from class_subject_teacher where teacherid = ?", dataObject.getId())
				.results(SubjectTeacherClass.class);
		for (SubjectTeacherClass sub : classub) {
			List<TeacherScheduleDTO> sublist = db
					.sql(" select classroom_periods.periodfrom, classroom_periods.periodto,"
							+ "subjects.subjectname,classrooms.gradeid,classrooms.sectionname from classroom_periods "
							+ " join subjects on classroom_periods.subjectid=subjects.id join classrooms on classrooms.id = classroom_periods.classroomid"
							+ " where classroom_periods.classroomid =? and classroom_periods.subjectid = ? "
							+ "and classroom_periods.dateofassigning = ?", sub.getClassid(), sub.getSubjectid(), date)
					.results(TeacherScheduleDTO.class);
			int variable = 0;
			for (TeacherScheduleDTO teacher : sublist) {
				list.add(sublist.get(variable));
				variable++;
			}

		}

		return list;

	}

	public ClassRoomDetails teacherModuleList(long tenantId, long id, String subjectname) {

		db = retrive.getDatabase(tenantId);
		ClassRoomDetails classroom = new ClassRoomDetails();
		classroom.setStudentsOfClassRoom(studentService.getStudentsOfClassRoom(tenantId, id));

		long subjectid = db.where("subjectname=?", subjectname).results(Subjects.class).get(0).getId();
		long gradeid = db.where("id=?", id).results(ClassRoom.class).get(0).getGradeid();
		List<TestTransferObject> listTetss = db
				.sql("select test_type.testtype,test_create.startdate, test_create.enddate from "
						+ "test_create join test_syllabus on test_create.gradeid=?"
						+ " and test_syllabus.subjectid=? and test_create.id=test_syllabus.testid join test_type on"
						+ " test_create.testtypeid=test_type.id ", gradeid, subjectid)
				.results(TestTransferObject.class);

		classroom.setTests(listTetss);

		return classroom;
	}

	public ClassRoomDetails teacherModulestudentsList(long tenantId, long id, String subjectname) {

		db = retrive.getDatabase(tenantId);
		ClassRoomDetails classroom = new ClassRoomDetails();
		classroom.setStudentsOfClassRoom(studentService.getStudentsOfClassRoom(id, tenantId));
		return classroom;
	}

	public List<TestTransferObject> getListOfsubjectTests(long tenantId, long id, String subjectname) {

		db = retrive.getDatabase(tenantId);
		long gradeid = db.where("id=?", id).results(ClassRoom.class).get(0).getGradeid();
		long subjectid = db.where("subjectname=?", subjectname).results(Subjects.class).get(0).getId();
		List<TestTransferObject> testsdetails = db
				.sql("SELECT  test_syllabus.id,test_syllabus.testid,test_type.testtype,test_mode.testmode,test_create.startdate,test_create.enddate,"
						+ "test_syllabus.subjectid,test_create.maxmarks,test_syllabus.syllabus " + "FROM test_create "
						+ "JOIN test_mode on test_create.modeid = test_mode.id "
						+ "JOIN test_type on test_create.testtypeid = test_type.id "
						+ "JOIN test_syllabus on test_syllabus.testid = test_create.id "
						+ "WHERE test_syllabus.subjectid = ? AND test_create.gradeid = ?", subjectid, gradeid)
				.results(TestTransferObject.class);

		return testsdetails;

	}

	public List<TimelineDTO> viewTimeline(long tenantId, TimelineDTO data) {

		db = retrive.getDatabase(tenantId);

		long classroomid = data.getId();
		long subjectid = db.where("subjectname = ?", data.getSubjectname()).results(Subjects.class).get(0).getId();

		List<TimelineDTO> lessons = db
				.sql("select lessons.id,lessons.lessonname,lessons.status,lessons.tags,lessons.lessonstartdate from lessons " + "where classroomid=? and subjectid=?",
						classroomid, subjectid)
				.results(TimelineDTO.class);
		for (TimelineDTO lesson : lessons) {
			List<WorkSheetsDTO> worksheets=db.sql("select worksheets.worksheetname,worksheets.createdby,worksheets.worksheetpath,classroom_worksheets.id,"
					+ "classroom_worksheets.dateofassigned,classroom_worksheets.worksheetduedate as duedate "
					+ "from worksheets "
					+ "join classroom_worksheets on classroom_worksheets.worksheetsid=worksheets.id "
					+ "where classroom_worksheets.classroomid=? and classroom_worksheets.subjectid=? and classroom_worksheets.lessonsid=?",
					classroomid, subjectid, lesson.getId()).results(WorkSheetsDTO.class);
			lesson.setWorksheets(worksheets);
			List<AssignmentDTO> assignments=db.sql("select assignments.id,assignments.assignmentname,assignments.dateofassigned,assignments.assignmentduedate "
					+ "from assignments "
					+ "where assignments.classroomid=? and assignments.subjectid=? and assignments.lessonsid=?",
					classroomid, subjectid, lesson.getId()).results(AssignmentDTO.class);
			
			lesson.setAssignments(assignments);
		}
		return lessons; 
	}

	public int addingLesson(long tenantId, TimelineDTO data) {

		int rowEffected = 0;
		db = retrive.getDatabase(tenantId);
		Lessons lesson = new Lessons();

		Transaction transact = db.startTransaction();
		try {
			lesson.setClassroomid(data.getId());
			lesson.setLessonname(data.getLessonname());
			lesson.setLessonstartdate(data.getLessonstartdate());
			lesson.setSubjectid(
					db.where("subjectname = ?", data.getSubjectname()).results(Subjects.class).get(0).getId());
			lesson.setTags(data.getTags());
			lesson.setStatus(data.getStatus());

			rowEffected = db.transaction(transact).insert(lesson).getRowsAffected();

			transact.commit();
		} catch (Exception e) {

			transact.rollback();
			return rowEffected;
		}
		return rowEffected;
	}

	public List<WorkSheetsDTO> listWorkSheetsbasedOn(long tenantId, WorkSheetsDTO data) {

		long gradeid = db.where("id = ?", data.getId()).results(ClassRoom.class).get(0).getGradeid();
		data.setGradeid(gradeid);
		List<WorkSheetsDTO> list = workSheetService.listingWorksheetsOfTenant(tenantId, data);
		return list;
	}

	public int assignAssignment(long tenantId, AssignmentDTO assigning) {

		db = retrive.getDatabase(tenantId);

		return db.insert(assignments(db, assigning)).getRowsAffected();
	}

	private Assignments assignments(Database db, AssignmentDTO assigning) {

		Assignments assignment = new Assignments();
		long classroomid = assigning.getId();
		String assignmentname = assigning.getAssignmentname();
		Date dateofassigned = assigning.getDateofassigned();
		Date assignmentduedate = assigning.getDuedate();
		assignment.setClassroomid(classroomid);
		assignment.setAssignmentname(assignmentname);
		assignment.setDateofassigned(dateofassigned);
		assignment.setAssignmentduedate(assignmentduedate);
		long subjectid = db.where("subjectname = ?", assigning.getSubjectname()).results(Subjects.class).get(0).getId();
		assignment.setSubjectid(subjectid);
		long lessonid = db.where("lessonname = ?", assigning.getLessonname()).results(Lessons.class).get(0).getId();
		assignment.setLessonsid(lessonid);

		return assignment;
	}

	public int worksheets(long tenantId, WorkSheetsDTO data) {
		db = retrive.getDatabase(tenantId);
		long lessonid = db.where("lessonname = ?", data.getLessonname()).results(Lessons.class).get(0).getId();

		ClassroomWorksheets worksheet = new ClassroomWorksheets();
		long classroomid = data.getId();
		worksheet.setClassroomid(classroomid);
		long worksheetsid = db.where("worksheetname = ?", data.getWorksheetname()).results(Worksheets.class).get(0)
				.getId();
		worksheet.setWorksheetsid(worksheetsid);
		worksheet.setDateofassigned(data.getDateofassigned());
		worksheet.setWorksheetduedate(data.getDuedate());
		long subjectid = db.where("subjectname = ?", data.getSubjectname()).results(Subjects.class).get(0).getId();
		worksheet.setSubjectid(subjectid);
		worksheet.setLessonsid(lessonid);
		List<ClassroomWorksheets> worksheets = db
				.where("classroomid=? and subjectid=? and  lessonsid=? and  worksheetsid=?", classroomid, subjectid,
 						lessonid,worksheetsid)
				.results(ClassroomWorksheets.class);
		if (worksheets.isEmpty()) {
			return db.insert(worksheet).getRowsAffected();
		} else
			return 0;
	}

	public List<Lessons> lessonsList(long tenantId, TimelineDTO data) {

		db = retrive.getDatabase(tenantId);

		long subjectid = db.where("subjectname = ?", data.getSubjectname()).results(Subjects.class).get(0).getId();

		return db.where("classroomid = ? and subjectid = ?", data.getId(), subjectid).results(Lessons.class);
	}

	
	public List<TestTransferObject> getListOfClassroomTests(long tenantId, long id) {

		db = retrive.getDatabase(tenantId);
		long gradeid = db.where("id=?", id).results(ClassRoom.class).get(0).getGradeid();

		List<TestTransferObject> testsdetails = db
				.sql("SELECT  test_syllabus.id,test_syllabus.testid,test_type.testtype,test_mode.testmode,"
						+ "test_create.startdate,test_create.enddate,"
						+ "test_syllabus.subjectid,test_create.maxmarks,test_syllabus.syllabus " + "FROM test_create "
						+ "JOIN test_mode on test_create.modeid = test_mode.id "
						+ "JOIN test_type on test_create.testtypeid = test_type.id "
						+ "JOIN test_syllabus on test_syllabus.testid = test_create.id "
						+ "WHERE test_create.gradeid = ?", gradeid)
				.results(TestTransferObject.class);

		return testsdetails;

	}

	public int deleteAssignedWorksheet(ClassroomWorksheets data, long tenantId) {
		db = retrive.getDatabase(tenantId);
		return db.delete(data).getRowsAffected();
	}

	public int deleteAssignedAssignment(Assignments data, long tenantId) {
		db = retrive.getDatabase(tenantId);
		return db.delete(data).getRowsAffected();

	}

	
}
