/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fidentis.gui.actions;

import com.jogamp.graph.math.Quaternion;
import cz.fidentis.comparison.ComparisonMethod;
import cz.fidentis.comparison.RegistrationMethod;
import cz.fidentis.comparison.icp.ICPTransformation;
import cz.fidentis.comparison.icp.KdTreeIndexed;
import cz.fidentis.comparison.procrustes.GPA;
import cz.fidentis.composite.FacePartType;
import cz.fidentis.composite.ModelInfo;
import cz.fidentis.controller.BatchComparison;
import cz.fidentis.controller.Comparison2Faces;
import cz.fidentis.controller.Composite;
import cz.fidentis.controller.Controller;
import cz.fidentis.controller.OneToManyComparison;
import cz.fidentis.controller.Project;
import cz.fidentis.gui.ConfigurationTopComponent;
import cz.fidentis.gui.GUIController;
import cz.fidentis.gui.ProjectTopComponent;
import cz.fidentis.gui.actions.newprojectwizard.ModelFileFilter;
import cz.fidentis.landmarkParser.CSVparser;
import cz.fidentis.featurepoints.FpModel;
import cz.fidentis.model.Model;
import cz.fidentis.model.ModelLoader;
import cz.fidentis.processing.exportProcessing.FPImportExport;
import cz.fidentis.utils.FileUtils;
import cz.fidentis.utilsException.FileManipulationException;
import cz.fidentis.visualisation.procrustes.PApainting;
import cz.fidentis.visualisation.procrustes.PApaintingInfo;
import cz.fidentis.visualisation.surfaceComparison.HDpainting;
import cz.fidentis.visualisation.surfaceComparison.HDpaintingInfo;
import cz.fidentis.visualisation.surfaceComparison.SelectionType;
import cz.fidentis.visualisation.surfaceComparison.VisualizationType;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.vecmath.Vector3f;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@ActionID(
        category = "Mode",
        id = "cz.fidentis.gui.actions.OpenProject")
@ActionRegistration(
        displayName = "#CTL_OpenProject")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 75, separatorAfter = 100),
    @ActionReference(path = "Shortcuts", name = "D-O")
})
@Messages("CTL_OpenProject=Open Project...")
public final class OpenProject implements ActionListener {

    private final byte[] buffer = new byte[1024];
    private File tempFile;

