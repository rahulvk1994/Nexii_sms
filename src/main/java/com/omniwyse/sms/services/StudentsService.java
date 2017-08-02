package com.omniwyse.sms.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dieselpoint.norm.Database;
import com.omniwyse.sms.models.Grades;
import com.omniwyse.sms.models.House;
import com.omniwyse.sms.models.StudentClassroom;
import com.omniwyse.sms.models.Students;
import com.omniwyse.sms.utils.ClassRoomStudents;
import com.omniwyse.sms.utils.StudentTransferObject;

@Service
public class StudentsService {

	@Autowired
    private com.omniwyse.sms.db.DatabaseRetrieval retrive;

	private Database db;

	private long gradeid;
	private String gradename;
	private String syllabustype;
	private String name;
	private Students students;

	public int addStudent(StudentTransferObject addStudent,long tenantId) {
		students=new Students();
		gradename = addStudent.getGradename();
		syllabustype = addStudent.getSyllabustype();
		students.setAddress(addStudent.getAddress());
		students.setAdmissionnumber(addStudent.getAdmissionnumber());
		students.setContactnumber(addStudent.getContactnumber());
		students.setDateofbirth(convertJavaDateToSqlDate(addStudent.getDateofbirth()));
		students.setDateofjoining(convertJavaDateToSqlDate(addStudent.getDateofjoining()));
		students.setEmailid(addStudent.getEmailid());
		students.setFathername(addStudent.getFathername());
		students.setGender(addStudent.getGender());
		students.setMothername(addStudent.getMothername());
		students.setName(addStudent.getName());
		db = retrive.getDatabase(tenantId);
		gradeid = db.where("gradename=? and syllabustype=?", gradename, syllabustype).results(Grades.class).get(0)
				.getId();
		List<House> house=db.where("housename=?", addStudent.getHousename()).results(House.class);
		if(!house.isEmpty())
		{
			students.setHouseid(house.get(0).getId());
		}
		students.setGradeid(gradeid);
		name = addStudent.getName();

		if (isValidStudent(name)) {
			int a = db.insert(students).getRowsAffected();
			return a;
		} else
			return 0;
	}
	public java.sql.Date convertJavaDateToSqlDate(Date date) {
		return new java.sql.Date(date.getTime());
	}


	private boolean isValidStudent(String name) {
		List<Students> students = db.where("name=?", name).results(Students.class);
		if (students.isEmpty())
			return true;
		else
			return false;
	}

	public int updateStudent(Students updateStudent,long tenantId) {
		db = retrive.getDatabase(tenantId);
		return db.update(updateStudent).getRowsAffected();
	}

	public int addStudentToClassroom(String admissionnumber, long classid,long tenantId) {
		StudentClassroom studentclassroom = new StudentClassroom();
		db = retrive.getDatabase(tenantId);
		long studentid = db.where("admissionnumber=?", admissionnumber).results(Students.class).get(0).getId();
		studentclassroom.setClassid(classid);
		studentclassroom.setStudentid(studentid);
		return db.insert(studentclassroom).getRowsAffected();
	}

	public List<ClassRoomStudents> getStudentsOfClassRoom(long classid,long tenantId) {
		db = retrive.getDatabase(tenantId);
		return db
				.sql("select students.name,students.id,students.fathername,students.admissionnumber from students inner join classroom_students on classroom_students.classid=? and classroom_students.studentid=students.id",
						classid)
				.results(ClassRoomStudents.class);

	}
	public List<Students> getStudentsOfGrade(long gradeid,long tenantId)
	{
		db = retrive.getDatabase(tenantId);
		return db.where("gradeid=?",gradeid).results(Students.class);
	}

}
