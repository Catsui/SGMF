CREATE DATABASE sgmf;

USE sgmf;

CREATE TABLE plano (
	Id int(11) NOT NULL AUTO_INCREMENT,
	Nome varchar(60) NOT NULL,
	Mensalidade decimal(10,2) NOT NULL,
    PRIMARY KEY (Id)
);

INSERT INTO plano (Nome, Mensalidade) VALUES ('Plano 1: Todos os dias',92.0);

CREATE TABLE aluno (
  Id int(11) NOT NULL AUTO_INCREMENT,
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



