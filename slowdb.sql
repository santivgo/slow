create database slowdb;
use slowdb;

create table usuario(
	id_usuario int auto_increment not null primary key,
    nome varchar(50) not null,
    endereco varchar(100) not null,
    contato varchar(200) not null,
    perfil ENUM ('admin', 'funcionario', 'cliente'),
    cpf varchar(50) unique not null,
    ativo bool DEFAULT true not null,
    login varchar(50) not null unique,
    senha varchar(50) not null
);


create table midia(
	id_midia int primary key not null auto_increment,
	nome varchar(50) not null, 
	filme boolean not null,
	data_lancamento DATE not null,
    img longblob,
    adulto bool not null,
    sinopse varchar(1000),
    qtd_copias int not null
);
create table categoria(
	id_categoria int auto_increment UNIQUE not null,
	nome varchar(50) UNIQUE primary key not null
);
create table midia_categoria(
	id int auto_increment unique not null,
	id_midia int not null,
    id_categoria int not null,
    
	primary key (id_midia, id_categoria),
	foreign key (id_midia) references midia(id_midia),
	foreign key (id_categoria) references categoria(id_categoria)
);


create table aluga(
	id int unique auto_increment not null,
	id_cli int not null,
    id_midia int not null,
    -- adicionar depois na tela de alugar pra 
    -- adicionar a data do inicio e a data do fim (selecionavel pro usuario)
    data_inicio DATE,
    data_limite DATE,
    primary key (id_cli, id_midia, id),
    foreign key (id_cli) references usuario(id_usuario),
    foreign key (id_midia) references midia(id_midia)
);

create table legenda(
	id_legenda int not null UNIQUE,
    nome_legenda varchar(50) primary key not null UNIQUE
);

create table audio(
	id_audio int not null UNIQUE,
    nome_audio varchar(50) primary key not null UNIQUE
);


create table legenda_filme(
	id int auto_increment unique,
	id_midia int not null,
    nome_legenda varchar(50) not null,
    
	PRIMARY KEY (id_midia, nome_legenda),
    foreign key (id_midia) references midia (id_midia),
	foreign key (nome_legenda) references legenda(nome_legenda)
    
);

create table audio_filme(
	id int auto_increment unique,
	id_midia int not null,
    nome_audio varchar(50) not null,
    
	primary key (id_midia, nome_audio),
    foreign key (id_midia) references midia(id_midia),
	foreign key (nome_audio) references audio(nome_audio)
    
);


INSERT into categoria values (28, 'Ação');
INSERT into categoria values (12, 'Aventura');
INSERT into categoria values (16, 'Animação');
INSERT into categoria values (38, 'Comédia');
INSERT into categoria values (80, 'Crime');
INSERT into categoria values (99, 'Documentário');
INSERT into categoria values (18, 'Drama');
INSERT into categoria values (10751, 'Família');
INSERT into categoria values (14, 'Fantasia');
INSERT into categoria values (36, 'História');
INSERT into categoria values (27, 'Terror');
INSERT into categoria values (53, 'Thriller');
INSERT into categoria values (10402, 'Musical');
INSERT into categoria values (9648, 'Mistério');
INSERT into categoria values (10749, 'Romance');
INSERT into categoria values (878, 'Ficção Científica');
INSERT into categoria values (10770, 'Cinema TV');
INSERT into categoria values (10752, 'Guerra');
INSERT into categoria values (37, 'Faroeste');
INSERT into categoria values (10759, 'Ação & Aventura (seriado)');
INSERT into categoria values (10762, 'Infântil');
INSERT into categoria values (10763, 'Programa de TV');
INSERT into categoria values (10766, 'Novela');
INSERT into categoria values (10764, 'Reality Show');
INSERT into categoria values (10765, 'Sci-Fi & Fantasia (seriado)');

SELECT img from midia;
insert into usuario (nome, endereco, contato, perfil, cpf, login, senha) VALUES 
('sant', 'ruac', '@instagram', 'admin','02209803306', 'admin',123);


drop database slowdb;

use slowdb;
select * from aluga;

drop table aluga;

