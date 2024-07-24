create database slowdb;
use slowdb;

create table usuario(
	id int auto_increment not null primary key,
    nome varchar(50) not null,
    endereco varchar(100) not null,
    contato varchar(200) not null,
    perfil ENUM ('admin', 'funcionario', 'cliente'),
    cpf int unique not null,
    status bool not null,
    login varchar(50) not null primary key,
    senha varchar(50) not null
);


create table midia(
	id int primary key auto_increment,
	nome varchar(50) not null, 
	filme bool not null,
	ano_lancamento int not null,
    img varchar(50),
    adulto bool not null,
    sinopse varchar(300),
    qtd_copias int
);

create table aluga(
	id_aluguel int primary key auto_increment,
	id_cli int,
    id_func int,
    id_midia int,
    data_inicio DATE,
    data_limite DATE,
    foreign key (id_cli) references usuarios(id),
	foreign key (id_cli) references usuarios(id)
);

create table receita(
	id_receita int primary key auto_increment,
    valor decimal,
    tipo ENUM ('multa', 'diaria'),
    data_receita date
);