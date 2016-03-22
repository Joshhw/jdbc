create table Students (
  sid INTEGER PRIMARY KEY,
  sname VARCHAR(20)
);

create table Courses (
  cid INTEGER PRIMARY KEY,
  cname VARCHAR(20),
  credits INTEGER
);

create table Enrolled (
  sid INTEGER,
  cid INTEGER,
  FOREIGN KEY (sid) REFERENCES Students,
  FOREIGN KEY (cid) REFERENCES Courses
);
