package io.github.s4gh.projecteditorrunactions;

import java.awt.event.ActionEvent;
import javax.swing.Action;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.awt.Actions;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle.Messages;

/**
 * Editor-toolbar action that delegates to
 * {@code com.tusharjoshi.runargs.RunFileAction} from the nbrunwithargs plugin.
 * The dependency on nbrunwithargs is optional at both compile time and
 * runtime: the delegate is resolved through {@link Actions#forID} — no direct
 * class reference is required, and the action stays disabled when the plugin
 * is not installed.
 */
@ActionID(
    category = "Editor",
    id = "io.github.s4gh.projecteditorrunactions.RunFileWithArgumentsToolbarAction"
)
@ActionRegistration(
    displayName = "#CTL_RunFileWithArgumentsToolbarAction",
    lazy = true
)
@ActionReference(
    path = "Editors/Toolbars/Default",
    position = 1630
)
@Messages("CTL_RunFileWithArgumentsToolbarAction=Run File with Arguments... (Alt+Shift+F6)")
public final class RunFileWithArgumentsToolbarAction extends AbstractFileCommandAction {

    static final String DELEGATE_CATEGORY = "Build";
    static final String DELEGATE_ID = "com.tusharjoshi.runargs.RunFileAction";

    public RunFileWithArgumentsToolbarAction(DataObject dobj) {
        super(dobj, null, Bundle.CTL_RunFileWithArgumentsToolbarAction(),
              WithArgumentsDelegate.delegateIcon(DELEGATE_CATEGORY, DELEGATE_ID));
    }

    @Override
    protected boolean isCommandEnabled() {
        Action delegate = WithArgumentsDelegate.contextDelegate(
                DELEGATE_CATEGORY, DELEGATE_ID, dataObject());
        return delegate != null && delegate.isEnabled();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Action delegate = WithArgumentsDelegate.contextDelegate(
                DELEGATE_CATEGORY, DELEGATE_ID, dataObject());
        if (delegate != null && delegate.isEnabled()) {
            delegate.actionPerformed(e);
        }
    }

    @Override
    protected AbstractFileCommandAction createInstance(DataObject dobj) {
        return new RunFileWithArgumentsToolbarAction(dobj);
    }
}
