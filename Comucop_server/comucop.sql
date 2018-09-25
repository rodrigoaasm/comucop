/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Rodrigo Maia
 * Created: 25/09/2018
 */

create database comucop;

use comucop;

create table departamento(
    dep_cod int(11) auto_increment,
    dep_nome varchar(50) not null,
    dep_sigla varchar(5) not null,
    dep_desc varchar (200),
    primary key(dep_cod)
);

create table funcionario(
    func_cod int(11) auto_increment,
    func_nome varchar (20) not null,
    func_sobrenome varchar(30) not null,
    func_cpf char(14) unique,
    func_perfil varchar(30),
    dep_cod int(11) not null,
    primary key(func_cod),
    foreign key(dep_cod) references departamento(dep_cod)
);

create table user(
    us_cod int(11) auto_increment,
    us_login varchar(20) unique,
    us_password varchar(32) not null,
    func_cod int(11) not null,
    primary key (us_cod),
    foreign key (func_cod) references funcionario(func_cod)
);