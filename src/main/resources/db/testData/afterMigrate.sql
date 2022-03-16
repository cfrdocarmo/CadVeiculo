set foreign_key_checks = 0;
SET sql_safe_updates=0;

delete from veiculo;


set foreign_key_checks = 1;
SET sql_safe_updates=1;

alter table veiculo auto_increment = 1;

insert into veiculo (id, veiculo, marca, ano, descricao, vendido,  created, updated) values (1, 'Jetta', '1', 2014, 'Veículo moderno com motor turbo!', false, utc_timestamp, utc_timestamp);
insert into veiculo (id, veiculo, marca, ano, descricao, vendido,  created, updated) values (2, 'Uno', '3', 2019, 'Veículo moderno com motor turbo!', false, utc_timestamp, utc_timestamp);
insert into veiculo (id, veiculo, marca, ano, descricao, vendido,  created, updated) values (3, 'Logan', '4', 2009, 'Veículo moderno com motor turbo!', false, utc_timestamp, utc_timestamp);