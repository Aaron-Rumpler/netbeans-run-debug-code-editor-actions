package io.github.s4gh.projecteditorrunactions;

import org.netbeans.spi.project.ActionProvider;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "Editor",
    id = "io.github.s4gh.projecteditorrunactions.RunFileToolbarAction"
)
@ActionRegistration(
    displayName = "#CTL_RunFileToolbarAction",
    lazy = false
)
@ActionReference(
    path = "Editors/Toolbars/Default",
    position = 1600
)
@Messages("CTL_RunFileToolbarAction=Run File (Shift+F6)")
public final class RunFileToolbarAction extends AbstractFileCommandAction {

    private static final String ICON =
            "org/netbeans/modules/project/ui/resources/runProject.png";

    public RunFileToolbarAction() {
        this(null);
    }

    private RunFileToolbarAction(DataObject dobj) {
        super(dobj, ActionProvider.COMMAND_RUN_SINGLE,
              Bundle.CTL_RunFileToolbarAction(), ICON);
    }

    @Override
    protected AbstractFileCommandAction createInstance(DataObject dobj) {
        return new RunFileToolbarAction(dobj);
    }
}