    public void openProject(File file, final ProjectTopComponent ntc) throws ParserConfigurationException, SAXException, IOException {
        tempFile = new File(FileUtils.instance().getTempDirectoryPath() + File.separator + String.valueOf(System.currentTimeMillis()));
        if (tempFile.exists()) {
            try {
                FileUtils.instance().deleteFolder(tempFile);
            } catch (FileManipulationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        tempFile.mkdir();

        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(file));
        ZipEntry entry = zipIn.getNextEntry();
        while (entry != null) {
            String fileName = entry.getName();
            File newFile = new File(tempFile.getAbsolutePath() + File.separator + fileName);

            new File(newFile.getParent()).mkdirs();
            FileOutputStream output = new FileOutputStream(newFile);
            int len;
            while ((len = zipIn.read(buffer)) > 0) {
                output.write(buffer, 0, len);
            }
            output.close();
            entry = zipIn.getNextEntry();
        }
        zipIn.closeEntry();
        zipIn.close();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();

        File projectFile = new File(tempFile.getAbsolutePath() + File.separator + "project.xml");
        Document doc = builder.parse(projectFile);
        doc.normalize();

        Element root = doc.getDocumentElement();
        final Project p = new Project(root.getAttribute("name"));
        p.setTempDirectory(tempFile);
        ntc.setProject(p);
        
        NodeList topNodes = root.getChildNodes();
        for (int i = 0; i < topNodes.getLength(); i++) {
            Node n = topNodes.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;
                switch (e.getTagName()) {
                    case "composite":
                        p.addComposite(NbBundle.getMessage(Controller.class, "tree.node.composite"));
                        createComposite(e, ntc);
                        break;
                    case "comparison-two-faces":
                        p.add2FacesComparison(NbBundle.getMessage(Controller.class, "tree.node.twoFacesComparison"));
                        createComparison2Faces(e, ntc);
                        break;
                    case "one-to-many-comparison":
                        p.addOneToManyComparison(NbBundle.getMessage(Controller.class, "tree.node.oneToMany"));
                        createOneToManyComparison(e, ntc);
                        break;
                    case "batch-comparison":
                        p.addBatchComparison(NbBundle.getMessage(Controller.class, "tree.node.batchComparison"));
                        createBatchComparison(e, ntc);
                        break;
                }
            }
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                updateGui(ntc, p);
            }
        });
    }
    
    private void updateGui(ProjectTopComponent ntc, Project p) {
        GUIController.getBlankProject(); // create panel for new project
        
        ntc.setProject(p);
        ntc.setName(String.valueOf(GUIController.getProjects().size()));
        ntc.setDisplayName(p.getName());
        p.setIndex(GUIController.getProjects().size());
        ntc.setTextureRendering(ButtonHelper.getTexturesMenuItem().isSelected());
        Controller.addProjcet(p);
        ntc.open();
        GUIController.setSelectedProjectTopComponent(ntc);

        ConfigurationTopComponent ctc = GUIController.getConfigurationTopComponent();

        if (p.getSelectedComposite() != null) {
            ButtonHelper.setCompositeEnabled(true);
            ButtonHelper.setViewerEnabled(true);
            ButtonHelper.setTexturesEnabled(true);
            ButtonHelper.getTexturesMenuItem().setSelected(true);
            ntc.getCompositePanel().setCompositeData(GUIController.getSelectedProjectTopComponent().getProject().getSelectedComposite());
            ntc.getCompositePanel().selectTemplates();
            ntc.getProject().setSelectedPart(1);

            ntc.setTextureRendering(ButtonHelper.getTexturesMenuItem().isSelected());
            GUIController.selectComposite();

        }
        if (p.getSelectedComparison2Faces() != null) {
                        Comparison2Faces comparison2f = p.getSelectedComparison2Faces();
            p.setSelectedPart(2);
            if (comparison2f.getModel1() != null) {
                ntc.getViewerPanel_2Faces().getCanvas1().setImportLabelVisible(false);
                ntc.getViewerPanel_2Faces().getListener1().setModels(comparison2f.getModel1());
            }
            if (comparison2f.getModel2() != null) {
                ntc.getViewerPanel_2Faces().getCanvas2().setImportLabelVisible(false);
                ntc.getViewerPanel_2Faces().getListener2().setModels(comparison2f.getModel2());
                if (comparison2f.getResultIcon() != null) {
                    ntc.getViewerPanel_2Faces().setResultButtonVisible(true, 0);
                    ntc.getViewerPanel_2Faces().getCanvas1().showResultIcon();
                }
            }
            if (comparison2f.getState() >= 3) {
                if (comparison2f.getResultIcon() != null) {
                    ntc.getViewerPanel_2Faces().setResultButtonVisible(false, 0);
                    ntc.getViewerPanel_2Faces().getCanvas1().showResultIcon();
                }
                
                if (comparison2f.getComparisonMethod() == ComparisonMethod.PROCRUSTES) {
                    ntc.getViewerPanel_2Faces().getListener1().setProcrustes(true);
                } else {
                    ntc.getViewerPanel_2Faces().getListener1().setModels(comparison2f.getHdPaintingInfo().getModel());
                    ntc.getViewerPanel_2Faces().getListener1().setHdInfo(comparison2f.getHdPaintingInfo());
                    ntc.getViewerPanel_2Faces().getListener1().setHdPaint(comparison2f.getHDP());
                    ntc.getViewerPanel_2Faces().getListener1().setPaintHD(true);
                    ntc.getViewerPanel_2Faces().getListener1().drawHD(true);
                }
            }
            ntc.show2FacesViewer();
        }
        if (p.getSelectedOneToManyComparison() != null) {
            OneToManyComparison comparison1N = p.getSelectedOneToManyComparison();
            p.setSelectedPart(3);
            if (comparison1N.getPrimaryModel() != null) {
                ntc.getOneToManyViewerPanel().getCanvas1().setImportLabelVisible(false);
                ntc.getOneToManyViewerPanel().getListener1().setModels(comparison1N.getPrimaryModel());
            }
            if (!comparison1N.getModels().isEmpty()) {
                ntc.getOneToManyViewerPanel().getCanvas2().setImportLabelVisible(false);
                ModelLoader loader = new ModelLoader();
                ntc.getOneToManyViewerPanel().getListener2().setModels(loader.loadModel(comparison1N.getModel(0), true, true));
            }
            if (comparison1N.getState() >= 3) {
                if (comparison1N.getComparisonMethod() == ComparisonMethod.PROCRUSTES) {
                    ntc.getOneToManyViewerPanel().getListener2().setProcrustes(true);
                } else {
                    ntc.getOneToManyViewerPanel().getListener2().setModels(comparison1N.getHdPaintingInfo().getModel());
                    ntc.getOneToManyViewerPanel().getListener2().setHdInfo(comparison1N.getHdPaintingInfo());
                    ntc.getOneToManyViewerPanel().getListener2().setHdPaint(comparison1N.getHDP());
                    ntc.getOneToManyViewerPanel().getListener2().setPaintHD(true);
                    ntc.getOneToManyViewerPanel().getListener2().drawHD(true);
                }
            }
            ntc.show1toNViewer();
        }
        if (p.getSelectedBatchComparison() != null) {
            BatchComparison comparison = p.getSelectedBatchComparison();
            p.setSelectedPart(4);
            if (!comparison.getModels().isEmpty()) {
                ntc.getViewerPanel_Batch().getCanvas1().setImportLabelVisible(false);
            }
            if (comparison.getState() >= 3) {
                if (comparison.getComparisonMethod() == ComparisonMethod.PROCRUSTES) {
                    ntc.getViewerPanel_Batch().getListener().setProcrustes(true);
                } else {
                    ntc.getViewerPanel_Batch().getListener().setModels(comparison.getHDinfo().getModel());
                    ntc.getViewerPanel_Batch().getListener().setHdInfo(comparison.getHDinfo());
                    ntc.getViewerPanel_Batch().getListener().setHdPaint(comparison.getHDP());
                    ntc.getViewerPanel_Batch().getListener().setPaintHD(true);
                    ntc.getViewerPanel_Batch().getListener().drawHD(true);
                }
            }
            ntc.showBatchViewer();
        }
        GUIController.updateNavigator();
        ntc.requestActive();
    }

    private void createComposite(Element projectE, ProjectTopComponent tc) {
        Composite composite = tc.getProject().getSelectedComposite();

        NodeList parts = projectE.getElementsByTagName("composite-model");
        for (int i = 0; i < parts.getLength(); i++) {
            Element partE = (Element) parts.item(i);
            File path = new File(partE.getAttribute("model-path"));
            ModelInfo info = tc.getCompositePanel().loadModel(path);
            FacePartType type = FacePartType.valueOf(partE.getAttribute("part-type"));
            Element e = (Element) partE.getElementsByTagName("translation").item(0);
            Vector3f translation = parseVector(e);
            e = (Element) partE.getElementsByTagName("initShift").item(0);
            Vector3f shift = parseVector(e);
            composite.addModel(path, type, translation, shift);
        }
    }

    private void createComparison2Faces(Element projectE, ProjectTopComponent tc) {
        Comparison2Faces comparison = tc.getProject().getSelectedComparison2Faces();
        NodeList children = projectE.getChildNodes();
        Element primaryE = null;
        Element secondaryE = null;
        Element hdInfoE = null;
        Element paInfoE = null;
        Element transE = null;
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            Element e = (Element) n;
            switch (e.getTagName()) {
                case "primary-model":
                    primaryE = e;
                    break;
                case "secondary-model":
                    secondaryE = e;
                    break;
                case "hdInfo":
                    hdInfoE = e;
                    break;
                case "paInfo":
                    paInfoE = e;
                    break;
                case "comp-model-transformations":
                    transE = e;
                    break;
            }
        }

        comparison.setName(projectE.getAttribute("name"));

        //comparison.setDecription(projectE.getAttribute("description"));

        String attr = projectE.getAttribute("hd");
        if (attr != null && !attr.isEmpty()) {
            File hdFile = new File(tempFile.getAbsolutePath() + File.separator + attr);
            comparison.setHd((ArrayList<Float>) FileUtils.instance().loadArbitraryObject(hdFile));
        }
        attr = projectE.getAttribute("sortedHdRelative");
        if (attr != null && !attr.isEmpty()) {
            File hdFile = new File(tempFile.getAbsolutePath() + File.separator + attr);
            comparison.setSortedHdValuesRelative((ArrayList<Float>) FileUtils.instance().loadArbitraryObject(hdFile));
        }
        attr = projectE.getAttribute("sortedHdAbs");
        if (attr != null && !attr.isEmpty()) {
            File hdFile = new File(tempFile.getAbsolutePath() + File.separator + attr);
            comparison.setSortedHdValuesAbs((ArrayList<Float>) FileUtils.instance().loadArbitraryObject(hdFile));
        }
        attr = projectE.getAttribute("showPointInfo");
        if (attr != null && !attr.isEmpty()) {
            comparison.setShowPointInfo(Boolean.parseBoolean(attr));
        }
        attr = projectE.getAttribute("pointColor");
        if (attr != null && !attr.isEmpty()) {
            comparison.setPointColor(new Color(Integer.parseInt(attr)));
        }
        attr = projectE.getAttribute("hdColor1");
        if (attr != null && !attr.isEmpty()) {
            //comparison.setHdColor1(new Color(Integer.parseInt(attr)));
        }
        attr = projectE.getAttribute("hdColor2");
        if (attr != null && !attr.isEmpty()) {
            comparison.setHdColor2(new Color(Integer.parseInt(attr)));
        }
        attr = projectE.getAttribute("primaryColor");
        if (attr != null && !attr.isEmpty()) {
            comparison.setPrimaryColor(new Color(Integer.parseInt(attr)));
        }
        attr = projectE.getAttribute("secondaryColor");
        if (attr != null && !attr.isEmpty()) {
            comparison.setSecondaryColor(new Color(Integer.parseInt(attr)));
        }
        attr = projectE.getAttribute("haussdorfTreshold");
        if (attr != null && !attr.isEmpty()) {
            comparison.setHausdorfMaxTreshold(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("haussdorfMinTreshold");
        if (attr != null && !attr.isEmpty()) {
            comparison.setHausdorfMinTreshold(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("fpScaling");
        if (attr != null && !attr.isEmpty()) {
            comparison.setFpScaling(Boolean.parseBoolean(attr));
        }
        attr = projectE.getAttribute("useDatabase");
        if (attr != null && !attr.isEmpty()) {
            comparison.setUseDatabase(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("fpTreshold");
        if (attr != null && !attr.isEmpty()) {
            comparison.setFpTreshold(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("fpSize");
        if (attr != null && !attr.isEmpty()) {
            comparison.setFpSize(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("icpErrorRate");
        if (attr != null && !attr.isEmpty()) {
            comparison.setICPerrorRate(Float.parseFloat(attr));
        }
        attr = projectE.getAttribute("icpMaxIteration");
        if (attr != null && !attr.isEmpty()) {
            comparison.setICPmaxIteration(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("registrationMethod");
        if (attr != null && !attr.isEmpty()) {
            comparison.setRegistrationMethod(RegistrationMethod.valueOf(attr));
        }
        attr = projectE.getAttribute("comparisonMethod");
        if (attr != null && !attr.isEmpty()) {
            comparison.setComparisonMethod(ComparisonMethod.valueOf(attr));
        }
        attr = projectE.getAttribute("fpDistance");
        if (attr != null && !attr.isEmpty()) {
            comparison.setFpDistance(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("fpResultSize");
        if (attr != null && !attr.isEmpty()) {
           // comparison.setFpResultSize(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("compareButtonEnabled");
        if (attr != null && !attr.isEmpty()) {
            comparison.setCompareButtonEnabled(Boolean.parseBoolean(attr));
        }
        attr = projectE.getAttribute("numericalResults");
        if (attr != null && !attr.isEmpty()) {
            comparison.setNumericalResults(attr);
        }
        attr = projectE.getAttribute("scaleEnabled");
        if (attr != null && !attr.isEmpty()) {
            comparison.setScaleEnabled(Boolean.parseBoolean(attr));
        }
        attr = projectE.getAttribute("modelIconFile");
        if (attr != null && !attr.isEmpty()) {
            File f = new File(tempFile.getAbsolutePath() + File.separator + attr);
            comparison.setModelIcon((ImageIcon) FileUtils.instance().loadArbitraryObject(f));
        }
        attr = projectE.getAttribute("resultIconFile");
        if (attr != null && !attr.isEmpty()) {
            File f = new File(tempFile.getAbsolutePath() + File.separator + attr);
            comparison.setResultIcon((ImageIcon) FileUtils.instance().loadArbitraryObject(f));
        }
        attr = projectE.getAttribute("valuesTypeIndex");
        if (attr != null && !attr.isEmpty()) {
            comparison.setValuesTypeIndex(Integer.parseInt(attr));
        }

        if (primaryE != null) {
            Element modelE = (Element) primaryE.getElementsByTagName("model").item(0);
            File modelFile = new File(tempFile.getAbsolutePath() + File.separator + modelE.getAttribute("name"));
            ModelLoader loader = new ModelLoader();
            Model model = loader.loadModel(modelFile, true, true);
            comparison.setModel1(model);
            comparison.setMainFace(new KdTreeIndexed(model.getVerts()));
        }

        if (secondaryE != null) {
            Element modelE = (Element) secondaryE.getElementsByTagName("model").item(0);
            File modelFile = new File(tempFile.getAbsolutePath() + File.separator + modelE.getAttribute("name"));
            ModelLoader loader = new ModelLoader();
            Model model = loader.loadModel(modelFile, true, true);
            comparison.setModel2(model);
        }

        if (transE != null) {
            ArrayList<ICPTransformation> transforms = parseICPTransformations(transE);
            //comparison.setCompModelTransformations(transforms);
        }

        if (hdInfoE != null) {
            boolean useRelative = Boolean.parseBoolean(hdInfoE.getAttribute("useRelative"));
            HDpaintingInfo info = parseHdInfo(hdInfoE, useRelative, comparison.getHd(), comparison.getModel1(), comparison.getHdColor1(), comparison.getHdColor2());

            comparison.setHdPaintingInfo(info);
            comparison.setHDP(new HDpainting(info));
        }

        if (paInfoE != null) {
            PApaintingInfo info = parsePaInfo(paInfoE);
            tc.getViewerPanel_Batch().getListener().setPaInfo(info);

            PApainting painting = new PApainting(info);
            tc.getViewerPanel_Batch().getListener().setPaPainting(painting);
        }

        attr = projectE.getAttribute("state");
        if (attr != null && !attr.isEmpty()) {
            comparison.setState(Integer.parseInt(attr));
        }
    }

    private void createOneToManyComparison(Element projectE, ProjectTopComponent tc) {
        OneToManyComparison comparison = tc.getProject().getSelectedOneToManyComparison();
        NodeList children = projectE.getChildNodes();
        Element registeredE = null;
        Element preregE = null;
        Element modelsE = null;
        Element primaryE = null;
        Element fpE = null;
        Element icpTransE = null;
        Element hdInfoE = null;
        Element paInfoE = null;
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            Element e = (Element) n;
            switch (e.getTagName()) {
                case "compared-models":
                    modelsE = e;
                    break;
                case "primary-model":
                    primaryE = e;
                    break;
                case "registered-models":
                    registeredE = e;
                    break;
                case "pre-registered-models":
                    preregE = e;
                    break;
                case "fp-models-transformations":
                    icpTransE = e;
                    break;
                case "facial-points":
                    fpE = e;
                    break;
                case "hdInfo":
                    hdInfoE = e;
                    break;
                case "paInfo":
                    paInfoE = e;
                    break;
            }
        }

        comparison.setName(projectE.getAttribute("name"));

        //comparison.setDecription(projectE.getAttribute("description"));

        String attr = projectE.getAttribute("hd");
        if (attr != null && !attr.isEmpty()) {
            File hdFile = new File(tempFile.getAbsolutePath() + File.separator + attr);
            comparison.setHd((ArrayList<Float>) FileUtils.instance().loadArbitraryObject(hdFile));
        }
        attr = projectE.getAttribute("sortedHdRelative");
        if (attr != null && !attr.isEmpty()) {
            File hdFile = new File(tempFile.getAbsolutePath() + File.separator + attr);
            comparison.setSortedHdRel((ArrayList<Float>) FileUtils.instance().loadArbitraryObject(hdFile));
        }
        attr = projectE.getAttribute("sortedHdAbs");
        if (attr != null && !attr.isEmpty()) {
            File hdFile = new File(tempFile.getAbsolutePath() + File.separator + attr);
            comparison.setSortedHdAbs((ArrayList<Float>) FileUtils.instance().loadArbitraryObject(hdFile));
        }
        attr = projectE.getAttribute("hdNumResults");
        if (attr != null && !attr.isEmpty()) {
            File numResultsFile = new File(tempFile.getAbsolutePath() + File.separator + attr);
            comparison.setNumResults((ArrayList<ArrayList<Float>>) FileUtils.instance().loadArbitraryObject(numResultsFile));
        }
        attr = projectE.getAttribute("showPointInfo");
        if (attr != null && !attr.isEmpty()) {
            comparison.setShowPointInfo(Boolean.parseBoolean(attr));
        }
        attr = projectE.getAttribute("pointColor");
        if (attr != null && !attr.isEmpty()) {
            comparison.setPointColor(new Color(Integer.parseInt(attr)));
        }
        attr = projectE.getAttribute("hdColor1");
        if (attr != null && !attr.isEmpty()) {
            comparison.setHdColor1(new Color(Integer.parseInt(attr)));
        }
        attr = projectE.getAttribute("hdColor2");
        if (attr != null && !attr.isEmpty()) {
            comparison.setHdColor2(new Color(Integer.parseInt(attr)));
        }
        attr = projectE.getAttribute("haussdorfMinTreshold");
        if (attr != null && !attr.isEmpty()) {
            comparison.setHausdorfMinTreshold(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("haussdorfMaxTreshold");
        if (attr != null && !attr.isEmpty()) {
            comparison.setHausdorfMaxTreshold(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("haussdorfMinTreshold");
        if (attr != null && !attr.isEmpty()) {
            comparison.setHausdorfMinTreshold(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("fpScaling");
        if (attr != null && !attr.isEmpty()) {
            comparison.setFpScaling(Boolean.parseBoolean(attr));
        }
        attr = projectE.getAttribute("fpTreshold");
        if (attr != null && !attr.isEmpty()) {
            comparison.setFpTreshold(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("fpSize");
        if (attr != null && !attr.isEmpty()) {
            comparison.setFpSize(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("icpErrorRate");
        if (attr != null && !attr.isEmpty()) {
            comparison.setICPerrorRate(Float.parseFloat(attr));
        }
        attr = projectE.getAttribute("icpMaxIteration");
        if (attr != null && !attr.isEmpty()) {
            comparison.setICPmaxIteration(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("templateIndex");
        if (attr != null && !attr.isEmpty()) {
            comparison.setTemplateIndex(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("registrationMethod");
        if (attr != null && !attr.isEmpty()) {
            comparison.setRegistrationMethod(RegistrationMethod.valueOf(attr));
        }
        attr = projectE.getAttribute("comparisonMethod");
        if (attr != null && !attr.isEmpty()) {
            comparison.setComparisonMethod(ComparisonMethod.valueOf(attr));
        }
        attr = projectE.getAttribute("fpDistance");
        if (attr != null && !attr.isEmpty()) {
            comparison.setFpDistance(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("fpResultSize");
        if (attr != null && !attr.isEmpty()) {
            //comparison.setFpResultSize(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("compareButtonEnabled");
        if (attr != null && !attr.isEmpty()) {
            comparison.setCompareButtonEnabled(Boolean.parseBoolean(attr));
        }
        attr = projectE.getAttribute("numericalResults");
        if (attr != null && !attr.isEmpty()) {
            comparison.setNumericalResults(attr);
        }
        attr = projectE.getAttribute("scaleEnabled");
        if (attr != null && !attr.isEmpty()) {
            comparison.setScaleEnabled(Boolean.parseBoolean(attr));
        }
        attr = projectE.getAttribute("valuesTypeIndex");
        if (attr != null && !attr.isEmpty()) {
            comparison.setValuesTypeIndex(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("metricTypeIndex");
        if (attr != null && !attr.isEmpty()) {
            comparison.setMetricTypeIndex(Integer.parseInt(attr));
        }

        if (primaryE != null) {
            Element modelE = (Element) primaryE.getElementsByTagName("model").item(0);
            ModelLoader loader = new ModelLoader();
            File modelFile = new File(tempFile.getAbsolutePath() + File.separator + modelE.getAttribute("name"));
            comparison.setPrimaryModel(loader.loadModel(modelFile, true, true));
        }

        if (modelsE != null) {
            ArrayList<File> files = parseModelsList(modelsE);
            for (File f : files) {
                comparison.addModel(f);
            }
        }

        if (preregE != null) {
            NodeList ch = preregE.getChildNodes();
            ArrayList<Model> models = new ArrayList<>();
            for (int i = 0; i < ch.getLength(); i++) {
                Node n = ch.item(i);
                if (n.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                Element modelE = (Element) n;
                ModelLoader loader = new ModelLoader();
                File modelFile = new File(tempFile.getAbsolutePath() + File.separator + modelE.getAttribute("name"));
                Model m = loader.loadModel(modelFile, true, true);
                models.add(m);
            }
            comparison.setPreregiteredModels(models);
        }

        if (fpE != null) {
            String fileName = tempFile.getAbsolutePath() + File.separator + fpE.getAttribute("file");
            List<FpModel> fpModels = CSVparser.load(fileName);
            FPImportExport.instance().alignPointsToModels(fpModels, comparison.getModels());
            for (FpModel model : fpModels) {
                comparison.addFacialPoints(model.getModelName(), model.getFacialPoints());
            }
        }

        if (icpTransE != null) {
            NodeList ch = icpTransE.getElementsByTagName("transformations");
            for (int i = 0; i < ch.getLength(); i++) {
                Element transE = (Element) ch.item(i);
                ArrayList<ICPTransformation> transforms = parseICPTransformations(transE);
                //comparison.addFpModelsTransformations(transforms);
            }
        }

        if (registeredE != null) {
            comparison.setRegisteredModels(parseModelsList(registeredE));
        }

        if (hdInfoE != null) {
            boolean isRelative = comparison.getValuesTypeIndex() == 0;
            ModelLoader modelLoader = new ModelLoader();
            File modelFile = null;
            if (comparison.getRegisteredModels() != null) {
                modelFile = comparison.getRegisteredModels().get(comparison.getTemplateIndex());
            } else {
                modelFile = comparison.getModel(comparison.getTemplateIndex());
            }
            Model template = modelLoader.loadModel(modelFile, false, false);

            HDpaintingInfo info = parseHdInfo(hdInfoE, isRelative, comparison.getHd(), template, comparison.getHdColor1(), comparison.getHdColor2());
            comparison.setHdPaintingInfo(info);
            comparison.setHDP(new HDpainting(info));
        }

        if (paInfoE != null) {
            PApaintingInfo info = parsePaInfo(paInfoE);
            tc.getOneToManyViewerPanel().getListener2().setPaInfo(info);

            PApainting paint = new PApainting(info);
            tc.getOneToManyViewerPanel().getListener2().setPaPainting(paint);
        }

        attr = projectE.getAttribute("state");
        if (attr != null && !attr.isEmpty()) {
            comparison.setState(Integer.parseInt(attr));
        }
    }

    private void createBatchComparison(Element projectE, ProjectTopComponent tc) {
        BatchComparison comparison = tc.getProject().getSelectedBatchComparison();
        NodeList children = projectE.getChildNodes();
        Element registeredE = null;
        Element preregE = null;
        Element modelsE = null;
        Element averageE = null;
        Element averageRegE = null;
        Element fpE = null;
        Element icpTransE = null;
        Element hdInfoE = null;
        Element paInfoE = null;
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            Element e = (Element) n;
            switch (e.getTagName()) {
                case "compared-models":
                    modelsE = e;
                    break;
                case "registered-models":
                    registeredE = e;
                    break;
                case "average-face":
                    averageE = e;
                    break;
                case "average-face-registered":
                    averageRegE = e;
                    break;
                case "pre-registered-models":
                    preregE = e;
                    break;
                case "fp-models-transformations":
                    icpTransE = e;
                    break;
                case "facial-points":
                    fpE = e;
                    break;
                case "hdInfo":
                    hdInfoE = e;
                    break;
                case "paInfo":
                    paInfoE = e;
                    break;
            }
        }

        comparison.setName(projectE.getAttribute("name"));

        //comparison.setDecription(projectE.getAttribute("description"));

        String attr = projectE.getAttribute("hd");
        if (attr != null && !attr.isEmpty()) {
            File hdFile = new File(tempFile.getAbsolutePath() + File.separator + attr);
            comparison.setHd((ArrayList<Float>) FileUtils.instance().loadArbitraryObject(hdFile));
        }
        attr = projectE.getAttribute("sortedHd");
        if (attr != null && !attr.isEmpty()) {
            File hdFile = new File(tempFile.getAbsolutePath() + File.separator + attr);
            comparison.setSortedHd((ArrayList<Float>) FileUtils.instance().loadArbitraryObject(hdFile));
        }
        attr = projectE.getAttribute("hdNumResults");
        if (attr != null && !attr.isEmpty()) {
            File numResultsFile = new File(tempFile.getAbsolutePath() + File.separator + attr);
           // comparison.setHdNumResults((ArrayList<ArrayList<ArrayList<Float>>>) FileUtils.instance().loadArbitraryObject(numResultsFile));
        }
        attr = projectE.getAttribute("csvDirName");
        if (attr != null && !attr.isEmpty()) {
            comparison.setHdCSVresults(new File(tempFile.getAbsolutePath() + File.separator + attr));
        }
        attr = projectE.getAttribute("hdVisualResults");
        if (attr != null && !attr.isEmpty()) {
            File visualResultsFile = new File(tempFile.getAbsolutePath() + File.separator + attr);
            comparison.setHdVisualResults((ArrayList<ArrayList<Float>>) FileUtils.instance().loadArbitraryObject(visualResultsFile));
        }
        attr = projectE.getAttribute("showPointInfo");
        if (attr != null && !attr.isEmpty()) {
            comparison.setShowPointInfo(Boolean.parseBoolean(attr));
        }
        attr = projectE.getAttribute("pointColor");
        if (attr != null && !attr.isEmpty()) {
            comparison.setPointColor(new Color(Integer.parseInt(attr)));
        }
        attr = projectE.getAttribute("hdColor1");
        if (attr != null && !attr.isEmpty()) {
            comparison.setHdColor1(new Color(Integer.parseInt(attr)));
        }
        attr = projectE.getAttribute("hdColor2");
        if (attr != null && !attr.isEmpty()) {
            comparison.setHdColor2(new Color(Integer.parseInt(attr)));
        }
        attr = projectE.getAttribute("valuesTypeIndex");
        if (attr != null && !attr.isEmpty()) {
            comparison.setValuesTypeIndex(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("metricTypeIndex");
        if (attr != null && !attr.isEmpty()) {
            comparison.setMetricTypeIndex(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("haussdorfMaxTreshold");
        if (attr != null && !attr.isEmpty()) {
            comparison.setHausdorfMaxTreshold(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("haussdorfMinTreshold");
        if (attr != null && !attr.isEmpty()) {
            comparison.setHausdorfMinTreshold(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("fpScaling");
        if (attr != null && !attr.isEmpty()) {
            comparison.setFpScaling(Boolean.parseBoolean(attr));
        }
        attr = projectE.getAttribute("fpTreshold");
        if (attr != null && !attr.isEmpty()) {
            comparison.setFpTreshold(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("fpSize");
        if (attr != null && !attr.isEmpty()) {
            comparison.setFpSize(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("icpErrorRate");
        if (attr != null && !attr.isEmpty()) {
            comparison.setICPerrorRate(Float.parseFloat(attr));
        }
        attr = projectE.getAttribute("icpMaxIteration");
        if (attr != null && !attr.isEmpty()) {
            comparison.setICPmaxIteration(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("icpNumOfHeads");
        if (attr != null && !attr.isEmpty()) {
            comparison.setICPnumberOfHeads(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("templateIndex");
        if (attr != null && !attr.isEmpty()) {
            comparison.setTemplateIndex(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("registrationMethod");
        if (attr != null && !attr.isEmpty()) {
            comparison.setRegistrationMethod(RegistrationMethod.valueOf(attr));
        }
        attr = projectE.getAttribute("comparisonMethod");
        if (attr != null && !attr.isEmpty()) {
            comparison.setComparisonMethod(ComparisonMethod.valueOf(attr));
        }
        attr = projectE.getAttribute("fpDistance");
        if (attr != null && !attr.isEmpty()) {
            comparison.setFpDistance(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("fpResultSize");
        if (attr != null && !attr.isEmpty()) {
            //comparison.setFpResultSize(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("compareButtonEnabled");
        if (attr != null && !attr.isEmpty()) {
            comparison.setCompareButtonEnabled(Boolean.parseBoolean(attr));
        }
        attr = projectE.getAttribute("registerButtonEnabled");
        if (attr != null && !attr.isEmpty()) {
            comparison.setRegisterButtonEnabled(Boolean.parseBoolean(attr));
        }
        attr = projectE.getAttribute("variance");
        if (attr != null && !attr.isEmpty()) {
           //comparison.setVariance(Integer.parseInt(attr));
        }
        attr = projectE.getAttribute("numericalResults");
        if (attr != null && !attr.isEmpty()) {
            comparison.setNumericalResults(attr);
        }
        attr = projectE.getAttribute("distanceToMean");
        if (attr != null && !attr.isEmpty()) {
            comparison.setDistanceToMeanConfiguration(attr);
        }
        attr = projectE.getAttribute("auxiliaryResultsFolder");
        if (attr != null && !attr.isEmpty()) {
            //comparison.setAuxiliaryResultsFolder(new File(tempFile.getAbsolutePath() + File.separator + attr));
        }
        attr = projectE.getAttribute("scaleEnabled");
        if (attr != null && !attr.isEmpty()) {
            comparison.setScaleEnabled(Boolean.parseBoolean(attr));
        }

        if (modelsE != null) {
            ArrayList<File> files = parseModelsList(modelsE);
            for (File f : files) {
                comparison.addModel(f);
            }
        }

        if (preregE != null) {
            NodeList ch = preregE.getChildNodes();
            ArrayList<Model> models = new ArrayList<>();
            for (int i = 0; i < ch.getLength(); i++) {
                Node n = ch.item(i);
                if (n.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                Element modelE = (Element) n;
                ModelLoader loader = new ModelLoader();
                File modelFile = new File(tempFile.getAbsolutePath() + File.separator + modelE.getAttribute("name"));
                Model m = loader.loadModel(modelFile, true, true);
                models.add(m);
            }
            comparison.setPreregiteredModels(models);
        }

        if (fpE != null) {
            String fileName = tempFile.getAbsolutePath() + File.separator + fpE.getAttribute("file");
            List<FpModel> fpModels = CSVparser.load(fileName);
            FPImportExport.instance().alignPointsToModels(fpModels, comparison.getModels());
            for (FpModel model : fpModels) {
                comparison.addFacialPoints(model.getModelName(), model.getFacialPoints());
            }
        }

        if (icpTransE != null) {
            NodeList ch = icpTransE.getElementsByTagName("transformations");
            for (int i = 0; i < ch.getLength(); i++) {
                Element transE = (Element) ch.item(i);
                ArrayList<ICPTransformation> transforms = parseICPTransformations(transE);
                //comparison.addFpModelsTransformations(transforms);
            }
        }

        if (registeredE != null) {
            comparison.setRegistrationResults(parseModelsList(registeredE));
        }

        /*if (averageRegE != null) {
            Element modelE = (Element) averageRegE.getElementsByTagName("model").item(0);
            ModelLoader loader = new ModelLoader();
            File modelFile = new File(tempFile.getAbsolutePath() + File.separator + modelE.getAttribute("name"));
            Model m = loader.loadModel(modelFile, true, true);
            comparison.setAverageFace(m);
        }*/

        if (averageE != null) {
            Element modelE = (Element) averageE.getElementsByTagName("model").item(0);
            ModelLoader loader = new ModelLoader();
            File modelFile = new File(tempFile.getAbsolutePath() + File.separator + modelE.getAttribute("name"));
            Model m = loader.loadModel(modelFile, true, true);
            comparison.setAverageFace(m);
        }

        if (hdInfoE != null) {
            boolean useRelative = Boolean.parseBoolean(hdInfoE.getAttribute("useRelative"));
            HDpaintingInfo info = parseHdInfo(hdInfoE, useRelative, comparison.getHd(), comparison.getAverageFace(), comparison.getHdColor1(), comparison.getHdColor2());

            comparison.setHDinfo(info);
            comparison.setHDP(new HDpainting(info));
        }

        if (paInfoE != null) {
            PApaintingInfo info = parsePaInfo(paInfoE);
            tc.getViewerPanel_Batch().getListener().setPaInfo(info);

            PApainting painting = new PApainting(info);
            tc.getViewerPanel_Batch().getListener().setPaPainting(painting);
        }

        attr = projectE.getAttribute("state");
        if (attr != null && !attr.isEmpty()) {
            comparison.setState(Integer.parseInt(attr));
        }
    }

    private ArrayList<File> parseModelsList(Element modelsE) {
        NodeList nodes = modelsE.getChildNodes();
        ArrayList<File> list = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            Element modelE = (Element) n;
            switch (modelE.getTagName()) {
                case "file":
                    list.add(new File(modelE.getAttribute("path")));
                    break;
                case "temp-file":
                    list.add(new File(tempFile.getAbsolutePath() + File.separator + modelE.getAttribute("name")));
                    break;
            }
        }
        return list;
    }

    private Vector3f parseVector(Element vecE) {
        float x = Float.parseFloat(vecE.getAttribute("x"));
        float y = Float.parseFloat(vecE.getAttribute("y"));
        float z = Float.parseFloat(vecE.getAttribute("z"));
        return new Vector3f(x, y, z);
    }

    private Object loadArbitraryObject(File loadFile) {
        if (loadFile.isDirectory() || !loadFile.canRead()) {
            throw new IllegalArgumentException("Source file must be readable file.");
        }

        Object result = null;
        try (FileInputStream fs = new FileInputStream(loadFile);
                ObjectInputStream input = new ObjectInputStream(fs);) {

            result = input.readObject();
        } catch (IOException e) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, "Could not load arbitrary object.", e);
        } catch (ClassNotFoundException e) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, "Cannot find class of loaded object.", e);
        }
        return result;
    }

    private ArrayList<ICPTransformation> parseICPTransformations(Element transE) {
        ArrayList<ICPTransformation> transforms = new ArrayList<>();
        NodeList c = transE.getElementsByTagName("icp-transformation");
        for (int j = 0; j < c.getLength(); j++) {
            Element t = (Element) c.item(j);
            float scale = Float.parseFloat(t.getAttribute("scale"));
            float meanD = Float.parseFloat(t.getAttribute("meanD"));
            float tx = Float.parseFloat(t.getAttribute("tX"));
            float ty = Float.parseFloat(t.getAttribute("tY"));
            float tz = Float.parseFloat(t.getAttribute("tZ"));
            Vector3f translation = new Vector3f(tx, ty, tz);
            float qx = Float.parseFloat(t.getAttribute("qX"));
            float qy = Float.parseFloat(t.getAttribute("qY"));
            float qz = Float.parseFloat(t.getAttribute("qZ"));
            float qw = Float.parseFloat(t.getAttribute("qW"));
            Quaternion q = new Quaternion(qw, qx, qy, qz);
            transforms.add(new ICPTransformation(translation, scale, q, meanD, null));
        }
        return transforms;

    }

    private HDpaintingInfo parseHdInfo(Element hdInfoE, boolean useRelative, List<Float> hd, Model model, Color hdColor1, Color hdColor2) {
        HDpaintingInfo info = new HDpaintingInfo(hd, model, useRelative);
        info.setMinColor(hdColor1.getRGBColorComponents(null));
        info.setMinColor(hdColor2.getRGBColorComponents(null));

        info.setMaxThreshValue(Float.parseFloat(hdInfoE.getAttribute("treshValue")));
        info.setMinSelection(Float.parseFloat(hdInfoE.getAttribute("minSelection")));
        info.setMaxSelection(Float.parseFloat(hdInfoE.getAttribute("maxSelection")));
        info.setIsSelection(Boolean.parseBoolean(hdInfoE.getAttribute("isSelection")));
        info.setIsRecomputed(Boolean.parseBoolean(hdInfoE.getAttribute("isRecomputed")));

        info.setvType(VisualizationType.valueOf(hdInfoE.getAttribute("viz-type")));
        info.setsType(SelectionType.valueOf(hdInfoE.getAttribute("selectionType")));
        info.setLenghtFactor(Float.parseFloat(hdInfoE.getAttribute("lengthFactor")));
        info.setDensity(Float.parseFloat(hdInfoE.getAttribute("density")));
        info.setRecompute(Boolean.parseBoolean(hdInfoE.getAttribute("recompute")));

        return info;
    }

    private PApaintingInfo parsePaInfo(Element paInfoE) {
        File gpaFile = new File(tempFile + File.separator + paInfoE.getAttribute("gpaFile"));
        GPA gpa = (GPA) loadArbitraryObject(gpaFile);
        PApaintingInfo info = new PApaintingInfo(gpa, null, Integer.parseInt(paInfoE.getAttribute("type")));

        info.setEnhance(Float.parseFloat(paInfoE.getAttribute("enhance")));
        info.setPointSize(Float.parseFloat(paInfoE.getAttribute("pointSize")));
        info.setIndexOfSelectedPoint(Integer.parseInt(paInfoE.getAttribute("selectedPoint")));
        info.setIndexOfSelectedConfig(Integer.parseInt(paInfoE.getAttribute("selectedConfig")));
        info.setFacialPointRadius(Float.parseFloat(paInfoE.getAttribute("pointRadius")));

        return info;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new ModelFileFilter(new String[]{"fid"}, "Fidentis project files"));
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            final File openFile = chooser.getSelectedFile();
            final ProjectTopComponent ntc = GUIController.getBlankProject();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    ProgressHandle p = ProgressHandleFactory.createHandle("Opening project from file " + openFile.getName());
                    try {
                        p.start();
                        openProject(openFile, ntc);
                    } catch (IOException | ParserConfigurationException | SAXException ex) {
                        JOptionPane.showMessageDialog(null, "Failed to open project");
                        ex.printStackTrace();
                    } finally {
                        p.finish();
                    }
                }
            };
            Thread t = new Thread(r);
            t.start();
        }
    }
}
