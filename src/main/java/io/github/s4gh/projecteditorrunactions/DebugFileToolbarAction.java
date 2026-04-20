package io.github.s4gh.projecteditorrunactions;

import org.netbeans.spi.project.ActionProvider;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "Editor",
    id = "io.github.s4gh.projecteditorrunactions.DebugFileToolbarAction"
)
@ActionRegistration(
    displayName = "#CTL_DebugFileToolbarAction",
    lazy = false
)
@ActionReference(
    path = "Editors/Toolbars/Default",
    position = 1610,
    separatorAfter = 1620
)
@Messages("CTL_DebugFileToolbarAction=Debug File (Ctrl+Shift+F5)")
public final class DebugFileToolbarAction extends AbstractFileCommandAction {

    private static final String ICON =
            "org/netbeans/modules/debugger/resources/debugProject.png";

    public DebugFileToolbarAction() {
        this(null);
    }

    private DebugFileToolbarAction(DataObject dobj) {
        super(dobj, ActionProvider.COMMAND_DEBUG_SINGLE,
              Bundle.CTL_DebugFileToolbarAction(), ICON);
    }

    @Override
    protected AbstractFileCommandAction createInstance(DataObject dobj) {
        return new DebugFileToolbarAction(dobj);
    }
}
