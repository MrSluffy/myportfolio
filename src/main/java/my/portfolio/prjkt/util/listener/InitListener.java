package my.portfolio.prjkt.util.listener;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;

public class InitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {
        serviceInitEvent.getSource().addUIInitListener(event -> {
            UI ui = event.getUI();
            VaadinSession session = ui.getSession();
            setErrorHandler(session, ui);
        });
    }

    private void setErrorHandler(VaadinSession session, UI ui) {
        session.setErrorHandler(error -> {
            ui.access(() -> {
                ConfirmDialog confirmDialog = new ConfirmDialog("Error",
                        "Internal Error: " + error.getThrowable().getMessage(),
                        "Do Something", confirmFire -> {
                });
                String trace = "";
                for (StackTraceElement s : error.getThrowable()
                        .getStackTrace()) {
                    trace = trace + "  at " + s.getClassName() + ".java line "
                            + s.getLineNumber() + " " + s.getMethodName()
                            + "\n";
                }
                confirmDialog.add(new Pre(trace));
                confirmDialog.setWidth("500px");
                confirmDialog.setHeight("500px");
                confirmDialog.open();
                error.getThrowable().printStackTrace();
            });
        });
    }
}
