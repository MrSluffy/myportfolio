package my.portfolio.prjkt.views.home;

import com.vaadin.flow.component.Component;
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
public class HomeView extends VerticalLayout {


    HorizontalLayout layout = new HorizontalLayout();

    private final String[] mRandomGreetings = new String[]{"simple.", "unique.", "Andrew"};

    public HomeView() {

        addClassName("home-view");
        addClassNames("flex", "items-start", "p-l", "rounded-l");

        setSpacing(false);

        add(configureContent());

        setSizeFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
    }

    private Component configureContent() {
        Image imgShape = new Image("images/image-shape.png", "business-shape-bg");
        imgShape.addClassName("image-shape");
        Image imgBusiness = new Image("images/image-business.png", "business");
        Label head = new Label("Hi, I'm " + getRandomGreetings());
        head.addClassName("h1-head");
        imgBusiness.addClassName("image-business");
        var text = new H4("I’m a developer with some unique ideas and knowledge in terms of technology.");
        text.addClassName("text-content");
        var qoute = new H3(" \"Great things start from the small ideas\"");
        qoute.addClassName("text-qoute");
        layout.addClassNames("flex", "items-start", "p-l", "rounded-l");
        layout.addClassName("content-layout");

        layout.add(head, text, qoute, imgBusiness, imgShape);
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
