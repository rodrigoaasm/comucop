package unifei.com.comucopdroid.controller;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**Classe responsável por efetuar o controle da aplicação*/

public class Controller {

    ArrayList<AppCompatActivity> activities;

    public Controller(AppCompatActivity activityParent){

        activities =  new ArrayList<AppCompatActivity>();//Iniciado lista de atividades da aplicação
        activities.add(activityParent);//Adicionando a activity que 