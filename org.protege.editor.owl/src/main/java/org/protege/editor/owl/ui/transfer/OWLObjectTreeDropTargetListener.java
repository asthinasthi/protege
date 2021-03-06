package org.protege.editor.owl.ui.transfer;

import java.awt.Rectangle;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;

import org.protege.editor.owl.ui.table.OWLObjectDropTargetListener;
import org.protege.editor.owl.ui.tree.OWLObjectTree;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 04-Jun-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class OWLObjectTreeDropTargetListener extends OWLObjectDropTargetListener {

    private OWLObjectTree tree;


    public OWLObjectTreeDropTargetListener(OWLObjectTree component) {
        super(component);
        this.tree = component;
    }


    protected boolean isDragAcceptable(DropTargetDragEvent event) {
        int row = tree.getRowForLocation(event.getLocation().x, event.getLocation().y);
        if (row == -1) {
            return false;
        }
        Rectangle r = tree.getRowBounds(row);
        if (r.contains(event.getLocation()) == false) {
            tree.setDropRow(-1);
            return false;
        }
        boolean isAcceptable = super.isDragAcceptable(event);
        if (isAcceptable) {
            tree.setDropRow(row);
        }
        return isAcceptable;
    }


    public void dragExit(DropTargetEvent dte) {
        super.dragExit(dte);
        tree.setDropRow(-1);
    }


    protected boolean isDropAcceptable(DropTargetDropEvent event) {
        return super.isDropAcceptable(event);
    }


    public void drop(DropTargetDropEvent dtde) {
        super.drop(dtde);
        tree.setDropRow(-1);
    }
}
