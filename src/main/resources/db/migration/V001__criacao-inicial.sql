create table veiculo (
	id bigint not null auto_increment,
	veiculo varchar(20) not null,
    marca varchar(15) not null,
    ano bigint not null,
    descricao text not null,
    vendido boolean not null,
	created datetime not null,
	updated datetime not null,
	
	primary key (id)
) engine=InnoDB default charset=utf8;