package io.github.s4gh.projecteditorrunactions;

import javax.swing.Action;
import javax.swing.Icon;

import org.openide.awt.Actions;
import org.openide.loaders.DataObject;
import org.openide.util.ContextAwareAction;
import org.openide.util.lookup.Lookups;

/**
 * Helpers for resolving the optional nbrunwithargs "Run/Debug File with
 * Arguments" actions through {@link Actions#forID} so the nbrunwithargs module
 * is not a compile- or run-time dependency. When the module is not installed,
 * {@link Actions#forID} returns {@code null}, which the helpers propagate.
 */
final class WithArgumentsDelegate {

    private WithArgumentsDelegate() {}

    static Icon delegateIcon(String category, String id) {
        Action delegate = Actions.forID(category, id);
        if (delegate == null) {
            return null;
        }
        Object icon = delegate.getValue(Action.SMALL_ICON);
        return icon instanceof Icon i ? i : null;
    }

    static Action contextDelegate(String category, String id, DataObject dobj) {
        Action delegate = Actions.forID(category, id);
        if (delegate == null) {
            return null;
        }
        if (dobj != null && delegate instanceof ContextAwareAction ca) {
            return ca.createContextAwareInstance(Lookups.fixed(dobj));
        }
        return delegate;
    }
}
