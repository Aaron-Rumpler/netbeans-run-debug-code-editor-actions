package io.github.s4gh.projecteditorrunactions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;

import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ActionProvider;

import org.openide.awt.Actions;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.ContextAwareAction;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.actions.Presenter;
import org.openide.util.lookup.Lookups;

/**
 * Shared base for editor-toolbar actions that act on the file currently shown
 * in the editor. A fresh instance is bound to each editor's
 * {@link DataObject} via {@link #createContextAwareInstance(Lookup)}, which
 * NbEditorToolBar calls with the editor's lookup when rendering
 * {@code Editors/Toolbars/Default}.
 *
 * <p>The default implementation delegates to the owning project's
 * {@link ActionProvider} using a command string (e.g.
 * {@link ActionProvider#COMMAND_RUN_SINGLE}). Subclasses that do not dispatch
 * through an {@code ActionProvider} command — for instance, ones that delegate
 * to another registered {@link Action} — pass {@code command = null} and
 * override {@link #isCommandEnabled()} and
 * {@link #actionPerformed(ActionEvent)}.
 */
abstract class AbstractFileCommandAction extends AbstractAction
        implements ContextAwareAction, Presenter.Toolbar {

    private final DataObject dobj;
    private final String command;

    protected AbstractFileCommandAction(DataObject dobj, String command,
                                        String displayName, String iconResource) {
        this(dobj, command, displayName,
             ImageUtilities.loadImageIcon(iconResource, false));
    }

    protected AbstractFileCommandAction(DataObject dobj, String command,
                                        String displayName, Icon icon) {
        super(displayName);
        this.dobj = dobj;
        this.command = command;
        if (icon != null) {
            putValue(Action.SMALL_ICON, icon);
        }
        putValue("hideActionText", Boolean.TRUE);
        setEnabled(isCommandEnabled());
    }

    protected final DataObject dataObject() {
        return dobj;
    }

    protected final ActionProvider actionProvider() {
        if (dobj == null) {
            return null;
        }
        FileObject fo = dobj.getPrimaryFile();
        Project prj = FileOwnerQuery.getOwner(fo);
        return prj == null ? null : prj.getLookup().lookup(ActionProvider.class);
    }

    protected boolean isCommandEnabled() {
        if (command == null) {
            return false;
        }
        ActionProvider ap = actionProvider();
        if (ap == null) {
            return false;
        }
        try {
            return ap.isActionEnabled(command, Lookups.fixed(dobj));
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (command == null) {
            return;
        }
        ActionProvider ap = actionProvider();
        if (ap == null) {
            return;
        }
        Lookup ctx = Lookups.fixed(dobj);
        if (ap.isActionEnabled(command, ctx)) {
            ap.invokeAction(command, ctx);
        }
    }

    @Override
    public final Action createContextAwareInstance(Lookup actionContext) {
        DataObject contextDobj = actionContext.lookup(DataObject.class);
        return contextDobj == null ? this : createInstance(contextDobj);
    }

    protected abstract AbstractFileCommandAction createInstance(DataObject dobj);

    @Override
    public JComponent getToolbarPresenter() {
        JButton btn = new JButton();
        Actions.connect(btn, this);
        btn.setFocusable(false);
        btn.putClientProperty("hideActionText", Boolean.TRUE);
        return btn;
    }
}
