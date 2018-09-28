/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Controller;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import model.Contato;
import model.Departamento;


/**
 *
 * @author Rodrigo Maia
 */
public class MainWindow extends javax.swing.JFrame {

    private static Point point = new Point();
    private JFrame t = this;
    private Controller ctrApp;

    /**
     * Creates new form MainWindow
     */
    public MainWindow(Controller actrApp) {
        initComponents();
        initComponentsExtra();
        TextArea.setVisible(false);
        ButtonEnviar.setVisible(false);
        this.ctrApp = actrApp;
    }

    public void initComponentsExtra() {

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                point.x = e.getX();
                point.y = e.getY();
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = t.getLocation();
                t.setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
            }
        });

        this.mainBar.setVisible(false);
    }

    public void loginOk() {
        this.censoredFrame.setVisible(false);
        this.mainBar.setVisible(true);
    }

    public void callMessage(String msg, String title, int typeMsg) {
        JOptionPane.showMessageDialog(this, msg, title, typeMsg);
    }

    //Adiciona departamentos
    public void addDeps() {
        //Recebe a lista de departamentos
        ArrayList<Departamento> listaDeps = ctrApp.getCtrDep().getListaDeps();
       
       // int x = 0;
        //Ira passar por todos os departamentos criando combobox, X é responsavel pela posição horizontal
        for (Departamento dp : listaDeps) {
            /*panelCont.add(new CellDepart(ctrApp,dp.getNome(),dp.getSigla(),dp.getCodigo()));
        }*/
            JComboBox combo = new JComboBox(new String[]{dp.getSigla() + "-" + dp.getNome()});
            combo.setSize(210, 50);
            //Adiciona um mouse listener para cada um dos combobox
            combo.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                //Quando o mouse passar por cima do combo ele solicitará para o servidor os funcinarios que estao la
                @Override
                public void mouseEntered(MouseEvent e) {
                    //Se o combobox so tiver um membro que é o seu proprio nome adiconara os contatos
                    if (combo.getItemCount() == 1) {
                        //Atribui no controle o departamento que foi solicitado as requisicoes
                        ctrApp.setDpReq(dp.getCodigo());
                        ctrApp.expToContacts("" + dp.getCodigo());

                        ArrayList<Contato> funcs = ctrApp.getListaConts();
                        //Apos receber os contatos adiciona eles no combobox
                        for (Contato f : funcs) {
                            combo.addItem(f.getCodigo()+ "-"+ f.getNome() + " " + f.getSobrenome());
                        }
                    }
                    tabCont.updateUI();                }

                @Override
                public void mouseExited(MouseEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });

            combo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (combo.getSelectedIndex() > 0) {
                        combo.getSelectedItem();
                        TextArea.setVisible(true);
                        TextArea.setEditable(false);
                        ButtonEnviar.setVisible(true);
                    }
                }

            });
            panelCont.add(combo);
        }
        tabCont.updateUI();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        censoredFrame = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        cxTxtLogin = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        cxPassword = new javax.swing.JPasswordField();
        superBody = new javax.swing.JPanel();
        painelChats = new javax.swing.JPanel();
        managerTab = new javax.swing.JTabbedPane();
        tabCont = new javax.swing.JPanel();
        scrolPaneCont = new javax.swing.JScrollPane();
        panelCont = new javax.swing.JPanel();
        tabRec = new javax.swing.JPanel();
        scrollPaneRec = new javax.swing.JScrollPane();
        panelRec = new javax.swing.JPanel();
        titleChat = new javax.swing.JPanel();
        bodyChat = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TextArea = new javax.swing.JTextArea();
        cxTextMsg = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        ButtonEnviar = new javax.swing.JButton();
        mainBar = new javax.swing.JMenuBar();
        menuNovo = new javax.swing.JMenu();
        cadFunc = new javax.swing.JMenuItem();
        cadDep = new javax.swing.JMenuItem();
        menuCons = new javax.swing.JMenu();
        colFunc = new javax.swing.JMenuItem();
        colDep = new javax.swing.JMenuItem();

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Comucop 1.0");
        setBackground(new java.awt.Color(0, 0, 102));
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        censoredFrame.setBackground(new java.awt.Color(51, 51, 51));

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        jPanel1.setForeground(new java.awt.Color(204, 204, 255));
        jPanel1.setMinimumSize(new java.awt.Dimension(400, 260));

        cxTxtLogin.setBackground(new java.awt.Color(153, 153, 153));
        cxTxtLogin.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Password:");

        jLabel4.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Login:");

        jButton2.setFont(new java.awt.Font("Segoe UI Semilight", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(102, 102, 102));
        jButton2.setText("Sign in");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        cxPassword.setBackground(new java.awt.Color(153, 153, 153));
        cxPassword.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cxTxtLogin)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(cxPassword))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(119, 119, 119)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(123, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cxTxtLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cxPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(40, 40, 40))
        );

        censoredFrame.add(jPanel1);

        getContentPane().add(censoredFrame, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 740, 420));

        superBody.setBackground(new java.awt.Color(255, 255, 255));
        superBody.setForeground(new java.awt.Color(255, 255, 255));
        superBody.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        painelChats.setBackground(new java.awt.Color(0, 0, 153));

        managerTab.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        managerTab.setToolTipText("");

        tabCont.setBackground(new java.awt.Color(204, 204, 204));

        scrolPaneCont.setMaximumSize(new java.awt.Dimension(225, 372));
        scrolPaneCont.setMinimumSize(new java.awt.Dimension(225, 372));

        panelCont.setLayout(new javax.swing.BoxLayout(panelCont, javax.swing.BoxLayout.Y_AXIS));
        scrolPaneCont.setViewportView(panelCont);

        javax.swing.GroupLayout tabContLayout = new javax.swing.GroupLayout(tabCont);
        tabCont.setLayout(tabContLayout);
        tabContLayout.setHorizontalGroup(
            tabContLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrolPaneCont, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
        );
        tabContLayout.setVerticalGroup(
            tabContLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrolPaneCont, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
        );

        managerTab.addTab("Contantos", tabCont);

        tabRec.setBackground(new java.awt.Color(204, 204, 204));

        panelRec.setLayout(new javax.swing.BoxLayout(panelRec, javax.swing.BoxLayout.Y_AXIS));
        scrollPaneRec.setViewportView(panelRec);

        javax.swing.GroupLayout tabRecLayout = new javax.swing.GroupLayout(tabRec);
        tabRec.setLayout(tabRecLayout);
        tabRecLayout.setHorizontalGroup(
            tabRecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneRec, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
        );
        tabRecLayout.setVerticalGroup(
            tabRecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneRec, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
        );

        managerTab.addTab("Recentes", tabRec);

        javax.swing.GroupLayout painelChatsLayout = new javax.swing.GroupLayout(painelChats);
        painelChats.setLayout(painelChatsLayout);
        painelChatsLayout.setHorizontalGroup(
            painelChatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(managerTab)
        );
        painelChatsLayout.setVerticalGroup(
            painelChatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelChatsLayout.createSequentialGroup()
                .addGap(0, 20, Short.MAX_VALUE)
                .addComponent(managerTab, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        superBody.add(painelChats, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 420));

        titleChat.setBackground(new java.awt.Color(0, 0, 153));

        javax.swing.GroupLayout titleChatLayout = new javax.swing.GroupLayout(titleChat);
        titleChat.setLayout(titleChatLayout);
        titleChatLayout.setHorizontalGroup(
            titleChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 590, Short.MAX_VALUE)
        );
        titleChatLayout.setVerticalGroup(
            titleChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        superBody.add(titleChat, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, 590, 40));

        bodyChat.setBackground(new java.awt.Color(204, 204, 204));

        TextArea.setColumns(20);
        TextArea.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextArea.setRows(5);
        jScrollPane1.setViewportView(TextArea);

        javax.swing.GroupLayout bodyChatLayout = new javax.swing.GroupLayout(bodyChat);
        bodyChat.setLayout(bodyChatLayout);
        bodyChatLayout.setHorizontalGroup(
            bodyChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
        );
        bodyChatLayout.setVerticalGroup(
            bodyChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
        );

        superBody.add(bodyChat, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, 590, 310));

        jTextPane1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        cxTextMsg.setViewportView(jTextPane1);

        superBody.add(cxTextMsg, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 350, 510, 70));

        ButtonEnviar.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        ButtonEnviar.setText("Enviar");
        ButtonEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonEnviarActionPerformed(evt);
            }
        });
        superBody.add(ButtonEnviar, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 350, -1, -1));

        getContentPane().add(superBody, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 820, 420));

        mainBar.setBackground(new java.awt.Color(0, 0, 102));
        mainBar.setBorder(null);
        mainBar.setAlignmentX(0.0F);

        menuNovo.setBackground(new java.awt.Color(0, 0, 102));
        menuNovo.setForeground(new java.awt.Color(255, 255, 255));
        menuNovo.setText("Novo");

        cadFunc.setText("Funcionário");
        cadFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadFuncActionPerformed(evt);
            }
        });
        menuNovo.add(cadFunc);

        cadDep.setText("Departamento");
        cadDep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadDepActionPerformed(evt);
            }
        });
        menuNovo.add(cadDep);

        mainBar.add(menuNovo);

        menuCons.setBackground(new java.awt.Color(0, 0, 102));
        menuCons.setForeground(new java.awt.Color(255, 255, 255));
        menuCons.setText("Consultar");

        colFunc.setText("Funcionário");
        colFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colFuncActionPerformed(evt);
            }
        });
        menuCons.add(colFunc);

        colDep.setText("Departamento");
        colDep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colDepActionPerformed(evt);
            }
        });
        menuCons.add(colDep);

        mainBar.add(menuCons);

        setJMenuBar(mainBar);
        mainBar.getAccessibleContext().setAccessibleName("");
        mainBar.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        try {
            ctrApp.tryEstablishCon(cxTxtLogin.getText(), cxPassword.getText());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro!", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void cadDepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadDepActionPerformed
        // TODO add your handling code here:
        ctrApp.getCtrDep().AbreJanela(1);
    }//GEN-LAST:event_cadDepActionPerformed

    private void colDepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colDepActionPerformed

        ctrApp.getCtrDep().AbreJanela(3);
        // TODO add your handling code here:
    }//GEN-LAST:event_colDepActionPerformed

    private void cadFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadFuncActionPerformed
        // TODO add your handling code here:
        ctrApp.getCtrFunc().AbreJanela(1);
    }//GEN-LAST:event_cadFuncActionPerformed

    private void colFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colFuncActionPerformed
        // TODO add your handling code here:
        ctrApp.getCtrFunc().AbreJanela(3);
    }//GEN-LAST:event_colFuncActionPerformed

    private void ButtonEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonEnviarActionPerformed
        // TODO add your handling code here:
        ctrApp.sendMsg();
    }//GEN-LAST:event_ButtonEnviarActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        ctrApp.finishCon();
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        ctrApp.finishCon();
    }//GEN-LAST:event_formWindowClosing

    public JTextArea getTextArea() {
        return TextArea;
    }

    public void setTextArea(JTextArea TextArea) {
        this.TextArea = TextArea;
    }

    public JButton getButtonEnviar() {
        return ButtonEnviar;
    }

    public void setButtonEnviar(JButton ButtonEnviar) {
        this.ButtonEnviar = ButtonEnviar;
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonEnviar;
    private javax.swing.JTextArea TextArea;
    private javax.swing.JPanel bodyChat;
    private javax.swing.JMenuItem cadDep;
    private javax.swing.JMenuItem cadFunc;
    private javax.swing.JPanel censoredFrame;
    private javax.swing.JMenuItem colDep;
    private javax.swing.JMenuItem colFunc;
    private javax.swing.JPasswordField cxPassword;
    private javax.swing.JScrollPane cxTextMsg;
    private javax.swing.JTextField cxTxtLogin;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JMenuBar mainBar;
    private javax.swing.JTabbedPane managerTab;
    private javax.swing.JMenu menuCons;
    private javax.swing.JMenu menuNovo;
    private javax.swing.JPanel painelChats;
    private javax.swing.JPanel panelCont;
    private javax.swing.JPanel panelRec;
    private javax.swing.JScrollPane scrolPaneCont;
    private javax.swing.JScrollPane scrollPaneRec;
    private javax.swing.JPanel superBody;
    private javax.swing.JPanel tabCont;
    private javax.swing.JPanel tabRec;
    private javax.swing.JPanel titleChat;
    // End of variables declaration//GEN-END:variables
}
