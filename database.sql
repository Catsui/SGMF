CREATE DATABASE sgmf;

USE sgmf;

CREATE TABLE PLANO (
	Id int(11) NOT NULL AUTO_INCREMENT,
	Nome varchar(60) NOT NULL,
	Mensalidade decimal(10,2) NOT NULL,
    PRIMARY KEY (Id)
);

INSERT INTO PLANO (Nome, Mensalidade) VALUES ('Plano 1: Todos os dias',92.0);

CREATE TABLE PLANOCRIANCA (
	Id int(11) NOT NULL AUTO_INCREMENT,
	Nome varchar(60) NOT NULL,
	Mensalidade decimal(10,2) NOT NULL,
    PRIMARY KEY (Id)
);

INSERT INTO PLANOCRIANCA (Nome, Mensalidade) VALUES ('Plano 1: Todos os dias',92.0);

CREATE TABLE ALUNO (
  Id int(11) NOT NULL AUTO_INCREMENT,
  Ativo boolean NOT NULL,
  Nome varchar(60) NOT NULL,
  DataNasc datetime(6) NOT NULL,
  Telefone varchar(20) DEFAULT NULL,
  DataInicioTreino datetime(6) DEFAULT NULL,
  PlanoId int DEFAULT NULL,
  Presenca boolean NOT NULL,
  Treino varchar(2000) DEFAULT NULL,
  PRIMARY KEY (Id),
  FOREIGN KEY (PlanoId) REFERENCES plano(Id)
);

CREATE TABLE ALUNOCRIANCA (
  Id int(11) NOT NULL AUTO_INCREMENT,
  Ativo boolean NOT NULL,
  Nome varchar(60) NOT NULL,
  DataNasc datetime(6) NOT NULL,
  Telefone varchar(20) DEFAULT NULL,
  DataInicioTreino datetime(6) DEFAULT NULL,
  PlanoId int DEFAULT NULL,
  Presenca boolean NOT NULL,
  Treino varchar(2000) DEFAULT NULL,
  PRIMARY KEY (Id),
  FOREIGN KEY (PlanoId) REFERENCES PLANOCRIANCA(Id)
);



