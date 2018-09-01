package view;

import controller.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Kevin
 */
public class CadastroFuncionario extends javax.swing.JFrame {

    private ControllerFuncionario ctrFunc;

    public CadastroFuncionario(ControllerFuncionario pCtr) {
        initComponents();
        ctrFunc = pCtr;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        ButtonCadDep = new javax.swing.JButton();
        ButtonCancDep = new javax.swing.JButton();
        textNome = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        comboSetor = new javax.swing.JComboBox<>();
        comboPerfil = new javax.swing.JComboBox<>();
        textCPF = new javax.swing.JFormattedTextField();
        textSobrenome = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 102));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        ButtonCadDep.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        ButtonCadDep.setText("Cadastrar");
        ButtonCadDep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCadDepActionPerformed(evt);
            }
        });

        ButtonCancDep.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        ButtonCancDep.setText("Fechar");
        ButtonCancDep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCancDepActionPerformed(evt);
            }
        });

        textNome.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Nome: ");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Perfil: ");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Setor:");

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("CPF:");

        comboSetor.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        comboSetor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Setor 1", "Setor 2", "Setor 3", "Setor 4" }));

        comboPerfil.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        comboPerfil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Funcionário", "Gerente" }));

        try {
            textCPF.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        textSobrenome.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Sobrenome:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboPerfil, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(textCPF)
                    .addComponent(textSobrenome)
                    .addComponent(textNome)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(comboSetor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 186, Short.MAX_VALUE)
                        .addComponent(ButtonCadDep)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonCancDep)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textNome, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textSobrenome, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textCPF, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboSetor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonCadDep)
                    .addComponent(ButtonCancDep))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ButtonCancDepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCancDepActionPerformed
        this.dispose();
        // TODO add your handling code here:
    }//GEN-LAST:event_ButtonCancDepActionPerformed

    private void ButtonCadDepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCadDepActionPerformed

        if(textCPF.toString().isEmpty() == true){
            JOptionPane.showMessageDialog(null, "Erro CPF", "O CPF informado não é válido.\n"
                    + "Por favor digite um CPF válido.", JOptionPane.ERROR_MESSAGE);
        }else{
            if(textNome.getText() == null){
                JOptionPane.showMessageDialog(null, "Erro Nome", "O campo nome deve ser preenchido.\n"
                    + "Por favor digite um nome.", JOptionPane.ERROR_MESSAGE);
            }else{
                if(textSobrenome.toString().isEmpty() == true){
                    JOptionPane.showMessageDialog(null, "Erro Sobrenome", "O campo sobrenome deve ser preenchido.\n"
                    + "Por favor digite um sobrenome.", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
    }//GEN-LAST:event_ButtonCadDepActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonCadDep;
    private javax.swing.JButton ButtonCancDep;
    private javax.swing.JComboBox<String> comboPerfil;
    private javax.swing.JComboBox<String> comboSetor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JFormattedTextField textCPF;
    private javax.swing.JTextField textNome;
    private javax.swing.JTextField textSobrenome;
    // End of variables declaration//GEN-END:variables
}
