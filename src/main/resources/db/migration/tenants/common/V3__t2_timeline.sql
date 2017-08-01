--******************** Timeline  TABLES ***********************************************************

CREATE TABLE lessons(
id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
lessonname VARCHAR(150) NOT NULL,
lessonstartdate date NOT NULL,
status VARCHAR(30),
tags VARCHAR(50) NOT NULL,
subjectid bigint NOT NULL,
classroomid bigint NOT NULL,
FOREIGN KEY(classroomid) REFERENCES classrooms(id),
FOREIGN KEY(subjectid) REFERENCES grade_subjects(id)
);

CREATE TABLE assignments(
id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
assignmentname VARCHAR(50) NOT NULL,
dateofassigned date,
assignmentduedate date,
classroomid bigint NOT NULL,
subjectid bigint NOT NULL,
lessonsid bigint,
FOREIGN KEY(lessonsid) REFERENCES lessons(id),
FOREIGN KEY(classroomid) REFERENCES classrooms(id),
FOREIGN KEY(subjectid) REFERENCES grade_subjects(id)
);

CREATE TABLE classroom_worksheets(
id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
worksheetsid bigint NOT NULL,
dateofassigned date,
worksheetduedate date,
classroomid bigint NOT NULL,
subjectid bigint NOT NULL,
lessonsid bigint,
FOREIGN KEY(lessonsid) REFERENCES lessons(id),
FOREIGN KEY(classroomid) REFERENCES classrooms(id),
FOREIGN KEY(worksheetsid) REFERENCES worksheets(id)
);








