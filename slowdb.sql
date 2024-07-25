create database slowdb;
use slowdb;

create table usuario(
	id_usuario int auto_increment not null primary key,
    nome varchar(50) not null,
    endereco varchar(100) not null,
    contato varchar(200) not null,
    perfil ENUM ('admin', 'funcionario', 'cliente'),
    cpf int unique not null,
    ativo bool not null,
    login varchar(50) not null unique,
    senha varchar(50) not null
);


create table midia(
	id_midia int primary key not null auto_increment,
	nome varchar(50) not null, 
	filme boolean not null,
	data_lancamento DATE not null,
    img varchar(50),
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
	id_aluguel int primary key auto_increment not null,
	id_cli int,
    id_func int,
    id_midia int,
    data_inicio DATE,
    data_limite DATE,
    foreign key (id_cli) references usuario(id_usuario),
	foreign key (id_func) references usuario(id_usuario),
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



INSERT into midia (id_midia, nome, filme, data_lancamento, img, adulto, sinopse, qtd_copias) values (
        '62',
        '2001: Uma Odisséia no Espaço',
        TRUE,
        '1968-04-02',
        './img/62.jpg',
        FALSE,
        'Desde a “Aurora do Homem” (a pré-história), um misterioso monólito negro parece emitir sinais de outra civilização, assim interferindo no nosso planeta. Quatro milhões de anos depois, no século XXI, uma equipe de astronautas liderados pelo experiente David Bowman e Frank Poole é enviada ao planeta Júpiter para investigar o enigmático monólito na nave Discovery, totalmente controlada pelo computador HAL-9000. Entretanto, no meio da viagem, HAL-9000 entra em pane e tenta assumir o controle da nave, eliminando um a um os tripulantes.',
        10);
INSERT into midia (id_midia, nome, filme, data_lancamento, img, adulto, sinopse, qtd_copias) values (
        '157336',
        'Interestelar',
        TRUE,
        '2014-11-05',
        './img/157336.jpg',
        FALSE,
        'As reservas naturais da Terra estão chegando ao fim e um grupo de astronautas recebe a missão de verificar possíveis planetas para receberem a população mundial, possibilitando a continuação da espécie. Cooper é chamado para liderar o grupo e aceita a missão sabendo que pode nunca mais ver os filhos. Ao lado de Brand, Jenkins e Doyle, ele seguirá em busca de um novo lar.',
        10);
INSERT into midia (id_midia, nome, filme, data_lancamento, img, adulto, sinopse, qtd_copias) values (
        '813477',
        'Shin Kamen Rider',
        TRUE,
        '2023-03-17',
        './img/813477.jpg',
        FALSE,
        'Um homem é forçado a ter poder e foi destituído de sua humanidade. Uma mulher é cética em relação à felicidade. Takeshi Hongo, o homem que sofreu o processo "Augment" feito pela SHOCKER, e Ruriko Midorikawa, uma rebelde da organização, scapam enquanto lutam contra assassinos. O que é a justiça? O que é o mal? Essa violência terá fim? Apesar de seu poder, Hongo tenta permanecer humano. Junto com a liberdade, Ruriko recuperou seu coração. Que caminhos eles escolherão?',
        10);
INSERT into midia (id_midia, nome, filme, data_lancamento, img, adulto, sinopse, qtd_copias) values (
        '37135',
        'Tarzan',
        TRUE,
        '1999-06-18',
        './img/37135.jpg',
        FALSE,
        'Um bebê perde os pais na selva. Órfão e sozinho, ele é encontrado por uma macaca que o cria como se fosse seu próprio filho. Tarzan cresce pensando ser um gorila, agindo e vivendo como tal. Quando uma equipe de pesquisadores chega à floresta, o rapaz percebe que é igual a eles. Tarzan encontra a bela Jane correndo perigo e a salva, apaixonando-se por ela. Ele se vê dividido entre o desejo de estar com gente da sua espécie e a lealdade com a família de gorilas que o criou.',
        10);
INSERT into midia (id_midia, nome, filme, data_lancamento, img, adulto, sinopse, qtd_copias) values (
        '562',
        'Duro de Matar',
        TRUE,
        '1988-07-15',
        './img/562.jpg',
        FALSE,
        'O policial de Nova York John McClane está visitando sua família no Natal. Ele participa de uma confraternização de fim de ano na sede da empresa japonesa em que a esposa trabalha. A festa é interrompida por terroristas que invadem o edifício de luxo. McClane não demora a perceber que não há ninguém para salvá-los, a não ser ele próprio.',
        10);
INSERT into midia (id_midia, nome, filme, data_lancamento, img, adulto, sinopse, qtd_copias) values (
        '8681',
        'Busca Implacável',
        TRUE,
        '2008-02-18',
        './img/8681.jpg',
        FALSE,
        'Bryan Mills é um ex-agente do governo, que deixou o emprego para que pudesse passar mais tempo com Kim, a filha que teve com sua ex-esposa Lenore. Ele passa então a trabalhar com antigos colegas, realizando serviços leves de segurança particular. Um dia Kim pede ao pai autorização para que viaje a Paris com uma amiga, a qual é negada pelo fato de que Bryan sabe bem os perigos que ela correria em um país estranho. Isto não a impede, fazendo a viagem assim mesmo. Só que o temor de Bryan se concretiza, já que logo após a chegada Kim e sua amiga desaparecem.',
        10);


INSERT into midia_categoria (id_midia, id_categoria) values (62, 878);
INSERT into midia_categoria (id_midia, id_categoria) values (62, 9648);
INSERT into midia_categoria (id_midia, id_categoria) values (62, 12);
INSERT into midia_categoria (id_midia, id_categoria) values (157336, 12);
INSERT into midia_categoria (id_midia, id_categoria) values (157336, 18);
INSERT into midia_categoria (id_midia, id_categoria) values (157336, 878);
INSERT into midia_categoria (id_midia, id_categoria) values (813477, 28);
INSERT into midia_categoria (id_midia, id_categoria) values (813477, 18);
INSERT into midia_categoria (id_midia, id_categoria) values (813477, 878);
INSERT into midia_categoria (id_midia, id_categoria) values (37135, 10751);
INSERT into midia_categoria (id_midia, id_categoria) values (37135, 12);
INSERT into midia_categoria (id_midia, id_categoria) values (37135, 16);
INSERT into midia_categoria (id_midia, id_categoria) values (37135, 18);
INSERT into midia_categoria (id_midia, id_categoria) values (562, 28);
INSERT into midia_categoria (id_midia, id_categoria) values (562, 53);
INSERT into midia_categoria (id_midia, id_categoria) values (8681, 28);
INSERT into midia_categoria (id_midia, id_categoria) values (8681, 53);


select midia.nome, categoria.nome from midia INNER JOIN midia_categoria mc on mc.id_midia = midia.id_midia INNER JOIN categoria on categoria.id_categoria = mc.id_categoria;select midia.nome, categoria.nome from midia INNER JOIN midia_categoria mc on mc.id_midia = midia.id_midia INNER JOIN categoria on categoria.id_categoria = mc.id_categoria;