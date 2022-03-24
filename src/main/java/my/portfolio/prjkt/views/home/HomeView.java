package my.portfolio.prjkt.views.home;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.*;
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
@JsModule("https://unpkg.com/@lottiefiles/lottie-player@latest/dist/lottie-player.js")
public class HomeView extends VerticalLayout implements HasComponents, HasStyle {

    HorizontalLayout layout = new HorizontalLayout();

    Image imgShape = new Image("images/image-shape.png", "business-shape-bg");
    Image imgBusiness = new Image("images/image-busines.png", "business");
    Image gif = new Image("images/business-team.gif", "gif");



    private final String[] mRandomGreetings = new String[]{"simple.", "unique.", "Andrew"};

    public HomeView() {
        setSpacing(true);
        addClassName("home-view");
        addClassNames("items-center","max-w-screen-lg", "mx-auto");
        UI.getCurrent().getPage()
                .addJsModule("https://unpkg.com/@lottiefiles/lottie-player@latest/dist/lottie-player.js");
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
        Div div = new Div();
        div.setSizeFull();
        div.getElement().setProperty("innerHTML", "<lottie-player class=\"gif-view\" src=\"https://assets1.lottiefiles.com/packages/lf20_0d8dwu0k.json\"  background=\"transparent\"  speed=\"1\" loop  autoplay></lottie-player>");
        var vr = new VerticalLayout(head, text, qoute);
        layout.add(vr, div);
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
