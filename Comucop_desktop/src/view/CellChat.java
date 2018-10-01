/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.*;

/**
 *
 * @author Rodrigo Maia
 */
public class CellChat extends javax.swing.JPanel {

    private Controller ctrApp;
    private int posicChat;
    /**
     * Creates new form CellChat
     */
    public CellChat(Controller aCtrApp,String nomeRem,String perfilRem,String lastMsgTxt,int unreadCountVal,int posicChat) {
        initComponents();
        labelNome.setText(nomeRem);
        labelPerfil.setText(perfilRem);
        lastMsg.setText(nomeRem);
        unreadCount.setText("" + unreadCountVal);
        this.ctrApp = aCtrApp;
        this.posicChat = posicChat;
    }



    public void setLastMsg(String msg){
        lastMsg.setText(msg);
    }  
    
    public void setUnreadCount(int count){
        unreadCount.setText(""+count);
    }  
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelNome = new javax.swing.JLabel();
        labelPerfil = new javax.swing.JLabel();
        lastMsg = new javax.swing.JLabel();
        unreadCount = new javax.swing.JLabel();

        setBackground(new java.awt.Color(153, 204, 255));
        setMaximumSize(new java.awt.Dimension(210, 50));
        setMinimumSize(new java.awt.Dimension(210, 50));
        setPreferredSize(new java.awt.Dimension(210, 50));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelNome.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        labelNome.setText("Nome Contanto");
        add(labelNome, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 140, -1));

        labelPerfil.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelPerfil.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelPerfil.setText("Perfil");
        add(labelPerfil, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 50, -1));

        lastMsg.setText("ultima mensagem");
        add(lastMsg, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 110, -1));

        unreadCount.setBackground(new java.awt.Color(51, 0, 255));
        unreadCount.setForeground(new java.awt.Color(255, 255, 255));
        unreadCount.setText("1");
        add(unreadCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 10, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        ctrApp.updateUIChat(posicChat);
    }//GEN-LAST:event_formMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelNome;
    private javax.swing.JLabel labelPerfil;
    private javax.swing.JLabel lastMsg;
    private javax.swing.JLabel unreadCount;
    // End of variables declaration//GEN-END:variables
}
