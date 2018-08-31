/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Rodrigo Maia
 */
//Padronizando Model das tabelas.
public class MyTableModel extends DefaultTableModel{
    
    public MyTableModel(){
        super();
    } 
    //Impedindo que haja edição nas tabelas     
    @Override
    public boolean isCellEditable(int row,int colunm){
         return false;
    }
}
