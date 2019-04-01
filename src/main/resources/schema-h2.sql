DROP TABLE employee;

CREATE TABLE employee (
  ID NUMBER(10,0) NOT NULL,
  fname VARCHAR2(255) NOT NULL DEFAULT '',
  lname VARCHAR2(255) NOT NULL DEFAULT '',
  title VARCHAR2(20) NOT NULL DEFAULT '',
  phone_number VARCHAR2(20) DEFAULT '',
  email VARCHAR2(255) NOT NULL DEFAULT '',
  hire_date VARCHAR2(20)  DEFAULT '',
  manager_id number(10,0),
  department_number number(10,0),
  PRIMARY KEY (ID));

DROP TABLE department;

CREATE TABLE department (
  department_Id NUMBER(10,0) NOT NULL AUTO_INCREMENT,
  name VARCHAR2(255) not null default '',
  manager_id number (10,0),
  PRIMARY KEY (department_Id)
);

DROP TABLE department_employee;

CREATE TABLE department_employee (
  department_Id NUMBER(10,0) NOT NULL,
  employee_id NUMBER(10,0) not null
);


DROP SEQUENCE hibernate_sequence;

CREATE SEQUENCE hibernate_sequence;