package my.portfolio.prjkt.views.session;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "logout")
public class LogoutView extends Composite<VerticalLayout> {
    public LogoutView() {
        UI.getCurrent().getPage().setLocation("project");
        VaadinSession.getCurrent().getSession().invalidate();
        VaadinSession.getCurrent().close();
    }

}
