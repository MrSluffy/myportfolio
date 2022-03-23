package my.portfolio.prjkt.views.home;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import my.portfolio.prjkt.views.MainLayout;

import java.util.Random;

@PageTitle("Home")
@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class HomeView extends VerticalLayout implements HasComponents, HasStyle {

    HorizontalLayout layout = new HorizontalLayout();

    Image imgShape = new Image("images/image-shape.png", "business-shape-bg");
    Image imgBusiness = new Image("images/image-business.png", "business");
    Image gif = new Image("images/business-team.gif", "gif");



    private final String[] mRandomGreetings = new String[]{"simple.", "unique.", "Andrew"};

    public HomeView() {

        addClassName("home-view");
        addClassNames("image-list-view", "items-center","max-w-screen-lg", "mx-auto", "pb-l", "px-l");

        setWidthFull();

        add(configureContent());
    }

    private Component configureContent() {

        imgShape.addClassName("image-shape");
        Label head = new Label("Hi, I'm " + getRandomGreetings());
        head.addClassName("h1-head");
        imgBusiness.addClassName("image-business");
        var text = new H4("I’m a developer with some unique ideas and knowledge in terms of technology.");
        text.addClassName("text-content");
        var qoute = new H3(" \"Great things start from the small ideas\"");
        qoute.addClassName("text-qoute");
        layout.addClassNames("flex", "items-start", "p-l", "rounded-l");
        layout.addClassName("content-layout");
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        layout.setSizeFull();
        gif.addClassName("gif-view");
        var vr = new VerticalLayout(head, text, qoute);
        layout.add(vr, gif);
        return layout;
    }

    private String getRandomGreetings() {
        return (this.mRandomGreetings[getLuckyNumber(0, 3) % mRandomGreetings.length]);
    }

    public int getLuckyNumber(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}
