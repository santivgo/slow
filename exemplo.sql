use LP2;
create table departamento(
  codigoDepartamento integer primary key,
  nome varchar(50));

create table empregado(
  codigoEmpregado integer primary key,
  nome varchar(50),
  endereco varchar(50),
  salario float,
  codigoDepartamento Integer,
  FOREIGN key(codigoDepartamento) references departamento(codigoDepartamento));

INSERT INTO departamento VALUES(100,"RH"),
(200,"Informática"),
(300,"Financeiro"),
(400,"Marketing");

insert into empregado values (1,"Ana","Rua Boa esperança",2500,200),
(2,"Beatriz","Avenida Coronel Antônio",3000,300),
(3,"Carlos","Avenida Doutor Antônio",2500,200),
(4,"Daniel","Travessa José Roberto",2600,400),
(5,"Érica","Travessa João Abreu",1500,100),
(6,"Fernando","Travessa 11 de setembro",2600,400),
(7,"Gean","Rua João Francisco",2500,200),
(8,"Helena","Rua Chico Nogueira",1500,100),
(9,"Iderlan","Rua Padre Rocha",3000,300),
(10,"Joana","Avenida Simão de Góes",2500,200);


select nome from departamento;

select nome, salario from empregado;

select nome, salario from empregado where empregado.salario > 2500;

select max(salario) maior, min(salario) from empregado inner join departamento on departamento.codigoDepartamento = empregado.codigoDepartamento where departamento.nome = "RH";

select count(nome) from empregado;

select nome from empregado where salario > 2000 order by salario desc;

select nome, salario from empregado where salario BETWEEN 1000 AND 2000 order by salario desc;

