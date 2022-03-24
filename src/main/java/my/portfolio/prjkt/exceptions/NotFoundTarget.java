package my.portfolio.prjkt.exceptions;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.ParentLayout;
import my.portfolio.prjkt.views.MainLayout;

import javax.servlet.http.HttpServletResponse;

@ParentLayout(MainLayout.class)
public class NotFoundTarget
        extends RouteNotFoundError {

    @Override
    public int setErrorParameter(BeforeEnterEvent event,
                                 ErrorParameter<NotFoundException> parameter) {
        Image img = new Image("images/notfound.png", "notfound");
        img.addClassName("not-found-img");
        var v = new VerticalLayout(img);
        v.addClassName("not-found-layout");
        v.setSizeFull();
        v.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        v.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        getElement().appendChild(v.getElement());
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
