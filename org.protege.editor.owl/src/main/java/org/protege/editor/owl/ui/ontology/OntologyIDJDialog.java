package org.protege.editor.owl.ui.ontology;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.ontology.wizard.create.OntologyIDPanel;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyID;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 12-May-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class OntologyIDJDialog extends JPanel {
	private static final long serialVersionUID = -3786605367035342914L;

	private JTextField ontologyIRIField;

    private JCheckBox enableVersionCheckBox;
    
    private JTextField versionIRIField;


    public static OWLOntologyID showDialog(OWLEditorKit editorKit, OWLOntologyID id) {
    	OntologyIDJDialog dialog = new OntologyIDJDialog(id);
    	int response = JOptionPane.showConfirmDialog(SwingUtilities.getAncestorOfClass(JFrame.class, editorKit.getOWLWorkspace()), 
    								  				 dialog, 
    								  				 "Refactor Ontology Name", 
    								  				 JOptionPane.OK_CANCEL_OPTION, 
    								  				 JOptionPane.QUESTION_MESSAGE);    	
    	return response == JOptionPane.OK_OPTION ? dialog.getOntologyID() : null;
    }
    
    public OntologyIDJDialog(OWLOntologyID id) {
        createUI(id);
    }
    
    public void createUI(OWLOntologyID id) {
        ontologyIRIField = new JTextField(OntologyPreferences.getInstance().generateURI().toString());
        if (!id.isAnonymous()) {
        	ontologyIRIField.setText(id.getOntologyIRI().toString());
        }

        enableVersionCheckBox = new JCheckBox("Enable Version Iri");
        enableVersionCheckBox.setEnabled(true);
        enableVersionCheckBox.setSelected(!id.isAnonymous() && id.getVersionIRI() != null);
        enableVersionCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                versionIRIField.setEnabled(enableVersionCheckBox.isSelected());
                if (versionIRIField.isEnabled()) {
                    versionIRIField.setText(ontologyIRIField.getText());
                }
            }
        });
        versionIRIField = new JTextField();
        if (!id.isAnonymous() && id.getVersionIRI() != null) {
        	versionIRIField.setText(id.getVersionIRI().toString());
        }
        else if (id.getOntologyIRI() != null){
        	versionIRIField.setText(id.getOntologyIRI().toString());
        }
        versionIRIField.setEnabled(!id.isAnonymous() && id.getVersionIRI() != null);


        Box holderPanel = new Box(BoxLayout.PAGE_AXIS);
        holderPanel.add(new JLabel("Ontology IRI"));
        holderPanel.add(ontologyIRIField);
        holderPanel.add(Box.createVerticalStrut(12));
        holderPanel.add(new JLabel("Version IRI"));
        holderPanel.add(versionIRIField);
        holderPanel.add(enableVersionCheckBox);
        add(holderPanel);
    }

    public OWLOntologyID getOntologyID() {
        try {
            URI ontologyURI = new URI(ontologyIRIField.getText());
            IRI ontologyIRI = IRI.create(ontologyURI);
            
            if (enableVersionCheckBox.isSelected()) {
                URI versionURI = new URI(versionIRIField.getText());
                IRI versionIRI = IRI.create(versionURI);

                return new OWLOntologyID(ontologyIRI, versionIRI);
            }
            else {
                return new OWLOntologyID(ontologyIRI);
            }
        }
        catch (URISyntaxException e) {
            return null;
        }
    }
}
