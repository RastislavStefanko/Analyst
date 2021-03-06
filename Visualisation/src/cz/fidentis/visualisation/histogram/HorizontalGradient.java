/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fidentis.visualisation.histogram;

import cz.fidentis.visualisation.surfaceComparison.HDpainting;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.geom.Rectangle2D;


/**
 *
 * @author xfurman
 */
public class HorizontalGradient extends javax.swing.JPanel {

    private HDpainting hdp;

    public void setHdp(HDpainting hdp) {
        this.hdp = hdp;
    }

    
    /**
     * Creates new form FlatHistogram
     */
    public HorizontalGradient() {
        initComponents();
    }
    
     public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int width = this.getWidth();
        int height = this.getHeight();
        if(hdp!=null){
         paintGrdient(g2, width, height);
        }
        
     }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 403, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 67, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
 private void paintGrdient(Graphics2D g2, int width, int heighth) {
        int parts = 100;
        float fraction = width / (float) parts;
        for (int i = 0; i < parts; i++) {
            Color c1 = new Color(ColorSpace.getInstance(ColorSpace.CS_sRGB), hdp.chooseColorHSVMapping((i * fraction),width,0), 1);
            Color c2 = new Color(ColorSpace.getInstance(ColorSpace.CS_sRGB), hdp.chooseColorHSVMapping(((i + 1) * fraction),width,0), 1);
            
            GradientPaint gp = new GradientPaint((i * fraction),0, c1, ((i + 1) * fraction),0, c2);
            g2.setPaint(gp);
            g2.fill(new Rectangle2D.Double((i * fraction),0,fraction,heighth));
        }

    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
