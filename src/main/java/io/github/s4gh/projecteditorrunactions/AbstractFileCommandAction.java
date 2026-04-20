package io.github.s4gh.projecteditorrunactions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
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
 * Shared base for editor-toolbar actions that delegate a single-file command
 * (run, debug, ...) to the project's {@link ActionProvider}. The current
 * editor's {@link DataObject} is injected by NetBeans when the lazy action is
 * materialized for the {@code Editors/Toolbars/Default} toolbar.
 */
abstract class AbstractFileCommandAction extends AbstractAction
        implements ContextAwareAction, Presenter.Toolbar {

    private final DataObject dobj;
    private final String command;

    protected AbstractFileCommandAction(DataObject dobj, String command,
                                        String displayName, String iconResource) {
        super(displayName);
        this.dobj = dobj;
        this.command = command;
        putValue(Action.SMALL_ICON, ImageUtilities.loadImageIcon(iconResource, false));
        putValue("hideActionText", Boolean.TRUE);
        setEnabled(isCommandEnabled());
    }

    private ActionProvider actionProvider() {
        if (dobj == null) {
            return null;
        }
        FileObject fo = dobj.getPrimaryFile();
        Project prj = FileOwnerQuery.getOwner(fo);
        return prj == null ? null : prj.getLookup().lookup(ActionProvider.class);
    }

    private boolean isCommandEnabled() {
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
