/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fidentis.gui.actions.newprojectwizard;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public final class NewProjectVisualPanel1 extends JPanel {

    /**
     * Creates new form NewProjectVisualPanel1
     */
    public NewProjectVisualPanel1() {
        initComponents();
    }

    public JTextField getModelLocationField(){
        return modelLocationTextField;
    }
    

    
    public JRadioButton getLoadRadioButton(){
        return loadRadioButton;
    }
    
    
    @Override
    public String getName() {
        return "Basic composite";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jFileChooser1 = new javax.swing.JFileChooser();
        Panel1 = new javax.swing.JPanel();
        loadRadioButton = new javax.swing.JRadioButton();
        createRadioButton = new javax.swing.JRadioButton();
        modelLocationTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        jFileChooser1.setApproveButtonText(org.openide.util.NbBundle.getMessage(NewProjectVisualPanel1.class, "NewProjectVisualPanel1.jFileChooser1.approveButtonText")); // NOI18N
        String[] extensions = new String[4];
        extensions[0] = "obj";
        extensions[1] = "stl";
        extensions[2] = "OBJ";
        extensions[3] = "STL";
        ModelFileFilter filter = new ModelFileFilter(extensions,"*.obj and *.stl");
        jFileChooser1.setFileFilter(filter);
        jFileChooser1.setSelectedFile(new File(System.getProperty("user.home")));

        buttonGroup1.add(loadRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(loadRadioButton, org.openide.util.NbBundle.getMessage(NewProjectVisualPanel1.class, "NewProjectVisualPanel1.loadRadioButton.text_1")); // NOI18N
        loadRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(createRadioButton);
        createRadioButton.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(createRadioButton, org.openide.util.NbBundle.getMessage(NewProjectVisualPanel1.class, "NewProjectVisualPanel1.createRadioButton.text_1")); // NOI18N
        createRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createRadioButtonActionPerformed(evt);
            }
        });

        modelLocationTextField.setText(org.openide.util.NbBundle.getMessage(NewProjectVisualPanel1.class, "NewProjectVisualPanel1.modelLocationTextField.text_1")); // NOI18N
        modelLocationTextField.setEnabled(false);
        modelLocationTextField.setMaximumSize(new java.awt.Dimension(6, 20));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(NewProjectVisualPanel1.class, "NewProjectVisualPanel1.jLabel1.text_1")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(NewProjectVisualPanel1.class, "NewProjectVisualPanel1.jButton1.text_1")); // NOI18N
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel1Layout = new javax.swing.GroupLayout(Panel1);
        Panel1.setLayout(Panel1Layout);
        Panel1Layout.setHorizontalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(75, 75, 75))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel1Layout.createSequentialGroup()
                        .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(createRadioButton)
                            .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(Panel1Layout.createSequentialGroup()
                                    .addGap(21, 21, 21)
                                    .addComponent(modelLocationTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(loadRadioButton, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jButton1)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        Panel1Layout.setVerticalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(createRadioButton)
                .addGap(9, 9, 9)
                .addComponent(loadRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(modelLocationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(183, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       int result = jFileChooser1.showOpenDialog(Panel1);
       if (result==JFileChooser.APPROVE_OPTION){
           modelLocationTextField.setText(jFileChooser1.getSelectedFile().getPath());
       }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void loadRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadRadioButtonActionPerformed
        modelLocationTextField.setEnabled(true);
        jButton1.setEnabled(true);
    }//GEN-LAST:event_loadRadioButtonActionPerformed

    private void createRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createRadioButtonActionPerformed
        modelLocationTextField.setEnabled(false);
        jButton1.setEnabled(false);
    }//GEN-LAST:event_createRadioButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Panel1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton createRadioButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JRadioButton loadRadioButton;
    private javax.swing.JTextField modelLocationTextField;
    // End of variables declaration//GEN-END:variables
}
