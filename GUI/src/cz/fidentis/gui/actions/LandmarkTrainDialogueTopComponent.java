/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fidentis.gui.actions;

import cz.fidentis.featurepoints.FacialPoint;
import cz.fidentis.featurepoints.FpModel;
import cz.fidentis.gui.GUIController;
import cz.fidentis.gui.ProjectTopComponent;
import cz.fidentis.landmarkParser.CSVparser;
import cz.fidentis.processing.exportProcessing.FPImportExport;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.vecmath.Vector3f;
import org.openide.windows.TopComponent;



public final class LandmarkTrainDialogueTopComponent extends TopComponent {

    private List<FpModel> selectedFiles;
    
    private FpModel newTrainingModel;
    
    public LandmarkTrainDialogueTopComponent() {
        this.selectedFiles = new ArrayList<>();
        initComponents();
        jTextField1.setText("undefined");
        //setName(Bundle.CTL_LandmarkTrainDialogueTopComponent());
        //setToolTipText(Bundle.HINT_LandmarkTrainDialogueTopComponent());

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        removeButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jProgressBar1 = new javax.swing.JProgressBar();
        useButton = new javax.swing.JButton();
        saveButton1 = new javax.swing.JButton();
        label6 = new java.awt.Label();
        label5 = new java.awt.Label();
        loadButton1 = new javax.swing.JButton();
        label4 = new java.awt.Label();
        label2 = new java.awt.Label();
        jTextField1 = new javax.swing.JTextField();
        label1 = new java.awt.Label();

        jPanel1.setPreferredSize(new java.awt.Dimension(576, 420));

        removeButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(removeButton, org.openide.util.NbBundle.getMessage(LandmarkTrainDialogueTopComponent.class, "LandmarkTrainDialogueTopComponent.removeButton.text")); // NOI18N
        removeButton.setToolTipText(org.openide.util.NbBundle.getMessage(LandmarkTrainDialogueTopComponent.class, "LandmarkTrainDialogueTopComponent.removeButton.toolTipText")); // NOI18N
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(jList1);

        jProgressBar1.setStringPainted(true);

        useButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(useButton, org.openide.util.NbBundle.getMessage(LandmarkTrainDialogueTopComponent.class, "LandmarkTrainDialogueTopComponent.useButton.text")); // NOI18N
        useButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useButtonActionPerformed(evt);
            }
        });

        saveButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(saveButton1, org.openide.util.NbBundle.getMessage(LandmarkTrainDialogueTopComponent.class, "LandmarkTrainDialogueTopComponent.saveButton1.text")); // NOI18N
        saveButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButton1ActionPerformed(evt);
            }
        });

        label6.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        label6.setText(org.openide.util.NbBundle.getMessage(LandmarkTrainDialogueTopComponent.class, "LandmarkTrainDialogueTopComponent.label6.text")); // NOI18N

        label5.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        label5.setText(org.openide.util.NbBundle.getMessage(LandmarkTrainDialogueTopComponent.class, "LandmarkTrainDialogueTopComponent.label5.text")); // NOI18N

        loadButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(loadButton1, org.openide.util.NbBundle.getMessage(LandmarkTrainDialogueTopComponent.class, "LandmarkTrainDialogueTopComponent.loadButton1.text")); // NOI18N
        loadButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButton1ActionPerformed(evt);
            }
        });

        label4.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        label4.setText(org.openide.util.NbBundle.getMessage(LandmarkTrainDialogueTopComponent.class, "LandmarkTrainDialogueTopComponent.label4.text")); // NOI18N

        label2.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        label2.setText(org.openide.util.NbBundle.getMessage(LandmarkTrainDialogueTopComponent.class, "LandmarkTrainDialogueTopComponent.label2.text")); // NOI18N

        jTextField1.setText(org.openide.util.NbBundle.getMessage(LandmarkTrainDialogueTopComponent.class, "LandmarkTrainDialogueTopComponent.jTextField1.text")); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        label1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        label1.setText(org.openide.util.NbBundle.getMessage(LandmarkTrainDialogueTopComponent.class, "LandmarkTrainDialogueTopComponent.label1.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeButton))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(82, 82, 82))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(35, 35, 35)
                                        .addComponent(useButton, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(52, 52, 52)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(saveButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(loadButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(53, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(removeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 86, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(loadButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saveButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(useButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 561, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void loadButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButton1ActionPerformed
        // TODO add your handling code here:

        final ProjectTopComponent tc = GUIController.getSelectedProjectTopComponent();
        List<FpModel> fpPoints = FPImportExport.instance().importPoints(tc, true);

        addFilesHandler(fpPoints, selectedFiles, jList1);
    }//GEN-LAST:event_loadButton1ActionPerformed

    private void saveButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButton1ActionPerformed
        // TODO add your handling code here:
        final ProjectTopComponent tc = GUIController.getSelectedProjectTopComponent();

        List<FpModel> tmp = new ArrayList<>();

        newTrainingModel.setModelName(jTextField1.getText());
        tmp.add(newTrainingModel);
        FPImportExport.instance().exportPoints(tc, tmp);
    }//GEN-LAST:event_saveButton1ActionPerformed

    private void useButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useButtonActionPerformed
        // TODO add your handling code here:

        newTrainingModel = trainigModel(selectedFiles);

        List<FpModel> tmp = new ArrayList<>();

        newTrainingModel.setModelName(jTextField1.getText());
        tmp.add(newTrainingModel);

        String filePath = "C:\\Rasto\\School\\Bakalarka\\tmpFidentis\\" + jTextField1.getText() + ".csv";
        CSVparser.save(tmp, filePath);
    }//GEN-LAST:event_useButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        // TODO add your handling code here:
        DefaultListModel model = (DefaultListModel) jList1.getModel();

        if(jList1.getSelectedIndex() != -1){
            selectedFiles.remove(jList1.getSelectedIndex());
            model.remove(jList1.getSelectedIndex());
        }
    }//GEN-LAST:event_removeButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private java.awt.Label label1;
    private java.awt.Label label2;
    private java.awt.Label label4;
    private java.awt.Label label5;
    private java.awt.Label label6;
    private javax.swing.JButton loadButton1;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton saveButton1;
    private javax.swing.JButton useButton;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    
    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
    
    
    private void addFilesHandler(List<FpModel> fpPoints, List<FpModel> selected, javax.swing.JList<String> jList){
        
        //adding all selected file fp points to list
        for (FpModel fp : fpPoints) {
                if (!fpContains(fp.getModelName(), selected)) {
                    selected.add(fp);
                } 
            }
        
        //data for window list
        DefaultListModel tmp = new DefaultListModel();
        
        for (FpModel fp : selected){
                tmp.addElement(fp.getModelName());
            }
        
        jList.setModel(tmp);
    }
    
    
    private boolean fpContains(String input, List<FpModel> selected){
        for (FpModel model : selected ){
            if(model.getModelName().equals(input)){
                return true;
            }
        }
        return false;
    }
    
    // create mean model from training shapes on input
    public FpModel trainigModel(List<FpModel> trainingShapes) {

        FpModel meanShape;
        meanShape = trainingShapes.get(0);
        jProgressBar1.setValue(0);
        
        int progress = 100/trainingShapes.size();
        int current = progress;
        for (int i = 1; i < trainingShapes.size(); i++) {
            current += progress;
            jProgressBar1.setValue(current);
            
            List<FacialPoint> values = trainingShapes.get(i).getFacialPoints();

            for (int j = 0; j < values.size(); j++) {
                Vector3f point = meanShape.getFacialPoints().get(j).getPosition();
                point.x += values.get(j).getPosition().x;
                point.y += values.get(j).getPosition().y;
                point.z += values.get(j).getPosition().z;

                meanShape.getFacialPoints().get(j).setCoords(point);
            }

        }

        for (int j = 0; j < meanShape.getPointsNumber(); j++) {
            Vector3f point = meanShape.getFacialPoints().get(j).getPosition();
            point.x /= trainingShapes.size();
            point.y /= trainingShapes.size();
            point.z /= trainingShapes.size();

            meanShape.getFacialPoints().get(j).setCoords(point);
        }
        jProgressBar1.setValue(100);

        return meanShape;
    }
    
}
